package dunnahar;

import static dunnahar.TipoToken.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Parser {
    private final List<Token> tokens;
    private int atual = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Comando> analisar() {
        List<Comando> programa = new ArrayList<>();
        while (!fim()) {
            Comando c = declaracao();
            if (c != null) programa.add(c);
        }
        return programa;
    }

    // ---------- comandos ----------
    private Comando declaracao() {
        if (verificar(IDENTIFICADOR)) return comandoIdentificador();

        Token t = avancar();
        switch (t.tipo()) {
            case COM: return null; // comentário
            case ACH: return escrever(true);
            case WRI: return escrever(false);
            case NAM: return declaracaoVariavel();
            case IFT: return se();
            case WHI: return enquanto();
            case FOR: return para();
            case FNK: return funcao();
            case RET: return retorno();
            case BRK: return new Comandos.Interromper();
            case ABRE_CHAVE: return bloco();
            default:
                throw new DunNaharException(t.linha(), "não esperava " + descrever(t) + " aqui");
        }
    }

    private Comando comandoIdentificador() {
        Token nome = avancar();
        if (combinar(IGUAL)) {
            return new Comandos.Atribuicao(nome.lexema(), expressao(), nome.linha());
        }
        if (verificar(ABRE_PAREN)) {
            return new Comandos.ExpressaoSimples(chamada(nome));
        }
        throw new DunNaharException(nome.linha(), "o que fazer com '" + nome.lexema() + "'?");
    }

    private Comando escrever(boolean quebraLinha) {
        consumir(ABRE_PAREN, "esperava '(' depois da runa de saída");
        Expressao e = expressao();
        consumir(FECHA_PAREN, "esperava ')' ao fim da saída");
        return new Comandos.Escrever(e, quebraLinha);
    }

    private Comando declaracaoVariavel() {
        Token nome = consumir(IDENTIFICADOR, "esperava um nome para o recipiente");
        Expressao inicial = combinar(IGUAL) ? expressao() : null;
        return new Comandos.Declaracao(nome.lexema(), inicial, nome.linha());
    }

    private Comando atribuicao() {
        Token nome = consumir(IDENTIFICADOR, "esperava o nome de um recipiente");
        consumir(IGUAL, "esperava '='");
        return new Comandos.Atribuicao(nome.lexema(), expressao(), nome.linha());
    }

    private Comando se() {
        consumir(ABRE_PAREN, "esperava '(' antes da condição");
        Expressao condicao = expressao();
        consumir(FECHA_PAREN, "esperava ')' depois da condição");
        Comando entao = blocoObrigatorio();

        while (verificar(COM)) avancar(); // comentários entre o bloco e a alternativa
        Comando senao = combinar(ELS) ? blocoObrigatorio() : null;
        return new Comandos.Se(condicao, entao, senao);
    }

    private Comando enquanto() {
        consumir(ABRE_PAREN, "esperava '(' antes da condição");
        Expressao condicao = expressao();
        consumir(FECHA_PAREN, "esperava ')' depois da condição");
        return new Comandos.Enquanto(condicao, blocoObrigatorio());
    }

    private Comando para() {
        consumir(ABRE_PAREN, "esperava '(' depois da runa de repetição");
        Comando inicio = combinar(NAM) ? declaracaoVariavel() : atribuicao();
        consumir(PONTO_VIRGULA, "esperava ';' depois do início");
        Expressao condicao = expressao();
        consumir(PONTO_VIRGULA, "esperava ';' depois da condição");
        Comando passo = atribuicao();
        consumir(FECHA_PAREN, "esperava ')' depois do passo");
        return new Comandos.Para(inicio, condicao, passo, blocoObrigatorio());
    }

    private Comando funcao() {
        Token nome = consumir(IDENTIFICADOR, "esperava o nome do feitiço");
        consumir(ABRE_PAREN, "esperava '(' depois do nome");
        List<String> parametros = new ArrayList<>();
        if (!verificar(FECHA_PAREN)) {
            do {
                parametros.add(consumir(IDENTIFICADOR, "esperava o nome de um parâmetro").lexema());
            } while (combinar(VIRGULA));
        }
        consumir(FECHA_PAREN, "esperava ')' depois dos parâmetros");
        consumir(ABRE_CHAVE, "esperava '{' para abrir o feitiço");
        return new Comandos.DeclaracaoFuncao(nome.lexema(), parametros, bloco(), nome.linha());
    }

    private Comando retorno() {
        Expressao valor = (verificar(FECHA_CHAVE) || fim()) ? null : expressao();
        return new Comandos.Retorno(valor);
    }

    private Comando blocoObrigatorio() {
        consumir(ABRE_CHAVE, "esperava '{'");
        return bloco();
    }

    /** Chamado depois que '{' já foi consumido. */
    private Comandos.Bloco bloco() {
        List<Comando> comandos = new ArrayList<>();
        while (!verificar(FECHA_CHAVE) && !fim()) {
            Comando c = declaracao();
            if (c != null) comandos.add(c);
        }
        consumir(FECHA_CHAVE, "esperava '}' para fechar o bloco");
        return new Comandos.Bloco(comandos);
    }

    // ---------- expressões (precedência crescente) ----------
    private Expressao expressao() { return igualdade(); }

    private Expressao igualdade() { return binaria(this::comparacao, IGUAL_IGUAL, DIFERENTE); }

    private Expressao comparacao() {
        return binaria(this::termo, MENOR, MENOR_IGUAL, MAIOR, MAIOR_IGUAL);
    }

    private Expressao termo() { return binaria(this::fator, MAIS, MENOS); }

    private Expressao fator() { return binaria(this::unaria, VEZES, DIVIDE, RESTO); }

    private Expressao binaria(Supplier<Expressao> proximo, TipoToken... operadores) {
        Expressao e = proximo.get();
        while (Arrays.asList(operadores).contains(olhar().tipo())) {
            Token op = avancar();
            e = new Expressoes.Binaria(e, op.tipo(), proximo.get(), op.linha());
        }
        return e;
    }

    private Expressao unaria() {
        if (verificar(MENOS)) {
            Token op = avancar();
            return new Expressoes.Unaria(unaria(), op.linha());
        }
        return primaria();
    }

    private Expressao primaria() {
        Token t = avancar();
        switch (t.tipo()) {
            case NUMERO:
            case TEXTO:
                return new Expressoes.Literal(t.literal());
            case NNL:
                return new Expressoes.Literal(null);
            case IDENTIFICADOR:
                return verificar(ABRE_PAREN) ? chamada(t) : new Expressoes.Variavel(t.lexema(), t.linha());
            case GET:
            case REA: {
                consumir(ABRE_PAREN, "esperava '(' depois da runa de entrada");
                consumir(FECHA_PAREN, "esperava ')' depois de '('");
                return new Expressoes.Entrada(t.tipo() == REA, t.linha());
            }
            case VAL: {
                consumir(ABRE_PAREN, "esperava '(' depois da runa de valor");
                Expressao interna = expressao();
                consumir(FECHA_PAREN, "esperava ')'");
                return new Expressoes.ValorExplicito(interna, t.linha());
            }
            case ABRE_PAREN: {
                Expressao e = expressao();
                consumir(FECHA_PAREN, "esperava ')'");
                return e;
            }
            default:
                throw new DunNaharException(t.linha(), "esperava um valor, mas encontrei " + descrever(t));
        }
    }

    private Expressao chamada(Token nome) {
        consumir(ABRE_PAREN, "esperava '(' na invocação");
        List<Expressao> argumentos = new ArrayList<>();
        if (!verificar(FECHA_PAREN)) {
            do {
                argumentos.add(expressao());
            } while (combinar(VIRGULA));
        }
        consumir(FECHA_PAREN, "esperava ')' depois dos argumentos");
        return new Expressoes.Chamada(nome.lexema(), argumentos, nome.linha());
    }

    // ---------- utilitários ----------
    private Token olhar() { return tokens.get(atual); }
    private boolean fim() { return olhar().tipo() == FIM; }
    private boolean verificar(TipoToken tipo) { return olhar().tipo() == tipo; }

    private Token avancar() {
        Token t = olhar();
        if (t.tipo() != FIM) atual++;
        return t;
    }

    private boolean combinar(TipoToken tipo) {
        if (!verificar(tipo)) return false;
        avancar();
        return true;
    }

    private Token consumir(TipoToken tipo, String mensagem) {
        if (verificar(tipo)) return avancar();
        Token t = olhar();
        throw new DunNaharException(t.linha(), mensagem + " (encontrei " + descrever(t) + ")");
    }

    private String descrever(Token t) {
        return t.tipo() == FIM ? "o fim do código" : "'" + t.lexema() + "'";
    }
}
