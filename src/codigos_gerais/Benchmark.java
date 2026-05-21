import sem_flyweight.FlorestaTradicional;
import com_flyweight.FlorestsFlyweight;
//Comparação da implementação tradicional vs flyweight
public class Benchmark {

    private static final int    TOTAL_ARVORES  = 1_000_000;
    private static final int    TOTAL_ESPECIES = 10;
    private static final int    WARMUP_RUNS    = 1;
    private static final int    BENCH_RUNS     = 3;
    private static final String SEP   = "=".repeat(70);
    private static final String SEP_L = "-".repeat(70);

    private static long[] tempoCriacaoTrad = new long[BENCH_RUNS];
    private static long[] tempoProcTrad    = new long[BENCH_RUNS];
    private static long[] tempoCriacaoFly  = new long[BENCH_RUNS];
    private static long[] tempoProcFly     = new long[BENCH_RUNS];
    private static long   memTrad = 0, memFly = 0;

    public static void main(String[] args) throws InterruptedException {
        imprimirCabecalho();
        imprimirAnaliseTeórica();

        System.out.println("\n[AQUECIMENTO] " + WARMUP_RUNS + " rodada(s) de warm-up da JVM...");
        for (int i = 0; i < WARMUP_RUNS; i++) {
            executarTradicional(false);
            executarFlyweight(false);
            System.gc(); Thread.sleep(300);
        }
        System.out.println("[AQUECIMENTO] Concluido. Iniciando benchmark real...\n");

        for (int r = 0; r < BENCH_RUNS; r++) {
            System.out.println(SEP_L);
            System.out.println("  RODADA " + (r + 1) + " / " + BENCH_RUNS);
            System.out.println(SEP_L);

            long[] rt = executarTradicional(true);
            tempoCriacaoTrad[r] = rt[0]; tempoProcTrad[r] = rt[1]; memTrad = rt[2];
            System.gc(); Thread.sleep(500);

            long[] rf = executarFlyweight(r == BENCH_RUNS - 1);
            tempoCriacaoFly[r] = rf[0]; tempoProcFly[r] = rf[1]; memFly = rf[2];
            System.gc(); Thread.sleep(500);
        }

        imprimirRelatorioFinal();
    }

    private static void imprimirCabecalho() {// Apresentação do cenário e ambiente de teste
        System.out.println(SEP);
        System.out.println("  BENCHMARK: Design Pattern Flyweight vs Implementacao Tradicional");
        System.out.println("  Cenario: Sistema de Monitoramento Ambiental - Amazonia");
        System.out.printf("  Arvores: %,d | Especies: %d | Rodadas de medicao: %d%n",
                TOTAL_ARVORES, TOTAL_ESPECIES, BENCH_RUNS);
        System.out.printf("  JVM: %s %s%n",
                System.getProperty("java.vm.name"), System.getProperty("java.version"));
        System.out.println(SEP);
    }

    private static long[] executarTradicional(boolean verbose) throws InterruptedException {
        System.gc(); Thread.sleep(200);
        Runtime rt = Runtime.getRuntime();
        long antes = rt.totalMemory() - rt.freeMemory();

        FlorestaTradicional floresta = new FlorestaTradicional();
        long tCriacao = floresta.popular();

        long depois = rt.totalMemory() - rt.freeMemory();
        long mem = depois - antes;
        long tProc = floresta.processarTodas();

        if (verbose)
            System.out.printf("  [TRADICIONAL] Criacao: %,5d ms | Proc: %,5d ms | Delta Heap: %s%n",
                    tCriacao, tProc, fmt(mem));
        return new long[]{tCriacao, tProc, mem};
    }

    private static long[] executarFlyweight(boolean verbose) throws InterruptedException {
        System.gc(); Thread.sleep(200);
        Runtime rt = Runtime.getRuntime();
        long antes = rt.totalMemory() - rt.freeMemory();

        FlorestsFlyweight floresta = new FlorestsFlyweight();
        long tCriacao = floresta.popular();

        long depois = rt.totalMemory() - rt.freeMemory();
        long mem = depois - antes;
        long tProc = floresta.processarTodas();

        if (verbose) {
            System.out.printf("  [FLYWEIGHT]   Criacao: %,5d ms | Proc: %,5d ms | Delta Heap: %s%n",
                    tCriacao, tProc, fmt(mem));
            System.out.printf("  [FLYWEIGHT]   Flyweights unicos na fabrica: %d (de %,d arvores)%n",
                    floresta.getTotalFlyweightsCriados(), floresta.getTotalArvores());
            floresta.listarEspecies();
        }
        return new long[]{tCriacao, tProc, mem};
    }

    private static void imprimirAnaliseTeórica() {
        System.out.println(SEP);
        System.out.println("  ANALISE TEORICA DE MEMORIA");
        System.out.println("  (JVM 64-bit com CompressedOops - padrao para heap < 32 GB)");
        System.out.println(SEP);

        final int HDR = 12, REF = 4, DBL = 8, FLT = 4, INT = 4, STR = 55;

        System.out.println("\n  Premissas de tamanho de tipos:");
        System.out.printf("    Object header  = %2d bytes  (mark 8B + klass 4B comprimido)%n", HDR);
        System.out.printf("    Referencia     = %2d bytes  (CompressedOops)%n", REF);
        System.out.printf("    double         = %2d bytes%n", DBL);
        System.out.printf("    float          = %2d bytes%n", FLT);
        System.out.printf("    int            = %2d bytes%n", INT);
        System.out.printf("    String media   = %2d bytes  (header + campos + char[] ~20 chars)%n", STR);

        // ArvoreCompleta: HDR + 3*REF(especie,cor,textura) + FLT + 2*DBL + INT + REF(saude)
        // = 12 + 12 + 4 + 16 + 4 + 4 = 52 -> pad to 56
        int camposObj = (3*REF) + FLT + (2*DBL) + INT + REF;
        int bruto     = HDR + camposObj;
        int pad       = (8 - bruto % 8) % 8;
        int objSize   = bruto + pad;

        long strEspeciesTrad = (long) TOTAL_ESPECIES * 3 * STR;
        long strStatusTrad   = (long) TOTAL_ARVORES * STR;
        long totalTrad       = (long) TOTAL_ARVORES * objSize + strEspeciesTrad + strStatusTrad;

        System.out.printf("%n  Tradicional (ArvoreCompleta - %d bytes por objeto):%n", objSize);
        System.out.printf("    Composicao: %dB header + 3*%dB refs + %dB float + 2*%dB doubles + %dB int + %dB ref + %dB pad%n",
                HDR, REF, FLT, DBL, INT, REF, pad);
        System.out.printf("    1.000.000 objetos x %d bytes:         %s%n", objSize, fmt((long)TOTAL_ARVORES*objSize));
        System.out.printf("    Strings especie (pool 10x3):           %s%n", fmt(strEspeciesTrad));
        System.out.printf("    Strings statusSaude (1 por arvore):    %s%n", fmt(strStatusTrad));
        System.out.printf("    TOTAL ESTIMADO (Tradicional):          %s%n", fmt(totalTrad));

        // EspecieArvore: HDR + 3*REF + FLT = 12 + 12 + 4 = 28 -> pad to 32
        int espBruto = HDR + (3*REF) + FLT;
        int espPad   = (8 - espBruto % 8) % 8;
        int espSize  = espBruto + espPad;

        // ArvoreContexto: HDR + REF + 2*DBL + INT + REF = 12 + 4 + 16 + 4 + 4 = 40 -> no pad
        int ctxBruto = HDR + REF + (2*DBL) + INT + REF;
        int ctxPad   = (8 - ctxBruto % 8) % 8;
        int ctxSize  = ctxBruto + ctxPad;

        long bytesEsps = (long) TOTAL_ESPECIES * espSize;
        long bytesCtxs = (long) TOTAL_ARVORES  * ctxSize;
        long strEspFly = (long) TOTAL_ESPECIES * 3 * STR;
        long strStFly  = (long) TOTAL_ARVORES * STR;
        long totalFly  = bytesEsps + bytesCtxs + strEspFly + strStFly;

        System.out.printf("%n  Flyweight:%n");
        System.out.printf("    EspecieArvore (%d bytes): 10 instancias = %s%n", espSize, fmt(bytesEsps));
        System.out.printf("    ArvoreContexto (%d bytes): 1M instancias = %s%n", ctxSize, fmt(bytesCtxs));
        System.out.printf("    Strings especie (pool 10x3):           %s%n", fmt(strEspFly));
        System.out.printf("    Strings statusSaude (1 por arvore):    %s%n", fmt(strStFly));
        System.out.printf("    TOTAL ESTIMADO (Flyweight):            %s%n", fmt(totalFly));

        long economia = totalTrad - totalFly;
        double pct    = 100.0 * economia / totalTrad;
        System.out.printf("%n  Economia teorica de memoria: %s (%.1f%% de reducao)%n", fmt(economia), pct);
        System.out.printf("  Explicacao: ArvoreContexto (%d B) vs ArvoreCompleta (%d B) = %d B menos por objeto%n",
                ctxSize, objSize, objSize - ctxSize);
        System.out.printf("  Os %d bytes de (3 refs de especie + float) deixam de ser%n", 3*REF+FLT);
        System.out.printf("  replicados 1.000.000 vezes — existem apenas 10 vezes na fabrica.%n");
    }

    private static void imprimirRelatorioFinal() {
        System.out.println("\n" + SEP);
        System.out.println("  RELATORIO FINAL - MEDIAS DE " + BENCH_RUNS + " RODADAS");
        System.out.println(SEP);

        long mCT = media(tempoCriacaoTrad), mPT = media(tempoProcTrad);
        long mCF = media(tempoCriacaoFly),  mPF = media(tempoProcFly);

        System.out.printf("%n  %-40s %10s %10s%n", "METRICA", "TRADICIONAL", "FLYWEIGHT");
        System.out.println("  " + "-".repeat(62));
        System.out.printf("  %-40s %,8d ms %,8d ms%n", "Criacao (1M objetos)", mCT, mCF);
        System.out.printf("  %-40s %,8d ms %,8d ms%n", "Processamento (1M iteracoes)", mPT, mPF);
        System.out.printf("  %-40s %10s %10s%n", "Memoria heap Delta", fmt(memTrad), fmt(memFly));
        System.out.println("  " + "-".repeat(62));

        double spCriacao = (double) mCT / Math.max(mCF, 1);
        double spProc    = (double) mPT / Math.max(mPF, 1);
        long   memGain   = memTrad - memFly;
        double memPct    = memGain > 0 ? 100.0 * memGain / memTrad : 0;

        System.out.printf("%n  Speedup criacao:         %.2fx (%s)%n",
                spCriacao, spCriacao >= 1 ? "Flyweight mais rapido" : "Tradicional mais rapido");
        System.out.printf("  Speedup processamento:   %.2fx (%s)%n",
                spProc, spProc >= 1 ? "Flyweight mais rapido" : "Tradicional mais rapido");
        System.out.printf("  Economia de heap medida: %s (%.1f%% menos)%n", fmt(memGain), memPct);

        System.out.println("\n" + SEP);
        System.out.println("  CONCLUSAO");
        System.out.println(SEP);
        System.out.println("\n  Flyweight indicado quando:");
        System.out.println("    [OK] Muitos objetos com dados repetidos (1M arvores / 10 especies)");
        System.out.println("    [OK] Estado claramente dividido em intrinseco x extrinseco");
        System.out.println("    [OK] Gargalo eh memoria RAM, nao CPU");
        System.out.println("\n  Tradeoffs:");
        System.out.println("    [!] Codigo mais complexo - separacao de responsabilidades obrigatoria");
        System.out.println("    [!] Leve overhead de indiracao por chamada de metodo");
        System.out.println("    [!] Flyweights devem ser imutaveis (facilitam thread-safety)");
    }

    private static long media(long[] v) {
        long s = 0;
        for (long x : v) s += x;
        return s / v.length;
    }

    static String fmt(long b) {
        if (b < 0)               return "-" + fmt(-b);
        if (b < 1_024)           return b + " B";
        if (b < 1_048_576)       return String.format("%.2f KB", b / 1024.0);
        if (b < 1_073_741_824L)  return String.format("%.2f MB", b / 1_048_576.0);
        return String.format("%.2f GB", b / 1_073_741_824.0);
    }
}
