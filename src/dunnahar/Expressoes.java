package dunnahar;

import java.util.List;
import java.util.Objects;

public final class Expressoes {
    private Expressoes() {}

    public record Literal(Object valor) implements Expressao {
        @Override public Object avaliar(Ambiente amb) { return valor; }
    }

    public record Variavel(String nome, int linha) implements Expressao {
        @Override public Object avaliar(Ambiente amb) { return amb.obter(nome, linha); }
    }

    public record Unaria(Expressao direita, int linha) implements Expressao {
        @Override public Object avaliar(Ambiente amb) {
            return -Valores.numero(direita.avaliar(amb), linha);
        }
    }

    public record Binaria(Expressao esquerda, TipoToken operador, Expressao direita, int linha)
            implements Expressao {
        @Override public Object avaliar(Ambiente amb) {
            Object a = esquerda.avaliar(amb);
            Object b = direita.avaliar(amb);

            switch (operador) {
                case MAIS:
                    if (a instanceof Double na && b instanceof Double nb) return na + nb;
                    return Valores.texto(a) + Valores.texto(b);
                case IGUAL_IGUAL: return Objects.equals(a, b);
                case DIFERENTE:   return !Objects.equals(a, b);
                default: break;
            }

            double x = Valores.numero(a, linha);
            double y = Valores.numero(b, linha);
            switch (operador) {
                case MENOS: return x - y;
                case VEZES: return x * y;
                case DIVIDE:
                    if (y == 0) throw new DunNaharException(linha, "divisão por zero");
                    return x / y;
                case RESTO: return x % y;
                case MENOR: return x < y;
                case MENOR_IGUAL: return x <= y;
                case MAIOR: return x > y;
                case MAIOR_IGUAL: return x >= y;
                default: throw new DunNaharException(linha, "operador desconhecido");
            }
        }
    }

    /** ᚷᛖᛏ() lê texto; ᚱᛖᚨ() lê número. */
    public record Entrada(boolean numerica, int linha) implements Expressao {
        @Override public Object avaliar(Ambiente amb) {
            String lida = amb.contexto().lerLinha();
            if (!numerica) return lida;
            try {
                return Double.parseDouble(lida.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                throw new DunNaharException(linha, "'" + lida + "' não é um número");
            }
        }
    }

    /** ᚡᚨᛚ(expr): trata o valor de forma explícita (texto numérico vira número). */
    public record ValorExplicito(Expressao interna, int linha) implements Expressao {
        @Override public Object avaliar(Ambiente amb) {
            Object v = interna.avaliar(amb);
            if (v instanceof String s) {
                try {
                    return Double.parseDouble(s.trim());
                } catch (NumberFormatException e) {
                    return s;
                }
            }
            return v;
        }
    }

    public record Chamada(String nome, List<Expressao> argumentos, int linha) implements Expressao {
        @Override public Object avaliar(Ambiente amb) {
            Comandos.DeclaracaoFuncao f = amb.contexto().funcao(nome);
            if (f == null) throw new DunNaharException(linha, "o feitiço '" + nome + "' não existe");
            if (f.parametros().size() != argumentos.size()) {
                throw new DunNaharException(linha, "o feitiço '" + nome + "' espera "
                        + f.parametros().size() + " argumento(s)");
            }
            Ambiente local = amb.global().filho();
            for (int i = 0; i < argumentos.size(); i++) {
                local.declarar(f.parametros().get(i), argumentos.get(i).avaliar(amb), linha);
            }
            try {
                f.corpo().executar(local);
            } catch (RetornoSinal r) {
                return r.valor();
            }
            return null;
        }
    }
}
