package es.ucm.arblemar.gamelogic.tablero;

public class Pista {
    public Pista() {
        _tipo = TipoPista.NONE;
        indexCelda = new int[2];
        _size = new int[2];
        _st = new String[2];
    }

    public void setPos(int[] p) {
        indexCelda = p;
    }

    public int[] getIndexCelda() {
        return indexCelda;
    }

    public int[] getSize(int winWidthLog) {
        switch (_tipo) {
            case CERRAR_CASILLA:
                _size[0] = winWidthLog / 4;
                _size[1] = (winWidthLog / 15) * 4;
                break;
            case DEBE_SER_PARED:
                _size[0] = (winWidthLog / 20) * 9;
                _size[1] = (winWidthLog / 9) * 4;
                break;
            case DEBE_SER_AZUL:
            case AZULES_ALCANZABLES:
                _size[0] = (winWidthLog / 5) * 2;
                _size[1] = (winWidthLog / 5) * 2;
                break;
            case DEMASIADAS_AZULES:
            case INSUFICIENTES_AZULES:
                _size[0] = (winWidthLog / 9) * 2;
                _size[1] = (winWidthLog / 10) * 3;
                break;
            case GRIS_ES_ROJA:
                _size[0] = (winWidthLog / 16) * 3;
                _size[1] = (winWidthLog / 8);
                break;
            case AZUL_ES_ROJA:
            case UNA_DIRECCION:
                _size[0] = (winWidthLog / 12) * 4;
                _size[1] = (winWidthLog / 11) * 4;
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
                _st[0] = "Si se pone una azul en una";
                _st[1] = "dirección se supera el valor";
                break;
            case DEBE_SER_AZUL:
            case AZULES_ALCANZABLES:
                _st[0] = "Se debe poner celda azul";
                _st[1] = "para alcanzar las vecinas";
                break;
            case UNA_DIRECCION:
                _st[0] = "Solo existe una";
                _st[1] = "direccón posible";
                break;
            case DEMASIADAS_AZULES:
                _st[0] = "La celda tiene";
                _st[1] = "demasiadas vecinas";
                break;
            case INSUFICIENTES_AZULES:
                _st[0] = "Este número no";
                _st[1] = "puede ver suficiente";
                break;
            case GRIS_ES_ROJA:
                _st[0] = "Esta debería";
                _st[1] = "ser fácil";
                break;
            case AZUL_ES_ROJA:
                _st[0] = "Una celda azul debe";
                _st[1] = "ver al menos otra azul";
                break;
            case NONE:
            default:
                _st[0] = " ";
                _st[1] = " ";
        }
        return _st;
    }

    // ATRIBUTOS
    /**
     * Tipo de la pista que se genera
     */
    private TipoPista _tipo;
    /**
     * Índice de la celda asociada a la pista
     */
    private int[] indexCelda;
    /**
     * Tamaño del texto de la pista
     */
    private final int[] _size;
    /**
     * Mensaje de la pista
     */
    private final String[] _st;
}