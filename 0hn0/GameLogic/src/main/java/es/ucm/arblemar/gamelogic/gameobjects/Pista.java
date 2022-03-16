package es.ucm.arblemar.gamelogic.gameobjects;

public class Pista {
    public Pista() {
        _tipo = TipoPista.NONE;
        pos = new int[2];
    }

    public void setPos(int x, int y) { pos[0] = x; pos[1] = y; }
    public int[] setPos() { return pos; }

    public void setTipo(TipoPista tipo) { _tipo = tipo; }
    public TipoPista getTipo() { return _tipo; }

    public String[] getTextoPista() {
        String[] st = new String[2];
        st[0] = "Esta celda ya ve";
        st[1] = "todas sus vecinas";
        return st;
    }

    private TipoPista _tipo;
    private int pos[];
}