package dunnahar;

import static dunnahar.Runa.*;

import java.util.List;

public final class Campanha {
    private Campanha() {}

    public static final List<Fase> FASES = List.of(
        new Fase(1, ACH, List.of(ACH),
            "Runas de invocação costumam levar parênteses e textos entre aspas."),
        new Fase(2, NAM, List.of(NAM, ACH),
            "Um nome, o sinal '=' e um valor. Depois, mostre o recipiente com a runa que já conhece."),
        new Fase(3, COM, List.of(COM, ACH),
            "Tudo o que vem depois da runa, até o fim da linha, é só para os olhos do próximo viajante."),
        new Fase(4, WRI, List.of(WRI),
            "Parece com uma runa de saída que você conhece, mas não encerra a linha. Use-a duas vezes seguidas."),
        new Fase(5, GET, List.of(GET, NAM, ACH),
            "Invoque com parênteses vazios e guarde o resultado em um recipiente."),
        new Fase(6, REA, List.of(REA, NAM, ACH),
            "Parente da runa anterior, mas espera um número. Some-o com algo."),
        new Fase(7, NNL, List.of(NNL, NAM, ACH),
            "Guarde-a em um recipiente e depois mostre-o."),
        new Fase(8, VAL, List.of(VAL, NAM, ACH),
            "Envolva uma expressão entre parênteses logo depois da runa."),
        new Fase(9, IFT, List.of(IFT, ACH),
            "Condição entre parênteses, ações entre chaves."),
        new Fase(10, ELS, List.of(IFT, ELS, ACH),
            "Logo depois das chaves da encruzilhada, abra o outro caminho, também com chaves."),
        new Fase(11, WHI, List.of(WHI, NAM, ACH),
            "Condição entre parênteses e chaves. Algo dentro precisa mudar, ou a roda nunca para."),
        new Fase(12, BRK, List.of(WHI, BRK, NAM, ACH),
            "Dentro da roda, uma condição decide quando quebrá-la."),
        new Fase(13, FOR, List.of(FOR, NAM, ACH),
            "Três partes entre parênteses, separadas por ';': início, condição e passo."),
        new Fase(14, FNK, List.of(FNK, ACH),
            "Nome, parâmetros entre parênteses e corpo entre chaves. Depois, invoque pelo nome."),
        new Fase(15, RET, List.of(FNK, RET, ACH),
            "Dentro do feitiço, devolva um valor e use a invocação dentro de uma saída.")
    );
}
