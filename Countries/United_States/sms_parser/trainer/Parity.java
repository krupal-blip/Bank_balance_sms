import java.io.*; import java.nio.charset.StandardCharsets; import java.util.*;
import java.util.regex.*;

/** Line-for-line JVM port of ModelWeights.load + Tokenizer + GruCell +
 *  CrfDecoder + SmsParser, used only to prove the exported .bin is read
 *  identically on the JVM (endianness + signed-byte semantics) and that
 *  token ids and output spans match the Python trainer. */
public class Parity {
    static final int VOCAB_SIZE = 4096, MAX_TRI = 8;
    static final Pattern TOKEN_RE = Pattern.compile("[A-Za-z0-9]+|[^\\sA-Za-z0-9]");
    static final String[] FUTURE_MARKERS = {"will be","shall be","is scheduled to","is due to",
        "is going to","is expected to","will get","shall get","would be","scheduled for","scheduled on"};
    static final String FUTURE_TOKEN = "futuremarkertoken";
    static final String[] TAGS = {"O","B-BANK","I-BANK","B-ACCOUNT","I-ACCOUNT","B-AMOUNT",
        "I-AMOUNT","B-BALANCE","I-BALANCE","B-MERCHANT","I-MERCHANT"};
    static final String[] TXN = {"CREDIT","DEBIT","OTHER"};
    static final String[] SRC = {"VIA_BANK","VIA_CARD","NONE"};

    static int fnv1a(String s) {
        int h = 0x811c9dc5;
        for (byte b : s.getBytes(StandardCharsets.UTF_8)) { h ^= (b & 0xff); h *= 0x01000193; }
        return h;
    }
    static int bucket(String s) { return (int)((((long)fnv1a(s)) & 0xffffffffL) % VOCAB_SIZE); }
    static boolean hasFuture(String text) {
        String t = text.toLowerCase();
        for (String m : FUTURE_MARKERS) if (t.contains(m)) return true;
        return false;
    }
    static int[] trigramIds(String tok) {
        if (tok.length() < 3) return new int[]{bucket(tok)};
        int n = Math.min(tok.length()-2, MAX_TRI); int[] o = new int[n];
        for (int i=0;i<n;i++) o[i]=bucket(tok.substring(i,i+3));
        return o;
    }
    static List<String> tokenize(String text) {
        List<String> raw = new ArrayList<>();
        Matcher m = TOKEN_RE.matcher(text.toLowerCase());
        while (m.find()) raw.add(m.group());
        if (hasFuture(text)) raw.add(0, FUTURE_TOKEN);
        return raw;
    }

    static class Tensor { float[] d; int rows, cols; Tensor(float[] d,int r,int c){this.d=d;rows=r;cols=c;}
        float get(int r,int c){return d[r*cols+c];} }
    static int readIntLE(DataInputStream s) throws IOException { return Integer.reverseBytes(s.readInt()); }
    static float readFloatLE(DataInputStream s) throws IOException {
        return Float.intBitsToFloat(Integer.reverseBytes(s.readInt())); }
    static Tensor tensor(DataInputStream s,int rows,int cols) throws IOException {
        float scale = readFloatLE(s); int count = readIntLE(s);
        if (count != rows*cols) throw new IOException("tensor size mismatch: got "+count+" want "+rows*cols);
        byte[] b = new byte[count]; s.readFully(b);
        float[] f = new float[count]; for (int i=0;i<count;i++) f[i]=b[i]*scale;
        return new Tensor(f,rows,cols);
    }
    static Map<String,Tensor> W = new HashMap<>();
    static int VOCAB, EMB, HID, TAGN, MTRI;
    static void load(String path) throws IOException {
        DataInputStream d = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
        byte[] magic = new byte[4]; d.readFully(magic);
        if (!new String(magic).equals("SMSM")) throw new IOException("bad model file magic");
        int ver = readIntLE(d); if (ver != 2) throw new IOException("unsupported version "+ver);
        VOCAB=readIntLE(d); EMB=readIntLE(d); HID=readIntLE(d); TAGN=readIntLE(d); MTRI=readIntLE(d);
        W.put("emb", tensor(d,VOCAB,EMB));
        W.put("wIhF", tensor(d,3*HID,EMB)); W.put("wHhF", tensor(d,3*HID,HID));
        W.put("bIhF", tensor(d,3*HID,1));   W.put("bHhF", tensor(d,3*HID,1));
        W.put("wIhB", tensor(d,3*HID,EMB)); W.put("wHhB", tensor(d,3*HID,HID));
        W.put("bIhB", tensor(d,3*HID,1));   W.put("bHhB", tensor(d,3*HID,1));
        W.put("tagW", tensor(d,TAGN,2*HID)); W.put("tagB", tensor(d,TAGN,1));
        W.put("isBankW", tensor(d,2,2*HID)); W.put("isBankB", tensor(d,2,1));
        W.put("ttypeW", tensor(d,3,2*HID));  W.put("ttypeB", tensor(d,3,1));
        W.put("srcW", tensor(d,3,2*HID));    W.put("srcB", tensor(d,3,1));
        W.put("crf", tensor(d,TAGN,TAGN));
        if (d.read() != -1) throw new IOException("trailing bytes after crfTrans");
        d.close();
    }
    static float sigmoid(float x){ return (float)(1.0/(1.0+Math.exp(-x))); }
    static float[][] gru(String dir, float[][] x) {
        Tensor wIh=W.get("wIh"+dir), wHh=W.get("wHh"+dir);
        float[] bIh=W.get("bIh"+dir).d, bHh=W.get("bHh"+dir).d;
        int T=x.length; float[][] hs=new float[T][HID]; float[] h=new float[HID];
        float[] gi=new float[3*HID], gh=new float[3*HID];
        for (int k=0;k<T;k++) {
            int t = dir.equals("F") ? k : T-1-k;
            for (int r=0;r<3*HID;r++){
                float si=bIh[r], sh=bHh[r];
                for (int c=0;c<EMB;c++) si+=wIh.get(r,c)*x[t][c];
                for (int c=0;c<HID;c++) sh+=wHh.get(r,c)*h[c];
                gi[r]=si; gh[r]=sh;
            }
            for (int i=0;i<HID;i++){
                float r0=sigmoid(gi[i]+gh[i]);
                float z=sigmoid(gi[HID+i]+gh[HID+i]);
                float n=(float)Math.tanh(gi[2*HID+i]+r0*gh[2*HID+i]);
                h[i]=(1f-z)*n+z*h[i];
            }
            System.arraycopy(h,0,hs[t],0,HID);
        }
        return hs;
    }
    static float[] linear(float[] x, Tensor Wt, float[] b){
        float[] o=new float[Wt.rows];
        for(int r=0;r<Wt.rows;r++){ float s=b[r]; for(int c=0;c<x.length;c++) s+=Wt.get(r,c)*x[c]; o[r]=s; }
        return o;
    }
    static float[] softmax(float[] x){
        float m=x[0]; for(float v:x) m=Math.max(m,v);
        float sum=0; float[] e=new float[x.length];
        for(int i=0;i<x.length;i++){ e[i]=(float)Math.exp(x[i]-m); sum+=e[i]; }
        for(int i=0;i<e.length;i++) e[i]/=sum;
        return e;
    }
    static int argmax(float[] x){ int a=0; for(int i=1;i<x.length;i++) if(x[i]>x[a]) a=i; return a; }
    static int[] viterbi(float[][] scores, Tensor trans, int K){
        int T=scores.length; if(T==0) return new int[0];
        float[] cur=scores[0].clone(); int[][] back=new int[T][K]; float[] next=new float[K];
        for(int t=1;t<T;t++){
            for(int j=0;j<K;j++){
                float best=Float.NEGATIVE_INFINITY; int arg=0;
                for(int i=0;i<K;i++){ float s=cur[i]+trans.get(i,j); if(s>best){best=s;arg=i;} }
                next[j]=best+scores[t][j]; back[t][j]=arg;
            }
            System.arraycopy(next,0,cur,0,K);
        }
        int last=0; for(int j=1;j<K;j++) if(cur[j]>cur[last]) last=j;
        int[] path=new int[T]; path[T-1]=last;
        for(int t=T-1;t>0;t--) path[t-1]=back[t][path[t]];
        return path;
    }
    static Map<String,String> mergeSpans(List<String> toks, int[] path){
        List<String[]> spans=new ArrayList<>(); List<List<String>> vals=new ArrayList<>();
        String curType=null; List<String> cur=new ArrayList<>();
        for(int i=0;i<toks.size();i++){
            String tag=TAGS[path[i]];
            if(tag.equals("O")){ if(curType!=null){spans.add(new String[]{curType}); vals.add(cur);} curType=null; cur=new ArrayList<>(); }
            else if(tag.startsWith("B-")){ if(curType!=null){spans.add(new String[]{curType}); vals.add(cur);} curType=tag.substring(2); cur=new ArrayList<>(); cur.add(toks.get(i)); }
            else { String e=tag.substring(2);
                if(e.equals(curType)) cur.add(toks.get(i));
                else { if(curType!=null){spans.add(new String[]{curType}); vals.add(cur);} curType=e; cur=new ArrayList<>(); cur.add(toks.get(i)); } }
        }
        if(curType!=null){ spans.add(new String[]{curType}); vals.add(cur); }
        Map<String,String> out=new LinkedHashMap<>();
        for(int i=0;i<spans.size();i++){
            String ty=spans.get(i)[0]; if(out.containsKey(ty)) continue;
            List<String> tk=vals.get(i);
            out.put(ty, (ty.equals("AMOUNT")||ty.equals("ACCOUNT")||ty.equals("BALANCE"))
                        ? String.join("", tk) : String.join(" ", tk));
        }
        return out;
    }
    public static void main(String[] a) throws Exception {
        load(a[0]);
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(a[1]), StandardCharsets.UTF_8));
        PrintWriter pw=new PrintWriter(new OutputStreamWriter(new FileOutputStream(a[2]), StandardCharsets.UTF_8));
        pw.printf("HEADER\t%d\t%d\t%d\t%d\t%d%n", VOCAB, EMB, HID, TAGN, MTRI);
        String line;
        while((line=br.readLine())!=null){
            if(line.isEmpty()) continue;
            String[] parts=line.split("\t",2);
            String addr=parts[0], body=parts[1];
            String full = addr.trim().isEmpty() ? body : addr+" | "+body;
            List<String> toks=tokenize(full);
            StringBuilder ids=new StringBuilder();
            for(String t:toks){ for(int id:trigramIds(t)) ids.append(id).append(','); ids.append(';'); }
            float[][] x=new float[toks.size()][EMB];
            Tensor emb=W.get("emb");
            for(int t=0;t<toks.size();t++){ int[] tri=trigramIds(toks.get(t));
                for(int c=0;c<EMB;c++){ float s=0; for(int id:tri) s+=emb.get(id,c); x[t][c]=s/tri.length; } }
            float[][] hF=gru("F",x), hB=gru("B",x);
            int T=toks.size();
            float[][] enc=new float[T][2*HID];
            for(int t=0;t<T;t++){ System.arraycopy(hF[t],0,enc[t],0,HID); System.arraycopy(hB[t],0,enc[t],HID,HID); }
            float[] pooled=new float[2*HID];
            for(int t=0;t<T;t++) for(int c=0;c<2*HID;c++) pooled[c]+=enc[t][c]/T;
            float[][] tagScores=new float[T][];
            for(int t=0;t<T;t++) tagScores[t]=linear(enc[t],W.get("tagW"),W.get("tagB").d);
            int[] path=viterbi(tagScores,W.get("crf"),TAGN);
            float[] ib=softmax(linear(pooled,W.get("isBankW"),W.get("isBankB").d));
            float[] tt=softmax(linear(pooled,W.get("ttypeW"),W.get("ttypeB").d));
            float[] sc=softmax(linear(pooled,W.get("srcW"),W.get("srcB").d));
            Map<String,String> f=mergeSpans(toks,path);
            StringBuilder p=new StringBuilder(); for(int v:path) p.append(v).append(',');
            pw.printf("%s\t%s\t%s\t%b\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n",
                String.join(" ",toks), ids, p,
                argmax(ib)==1, TXN[argmax(tt)], SRC[argmax(sc)],
                f.getOrDefault("BANK",""), f.getOrDefault("ACCOUNT",""), f.getOrDefault("AMOUNT",""),
                f.getOrDefault("BALANCE",""), f.getOrDefault("MERCHANT",""));
        }
        pw.close();
    }
}
