package es.ucm.arblemar.gamelogic.enums;

/**
 * Enumera los 3 tipos diferentes de celda que hay en el juego
 */
public enum TipoCelda {
    GRIS(0),
    AZUL(1),
    ROJO(2),

    MAX(3);

    TipoCelda(int i) {
        this.value = i;
    }

    public int getValue(){
        return value;
    }

    private int value;
};