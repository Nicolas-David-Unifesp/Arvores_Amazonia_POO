package com_flyweight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 Contador de floresta COM o padrão Flyweight.
 Cria 1.000.000 dados leves com apenas 10 flyweights de espécie.
 */
public class FlorestsFlyweight {

    private static final int TOTAL_ARVORES = 1_000_000;

    private static final Object[][] ESPECIES = {
        {"Ipê",          "Amarelo vibrante",   "Rugosa profunda",    40f},
        {"Castanheira",  "Verde escuro",        "Escamosa grossa",    50f},
        {"Sumaúma",      "Verde médio",         "Lisa esbranquiçada", 70f},
        {"Andiroba",     "Verde amarelado",     "Fissurada",          35f},
        {"Mogno",        "Verde brilhante",     "Estriada",           45f},
        {"Cedro",        "Verde grisalho",      "Reticulada",         30f},
        {"Pau-Brasil",   "Verde claro",         "Áspera irregular",   15f},
        {"Seringueira",  "Verde intenso",       "Levemente rugosa",   30f},
        {"Açaí",         "Verde vivo",          "Lisa fibrosa",       25f},
        {"Guaraná",      "Verde musgo",         "Delgada lisa",       10f}
    };

    private static final String[] STATUS_SAUDE = {"Saudável", "Doente", "Seca"};

    private final FabricaEspecies fabrica = new FabricaEspecies();
    private final List<ArvoreContexto> arvores = new ArrayList<>(TOTAL_ARVORES);

    public long popular() {
        Random random = new Random(42);
        long inicio = System.currentTimeMillis();

        for (int i = 0; i < TOTAL_ARVORES; i++) {
            Object[] esp = ESPECIES[i % ESPECIES.length];

            EspecieArvore flyweight = fabrica.obterEspecie(
                (String) esp[0], (String) esp[1], (String) esp[2], (float) esp[3]);

            arvores.add(new ArvoreContexto(
                flyweight,
                -73.0 + random.nextDouble() * 13.0,
                -10.0 + random.nextDouble() * 12.0,
                random.nextInt(500) + 1,
                STATUS_SAUDE[random.nextInt(3)]
            ));
        }

        return System.currentTimeMillis() - inicio;
    }

    public long processarTodas() {
        long inicio = System.currentTimeMillis();
        for (ArvoreContexto a : arvores) a.processar();
        return System.currentTimeMillis() - inicio;
    }

    public int getTotalArvores()           { return arvores.size(); }
    public int getTotalFlyweightsCriados() { return fabrica.totalEspeciesCriadas(); }
    public void listarEspecies()           { fabrica.listarEspecies(); }
}
