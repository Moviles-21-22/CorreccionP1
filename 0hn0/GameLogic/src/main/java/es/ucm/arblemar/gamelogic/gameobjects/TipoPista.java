package es.ucm.arblemar.gamelogic.gameobjects;

public enum TipoPista {
    NONE(0),
    /**
     * Si un número tiene ya visibles el número de celdas que dice, entonces se puede
     * “cerrar”, es decir, poner paredes en los extremos
     */
    CERRAR_CASILLA(1),
    /**
     * Si pusiéramos un punto azul en una celda vacía, superaríamos el número de visibles
     * del número, y por tanto, debe ser una pared
     */
    DEBE_SER_PARED(2);

    TipoPista(int i) {
        _numPista = i;
    }

    public int getNumPista() { return _numPista; }

    private int _numPista;
}