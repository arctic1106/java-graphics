package quiniela;

import java.util.Arrays;
import java.util.Locale;

public class Model {
    public static final double PRECIO_APUESTA = 0.75;

    public int jornada = 75;
    public Partido[] partidos = new Partido[15];
    public String[] signos = new String[14];
    public boolean[] cond = new boolean[14];
    public int var = 2;
    public int equ = -1;
    public int dos = -1;
    public int plenoLocal = 0;
    public int plenoVisit = 0;

    public long columnas;
    public int maximo;
    public long[] porMaximo = new long[5];

    public Model() {
        String[][] datos = {
            {"Vps", "Fc Inter Turku", "14:00"}, {"Tps", "Ifk Mariehamn", "14:00"},
            {"Ac Oulu", "Ilves", "16:00"}, {"Fc Lahti", "Ff Jaro", "14:00"},
            {"If Gnistan", "Kups Kuopio", "14:00"}, {"Fredrikstad", "Sandefjord", "14:00"},
            {"Start", "Viking", "14:00"}, {"Molde", "Sarpsborg", "17:00"},
            {"Kfum Oslo", "Kristiansund", "17:00"}, {"Aalesunds", "Tromso", "17:00"},
            {"Brann", "Rosenborg", "19:15"}, {"Hacken", "Kalmar", "14:00"},
            {"Goteborg", "Degerfors", "14:00"}, {"Brommapojkarna", "Malmo", "14:00"},
            {"Aik", "Orgryte", "16:30"},
        };
        for (int i = 0; i < 15; i++) partidos[i] = new Partido(datos[i][0], datos[i][1], datos[i][2]);
        partidos[1].resultado(3, 0);
        partidos[3].resultado(2, 0);
        partidos[4].resultado(0, 1);
        partidos[5].resultado(1, 0);
        partidos[6].resultado(0, 3);
        partidos[11].resultado(1, 1);
        for (int i = 0; i < 14; i++) signos[i] = "1";
        signos[9] = "1X";
        signos[10] = "12";
        signos[11] = "1X2";
        cond[9] = true;
        cond[10] = true;
        cond[11] = true;
        recalc();
    }

    public void toggleSigno(int i, char s) {
        String cur = signos[i];
        String next = cur.indexOf(s) >= 0 ? cur.replace(String.valueOf(s), "") : cur + s;
        StringBuilder sb = new StringBuilder();
        for (char k : "1X2".toCharArray()) if (next.indexOf(k) >= 0) sb.append(k);
        signos[i] = sb.toString();
        if (signos[i].length() < 2) cond[i] = false;
        recalc();
    }

    public void toggleCond(int i) {
        if (signos[i].length() > 1) cond[i] = !cond[i];
        recalc();
    }

    public void setVar(int v) {
        var = var == v ? -1 : v;
        recalc();
    }

    public void setEqu(int v) {
        equ = equ == v ? -1 : v;
        recalc();
    }

    public void setDos(int v) {
        dos = dos == v ? -1 : v;
        recalc();
    }

    public void recalc() {
        int sinResultado = 0;
        for (int i = 0; i < 14; i++) if (!partidos[i].fin) sinResultado++;
        long[][][] dp = new long[15][15][15];
        long[][][] sig = new long[15][15][15];
        dp[0][0][0] = 1;
        for (int i = 0; i < 14; i++) {
            int lim = i + 1;
            for (int h = 0; h <= lim; h++)
                for (int x = 0; x <= lim; x++) Arrays.fill(sig[h][x], 0, lim + 1, 0);
            char real = partidos[i].real();
            for (int h = 0; h < lim; h++)
                for (int x = 0; x < lim; x++)
                    for (int d = 0; d < lim; d++) {
                        long n = dp[h][x][d];
                        if (n == 0) continue;
                        for (char s : signos[i].toCharArray()) {
                            int nx = x + (cond[i] && s == 'X' ? 1 : 0);
                            int nd = d + (cond[i] && s == '2' ? 1 : 0);
                            int nh = h + (s == real ? 1 : 0);
                            sig[nh][nx][nd] += n;
                        }
                    }
            long[][][] t = dp;
            dp = sig;
            sig = t;
        }
        columnas = 0;
        maximo = 0;
        Arrays.fill(porMaximo, 0);
        for (int h = 0; h < 15; h++)
            for (int x = 0; x < 15; x++)
                for (int d = 0; d < 15; d++) {
                    long n = dp[h][x][d];
                    if (n == 0) continue;
                    if (var >= 0 && x + d != var) continue;
                    if (equ >= 0 && x != equ) continue;
                    if (dos >= 0 && d != dos) continue;
                    columnas += n;
                    int max = h + sinResultado;
                    if (max > maximo) maximo = max;
                    if (max >= 10) porMaximo[max - 10] += n;
                }
    }

    public String precio() {
        return String.format(Locale.forLanguageTag("es-ES"), "%.2f €", columnas * PRECIO_APUESTA);
    }
}
