"""Tokenizer — byte-for-byte parity with Templates/.../smsmodel/Tokenizer.kt"""
import re
VOCAB_SIZE = 4096
MAX_TRI = 8
TOKEN_RE = re.compile(r"[A-Za-z0-9]+|[^\sA-Za-z0-9]")
# Kotlin Tokenizer.FUTURE_MARKERS + the markers named in the training spec
# (.ai/ML_MODEL_TRAINING_GUIDELINES.md). Tokenizer.kt is patched to the same
# list in this commit so trainer and runtime cannot drift.
FUTURE_MARKERS = ["will be", "shall be", "is scheduled to", "is due to", "is going to",
                  "is expected to", "will get", "shall get", "would be",
                  "scheduled for", "scheduled on"]
FUTURE_TOKEN = "futuremarkertoken"

def fnv1a(s):
    h = 0x811c9dc5
    for b in s.encode('utf-8'):
        h ^= b
        h = (h * 0x01000193) & 0xFFFFFFFF
    return h

def bucket(s):
    return fnv1a(s) % VOCAB_SIZE

def has_future_marker(text):
    t = text.lower()
    return any(m in t for m in FUTURE_MARKERS)

def trigram_ids(tok):
    if len(tok) < 3:
        return [bucket(tok)]
    n = min(len(tok) - 2, MAX_TRI)
    return [bucket(tok[i:i+3]) for i in range(n)]

def tokenize(text):
    """Returns (tokens, trigram_id_lists). Mirrors Tokenizer.tokenize."""
    raw = TOKEN_RE.findall(text.lower())
    if has_future_marker(text):
        raw.insert(0, FUTURE_TOKEN)
    return raw, [trigram_ids(t) for t in raw]

def tokenize_spans(text):
    """Same tokens, plus (start,end) char offsets into `text` (None for the
    synthetic future token). Needed to project regex-found field spans onto
    token indices when building CRF labels."""
    low = text.lower()
    toks, spans = [], []
    for m in TOKEN_RE.finditer(low):
        toks.append(m.group()); spans.append((m.start(), m.end()))
    if has_future_marker(text):
        toks.insert(0, FUTURE_TOKEN); spans.insert(0, None)
    return toks, spans

def runtime_input(address, body):
    """SmsParser.parse prepends the sender exactly like this."""
    return f"{address} | {body}" if address.strip() else body
