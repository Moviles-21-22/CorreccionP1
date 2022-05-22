package es.ucm.arblemar.gamelogic;

public enum TipoUndo {
    NONE(0),
    GRIS(1),
    AZUL(2),
    ROJO(3);

    private TipoUndo(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    private final int value;
}
