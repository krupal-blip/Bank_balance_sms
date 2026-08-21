"""Export int8-quantized weights in ModelWeights.load()'s exact tensor order.

File layout (all multi-byte values little-endian, matching readIntLE/readFloatLE):
  magic  "SMSM"                       4 bytes
  version int32 = 2
  vocabSize, embDim, hidden, numTags, maxTri   5 x int32
  then 18 tensors, each:  float32 scale | int32 count | count x int8
  dequantized by the reader as  value = byte * scale
"""
import struct, sys, os, pickle
import numpy as np
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from model import V, E, H, NT
from tok import MAX_TRI

ORDER = ['emb','wIhF','wHhF','bIhF','bHhF','wIhB','wHhB','bIhB','bHhB',
         'tagW','tagB','isBankW','isBankB','ttypeW','ttypeB','srcW','srcB','crf']

def quant(a):
    a = np.asarray(a, dtype=np.float64)
    mx = float(np.abs(a).max())
    scale = mx/127.0 if mx > 0 else 1.0
    q = np.clip(np.rint(a/scale), -127, 127).astype(np.int8)
    err = float(np.abs(q.astype(np.float64)*scale - a).max())
    return scale, q, err

def export(p, path):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    out = bytearray()
    out += b'SMSM'
    for n in (2, V, E, H, NT, MAX_TRI):
        out += struct.pack('<i', n)
    report = []
    for name in ORDER:
        a = p[name]
        a2 = a.reshape(a.shape[0], -1) if a.ndim > 1 else a.reshape(-1, 1)
        scale, q, err = quant(a2)
        out += struct.pack('<f', scale)
        out += struct.pack('<i', q.size)
        out += q.tobytes(order='C')
        report.append((name, a2.shape, scale, err))
    open(path,'wb').write(bytes(out))
    return report

if __name__ == '__main__':
    d = os.path.dirname(os.path.abspath(__file__))
    p = pickle.load(open(os.path.join(d,'weights.pkl'),'rb'))
    dest = sys.argv[1] if len(sys.argv) > 1 else '/Volumes/Extra/backup/R&D/Bank_balance/app/src/main/assets/models/sms_model_us.bin'
    rep = export(p, dest)
    print('wrote %s (%.1f KB)' % (dest, os.path.getsize(dest)/1024.0))
    print('%-10s %-12s %12s %12s' % ('tensor','shape','scale','max q-err'))
    for n, s, sc, e in rep:
        print('%-10s %-12s %12.3e %12.3e' % (n, 'x'.join(map(str,s)), sc, e))
