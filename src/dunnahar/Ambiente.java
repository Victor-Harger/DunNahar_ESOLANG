package dunnahar;

import java.util.HashMap;
import java.util.Map;

public final class Ambiente {
    private final Map<String, Object> variaveis = new HashMap<>();
    private final Ambiente pai;
    private final Contexto contexto;

    public Ambiente(Contexto contexto) {
        this(null, contexto);
    }

    private Ambiente(Ambiente pai, Contexto contexto) {
        this.pai = pai;
        this.contexto = contexto;
    }

    public Ambiente filho() { return new Ambiente(this, contexto); }

    public Ambiente global() {
        Ambiente a = this;
        while (a.pai != null) a = a.pai;
        return a;
    }

    public Contexto contexto() { return contexto; }

    public void declarar(String nome, Object valor, int linha) {
        if (variaveis.containsKey(nome)) {
            throw new DunNaharException(linha, "o recipiente '" + nome + "' já existe neste escopo");
        }
        variaveis.put(nome, valor);
    }

    public void atribuir(String nome, Object valor, int linha) {
        if (variaveis.containsKey(nome)) variaveis.put(nome, valor);
        else if (pai != null) pai.atribuir(nome, valor, linha);
        else throw new DunNaharException(linha, "o recipiente '" + nome + "' não foi declarado");
    }

    public Object obter(String nome, int linha) {
        if (variaveis.containsKey(nome)) return variaveis.get(nome);
        if (pai != null) return pai.obter(nome, linha);
        throw new DunNaharException(linha, "o recipiente '" + nome + "' não foi declarado");
    }
}
