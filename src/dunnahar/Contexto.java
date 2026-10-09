package dunnahar;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/** Estado compartilhado de uma execução: E/S, funções e proteção contra loops infinitos. */
public final class Contexto {
    private static final long LIMITE_PASSOS = 1_000_000L;

    private final PrintStream saida;
    private final Scanner entrada;
    private final Map<String, Comandos.DeclaracaoFuncao> funcoes = new HashMap<>();
    private boolean houveSaida = false;
    private long passos = 0;

    public Contexto(PrintStream saida, Scanner entrada) {
        this.saida = saida;
        this.entrada = entrada;
    }

    public void escrever(String texto) {
        houveSaida = true;
        saida.print(texto);
        saida.flush();
    }

    public String lerLinha() {
        saida.print("» ");
        saida.flush();
        return entrada.hasNextLine() ? entrada.nextLine() : "";
    }

    public void registrarFuncao(Comandos.DeclaracaoFuncao f) { funcoes.put(f.nome(), f); }

    public Comandos.DeclaracaoFuncao funcao(String nome) { return funcoes.get(nome); }

    public boolean houveSaida() { return houveSaida; }

    public void contarPasso() {
        if (++passos > LIMITE_PASSOS) {
            throw new DunNaharException("o feitiço girou demais (limite de repetições atingido)");
        }
    }
}
