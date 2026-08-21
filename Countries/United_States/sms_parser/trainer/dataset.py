import csv, sys, os, collections
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import tok as T, labeler, spans

CSV = '/Volumes/Extra/backup/R&D/Bank_balance/Countries/United_States/sms_parser/us_training_corpus_v1.csv'
TTYPE = ['CREDIT','DEBIT','OTHER']
SRC   = ['VIA_BANK','VIA_CARD','NONE']

def build(dedupe=True):
    rows = list(csv.DictReader(open(CSV)))
    seen, data, dups = set(), [], 0
    for r in rows:
        sender, body = r['sender'], r['body']
        key = (sender, body)
        if dedupe and key in seen:
            dups += 1
            continue
        seen.add(key)
        executed = labeler.is_executed(sender, body)
        toks, tags, _ = spans.tag_sequence(sender, body)
        if not toks: continue
        data.append({
            'sender': sender, 'body': body,
            'tri': [T.trigram_ids(t) for t in toks],
            'tags': tags,
            'isbank': 1 if executed else 0,
            'ttype': TTYPE.index(labeler.txn_type(sender, body, executed)),
            'src':   SRC.index(labeler.source(sender, body, executed)),
            'ntok': len(toks),
        })
    return data, dups

if __name__ == '__main__':
    d, dups = build()
    c = collections.Counter((x['isbank'], TTYPE[x['ttype']], SRC[x['src']]) for x in d)
    print('rows kept %d (exact duplicates dropped: %d)' % (len(d), dups))
    print('isbank  1=%d 0=%d' % (sum(x['isbank'] for x in d), sum(1-x['isbank'] for x in d)))
    print('ttype  ', collections.Counter(TTYPE[x['ttype']] for x in d))
    print('src    ', collections.Counter(SRC[x['src']] for x in d))
    print('tokens  min=%d max=%d mean=%.1f' % (min(x['ntok'] for x in d), max(x['ntok'] for x in d),
                                               sum(x['ntok'] for x in d)/len(d)))
    tg = collections.Counter()
    for x in d:
        for t in x['tags']: tg[spans.TAGS[t]] += 1
    print('tag distribution:', dict(tg.most_common()))
