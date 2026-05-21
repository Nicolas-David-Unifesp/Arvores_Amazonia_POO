package sem_flyweight;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 Representa uma árvore completa com TODOS os seus atributos.
 Estado intrínseco (imutável) e extrínseco (mutável) ficam no mesmo objeto.
 PROBLEMA: Basicamente com 1 milhão de árvores e só 10 espécies, os dados de espécie são
 repetidos 100.000 vezes cada. O que causa um enorme desperdício de memória. 
 */
public class ArvoreCompleta {

    // ── Estado Intrínseco (Imutável) ─────────────────────────────────────
    private final String especie;
    private final String corFolhas;
    private final String texturaCasca;
    private final float  alturaMaximaMedia;

    // ── Estado Extrínseco (Mutável) ──────────────────────────────────────
    private double coordenadaX;
    private double coordenadaY;
    private int    idadeAtual;
    private String statusSaude;

    public ArvoreCompleta(String especie, String corFolhas, String texturaCasca,
                          float alturaMaximaMedia, double coordenadaX, double coordenadaY,
                          int idadeAtual, String statusSaude) {
        this.especie           = especie;
        this.corFolhas         = corFolhas;
        this.texturaCasca      = texturaCasca;
        this.alturaMaximaMedia = alturaMaximaMedia;
        this.coordenadaX       = coordenadaX;
        this.coordenadaY       = coordenadaY;
        this.idadeAtual        = idadeAtual;
        this.statusSaude       = statusSaude;
    }

    /** Simula processamento/renderização — acessa todos os campos */
    public void processar() {
        String r = especie + corFolhas + texturaCasca + alturaMaximaMedia
                 + coordenadaX + coordenadaY + idadeAtual + statusSaude;
        if (r.isEmpty()) System.out.println("Nunca");
    }

    public String getEspecie()           { return especie; }
    public String getCorFolhas()         { return corFolhas; }
    public String getTexturaCasca()      { return texturaCasca; }
    public float  getAlturaMaximaMedia() { return alturaMaximaMedia; }
    public double getCoordenadaX()       { return coordenadaX; }
    public double getCoordenadaY()       { return coordenadaY; }
    public int    getIdadeAtual()        { return idadeAtual; }
    public String getStatusSaude()       { return statusSaude; }
    public void   setStatusSaude(String s) { this.statusSaude = s; }
    public void   setIdadeAtual(int i)     { this.idadeAtual = i; }
}
