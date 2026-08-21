"""Weak-supervision label derivation.

The corpus CSV carries the OLD parser's own predictions in is_transaction /
txn_type / source, and that parser is the one measured at ~90% with ~30 false
positives (non-events scored as executed transactions). Training on its output
would bake the bug in, so labels are re-derived here from message semantics and
then validated against the per-batch expected_summary ground truth in the
processed XMLs.
"""
import re

NOISE_SENDERS = {'89887','55123','262966','22000','37777','86753','672566','54321','77958'}
BANK_SENDERS  = {'24273':'Chase','322632':'Bank of America','93557':'Wells Fargo',
                 '692484':'Citibank','95686':'Citibank','227898':'Capital One'}

# --- executed-movement evidence -------------------------------------------------
POS = [r'\bwas credited\b', r'\bwas debited\b', r'\bhas been debited\b', r'\bposted to\b',
       r'\bwas charged to\b', r'\bwas charged the final amount\b', r'\byou made a\b',
       r'\bpurchase of\b', r'\bdebit card purchase\b', r'\bwas sent from\b',
       r'\bis complete\b', r'\bwas accepted\b', r'\bhas cleared\b', r'\bwas assessed\b',
       r'\bwas drafted\b', r'\bwe received your (?:autopay )?payment\b', r'\bdeposited to\b',
       r'\byou withdrew\b', r'\byou sent\b', r'\bsent you\b', r'\bwas applied to\b',
       r'\bwas credited back\b', r'\bwas received on\b', r'\bwas reversed and debited\b',
       r'\bhas been reversed and debited\b', r'\binterest of \$', r'\bdirect deposit of\b',
       r'\ba deposit of\b', r'\ban? (?:ach|external|closing|balance transfer) (?:credit|debit|transfer|payment)\b',
       r'\bincoming wire of\b', r'\bwas taken on\b', r'\bfee of \$[\d,.]+ (?:was|posted)\b',
       r'\bbonus was credited\b', r'\bwas applied\b', r'\bwas correc?ted after\b',
       # phrasings used by the earlier batches (2-5)
       r'payment of \$[\d,.]+ received', r'\byou transferred\b', r'\bwas received into\b',
       r'\ba cash deposit of\b', r'\bwas made at atm\b',
       r'was returned .*and reversed', r'\bwas returned\b.*\breversed\b',
       r'deposit of \$[\d,.]+ received', r'\bhas been applied\b']

# --- non-event evidence (checked BEFORE falling through to POS) ------------------
NEG = [
  # OTP / codes
  r'one-time code', r'access code', r'verification code', r'authorization code',
  r'\bis your (?:citi )?(?:access|verification|authorization)\b', r'^g-\d+ is your',
  # marketing
  r'txt stop', r'reply info', r'reply join', r'opt out', r'\bno credit check\b',
  # scheduled / future
  r'is scheduled (?:for|to|on)', r'will be sent on', r'will be sent from', r'will draft',
  r'no funds have been sent', r'no money has moved', r'will be charged when',
  r'scheduled transfer notice', r'reminder -', r'\bwill be available on\b.*\bavailable now\b',
  r'first payment of', r'\bplan was created\b', r'\bpayments of \$',
  # holds / pre-auth / declines
  r'pending authorization', r'\ba (?:temporary )?hold of\b', r'\bhold placed by\b',
  r'has been released', r'may vary', r'was declined', r'\bdeclined at\b',
  r'no charge was made', r'\bwas locked\b',
  # fraud prompts / phishing
  r'reply yes', r'reply 1 if', r'text fraud', r'did you attempt', r'restore access',
  r'verify your identity', r'confirm your identity', r'-verify\.example', r'-secure',
  r'unclaimed refund', r'claim it now',
  # statements / informational
  r'statement (?:is|has|will|for)', r'is (?:now )?available', r'minimum payment due',
  r'no action needed', r'is ready', r'terms of service', r'permissions', r'paperless',
  r'\bapy\b', r'1099', r'informed delivery', r'out for delivery', r'was delivered',
  r'has shipped', r'arriving', r'scheduled for delivery', r'could not deliver',
  r'delivery attempted', r'being held at your local post office',
  # account admin / lifecycle notices
  r'is now active', r'has been added', r'has been removed', r'is being processed',
  r'is now closed', r'was cancelled', r'expires \d', r'replacement card', r'is on its way',
  r'username was created', r'set up account alerts', r'was updated', r'new sign-in',
  r'signed in', r'is now linked', r'linked to chase', r'is now set up', r'was created',
  r'you.?re approved', r'application is approved', r'earn a \$', r'\bbonus when\b',
  r'\bwill show\b', r'\bpromotional period\b', r'\bcredit line on card ending\b.*\bremains\b',
  r'\bgo paperless\b', r'\bmobile check deposit is now available\b',
  r'\bwithdrawals from savings\b', r'\bpermitted withdrawals\b',
  # rewards / accrued-but-unposted
  r'points', r'cash rewards', r'cash back', r"you'?ve earned", r'will post next cycle',
  r'earned \$[\d,.]+ in interest', r'redeem',
  # dispute process (not money)
  r'we have received your dispute', r'dispute (?:for|of) the', r'has been opened',
  r'has been filed', r'is (?:still )?under review', r'has responded',
  r'resolved in your favour', r'resolved in your favor', r'not in your favor',
  r'is now permanent', r'will be reversed', r'has been decided', r'how disputes',
  r'\bcase [a-z0-9-]+ is\b',
  # requests / other people's money
  r'requested \$', r'is requesting', r'open venmo', r'funds are in your venmo',
  r'has now enrolled', r'recipient list', r'update your payment method',
  r"couldn'?t process your payment", r'price changes to', r'plan renews',
  r'\bnegative balance of\b', r'\bbelow your \$', r'\bis below\b', r'\bdeposit funds\b',
  r'\bfully available\b', r'\bverify the two small deposits\b',
  r'\bfinish linking\b', r'\bcash deposits are verified\b',
  r'\bmaintenance fee\b.*\bwaive\b', r'\bletting us know\b', r'\bcards will work\b',
  r'\bforeign transaction fee may apply\b', r'\bunaffected\b',
]
POS_RE = [re.compile(p) for p in POS]
NEG_RE = [re.compile(p) for p in NEG]

def is_executed(sender, body):
    b = body.lower()
    if sender in NOISE_SENDERS or sender.startswith('+'):
        return False
    if sender not in BANK_SENDERS:
        return False
    neg = any(r.search(b) for r in NEG_RE)
    pos = any(r.search(b) for r in POS_RE)
    if neg and not pos:
        return False
    if neg and pos:
        # both fired: an executed movement wins only on an unambiguous settle
        # phrase, so "will be sent on"-style notices stay negative
        strong = re.search(r'(charged the final amount|was credited|was debited|has been debited'
                           r'|you made a \$[\d,.]+ debit card purchase|payment of \$[\d,.]+ received'
                           r'|you transferred|was received into|a cash deposit of'
                           r'|posted to|is complete|we received your|was drafted|was accepted'
                           r'|has cleared|deposited to|was received on|was applied to'
                           r'|was reversed and debited|was assessed|was sent from)', b)
        return bool(strong)
    return pos

CREDIT_RE = re.compile(r'(direct deposit|deposit of|was credited|a credit of|credited to|credited back'
                       r'|refund|sent you|we received your|interest of|bonus was credited'
                       r'|provisional credit of|cash deposit|payout|ach credit|incoming wire'
                       r'|balance transfer payment|was applied to your (?:credit card|account)'
                       r'|reversal\) posted|duplicate charge reversal)')
DEBIT_RE  = re.compile(r'(purchase|withdrew|withdrawal|was debited|has been debited|debit of'
                       r'|fee of|was charged|is complete|was sent from|you sent|ach debit'
                       r'|has cleared|was assessed|was drafted|adjustment of|reversed and debited'
                       r'|closing transfer|cash advance|annual membership fee|balance transfer of'
                       r'|bill pay payment|transfer of)')

def txn_type(sender, body, executed):
    if not executed: return 'OTHER'
    b = body.lower()
    # a reversed provisional credit is a debit even though "credit" appears
    if re.search(r'(reversed and debited|has been reversed)', b): return 'DEBIT'
    if CREDIT_RE.search(b) and not re.search(r'(you sent|purchase|withdrew|fee of)', b):
        return 'CREDIT'
    if DEBIT_RE.search(b): return 'DEBIT'
    return 'CREDIT' if CREDIT_RE.search(b) else 'OTHER'

def source(sender, body, executed):
    if not executed: return 'NONE'
    b = body.lower()
    if re.search(r'from acct|from account ending|from checking|from savings', b): return 'VIA_BANK'
    if re.search(r'card ending|credit card|debit card|on card \d', b): return 'VIA_CARD'
    return 'VIA_BANK'
