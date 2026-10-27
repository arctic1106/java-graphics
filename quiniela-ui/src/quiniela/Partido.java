package quiniela;

public class Partido {
    public String local;
    public String visit;
    public String hora;
    public boolean fin;
    public int golesLocal;
    public int golesVisit;

    public Partido(String local, String visit, String hora) {
        this.local = local;
        this.visit = visit;
        this.hora = hora;
    }

    public Partido resultado(int gl, int gv) {
        fin = true;
        golesLocal = gl;
        golesVisit = gv;
        return this;
    }

    public char real() {
        if (!fin) return 0;
        if (golesLocal > golesVisit) return '1';
        if (golesLocal < golesVisit) return '2';
        return 'X';
    }
}
