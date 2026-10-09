package dunnahar;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Garante UTF-8 na saída e na entrada (necessário para exibir runas).
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        Scanner entrada = new Scanner(System.in, StandardCharsets.UTF_8);

        Codex codex = Codex.carregar(Path.of("dunnahar.save"));
        new Jogo(entrada, codex, Path.of("grimorio.dnh")).iniciar();
    }
}
