"""Read the exported .bin the way ModelWeights.load() does, run the same
forward pass SmsParser does, and confirm the quantized binary reproduces the
float model. This is the Python half of the parity fixture."""
import struct, sys, os, pickle, math
import numpy as np
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import tok as T, spans, dataset, model
from export import ORDER

def load(path):
    b = open(path,'rb').read(); o = 0
    assert b[0:4] == b'SMSM', 'bad magic'
    o = 4
    ver, vocab, emb, hid, tags, maxtri = struct.unpack_from('<6i', b, o); o += 24
    assert ver == 2, 'version %d' % ver
    shapes = {'emb':(vocab,emb),'wIhF':(3*hid,emb),'wHhF':(3*hid,hid),'bIhF':(3*hid,1),
              'bHhF':(3*hid,1),'wIhB':(3*hid,emb),'wHhB':(3*hid,hid),'bIhB':(3*hid,1),
              'bHhB':(3*hid,1),'tagW':(tags,2*hid),'tagB':(tags,1),'isBankW':(2,2*hid),
              'isBankB':(2,1),'ttypeW':(3,2*hid),'ttypeB':(3,1),'srcW':(3,2*hid),
              'srcB':(3,1),'crf':(tags,tags)}
    w = {}
    for name in ORDER:
        r, c = shapes[name]
        scale, = struct.unpack_from('<f', b, o); o += 4
        count, = struct.unpack_from('<i', b, o); o += 4
        assert count == r*c, 'tensor size mismatch %s: %d != %d' % (name, count, r*c)
        arr = np.frombuffer(b, dtype=np.int8, count=count, offset=o).astype(np.float32)*scale
        o += count
        w[name] = arr.reshape(r, c) if c > 1 else arr.reshape(-1)
    assert o == len(b), 'trailing bytes: read %d of %d' % (o, len(b))
    return dict(vocab=vocab, embDim=emb, hidden=hid, numTags=tags, maxTri=maxtri, **w)

def sigmoid(x): return 1.0/(1.0+np.exp(-x))

def gru(w, d, x, Hh):
    Tn = x.shape[0]; hs = np.zeros((Tn, Hh), dtype=np.float32); h = np.zeros(Hh, dtype=np.float32)
    order = range(Tn) if d=='F' else range(Tn-1,-1,-1)
    for t in order:
        gi = w['wIh'+d] @ x[t] + w['bIh'+d]
        gh = w['wHh'+d] @ h  + w['bHh'+d]
        r = sigmoid(gi[:Hh]+gh[:Hh]); z = sigmoid(gi[Hh:2*Hh]+gh[Hh:2*Hh])
        n = np.tanh(gi[2*Hh:] + r*gh[2*Hh:])
        h = (1-z)*n + z*h; hs[t] = h
    return hs

def parse(w, address, body):
    """Mirrors SmsParser.parse(body, address)."""
    full = T.runtime_input(address, body)
    toks, tri = T.tokenize(full)
    if not toks: return None
    Hh = w['hidden']
    x = np.array([w['emb'][ids].mean(axis=0) for ids in tri], dtype=np.float32)
    enc = np.concatenate([gru(w,'F',x,Hh), gru(w,'B',x,Hh)], axis=1)
    pooled = enc.mean(axis=0)
    em = enc @ w['tagW'].T + w['tagB']
    path = model.crf_viterbi(em, w['crf'])
    def sm(z):
        z = z - z.max(); e = np.exp(z); return e/e.sum()
    ib = sm(w['isBankW'] @ pooled + w['isBankB'])
    tt = sm(w['ttypeW'] @ pooled + w['ttypeB'])
    sc = sm(w['srcW'] @ pooled + w['srcB'])
    f = spans.decode_fields(toks, path)
    return dict(isBankTxn=bool(ib.argmax()==1), confidence=float(ib.max()),
                transactionType=['CREDIT','DEBIT','OTHER'][int(tt.argmax())],
                typeOf=['VIA_BANK','VIA_CARD','NONE'][int(sc.argmax())],
                bankSpan=f.get('BANK',''), accountNumber=f.get('ACCOUNT',''),
                amount=f.get('AMOUNT',''), avlBal=f.get('BALANCE',''),
                merchantName=f.get('MERCHANT',''), tags=path.tolist(), tokens=toks)

if __name__ == '__main__':
    d = os.path.dirname(os.path.abspath(__file__))
    binp = sys.argv[1] if len(sys.argv)>1 else '/Volumes/Extra/backup/R&D/Bank_balance/app/src/main/assets/models/sms_model_us.bin'
    w = load(binp)
    print('header OK: vocab=%d embDim=%d hidden=%d numTags=%d maxTri=%d' %
          (w['vocab'], w['embDim'], w['hidden'], w['numTags'], w['maxTri']))
    p = pickle.load(open(os.path.join(d,'weights.pkl'),'rb'))
    data, _ = dataset.build()
    agree = tot = 0; ib_ok = tt_ok = sc_ok = tag_ok = tag_tot = 0
    for s in data:
        r = parse(w, s['sender'], s['body'])
        fp, fib, ftt, fsc = model.predict(p, s['tri'])
        tot += 1
        agree += int(r['isBankTxn'] == bool(fib.argmax()==1)
                     and r['transactionType'] == ['CREDIT','DEBIT','OTHER'][int(ftt.argmax())]
                     and r['typeOf'] == ['VIA_BANK','VIA_CARD','NONE'][int(fsc.argmax())]
                     and r['tags'] == fp.tolist())
        ib_ok += int(r['isBankTxn'] == bool(s['isbank']))
        tt_ok += int(r['transactionType'] == dataset.TTYPE[s['ttype']])
        sc_ok += int(r['typeOf'] == dataset.SRC[s['src']])
        g = s['tags']; tag_ok += sum(1 for a,b2 in zip(r['tags'], g) if a==b2); tag_tot += len(g)
    print('quantized-vs-float agreement on all %d rows: %.2f%%' % (tot, 100.0*agree/tot))
    print('quantized model vs labels: isbank %.3f  ttype %.3f  src %.3f  tag-token %.3f' %
          (ib_ok/tot, tt_ok/tot, sc_ok/tot, tag_ok/tag_tot))
