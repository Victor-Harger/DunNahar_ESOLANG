package dunnahar;

import java.util.List;

/**
 * @param alvo      runa que o enigma esconde
 * @param exigidas  runas que precisam aparecer no código (a nova + conhecidas)
 * @param dica      dica de sintaxe (nunca revela o significado da runa)
 */
public record Fase(int numero, Runa alvo, List<Runa> exigidas, String dica) {
    public boolean exigeSaida() {
        return exigidas.contains(Runa.ACH) || exigidas.contains(Runa.WRI);
    }
}
