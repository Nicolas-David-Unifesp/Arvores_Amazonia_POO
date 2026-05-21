# Análise de Algoritmos para as Árvores na Amazonia
Fiz no código uma análise tanto teórica quanto pelo branckmark. 
É possível perceber que o FlyWeight, nesse experimento específico, consegue economizar 27 M, o que confirma um benefício real do padrão.
Quando o método não é implementado, temos que com 1 milhão de árvores e só 10 espécies, os dados de espécie são repetidos 100.000 vezes cada. O que causa um enorme desperdício de memória. E, caso utilizemos FlyWeight teremos criado 1.000.000 dados leves com apenas 10 flyweights de espécie. 

## Resultados do Benchmark (Foi feita uma mediana de 3 rodadas)

| Métrica | Tradicional | Flyweight |
|---|---|---|
| Criação (1M objetos) | ~122 ms | ~126 ms |
| Processamento (1M iterações) | ~216 ms | ~285 ms |
| Heap consumido (medido) | 71 MB | 43 MB |
| Economia de heap | — | −27 MB (−38%) |

## Análise teórica de memória

| Campo eliminado por objeto | Economia por árvore |
|---|---|
| `String especie` *(ref 4B + objeto ~55B)* | ~59B |
| `String corFolhas` *(ref 4B + objeto ~55B)* | ~59B |
| `String texturaCasca` *(ref 4B + objeto ~55B)* | ~59B |
| `float alturaMaxima` | 4B |

| Total por árvore | Economia |
|---|---|
| **Total estimado** | **~181B a menos na teoria** |

