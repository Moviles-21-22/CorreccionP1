package es.ucm.arblemar.gamelogic.tablero;

public enum TipoPista {
    /**
     * No hay ninguna pista activa
     */
    NONE(1),

    //Pistas básicas

    /**
     * Si un número tiene ya visibles el número de celdas que dice, entonces se puede
     * “cerrar”, es decir, poner paredes en los extremos
     */
    CERRAR_CASILLA(2),

    /**
     * Si pusiéramos un punto azul en una celda vacía, superaríamos el número de visibles
     * del número, y por tanto, debe ser una pared
     */
    DEBE_SER_PARED(3),

    /**
     * Si no ponemos un punto en alguna celda vacía, entonces es imposible alcanzar el
     * número
     */
    DEBE_SER_AZUL(4),

    //Pistas sobre celdas sin número

    /**
     * Si una celda está vacía y cerrada y no ve ninguna celda azul, entonces es pared (todos
     * los puntos azules deben ver al menos a otro)
     */
    GRIS_ES_ROJA(5),

    /**
     * En sentido opuesto, si hay una celda azul puesta por el usuario que está cerrada
     * y no ve a ninguna otra, entonces se trata de un error por el mismo motivo.
     */
    AZUL_ES_ROJA(6),

    //Pistas cuando el jugador se ha equivocado

    /**
     * Un número tiene más casillas azules visibles de las que debería
     */
    DEMASIADAS_AZULES(7),

    /**
     * Un número tiene una cantidad insuficiente de casillas azules visibles y sin embargo
     * ya está “cerrada” (no puede ampliarse más por culpa de paredes)
     */
    INSUFICIENTES_AZULES(8);

    private TipoPista(int i) {
        this.value = i;
    }

    public int getValue(){
        return value;
    }

    private int value;
}