package dunnahar;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class Interpretador {
    private final Contexto contexto;

    public Interpretador(PrintStream saida, Scanner entrada) {
        this.contexto = new Contexto(saida, entrada);
    }

    public void executar(List<Comando> programa) {
        Ambiente global = new Ambiente(contexto);
        try {
            for (Comando c : programa) c.executar(global);
        } catch (InterrupcaoSinal e) {
            throw new DunNaharException("a runa de interrupção só funciona dentro de uma repetição");
        } catch (RetornoSinal e) {
            throw new DunNaharException("a runa de retorno só funciona dentro de um feitiço");
        } catch (StackOverflowError e) {
            throw new DunNaharException("o feitiço se invocou vezes demais");
        }
    }

    public boolean houveSaida() {
        return contexto.houveSaida();
    }
}
