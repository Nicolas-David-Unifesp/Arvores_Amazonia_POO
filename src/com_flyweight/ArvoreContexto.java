package com_flyweight;

/**
 * CONTEXTO — representa UMA árvore individual.
 * Mantém apenas o estado extrínseco + referência ao flyweight compartilhado.
 *
 * Economia: em vez de armazenar os 4 campos de espécie, guarda só 1 referência (4 bytes).
 */
public class ArvoreContexto {

    // 1 referência (4 bytes) ao invés de 4 strings + 1 float duplicados
    private final EspecieArvore especieFlyweight;

    // Estado extrínseco exclusivo desta árvore
    private double coordenadaX;
    private double coordenadaY;
    private int    idadeAtual;
    private String statusSaude;

    public ArvoreContexto(EspecieArvore especieFlyweight,
                          double coordenadaX, double coordenadaY,
                          int idadeAtual, String statusSaude) {
        this.especieFlyweight = especieFlyweight;
        this.coordenadaX      = coordenadaX;
        this.coordenadaY      = coordenadaY;
        this.idadeAtual       = idadeAtual;
        this.statusSaude      = statusSaude;
    }

    /** Delega ao flyweight passando o estado extrínseco como argumento. */
    public void processar() {
        especieFlyweight.processar(coordenadaX, coordenadaY, idadeAtual, statusSaude);
    }

    public EspecieArvore getEspecie()    { return especieFlyweight; }
    public double getCoordenadaX()       { return coordenadaX; }
    public double getCoordenadaY()       { return coordenadaY; }
    public int    getIdadeAtual()        { return idadeAtual; }
    public String getStatusSaude()       { return statusSaude; }
    public void   setStatusSaude(String s) { this.statusSaude = s; }
    public void   setIdadeAtual(int i)     { this.idadeAtual = i; }
}
