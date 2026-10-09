package dunnahar;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Codex {
    private final Set<Runa> descobertas = EnumSet.noneOf(Runa.class);
    private final Path arquivoSave;

    private Codex(Path arquivoSave) {
        this.arquivoSave = arquivoSave;
    }

    public static Codex carregar(Path arquivoSave) {
        Codex codex = new Codex(arquivoSave);
        if (Files.exists(arquivoSave)) {
            try {
                for (String linha : Files.readAllLines(arquivoSave, StandardCharsets.UTF_8)) {
                    try {
                        codex.descobertas.add(Runa.valueOf(linha.trim()));
                    } catch (IllegalArgumentException ignorada) {
                        // linha inválida no save: ignora
                    }
                }
            } catch (IOException e) {
                System.out.println("Aviso: não consegui ler o progresso salvo (" + e.getMessage() + ")");
            }
        }
        return codex;
    }

    public boolean conhece(Runa runa) { return descobertas.contains(runa); }

    public Set<Runa> descobertas() { return Collections.unmodifiableSet(descobertas); }

    /** Registra a runa e salva em disco. Retorna true se era nova. */
    public boolean descobrir(Runa runa) {
        boolean nova = descobertas.add(runa);
        if (nova) salvar();
        return nova;
    }

    /** Primeira fase cuja runa-alvo ainda não foi descoberta. */
    public Optional<Fase> faseAtual() {
        return Campanha.FASES.stream().filter(f -> !descobertas.contains(f.alvo())).findFirst();
    }

    public void exibir() {
        System.out.println();
        System.out.println("══════ CODEX DE DUN NAHAR (" + descobertas.size() + "/" + Runa.values().length + ") ══════");
        for (Runa r : Runa.values()) {
            if (descobertas.contains(r)) {
                System.out.printf("  %s  [%s]  %s%n", r.glifo(), r.categoria().rotulo(), r.descricao());
            } else {
                System.out.println("  ???  [???]  runa ainda selada");
            }
        }
        System.out.println("═══════════════════════════════════════");
    }

    private void salvar() {
        try {
            List<String> linhas = descobertas.stream().map(Enum::name).toList();
            Files.write(arquivoSave, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Aviso: não consegui salvar o progresso (" + e.getMessage() + ")");
        }
    }
}
