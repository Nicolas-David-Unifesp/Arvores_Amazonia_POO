package sem_flyweight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 Contador de floresta SEM o padrão Flyweight.
 Cria 1 objeto ArvoreCompleta por árvore — 1.000.000 objetos pesados.
 */
public class FlorestaTradicional {

    private static final int TOTAL_ARVORES = 1_000_000;

    private static final String[][] ESPECIES = {
        {"Ipê",          "Amarelo vibrante",   "Rugosa profunda",    "40"},
        {"Castanheira",  "Verde escuro",        "Escamosa grossa",    "50"},
        {"Sumaúma",      "Verde médio",         "Lisa esbranquiçada", "70"},
        {"Andiroba",     "Verde amarelado",     "Fissurada",          "35"},
        {"Mogno",        "Verde brilhante",     "Estriada",           "45"},
        {"Cedro",        "Verde grisalho",      "Reticulada",         "30"},
        {"Pau-Brasil",   "Verde claro",         "Áspera irregular",   "15"},
        {"Seringueira",  "Verde intenso",       "Levemente rugosa",   "30"},
        {"Açaí",         "Verde vivo",          "Lisa fibrosa",       "25"},
        {"Guaraná",      "Verde musgo",         "Delgada lisa",       "10"}
    };

    private static final String[] STATUS_SAUDE = {"Saudável", "Doente", "Seca"};

    private final List<ArvoreCompleta> arvores = new ArrayList<>(TOTAL_ARVORES);

    public long popular() {
        Random random = new Random(42);
        long inicio = System.currentTimeMillis();

        for (int i = 0; i < TOTAL_ARVORES; i++) {
            String[] esp = ESPECIES[i % ESPECIES.length];
            arvores.add(new ArvoreCompleta(
                esp[0], esp[1], esp[2], Float.parseFloat(esp[3]),
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
        for (ArvoreCompleta a : arvores) a.processar();
        return System.currentTimeMillis() - inicio;
    }

    public int getTotalArvores() { return arvores.size(); }
}
