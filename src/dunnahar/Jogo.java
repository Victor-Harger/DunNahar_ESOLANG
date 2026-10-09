package dunnahar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Jogo {
    private final Scanner entrada;
    private final Codex codex;
    private final Path grimorio;

    public Jogo(Scanner entrada, Codex codex, Path grimorio) {
        this.entrada = entrada;
        this.codex = codex;
        this.grimorio = grimorio;
    }

    public void iniciar() {
        prepararGrimorio();
        System.out.println("""

            ╔══════════════════════════════╗
            ║         D U N   N A H A R    ║
            ║   A linguagem das runas      ║
            ╚══════════════════════════════╝
            """);

        boolean mostrarEnigma = true;
        while (true) {
            Optional<Fase> proxima = codex.faseAtual();
            if (proxima.isEmpty()) {
                vitoria();
                return;
            }
            Fase fase = proxima.get();
            if (mostrarEnigma) {
                exibirEnigma(fase);
                mostrarEnigma = false;
            }

            System.out.println("\n[1] Reler enigma  [2] Dica  [3] Verificar grimório  [4] Codex  [5] Sair");
            System.out.print("> ");
            String opcao = entrada.hasNextLine() ? entrada.nextLine().trim() : "5";

            switch (opcao) {
                case "1": exibirEnigma(fase); break;
                case "2": System.out.println("💡 " + fase.dica()); break;
                case "3":
                    if (verificar(fase)) mostrarEnigma = true;
                    break;
                case "4": codex.exibir(); break;
                case "5":
                    System.out.println("Até a próxima, viajante.");
                    return;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    // ---------- verificação ----------
    /** @return true se a fase foi concluída. */
    private boolean verificar(Fase fase) {
        String fonte;
        try {
            fonte = Files.readString(grimorio, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("✖ Não consegui ler " + grimorio + ": " + e.getMessage());
            return false;
        }

        try {
            List<Token> tokens = new Lexer(fonte).escanear();
            Set<Runa> usadas = runasUsadas(tokens);
            if (usadas.isEmpty()) throw new DunNaharException("O grimório não contém nenhuma runa.");
            verificarSelos(usadas, fase);

            List<Comando> programa = new Parser(tokens).analisar();

            System.out.println("─── saída do feitiço ───");
            Interpretador interpretador = new Interpretador(System.out, entrada);
            interpretador.executar(programa);
            System.out.println("────────────────────────");

            avaliar(fase, usadas, interpretador.houveSaida());
        } catch (DunNaharException e) {
            System.out.println("✖ " + e.getMessage());
            return false;
        }

        codex.descobrir(fase.alvo());
        System.out.println("\n✦ Runa decifrada! " + fase.alvo().glifo() + " = " + fase.alvo().descricao());
        System.out.println("  Ela foi gravada no seu Codex.");
        return true;
    }

    private Set<Runa> runasUsadas(List<Token> tokens) {
        Set<Runa> usadas = EnumSet.noneOf(Runa.class);
        for (Token t : tokens) {
            Runa r = Runa.porTipo(t.tipo());
            if (r != null) usadas.add(r);
        }
        return usadas;
    }

    private void verificarSelos(Set<Runa> usadas, Fase fase) {
        for (Runa r : usadas) {
            if (!codex.conhece(r) && r != fase.alvo()) {
                throw new DunNaharException("A runa " + r.glifo() + " ainda está selada para você.");
            }
        }
    }

    private void avaliar(Fase fase, Set<Runa> usadas, boolean houveSaida) {
        if (!usadas.contains(fase.alvo())) {
            throw new DunNaharException("A runa do enigma não ressoou no seu código. Releia o enigma.");
        }
        for (Runa r : fase.exigidas()) {
            if (!usadas.contains(r)) {
                throw new DunNaharException("Este enigma pede que você combine a runa nova com " + r.glifo() + ".");
            }
        }
        if (fase.exigeSaida() && !houveSaida) {
            throw new DunNaharException("O feitiço funcionou, mas não escreveu nada na saída.");
        }
    }

    // ---------- apresentação ----------
    private void exibirEnigma(Fase fase) {
        System.out.println("\n✦ FASE " + fase.numero() + " de " + Campanha.FASES.size() + " ✦");
        System.out.println("  \"" + fase.alvo().enigma() + " = " + fase.alvo().glifo() + "\"");
        System.out.println("  Descubra o que a runa faz e escreva um código com ela em '" + grimorio + "'.");
    }

    private void vitoria() {
        codex.exibir();
        System.out.println("\n★ Você decifrou todas as runas de Dun Nahar. ★");
    }

    private void prepararGrimorio() {
        if (Files.notExists(grimorio)) {
            try {
                Files.writeString(grimorio, "", StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("Não consegui criar " + grimorio + ": " + e.getMessage());
            }
        }
    }
}
