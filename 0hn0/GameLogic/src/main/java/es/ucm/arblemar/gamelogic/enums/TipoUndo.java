package es.ucm.arblemar.gamelogic.enums;

/**
 * Enumera los diferentes tipos de acciones de deshacer
 * movimiento que se pueden dar en el juego
 */
public enum TipoUndo {
    NONE(0),
    GRIS(1),
    AZUL(2),
    ROJO(3);

    TipoUndo(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    private final int value;
}
