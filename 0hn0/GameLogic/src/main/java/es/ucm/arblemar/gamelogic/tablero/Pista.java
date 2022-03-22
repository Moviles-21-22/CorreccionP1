package es.ucm.arblemar.gamelogic.tablero;

public class Pista {
    public Pista() {
        _tipo = TipoPista.NONE;
        _pos = new int[2];
        _size = new int[2];
        _st = new String[2];
    }

    public void setPos(int[] p) {
        _pos = p;
    }

    public int[] getPos() {
        return _pos;
    }

    public int[] getSize(int winWidthLog) {
        switch (_tipo) {
            case CERRAR_CASILLA:
                _size[0] = winWidthLog / 4;
                _size[1] = (winWidthLog / 15) * 4;
                break;
            case DEBE_SER_PARED:
                _size[0] = (winWidthLog / 20) * 9;
                _size[1] = (winWidthLog / 36) * 17;
                break;
            case DEBE_SER_AZUL:
                _size[0] = (winWidthLog / 10) * 4;
                _size[1] = (winWidthLog / 10) * 4;
                break;
            case DEMASIADAS_AZULES:
            case INSUFICIENTES_AZULES:
                _size[0] = (winWidthLog / 9) * 2;
                _size[1] = (winWidthLog / 10) * 3;
                break;
        }
        return _size;
    }

    public void setTipo(TipoPista tipo) {
        _tipo = tipo;
    }

    public TipoPista getTipo() {
        return _tipo;
    }

    public String[] getTextoPista() {
        switch (_tipo) {
            case CERRAR_CASILLA:
                _st[0] = "Esta celda ya ve";
                _st[1] = "todas sus vecinas";
                break;
            case DEBE_SER_PARED:
                _st[0] = "Se debe poner una pared o";
                _st[1] = "pasará el número de vecinas";
                break;
            case DEBE_SER_AZUL:
                _st[0] = "Se debe poner celda azul";
                _st[1] = "para alcanzar las vecinas";
                break;
            case DEMASIADAS_AZULES:
                _st[0] = "La celda tiene";
                _st[1] = "demasiadas vecinas";
                break;
            case INSUFICIENTES_AZULES:
                _st[0] = "Este número no";
                _st[1] = "puede ver demasiado";
                break;
        }
        return _st;
    }

    private TipoPista _tipo;
    //Para renderizar el círculo negro
    private int[] _pos;
    private final int[] _size;
    private final String[] _st;
}