package dunnahar;

/** Sinais de controle de fluxo (sem stack trace, para ficarem baratos). */
final class InterrupcaoSinal extends RuntimeException {
    InterrupcaoSinal() { super(null, null, false, false); }
}

final class RetornoSinal extends RuntimeException {
    private final transient Object valor;

    RetornoSinal(Object valor) {
        super(null, null, false, false);
        this.valor = valor;
    }

    Object valor() { return valor; }
}
