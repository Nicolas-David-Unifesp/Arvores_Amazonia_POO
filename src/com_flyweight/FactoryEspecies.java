package com_flyweight;

import java.util.HashMap;
import java.util.Map;

/*
 FLYWEIGHT FACTORY — pool de objetos EspecieArvore.
 Garante que cada espécie seja criada UMA ÚNICA VEZ.
 */
public class FabricaEspecies {

    private final Map<String, EspecieArvore> cache = new HashMap<>();

    public EspecieArvore obterEspecie(String especie, String corFolhas,
                                      String texturaCasca, float alturaMax) {
        return cache.computeIfAbsent(especie,
            k -> new EspecieArvore(especie, corFolhas, texturaCasca, alturaMax));
    }

    public int totalEspeciesCriadas() { return cache.size(); }

    public void listarEspecies() {
        System.out.println("\n  ── Espécies armazenadas na fábrica (flyweights) ──");
        cache.forEach((k, v) -> System.out.println("  [" + k + "] → " + v));
    }
}
