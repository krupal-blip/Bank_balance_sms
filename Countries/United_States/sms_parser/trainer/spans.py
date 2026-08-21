"""Derive BIO tag sequences for the 11-tag CRF from message text.

Field spans are located on the runtime string ("<address> | <body>") with regex,
then projected onto token indices via char offsets, so the tags line up exactly
with what Tokenizer.tokenize produces at inference time.
"""
import re
import tok as T

TAGS = ["O","B-BANK","I-BANK","B-ACCOUNT","I-ACCOUNT","B-AMOUNT","I-AMOUNT",
        "B-BALANCE","I-BALANCE","B-MERCHANT","I-MERCHANT"]
TAG2I = {t:i for i,t in enumerate(TAGS)}

BANK_RE = re.compile(r'Bank of America|Wells Fargo|Citibank|Citi|Chase|BofA')
BAL_RE  = re.compile(r'(?:Avail(?:able)?\s+[Bb]al(?:ance)?|Savings\s+bal|Checking\s+avail\s+bal'
                     r'|Avail\s+credit|Current\s+balance)\s*:?\s*(-?\$?)([\d,]+\.\d\d)')
AMT_RE  = re.compile(r'\$([\d,]+\.\d\d)')
ACCT_PATS = [
    r'from\s+acct\s*\.{0,3}\s*(\d{3,})', r'from\s+account\s+ending\s*(?:in\s*)?(\d{3,})',
    r'from\s+(?:checking|savings)\s*\.{0,3}\s*(\d{3,})',
    r'drafted\s+from\s+account\s+ending\s*(\d{3,})',
    r'posted\s+to\s+(?:your\s+)?account\s+ending\s*(?:in\s*)?(\d{3,})',
    r'(?:credit|debit)\s+card\s+ending\s*(?:in\s*)?(\d{3,})',
    r'card\s+ending\s*(?:in\s*)?(\d{3,})',
    r'(?:acct|account)\s+ending\s*(?:in\s*)?(\d{3,})',
    r'(?:acct|account)\s*\.{2,3}\s*(\d{3,})',
    r'\.\.\.(\d{3,})', r'\bcard\s+(\d{4})\b',
]
MERCH_PATS = [
    r'\bat\s+([A-Z0-9][A-Za-z0-9&.\'/#*-]*(?:\s+[A-Z0-9][A-Za-z0-9&.\'/#*-]*){0,4})'
    r'(?=\s+(?:on\s+\d|with\s|was\s|is\s|has\s|pending|for\s)|[.,])',
    r'\(([A-Z][A-Z0-9 &./-]{2,40}?)\)',
    r'\bfrom\s+([A-Z][A-Z0-9&./-]*(?:\s+[A-Z0-9][A-Z0-9&./-]*){0,4})(?=\s+(?:posted|was|is|tax))',
    r'\bto\s+([A-Z][A-Z0-9&./-]*(?:\s+[A-Z0-9][A-Z0-9&./-]*){0,4})(?=\s+(?:was|from|is|with|on))',
    r'^([A-Z][A-Z ]{3,30}?)\s+sent you',
]

def _span_of_group(m, gi=1):
    return (m.start(gi), m.end(gi))

def field_spans(address, body):
    """Char spans (on the runtime string) for each field present."""
    prefix = f"{address} | " if address.strip() else ""
    off = len(prefix)
    out = {}
    m = BANK_RE.search(body)
    if m: out['BANK'] = (m.start()+off, m.end()+off)
    bal = BAL_RE.search(body)
    if bal: out['BALANCE'] = (bal.start(2)+off, bal.end(2)+off)
    for a in AMT_RE.finditer(body):
        s, e = a.start(1)+off, a.end(1)+off
        if 'BALANCE' in out and not (e <= out['BALANCE'][0] or s >= out['BALANCE'][1]):
            continue                      # that $-amount IS the balance
        out['AMOUNT'] = (s, e); break
    for p in ACCT_PATS:
        m = re.search(p, body, re.I)
        if m:
            out['ACCOUNT'] = (m.start(1)+off, m.end(1)+off); break
    for p in MERCH_PATS:
        m = re.search(p, body, re.M)
        if m:
            s, e = m.start(1)+off, m.end(1)+off
            clash = any(not (e <= v[0] or s >= v[1]) for v in out.values())
            if not clash:
                out['MERCHANT'] = (s, e); break
    return out

def tag_sequence(address, body):
    full = T.runtime_input(address, body)
    toks, offs = T.tokenize_spans(full)
    tags = [0]*len(toks)
    fs = field_spans(address, body)
    for ftype, (s, e) in fs.items():
        first = True
        for i, o in enumerate(offs):
            if o is None: continue
            ts, te = o
            if ts >= s and te <= e:
                tags[i] = TAG2I[('B-' if first else 'I-')+ftype]
                first = False
    return toks, tags, fs

def decode_fields(toks, tags):
    """Mirror of SmsParser.mergeSpans — first span of each type wins."""
    spans, cur_t, cur = [], None, []
    def flush():
        nonlocal cur_t, cur
        if cur_t: spans.append((cur_t, cur))
        cur_t, cur = None, []
    for i, t in enumerate(tags):
        name = TAGS[t]
        if name == 'O': flush()
        elif name.startswith('B-'): flush(); cur_t = name[2:]; cur = [toks[i]]
        else:
            e = name[2:]
            if cur_t == e: cur.append(toks[i])
            else: flush(); cur_t = e; cur = [toks[i]]
    flush()
    out = {}
    for ty, tk in spans:
        if ty in out: continue
        out[ty] = ''.join(tk) if ty in ('AMOUNT','ACCOUNT','BALANCE') else ' '.join(tk)
    return out
