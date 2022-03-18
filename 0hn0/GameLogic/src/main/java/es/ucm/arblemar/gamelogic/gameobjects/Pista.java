package es.ucm.arblemar.gamelogic.gameobjects;

public class Pista {
    public Pista() {
        _tipo = TipoPista.NONE;
        pos = new int[2];
        size = new int[2];
        st = new String[2];
    }

    public void setPos(int x, int y) { pos[0] = x; pos[1] = y; }
    public int[] getPos() {
        return pos;
    }

    public int[] getSize(int winWidthLog) {
        switch (_tipo) {
            case CERRAR_CASILLA:
                size[0] = winWidthLog / 4;
                size[1] = (winWidthLog / 15) * 4;
                break;
            case DEBE_SER_PARED:
                size[0] = (winWidthLog / 20) * 9;
                size[1] = (winWidthLog / 36) * 17;
                break;
            case DEBE_SER_AZUL:
                size[0] = (winWidthLog / 10) * 4;
                size[1] = (winWidthLog / 10) * 4;
                break;
        }
        return size;
    }

    public void setTipo(TipoPista tipo) { _tipo = tipo; }
    public TipoPista getTipo() { return _tipo; }

    public String[] getTextoPista() {
        switch (_tipo) {
            case CERRAR_CASILLA:
                st[0] = "Esta celda ya ve";
                st[1] = "todas sus vecinas";
                break;
            case DEBE_SER_PARED:
                st[0] = "Se debe poner una pared o";
                st[1] = "pasará el número de vecinas";
                break;
            case DEBE_SER_AZUL:
                st[0] = "Se debe poner celda azul";
                st[1] = "para alcanzar las vecinas";
                break;
        }
        return st;
    }

    private TipoPista _tipo;
    private int pos[];  //Para renderizar el círculo negro
    private int size[];
    private String[] st;
}