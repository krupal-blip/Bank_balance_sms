import sys, os, time, pickle, collections
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np, model, dataset, spans
from model import V, E, H, NT, sigmoid

RNG = np.random.default_rng(20260821)

# ---------------------------------------------------- phase 1: self-supervised
def pretrain(p, data, epochs=4, win=2, neg=5, lr=0.05):
    """CBOW over trigram buckets with negative sampling. Shapes the embedding
    table only — this is the phase MODEL_TRAINING.md says teaches the space that
    money-movement verbs are interchangeable (shared trigrams -> shared vectors).
    """
    freq = np.zeros(V)
    for s in data:
        for ids in s['tri']:
            for i in ids: freq[i] += 1
    dist = (freq ** 0.75); dist /= dist.sum()
    emb = p['emb']
    for ep in range(epochs):
        tot, n = 0.0, 0
        for s in RNG.permutation(len(data)):
            tri = data[s]['tri']; Tn = len(tri)
            vecs = np.array([emb[ids].mean(axis=0) for ids in tri])
            for t in range(Tn):
                lo, hi = max(0, t-win), min(Tn, t+win+1)
                ctx_idx = [i for i in range(lo, hi) if i != t]
                if not ctx_idx: continue
                ctx = vecs[ctx_idx].mean(axis=0)
                pos = tri[t]
                negs = RNG.choice(V, size=neg, p=dist)
                pv = emb[pos].mean(axis=0)
                sp = sigmoid(ctx @ pv)
                tot += -np.log(max(sp, 1e-12)); n += 1
                dctx = (sp - 1.0) * pv
                gpos = (sp - 1.0) * ctx
                for i in pos: emb[i] -= lr * gpos / len(pos)
                for j in negs:
                    sn = sigmoid(ctx @ emb[j])
                    tot += -np.log(max(1.0-sn, 1e-12))
                    dctx += sn * emb[j]
                    emb[j] -= lr * sn * ctx
                for i in ctx_idx:
                    for b in tri[i]:
                        emb[b] -= lr * dctx / (len(ctx_idx) * len(tri[i]))
        print('  pretrain epoch %d/%d  loss/token %.4f' % (ep+1, epochs, tot/max(n,1)))
    return p

# ---------------------------------------------------- evaluation
def evaluate(p, data):
    ib = collections.Counter(); tt = tc = sc = st = 0; tokok = toktot = 0
    fields = collections.Counter(); ftot = collections.Counter()
    for s in data:
        path, pib, ptt, psrc = model.predict(p, s['tri'])
        pred = int(pib.argmax())
        ib[('tp' if pred and s['isbank'] else 'tn' if not pred and not s['isbank']
            else 'fp' if pred else 'fn')] += 1
        tc += int(ptt.argmax() == s['ttype']); sc += int(psrc.argmax() == s['src']); st += 1
        gold = np.array(s['tags'])
        tokok += int((path == gold).sum()); toktot += len(gold)
        toks = [None]*len(gold)
        gf = spans.decode_fields([str(i) for i in range(len(gold))], gold)
        pf = spans.decode_fields([str(i) for i in range(len(gold))], path)
        for k in set(gf) | set(pf):
            ftot[k] += 1
            if gf.get(k) == pf.get(k): fields[k] += 1
    tp, fp, fn, tn = ib['tp'], ib['fp'], ib['fn'], ib['tn']
    prec = tp/max(tp+fp,1); rec = tp/max(tp+fn,1)
    return dict(isbank_acc=(tp+tn)/max(st,1), prec=prec, rec=rec,
                f1=2*prec*rec/max(prec+rec,1e-9), fp=fp, fn=fn,
                ttype_acc=tc/max(st,1), src_acc=sc/max(st,1),
                tag_acc=tokok/max(toktot,1),
                fields={k: fields[k]/max(ftot[k],1) for k in ftot})

def class_weights(data, key, n):
    c = collections.Counter(s[key] for s in data)
    tot = sum(c.values())
    return np.array([tot/(n*max(c[i],1)) for i in range(n)])

def main():
    data, dups = dataset.build()
    print('corpus: %d unique rows (%d exact duplicates dropped)' % (len(data), dups))
    idx = RNG.permutation(len(data))
    pos = [i for i in idx if data[i]['isbank']==1]; neg = [i for i in idx if data[i]['isbank']==0]
    nv_p, nv_n = int(0.15*len(pos)), int(0.15*len(neg))
    val_i = set(pos[:nv_p]) | set(neg[:nv_n])
    train = [data[i] for i in range(len(data)) if i not in val_i]
    val   = [data[i] for i in range(len(data)) if i in val_i]
    print('split: train %d / val %d' % (len(train), len(val)))

    w_ib = class_weights(train,'isbank',2); w_tt = class_weights(train,'ttype',3)
    w_sc = class_weights(train,'src',3)
    print('class weights  isbank %s  ttype %s  src %s' %
          (np.round(w_ib,2), np.round(w_tt,2), np.round(w_sc,2)))

    p = model.init_params()
    print('phase 1 — self-supervised trigram pretrain')
    t0=time.time(); pretrain(p, train); print('  (%.1fs)' % (time.time()-t0))

    print('phase 2 — supervised finetune (CRF + 3 heads)')
    opt = model.Adam(p, lr=4e-3)
    best, best_p = -1, None
    EPOCHS = 30
    for ep in range(EPOCHS):
        opt.lr = 4e-3 * (0.5 ** (ep/12.0))
        t0 = time.time(); tot = 0.0
        for i in RNG.permutation(len(train)):
            L, g = model.loss_and_grads(p, train[i], w_ib, w_tt, w_sc)
            opt.step(p, g); tot += L
        m = evaluate(p, val)
        score = m['f1'] + m['tag_acc'] + m['ttype_acc'] + m['src_acc']
        if score > best:
            best, best_p = score, {k: v.copy() for k, v in p.items()}
        print('  ep %2d loss %7.3f | val isbank acc %.3f f1 %.3f (fp %d fn %d) | ttype %.3f src %.3f | tag %.3f | %.0fs'
              % (ep+1, tot/len(train), m['isbank_acc'], m['f1'], m['fp'], m['fn'],
                 m['ttype_acc'], m['src_acc'], m['tag_acc'], time.time()-t0))
    p = best_p
    print('\nbest checkpoint metrics:')
    for name, ds in (('val', val), ('train', train)):
        m = evaluate(p, ds)
        print('  %-5s isbank acc %.3f  P %.3f  R %.3f  F1 %.3f | ttype %.3f | src %.3f | tag %.3f'
              % (name, m['isbank_acc'], m['prec'], m['rec'], m['f1'], m['ttype_acc'], m['src_acc'], m['tag_acc']))
        print('        field exact-match: %s' % {k: round(v,3) for k,v in sorted(m['fields'].items())})
    pickle.dump(p, open(os.path.join(os.path.dirname(os.path.abspath(__file__)),'weights.pkl'),'wb'))
    print('\nsaved weights.pkl')

if __name__ == '__main__':
    main()
