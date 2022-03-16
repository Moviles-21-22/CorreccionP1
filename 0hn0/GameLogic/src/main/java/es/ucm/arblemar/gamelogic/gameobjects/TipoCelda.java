package es.ucm.arblemar.gamelogic.gameobjects;

public enum TipoCelda {
    GRIS(0),
    AZUL(1),
    ROJO(2),

    MAX(3);

    private TipoCelda(int i) {
        this.value = i;
    }

    public int getValue(){
        return value;
    }

    private int value;
};