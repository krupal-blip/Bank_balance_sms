"""BiGRU + CRF multi-task NLU, numpy, manual backprop.

Forward pass is written to match the Kotlin runtime step for step:
  * token embedding = MEAN of its trigram-bucket embeddings   (SmsParser)
  * GRU gate rows are [reset | update | new], h = (1-z)*n + z*h_prev  (GruCell)
  * enc[t] = [hF[t] ; hB[t]] ; pooled = mean_t enc[t]         (SmsParser)
  * CRF score = sum emissions + sum crf[prev, cur]            (CrfDecoder)
"""
import numpy as np

V, E, H, NT = 4096, 48, 64, 11

def sigmoid(x): return 1.0/(1.0+np.exp(-x))

def init_params(seed=20260821):
    r = np.random.default_rng(seed)
    def u(*shape, s=None):
        s = s if s else (1.0/np.sqrt(shape[-1]))
        return r.uniform(-s, s, shape).astype(np.float64)
    p = {'emb': u(V, E, s=0.5)}
    for d in ('F','B'):
        p['wIh'+d] = u(3*H, E); p['wHh'+d] = u(3*H, H)
        p['bIh'+d] = np.zeros(3*H); p['bHh'+d] = np.zeros(3*H)
    p['tagW'] = u(NT, 2*H); p['tagB'] = np.zeros(NT)
    p['isBankW'] = u(2, 2*H); p['isBankB'] = np.zeros(2)
    p['ttypeW'] = u(3, 2*H);  p['ttypeB'] = np.zeros(3)
    p['srcW'] = u(3, 2*H);    p['srcB'] = np.zeros(3)
    p['crf'] = u(NT, NT, s=0.1)
    return p

# ------------------------------------------------------------------ embeddings
def embed(p, tri):
    x = np.empty((len(tri), E))
    for t, ids in enumerate(tri):
        x[t] = p['emb'][ids].mean(axis=0)
    return x

def embed_grad(g, tri, dx):
    for t, ids in enumerate(tri):
        g['emb'][ids] += dx[t] / len(ids)

# ------------------------------------------------------------------ GRU
def gru_forward(p, d, x):
    T_ = x.shape[0]
    wIh, wHh, bIh, bHh = p['wIh'+d], p['wHh'+d], p['bIh'+d], p['bHh'+d]
    hs = np.zeros((T_, H)); cache = []
    h = np.zeros(H)
    order = range(T_) if d == 'F' else range(T_-1, -1, -1)
    for t in order:
        gi = wIh @ x[t] + bIh
        gh = wHh @ h + bHh
        r = sigmoid(gi[:H] + gh[:H])
        z = sigmoid(gi[H:2*H] + gh[H:2*H])
        n = np.tanh(gi[2*H:] + r * gh[2*H:])
        h_new = (1.0 - z) * n + z * h
        cache.append((t, x[t], h.copy(), r, z, n, gh[2*H:].copy()))
        h = h_new
        hs[t] = h
    return hs, cache

def gru_backward(p, g, d, cache, dhs, x_shape):
    wIh, wHh = p['wIh'+d], p['wHh'+d]
    dx = np.zeros(x_shape)
    dh = np.zeros(H)
    for (t, xt, h_prev, r, z, n, gh_n) in reversed(cache):
        dh = dh + dhs[t]
        dn = dh * (1.0 - z)
        dz = dh * (h_prev - n)
        dh_prev = dh * z
        dn_pre = dn * (1.0 - n*n)
        dr = dn_pre * gh_n
        dgh_n = dn_pre * r
        dz_pre = dz * z * (1.0 - z)
        dr_pre = dr * r * (1.0 - r)
        dgi = np.concatenate([dr_pre, dz_pre, dn_pre])
        dgh = np.concatenate([dr_pre, dz_pre, dgh_n])
        g['wIh'+d] += np.outer(dgi, xt); g['bIh'+d] += dgi
        g['wHh'+d] += np.outer(dgh, h_prev); g['bHh'+d] += dgh
        dx[t] += wIh.T @ dgi
        dh = dh_prev + wHh.T @ dgh
    return dx

# ------------------------------------------------------------------ CRF
def crf_nll_and_grads(em, trans, gold):
    T_, K = em.shape
    alpha = np.empty((T_, K)); alpha[0] = em[0]
    for t in range(1, T_):
        m = alpha[t-1][:, None] + trans
        mx = m.max(axis=0)
        alpha[t] = em[t] + mx + np.log(np.exp(m - mx).sum(axis=0))
    mx = alpha[T_-1].max(); logZ = mx + np.log(np.exp(alpha[T_-1]-mx).sum())
    beta = np.zeros((T_, K))
    for t in range(T_-2, -1, -1):
        m = trans + (em[t+1] + beta[t+1])[None, :]
        mx = m.max(axis=1)
        beta[t] = mx + np.log(np.exp(m - mx[:, None]).sum(axis=1))
    gold_score = em[0, gold[0]] + sum(em[t, gold[t]] + trans[gold[t-1], gold[t]] for t in range(1, T_))
    nll = logZ - gold_score
    marg = np.exp(alpha + beta - logZ)
    dem = marg.copy()
    for t in range(T_): dem[t, gold[t]] -= 1.0
    dtr = np.zeros_like(trans)
    for t in range(T_-1):
        m = alpha[t][:, None] + trans + (em[t+1] + beta[t+1])[None, :]
        dtr += np.exp(m - logZ)
        dtr[gold[t], gold[t+1]] -= 1.0
    return nll, dem, dtr

def crf_viterbi(em, trans):
    T_, K = em.shape
    cur = em[0].copy(); back = np.zeros((T_, K), dtype=int)
    for t in range(1, T_):
        m = cur[:, None] + trans
        back[t] = m.argmax(axis=0)
        cur = m.max(axis=0) + em[t]
    path = np.zeros(T_, dtype=int); path[-1] = int(cur.argmax())
    for t in range(T_-1, 0, -1): path[t-1] = back[t][path[t]]
    return path

# ------------------------------------------------------------------ full pass
def forward(p, tri):
    x = embed(p, tri)
    hF, cF = gru_forward(p, 'F', x)
    hB, cB = gru_forward(p, 'B', x)
    enc = np.concatenate([hF, hB], axis=1)
    pooled = enc.mean(axis=0)
    em = enc @ p['tagW'].T + p['tagB']
    logits = {k: p[k+'W'] @ pooled + p[k+'B'] for k in ('isBank','ttype','src')}
    return dict(x=x, hF=hF, hB=hB, cF=cF, cB=cB, enc=enc, pooled=pooled, em=em, logits=logits)

def softmax(z):
    z = z - z.max(); e = np.exp(z); return e / e.sum()

def loss_and_grads(p, sample, w_isbank, w_ttype, w_src, lam=(1.0, 1.0, 0.7, 0.7)):
    tri, tags = sample['tri'], np.array(sample['tags'])
    f = forward(p, tri)
    T_ = len(tri)
    g = {k: np.zeros_like(v) for k, v in p.items()}
    total = 0.0

    # CRF sequence loss
    nll, dem, dtr = crf_nll_and_grads(f['em'], p['crf'], tags)
    total += lam[0] * nll / T_
    dem = dem * (lam[0] / T_); dtr = dtr * (lam[0] / T_)
    g['crf'] += dtr
    g['tagW'] += dem.T @ f['enc']; g['tagB'] += dem.sum(axis=0)
    denc = dem @ p['tagW']

    # three classification heads off the pooled vector
    dpooled = np.zeros(2*H)
    for name, key, wt, lm in (('isBank','isbank',w_isbank,lam[1]),
                              ('ttype','ttype',w_ttype,lam[2]),
                              ('src','src',w_src,lam[3])):
        y = sample[key]; pr = softmax(f['logits'][name])
        cw = wt[y]
        total += lm * cw * -np.log(max(pr[y], 1e-12))
        dl = pr.copy(); dl[y] -= 1.0; dl *= lm * cw
        g[name+'W'] += np.outer(dl, f['pooled']); g[name+'B'] += dl
        dpooled += p[name+'W'].T @ dl
    denc += dpooled[None, :] / T_

    dhF, dhB = denc[:, :H], denc[:, H:]
    dx = gru_backward(p, g, 'F', f['cF'], dhF, f['x'].shape)
    dx += gru_backward(p, g, 'B', f['cB'], dhB, f['x'].shape)
    embed_grad(g, tri, dx)
    return total, g

def predict(p, tri):
    f = forward(p, tri)
    path = crf_viterbi(f['em'], p['crf'])
    return (path,
            softmax(f['logits']['isBank']),
            softmax(f['logits']['ttype']),
            softmax(f['logits']['src']))

# ------------------------------------------------------------------ Adam
class Adam:
    def __init__(self, p, lr=3e-3, b1=0.9, b2=0.999, eps=1e-8):
        self.lr, self.b1, self.b2, self.eps, self.t = lr, b1, b2, eps, 0
        self.m = {k: np.zeros_like(v) for k, v in p.items()}
        self.v = {k: np.zeros_like(v) for k, v in p.items()}
    def step(self, p, g, clip=5.0):
        self.t += 1
        nrm = np.sqrt(sum((gv**2).sum() for gv in g.values()))
        sc = min(1.0, clip/(nrm+1e-12))
        for k in p:
            gk = g[k]*sc
            self.m[k] = self.b1*self.m[k] + (1-self.b1)*gk
            self.v[k] = self.b2*self.v[k] + (1-self.b2)*(gk*gk)
            mh = self.m[k]/(1-self.b1**self.t); vh = self.v[k]/(1-self.b2**self.t)
            p[k] -= self.lr*mh/(np.sqrt(vh)+self.eps)
