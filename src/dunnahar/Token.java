package dunnahar;

public record Token(TipoToken tipo, String lexema, Object literal, int linha) {
    @Override
    public String toString() {
        return tipo + " '" + lexema + "' (linha " + linha + ")";
    }
}
