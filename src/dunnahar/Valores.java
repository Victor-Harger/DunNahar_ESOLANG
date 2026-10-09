package dunnahar;

final class Valores {
    private Valores() {}

    static String texto(Object v) {
        if (v == null) return Runa.NNL.glifo();
        if (v instanceof Double d) {
            if (d == Math.rint(d) && !Double.isInfinite(d)) return String.valueOf((long) d.doubleValue());
            return d.toString();
        }
        if (v instanceof Boolean b) return b ? "verdade" : "falso";
        return v.toString();
    }

    static boolean verdadeiro(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean b) return b;
        if (v instanceof Double d) return d != 0;
        return !v.toString().isEmpty();
    }

    static double numero(Object v, int linha) {
        if (v instanceof Double d) return d;
        throw new DunNaharException(linha, "esperava um número, mas recebi " + texto(v));
    }
}
