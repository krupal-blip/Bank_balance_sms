# Copy these three verbatim

`GruCell.kt`, `CrfDecoder.kt`, `ModelWeights.kt` contain **no region assumptions**. They
are float math and binary IO. There is no template for them because there is nothing to
fill in.

```bash
cp ../../../src/smsmodel/GruCell.kt      <your>/smsmodel/
cp ../../../src/smsmodel/CrfDecoder.kt   <your>/smsmodel/
cp ../../../src/smsmodel/ModelWeights.kt <your>/smsmodel/
```

| File | What it is | Size |
|---|---|---|
| `GruCell.kt` | One GRU timestep: gates, tanh, sigmoid | ~1.2 KB |
| `CrfDecoder.kt` | Viterbi decode over the tag lattice | ~1.2 KB |
| `ModelWeights.kt` | `DataInputStream` reader + `Tensor(data, rows, cols)` | ~3 KB |

---

## Two things that will bite you anyway

### 1. `GruCell` must be created per `parse()` call — never held as a field

It carries **mutable scratch buffers**. A single shared parser instance handling two
messages concurrently on `Dispatchers.Default` corrupted each other's buffers: measured
**~0.5% of parses returned a wrong or empty amount** under 8-thread load.

The symptom is not a crash. It is a small, load-dependent fraction of wrong amounts —
effectively undebuggable from a crash report. `SmsParser.parse()` as templated creates both
cells locally and is reentrant. Keep it that way.

### 2. `ModelWeights.load()` tensor order is a contract with your trainer

The reader consumes tensors in a fixed order. Your exporter must write that exact order, or
you get garbage output with no error. Order is listed in `../../MODEL_TRAINING.md`, step 4.

If you change any tensor shape, change the Kotlin reader and the Python writer **in the
same commit**, and re-run the parity fixture.
