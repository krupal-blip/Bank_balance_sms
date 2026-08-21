"""Build a 50-message parity fixture and emit the Python side of it."""
import sys, os
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import numpy as np, tok as T, spans, dataset, verify

d = os.path.dirname(os.path.abspath(__file__))
BIN = '/Volumes/Extra/backup/R&D/Bank_balance/app/src/main/assets/models/sms_model_us.bin'
data, _ = dataset.build()
rng = np.random.default_rng(7)
# stratified: 25 executed, 25 non-events, spread over senders
pos = [s for s in data if s['isbank']==1]; neg = [s for s in data if s['isbank']==0]
pick = [pos[i] for i in rng.choice(len(pos), 25, replace=False)] + \
       [neg[i] for i in rng.choice(len(neg), 25, replace=False)]
with open(os.path.join(d,'fixture.tsv'),'w') as fh:
    for s in pick:
        fh.write('%s\t%s\n' % (s['sender'], s['body'].replace('\t',' ').replace('\n',' ')))
w = verify.load(BIN)
out = open(os.path.join(d,'fixture_py.tsv'),'w')
out.write('HEADER\t%d\t%d\t%d\t%d\t%d\n' % (w['vocab'],w['embDim'],w['hidden'],w['numTags'],w['maxTri']))
for s in pick:
    full = T.runtime_input(s['sender'], s['body'])
    toks, tri = T.tokenize(full)
    ids = ''.join(','.join(str(i) for i in t)+',;' for t in tri)
    r = verify.parse(w, s['sender'], s['body'])
    path = ','.join(str(v) for v in r['tags'])+','
    out.write('%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' % (
        ' '.join(toks), ids, path,
        'true' if r['isBankTxn'] else 'false', r['transactionType'], r['typeOf'],
        r['bankSpan'], r['accountNumber'], r['amount'], r['avlBal'], r['merchantName']))
out.close()
print('fixture: 50 messages (25 executed, 25 non-events)')
