# Dun Nahar

> Uma esolang em forma de jogo: enigmas revelam runas, e você descobre o que cada uma faz escrevendo código.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Status](https://img.shields.io/badge/status-jogável-brightgreen)

## Sobre o projeto

Desenvolvi este projeto para responder a uma pergunta que fiz a mim mesmo há algum tempo: como seria criar uma linguagem de programação própria?

Embora o resultado inicial tenha ficado como uma versão bem mais enxuta e simplificada da minha ideia original, estou satisfeito com a evolução do projeto. Ele me permitiu aprofundar diversos conceitos complexos de Java e espero, algum dia, reproduzi-lo em uma escala mais próxima da visão inicial.

## O que o projeto demonstra

- **Construção de uma linguagem do zero**: análise léxica (`Lexer`), análise sintática (`Parser`) e interpretação (`Interpretador`), sem bibliotecas externas.
- **AST (árvore sintática abstrata)** modelada com `Comandos` e `Expressoes`.
- **Gerenciamento de escopo e variáveis** com `Ambiente` e `Contexto`.
- **Suporte a Unicode**: o código da linguagem é escrito com runas, o que exige cuidado com codificação UTF-8.
- **Estruturas de linguagem**: variáveis, condicionais, laços `while` e `for`, funções com retorno e operadores aritméticos, relacionais e de comparação.
- **Design de jogo**: campanha em fases, sistema de dicas, Codex de runas descobertas e progresso salvo em arquivo.
- **Orientação a objetos em Java**, com código separado por responsabilidade.

<details>
<summary><b>Ver demonstração</b></summary>
<br>

### Menu principal
![Menu principal](docs/img/01-menu.png)

### Enigma revelando uma runa
![Enigma](docs/img/02-enigma.png)

### Código escrito no grimório
![Grimório](docs/img/03-grimorio.png)

### Codex de runas
![Codex](docs/img/04-codex.png)

</details>

## Como jogar

Requer **Java 17 ou superior**.

| Sistema | Como executar |
|---|---|
| Windows | Dois cliques em `jogar.bat` |
| Linux/macOS | `./jogar.sh` |
| Manual | `java -Dfile.encoding=UTF-8 -jar dunnahar.jar` |

1. O jogo mostra um enigma com uma runa. Exemplo: `"Aquele que revela seus segredos para os leigos = ᚨᚲᚺ"`.
2. Descubra o que a runa faz. Copie a runa e escreva um código com ela no arquivo **`grimorio.dnh`** (criado na primeira execução), usando qualquer editor fora do terminal. Salve em UTF-8.
3. No jogo, escolha `[3] Verificar grimório`. Se estiver certo, a runa é gravada no Codex e a próxima fase começa.
4. `[4] Codex` mostra só as runas que você já descobriu. As outras ficam seladas.
5. `[2] Dica` dá uma pista de sintaxe, sem revelar o significado da runa.

O progresso fica em `dunnahar.save`. Para recomeçar, apague esse arquivo.

## Sintaxe geral

Depois de descobrir as runas, a estrutura da linguagem é esta:

```
<runa de saída>("texto")
<runa de variável> x = 10
x = x + 1
<runa de if> (x > 5) { ... } <runa de else> { ... }
<runa de while> (x < 10) { ... }
<runa de for> (<runa de variável> i = 0; i < 5; i = i + 1) { ... }
<runa de função> nome(a, b) { <runa de retorno> a + b }
```

**Operadores:** `+ - * / %  == != < <= > >=`
**Valores:** números, textos entre aspas e `null` (que tem runa própria).

## Compilar

Requer JDK 17+.

- Linux/macOS: `./compilar.sh`
- Windows: `compilar.bat`

## Arquitetura

```
src/dunnahar/
├── Runa, TipoToken, Token     # Vocabulário da linguagem
├── Lexer                      # Texto -> tokens
├── Parser                     # Tokens -> AST
├── Comandos, Expressoes       # Nós da AST
├── Interpretador              # Executa a AST
├── Ambiente, Contexto         # Escopo e estado de execução
└── Fase, Campanha, Codex,     # Camada de jogo
    Jogo, Main
```

Fluxo de execução: **código `.dnh` → Lexer → Parser → AST → Interpretador**.

> `spoilers/gabarito_completo.dnh` contém um programa que usa todas as runas. **Spoiler!**

## Autor

**Victor Harger**.

[GitHub](https://github.com/Victor-Harger) · [LinkedIn](https://linkedin.com/in/victor-gabriel-prado-harger)
