package dunnahar;

public enum Runa {
    // A ordem das constantes é a ordem de descoberta das fases.
    ACH("ᚨᚲᚺ", TipoToken.ACH, Categoria.SAIDA, "Escrever/imprimir um valor",
        "Aquele que revela seus segredos para os leigos"),
    NAM("ᚾᚨᛗ", TipoToken.NAM, Categoria.VARIAVEL, "Declarar uma variável",
        "O recipiente sem forma que guarda o que lhe confiam e só atende por um nome"),
    COM("ᚲᛟᛗ", TipoToken.COM, Categoria.SINTAXE, "Indicar comentário",
        "Sussurro escrito que o oráculo jamais ouve, mas o próximo viajante lê"),
    WRI("ᚹᚱᛁ", TipoToken.WRI, Categoria.SAIDA, "Escrever texto na saída",
        "A tinta que corre sem pressa e não quebra a linha de quem escreve"),
    GET("ᚷᛖᛏ", TipoToken.GET, Categoria.ENTRADA, "Obter/ler uma entrada",
        "Aquele que estende a mão e espera o viajante depositar uma palavra"),
    REA("ᚱᛖᚨ", TipoToken.REA, Categoria.ENTRADA, "Ler um valor",
        "Aquele que procura o número escondido entre as palavras do viajante"),
    NNL("ᚾᚾᛚ", TipoToken.NNL, Categoria.VALOR, "Representar null/ausência de valor",
        "O vazio que também é um valor: a ausência que ocupa lugar"),
    VAL("ᚡᚨᛚ", TipoToken.VAL, Categoria.VALOR, "Trabalhar explicitamente com um valor",
        "Aquele que pesa e declara, sem rodeios, o que algo realmente vale"),
    IFT("ᛁᚠᛏ", TipoToken.IFT, Categoria.CONTROLE, "Iniciar uma condição if",
        "A encruzilhada onde o caminho só se abre se a verdade o permitir"),
    ELS("ᛖᛚᛋ", TipoToken.ELS, Categoria.CONTROLE, "Alternativa else",
        "O caminho esquecido que se abre quando a encruzilhada se fecha"),
    WHI("ᚹᚺᛁ", TipoToken.WHI, Categoria.REPETICAO, "Iniciar um while",
        "A roda que gira enquanto a condição ainda respira"),
    BRK("ᛒᚱᚲ", TipoToken.BRK, Categoria.CONTROLE, "Interromper um bloco/repetição",
        "O golpe que quebra a roda no meio do giro"),
    FOR("ᚠᛟᚱ", TipoToken.FOR, Categoria.REPETICAO, "Iniciar um for",
        "O ritual de passos contados: começa, mede e avança até o fim"),
    FNK("ᚠᚾᚲ", TipoToken.FNK, Categoria.FUNCAO, "Declarar uma função",
        "O feitiço nomeado que guarda um ritual para ser invocado outra vez"),
    RET("ᚱᛖᛏ", TipoToken.RET, Categoria.FUNCAO, "Retornar um valor",
        "A resposta que o feitiço devolve a quem o invocou");

    public enum Categoria {
        SAIDA("Saída"), VARIAVEL("Variável"), ENTRADA("Entrada"), CONTROLE("Controle"),
        REPETICAO("Repetição"), FUNCAO("Função"), VALOR("Valor"), SINTAXE("Sintaxe");

        private final String rotulo;

        Categoria(String rotulo) { this.rotulo = rotulo; }

        public String rotulo() { return rotulo; }
    }

    private final String glifo;
    private final TipoToken tipo;
    private final Categoria categoria;
    private final String descricao;
    private final String enigma;

    Runa(String glifo, TipoToken tipo, Categoria categoria, String descricao, String enigma) {
        this.glifo = glifo;
        this.tipo = tipo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.enigma = enigma;
    }

    public String glifo() { return glifo; }
    public TipoToken tipo() { return tipo; }
    public Categoria categoria() { return categoria; }
    public String descricao() { return descricao; }
    public String enigma() { return enigma; }

    public static Runa porGlifo(String glifo) {
        for (Runa r : values()) if (r.glifo.equals(glifo)) return r;
        return null;
    }

    public static Runa porTipo(TipoToken tipo) {
        for (Runa r : values()) if (r.tipo == tipo) return r;
        return null;
    }
}
