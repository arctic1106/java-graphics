package quiniela;

import ui.Canvas;
import ui.Theme;
import ui.Ui;
import ui.Widgets;

public class QuinielaScreen {
    static final float BAR_H = 56;
    static final float ROW_H = 52;
    static final float PLENO_H = 76;
    static final float BTN = 31;
    static final float GAP = 3.5f;
    static final float M = 5;

    public Model m = new Model();

    public void draw(Ui ui) {
        Canvas c = ui.c;
        float barBottom = ui.insetTop + BAR_H;
        ui.clipTop = barBottom;
        background(ui);
        c.save();
        c.clipRect(0, barBottom, ui.w, ui.h - barBottom);
        c.translate(0, -ui.scrollY);
        content(ui);
        c.restore();
        bar(ui);
    }

    public void background(Ui ui) {
        ui.c.fillRect(0, 0, ui.w, ui.h, Theme.SLATE50);
    }

    public void content(Ui ui) {
        ui.clipTop = ui.insetTop + BAR_H;
        float y = ui.clipTop + 8;
        y = partidos(ui, y);
        y = condiciones(ui, y + 6);
        y = precio(ui, y + 6);
        y = escrutinio(ui, y + 6);
        ui.contentH = y + 16 + ui.insetBottom;
    }

    float partidos(Ui ui, float y0) {
        Canvas c = ui.c;
        float pw = ui.w - 2 * M;
        Widgets.panel(c, M, y0, pw, 14 * ROW_H + PLENO_H, 12);
        float bx = ui.w - M - 4 - (4 * BTN + 3 * GAP);
        for (int i = 0; i < 14; i++) {
            float y = y0 + i * ROW_H;
            float cy = y + ROW_H / 2;
            Partido p = m.partidos[i];
            if (i > 0) Widgets.divider(c, M + 8, y, pw - 16, Theme.SLATE100);
            Widgets.pill(c, M + 5, cy, 22, 15, String.valueOf(i + 1), 11, Theme.SLATE100, Theme.SLATE500);
            c.save();
            c.clipRect(M + 30, y, bx - 74 - M - 30 + 4, ROW_H);
            Widgets.stack(c, p.local, p.visit, M + 32, cy, 14, 10, false, Theme.SLATE800);
            c.restore();
            estado(c, p, bx, cy);
            for (int k = 0; k < 3; k++) {
                char s = "1X2".charAt(k);
                float x = bx + k * (BTN + GAP);
                if (Widgets.toggle(ui, x, cy - BTN / 2, BTN, BTN, 8, String.valueOf(s), 16, m.signos[i].indexOf(s) >= 0, Theme.SLATE800)) {
                    m.toggleSigno(i, s);
                }
            }
            float cx = bx + 3 * (BTN + GAP);
            if (Widgets.toggle(ui, cx, cy - BTN / 2, BTN, BTN, 8, "C", 16, m.cond[i], Theme.GREEN600)) m.toggleCond(i);
        }
        pleno(ui, M, y0 + 14 * ROW_H, pw);
        return y0 + 14 * ROW_H + PLENO_H;
    }

    void pleno(Ui ui, float px, float y, float pw) {
        Canvas c = ui.c;
        Partido p = m.partidos[14];
        float cy = y + PLENO_H / 2;
        Widgets.divider(c, px + 8, y, pw - 16, Theme.SLATE100);
        Widgets.pill(c, px + 5, cy, 22, 15, "15", 11, Theme.SLATE100, Theme.SLATE500);
        Widgets.stack(c, p.local, p.visit, px + 32, cy, 14, 10, false, Theme.SLATE800);
        float bw = 24;
        float g = 6;
        float x0 = ui.w - M - 4 - (4 * bw + 3 * g);
        c.text("2 Aug", x0 - 12, cy - 8, 11, false, Theme.SLATE500, Canvas.RIGHT);
        c.text(p.hora, x0 - 12, cy + 8, 11, false, Theme.SLATE500, Canvas.RIGHT);
        for (int fila = 0; fila < 2; fila++) {
            float by = cy - 26.5f + fila * 29;
            int elegido = fila == 0 ? m.plenoLocal : m.plenoVisit;
            for (int k = 0; k < 4; k++) {
                String t = k == 3 ? "M" : String.valueOf(k);
                if (Widgets.toggle(ui, x0 + k * (bw + g), by, bw, bw, 8, t, 16, elegido == k, Theme.SLATE800)) {
                    if (fila == 0) m.plenoLocal = k;
                    else m.plenoVisit = k;
                }
            }
        }
    }

    void estado(Canvas c, Partido p, float bx, float cy) {
        float x = bx - 70;
        if (p.fin) {
            Widgets.centered(c, String.valueOf(p.golesLocal), x + 12, cy - 11, 16, true, Theme.SLATE900);
            Widgets.centered(c, String.valueOf(p.golesVisit), x + 12, cy + 11, 16, true, Theme.SLATE900);
            Widgets.label(c, "Fin.", x + 32, cy, 12, false, Theme.SLATE500);
        } else {
            Widgets.tag(c, x, cy, 20, "1", 13, Theme.SLATE500, Theme.WHITE);
            Widgets.stack(c, "2 Aug", p.hora, x + 26, cy, 12, 8, false, Theme.SLATE500);
        }
    }

    float condiciones(Ui ui, float y0) {
        Canvas c = ui.c;
        float pw = ui.w - 2 * M;
        float rowH = 38;
        float h = 3 * rowH + 8;
        Widgets.panel(c, M, y0, pw, h, 12);
        String[] et = {"VAR", "EQU", "DOS"};
        int[] sel = {m.var, m.equ, m.dos};
        float x0 = M + 38;
        float gap = 3;
        float cw = (pw - 38 - 8 - 9 * gap) / 10;
        for (int f = 0; f < 3; f++) {
            float cy = y0 + 4 + f * rowH + rowH / 2;
            Widgets.label(c, et[f], M + 9, cy, 12, true, Theme.SLATE500);
            for (int k = 0; k < 10; k++) {
                if (Widgets.toggle(ui, x0 + k * (cw + gap), cy - 13, cw, 26, 8, String.valueOf(k), 14, sel[f] == k, Theme.GREEN600)) {
                    if (f == 0) m.setVar(k);
                    else if (f == 1) m.setEqu(k);
                    else m.setDos(k);
                }
            }
        }
        return y0 + h;
    }

    float precio(Ui ui, float y0) {
        Canvas c = ui.c;
        Widgets.card(c, M + 4, y0, ui.w - 2 * M - 8, 36, 14, Theme.SLATE900);
        Widgets.centered(c, "PRECIO TOTAL: " + m.precio(), ui.w / 2, y0 + 18, 16, true, Theme.WHITE);
        return y0 + 36;
    }

    float escrutinio(Ui ui, float y0) {
        Canvas c = ui.c;
        float pw = ui.w - 2 * M - 8;
        float x = M + 4;
        Widgets.card(c, x, y0, pw, 100, 14, Theme.SLATE900);
        Widgets.centered(c, "RESUMEN DE ACIERTOS", ui.w / 2, y0 + 17, 12, true, Theme.SLATE400);
        Widgets.divider(c, x + 12, y0 + 30, pw - 24, Theme.SLATE600);
        String[] et = {"10", "11", "12", "13", "14", "P15"};
        float cw = (pw - 24) / 6;
        for (int k = 0; k < 6; k++) {
            float cx = x + 12 + cw * k + cw / 2;
            Widgets.centered(c, et[k], cx, y0 + 44, 11, false, Theme.SLATE400);
            Widgets.centered(c, k < 5 ? String.valueOf(m.porMaximo[k]) : "0", cx, y0 + 62, 18, true, Theme.WHITE);
        }
        Widgets.centered(c, m.columnas + " COLUMNAS • MÁXIMO: " + m.maximo, ui.w / 2, y0 + 84, 11, true, Theme.WHITE);
        return y0 + 100;
    }

    public void bar(Ui ui) {
        Canvas c = ui.c;
        float W = ui.w;
        float cy = ui.insetTop + BAR_H / 2;
        c.fillRect(0, 0, W, ui.insetTop + BAR_H, Theme.TOPBAR);
        for (int k = -1; k <= 1; k++) {
            c.line(14, cy + k * 6, 32, cy + k * 6, 2, Theme.WHITE);
            c.fillCircle(W - 22, cy + k * 6, 1.8f, Theme.WHITE);
        }
        Widgets.label(c, "JORNADA " + m.jornada, 50, cy, 20, true, Theme.WHITE);
        chevron(c, W - 150, cy, -1);
        if (ui.clickedFixed(W - 168, cy - 20, 36, 40)) m.jornada--;
        chevron(c, W - 66, cy, 1);
        if (ui.clickedFixed(W - 84, cy - 20, 36, 40)) m.jornada++;
        float calx = W - 108;
        c.strokeRoundRect(calx - 9, cy - 9, 18, 18, 3, 2, Theme.WHITE);
        c.line(calx - 9, cy - 3, calx + 9, cy - 3, 2, Theme.WHITE);
        c.line(calx - 4, cy - 12, calx - 4, cy - 6, 2, Theme.WHITE);
        c.line(calx + 4, cy - 12, calx + 4, cy - 6, 2, Theme.WHITE);
    }

    void chevron(Canvas c, float x, float cy, int dir) {
        c.line(x - 4 * dir, cy - 8, x + 4 * dir, cy, 2, Theme.WHITE);
        c.line(x + 4 * dir, cy, x - 4 * dir, cy + 8, 2, Theme.WHITE);
    }
}
