package dunnahar;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String fonte;
    private final List<Token> tokens = new ArrayList<>();
    private int inicio = 0;
    private int atual = 0;
    private int linha = 1;

    public Lexer(String fonte) {
        this.fonte = fonte;
    }

    public List<Token> escanear() {
        while (!fim()) {
            inicio = atual;
            escanearToken();
        }
        tokens.add(new Token(TipoToken.FIM, "", null, linha));
        return tokens;
    }

    private void escanearToken() {
        char c = avancar();
        switch (c) {
            case ' ': case '\r': case '\t': case '\uFEFF': break;
            case '\n': linha++; break;
            case '(': adicionar(TipoToken.ABRE_PAREN); break;
            case ')': adicionar(TipoToken.FECHA_PAREN); break;
            case '{': adicionar(TipoToken.ABRE_CHAVE); break;
            case '}': adicionar(TipoToken.FECHA_CHAVE); break;
            case ',': adicionar(TipoToken.VIRGULA); break;
            case ';': adicionar(TipoToken.PONTO_VIRGULA); break;
            case '+': adicionar(TipoToken.MAIS); break;
            case '-': adicionar(TipoToken.MENOS); break;
            case '*': adicionar(TipoToken.VEZES); break;
            case '/': adicionar(TipoToken.DIVIDE); break;
            case '%': adicionar(TipoToken.RESTO); break;
            case '=': adicionar(combinar('=') ? TipoToken.IGUAL_IGUAL : TipoToken.IGUAL); break;
            case '<': adicionar(combinar('=') ? TipoToken.MENOR_IGUAL : TipoToken.MENOR); break;
            case '>': adicionar(combinar('=') ? TipoToken.MAIOR_IGUAL : TipoToken.MAIOR); break;
            case '!':
                if (!combinar('=')) throw erro("'!' sozinho não significa nada aqui");
                adicionar(TipoToken.DIFERENTE);
                break;
            case '"': texto(); break;
            default:
                if (ehRuna(c)) runa();
                else if (ehDigito(c)) numero();
                else if (Character.isLetter(c) || c == '_') identificador();
                else throw erro("caractere inesperado '" + c + "'");
        }
    }

    private void runa() {
        while (!fim() && ehRuna(olhar())) avancar();
        String glifo = fonte.substring(inicio, atual);
        Runa runa = Runa.porGlifo(glifo);
        if (runa == null) throw erro("a runa '" + glifo + "' não existe em Dun Nahar");

        if (runa.tipo() == TipoToken.COM) {
            int comecoComentario = atual;
            while (!fim() && olhar() != '\n') avancar();
            adicionar(TipoToken.COM, fonte.substring(comecoComentario, atual).trim());
        } else {
            adicionar(runa.tipo());
        }
    }

    private void texto() {
        while (!fim() && olhar() != '"') {
            if (olhar() == '\n') throw erro("texto sem aspas de fechamento");
            avancar();
        }
        if (fim()) throw erro("texto sem aspas de fechamento");
        avancar(); // aspas finais
        adicionar(TipoToken.TEXTO, fonte.substring(inicio + 1, atual - 1));
    }

    private void numero() {
        while (!fim() && ehDigito(olhar())) avancar();
        if (!fim() && olhar() == '.' && atual + 1 < fonte.length() && ehDigito(fonte.charAt(atual + 1))) {
            avancar();
            while (!fim() && ehDigito(olhar())) avancar();
        }
        adicionar(TipoToken.NUMERO, Double.parseDouble(fonte.substring(inicio, atual)));
    }

    private void identificador() {
        while (!fim() && !ehRuna(olhar()) && (Character.isLetterOrDigit(olhar()) || olhar() == '_')) avancar();
        adicionar(TipoToken.IDENTIFICADOR);
    }

    // ---------- utilitários ----------
    private boolean ehRuna(char c) { return c >= '\u16A0' && c <= '\u16FF'; }
    private boolean ehDigito(char c) { return c >= '0' && c <= '9'; }
    private boolean fim() { return atual >= fonte.length(); }
    private char avancar() { return fonte.charAt(atual++); }
    private char olhar() { return fonte.charAt(atual); }

    private boolean combinar(char esperado) {
        if (fim() || fonte.charAt(atual) != esperado) return false;
        atual++;
        return true;
    }

    private void adicionar(TipoToken tipo) { adicionar(tipo, null); }

    private void adicionar(TipoToken tipo, Object literal) {
        tokens.add(new Token(tipo, fonte.substring(inicio, atual), literal, linha));
    }

    private DunNaharException erro(String mensagem) {
        return new DunNaharException(linha, mensagem);
    }
}
