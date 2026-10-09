# Dun Nahar

Uma Esolang em forma de jogo: enigmas revelam runas, e você descobre o que cada uma faz escrevendo código em arquivos `.dnh`.

## Como jogar

Precisa de **Java 17 ou superior**.

- Windows: dê dois cliques em `jogar.bat`
- Linux/macOS: `./jogar.sh`
- Ou manualmente: `java -Dfile.encoding=UTF-8 -jar dunnahar.jar`

1. O jogo mostra um enigma com uma runa. Ex.: `"Aquele que revela seus segredos para os leigos = ᚨᚲᚺ"`.
2. Descubra o que a runa faz. Copie a runa e escreva um código com ela no arquivo **`grimorio.dnh`** (criado na primeira execução), usando qualquer editor, fora do terminal. Salve em UTF-8.
3. No jogo, escolha `[3] Verificar grimório`. Se estiver certo, a runa é gravada no Codex e a próxima fase começa.
4. `[4] Codex` mostra só as runas que você já descobriu. As outras ficam seladas.
5. `[2] Dica` dá uma pista de sintaxe (sem revelar o significado da runa).

Seu progresso fica em `dunnahar.save`. Para recomeçar, apague esse arquivo.

## Sintaxe geral (depois de descobrir as runas)

```
<runa de saída>("texto")
<runa de variável> x = 10
x = x + 1
<runa de if> (x > 5) { ... } <runa de else> { ... }
<runa de while> (x < 10) { ... }
<runa de for> (<runa de variável> i = 0; i < 5; i = i + 1) { ... }
<runa de função> nome(a, b) { <runa de retorno> a + b }
```

Operadores: `+ - * / %  == != < <= > >=`. Valores: números, textos entre aspas, `null` (runa própria).

## Compilar

Precisa do JDK 17+: `./compilar.sh` (Linux/macOS) ou `compilar.bat` (Windows).

## Estrutura

- `src/dunnahar/`: código-fonte
  - `Runa`, `TipoToken`, `Token`, `Lexer`, `Parser`
  - `Comandos` e `Expressoes` (AST), `Interpretador`, `Ambiente`, `Contexto`
  - `Fase`, `Campanha`, `Codex`, `Jogo`, `Main`
- `spoilers/gabarito_completo.dnh`: programa que usa todas as runas (**spoiler!**)
