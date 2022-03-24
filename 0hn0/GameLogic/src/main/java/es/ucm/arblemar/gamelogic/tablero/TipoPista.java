package es.ucm.arblemar.gamelogic.tablero;

public enum TipoPista {
    /**
     * No hay ninguna pista activa
     */
    NONE,

    /**
     * Si un número tiene ya visibles el número de celdas que dice, entonces se puede
     * “cerrar”, es decir, poner paredes en los extremos
     */
    CERRAR_CASILLA,

    /**
     * Si pusiéramos un punto azul en una celda vacía, superaríamos el número de visibles
     * del número, y por tanto, debe ser una pared
     */
    DEBE_SER_PARED,

    /**
     * Si no ponemos un punto en alguna celda vacía, entonces es imposible alcanzar el
     * número
     */
    DEBE_SER_AZUL,

    /**
     * Un número tiene más casillas azules visibles de las que debería
     */
    DEMASIADAS_AZULES,

    /**
     * Un número tiene una cantidad insuficiente de casillas azules visibles y sin embargo
     * ya está “cerrada” (no puede ampliarse más por culpa de paredes)
     */
    INSUFICIENTES_AZULES,

    /**
     * Si una celda está vacía y cerrada y no ve ninguna celda azul, entonces es pared (todos
     * los puntos azules deben ver al menos a otro)
     */
    GRIS_ES_ROJA,

    /**
     * En sentido opuesto, si hay una celda azul puesta por el usuario que está cerrada
     * y no ve a ninguna otra, entonces se trata de un error por el mismo motivo.
     */
    AZUL_ES_ROJA;

    TipoPista() {}
}