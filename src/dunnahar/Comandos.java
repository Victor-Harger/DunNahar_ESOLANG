package dunnahar;

import java.util.List;

public final class Comandos {
    private Comandos() {}

    /** ᚨᚲᚺ (com quebra de linha) e ᚹᚱᛁ (sem quebra). */
    public record Escrever(Expressao expressao, boolean quebraLinha) implements Comando {
        @Override public void executar(Ambiente amb) {
            String texto = Valores.texto(expressao.avaliar(amb));
            amb.contexto().escrever(quebraLinha ? texto + "\n" : texto);
        }
    }

    public record Declaracao(String nome, Expressao inicial, int linha) implements Comando {
        @Override public void executar(Ambiente amb) {
            amb.declarar(nome, inicial == null ? null : inicial.avaliar(amb), linha);
        }
    }

    public record Atribuicao(String nome, Expressao valor, int linha) implements Comando {
        @Override public void executar(Ambiente amb) {
            amb.atribuir(nome, valor.avaliar(amb), linha);
        }
    }

    public record Bloco(List<Comando> comandos) implements Comando {
        @Override public void executar(Ambiente amb) {
            Ambiente local = amb.filho();
            for (Comando c : comandos) c.executar(local);
        }
    }

    public record Se(Expressao condicao, Comando entao, Comando senao) implements Comando {
        @Override public void executar(Ambiente amb) {
            if (Valores.verdadeiro(condicao.avaliar(amb))) entao.executar(amb);
            else if (senao != null) senao.executar(amb);
        }
    }

    public record Enquanto(Expressao condicao, Comando corpo) implements Comando {
        @Override public void executar(Ambiente amb) {
            try {
                while (Valores.verdadeiro(condicao.avaliar(amb))) {
                    amb.contexto().contarPasso();
                    corpo.executar(amb);
                }
            } catch (InterrupcaoSinal ignorado) {
                // ᛒᚱᚲ quebrou a roda
            }
        }
    }

    public record Para(Comando inicio, Expressao condicao, Comando passo, Comando corpo)
            implements Comando {
        @Override public void executar(Ambiente amb) {
            Ambiente local = amb.filho();
            inicio.executar(local);
            try {
                while (Valores.verdadeiro(condicao.avaliar(local))) {
                    local.contexto().contarPasso();
                    corpo.executar(local);
                    passo.executar(local);
                }
            } catch (InterrupcaoSinal ignorado) {
                // ᛒᚱᚲ quebrou a roda
            }
        }
    }

    public record Interromper() implements Comando {
        @Override public void executar(Ambiente amb) { throw new InterrupcaoSinal(); }
    }

    public record DeclaracaoFuncao(String nome, List<String> parametros, Bloco corpo, int linha)
            implements Comando {
        @Override public void executar(Ambiente amb) { amb.contexto().registrarFuncao(this); }
    }

    public record Retorno(Expressao valor) implements Comando {
        @Override public void executar(Ambiente amb) {
            throw new RetornoSinal(valor == null ? null : valor.avaliar(amb));
        }
    }

    /** Chamada de função usada como instrução. */
    public record ExpressaoSimples(Expressao expressao) implements Comando {
        @Override public void executar(Ambiente amb) { expressao.avaliar(amb); }
    }
}
