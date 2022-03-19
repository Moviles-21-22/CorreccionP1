package es.ucm.arblemar.gamelogic.gameobjects;

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

    //PISTAS QUE SIGNIFCAN ERROR

    /**
     * Un número tiene más casillas azules visibles de las que debería
     */
    DEMASIADAS_AZULES;

    TipoPista() {}
}