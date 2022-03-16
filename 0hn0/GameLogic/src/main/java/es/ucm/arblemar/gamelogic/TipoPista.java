package es.ucm.arblemar.gamelogic;

public enum TipoPista {
    /**
     * Si un número tiene ya visibles el número de celdas que dice, entonces se puede
     * “cerrar”, es decir, poner paredes en los extremos
     */
    NONE(0),
    CERRAR_CASILLA(1);

    TipoPista(int i) {
        _numPista = i;
    }

    public int getNumPista() { return _numPista; }

    private int _numPista;
}