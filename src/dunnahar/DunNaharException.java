package dunnahar;

public class DunNaharException extends RuntimeException {
    public DunNaharException(String mensagem) {
        super(mensagem);
    }

    public DunNaharException(int linha, String mensagem) {
        super("Linha " + linha + ": " + mensagem);
    }
}
