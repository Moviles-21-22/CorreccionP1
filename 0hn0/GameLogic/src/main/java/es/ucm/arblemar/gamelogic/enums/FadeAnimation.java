package es.ucm.arblemar.gamelogic.enums;

/**
 * Enumera las diferentes animaciones de fade-in/out que se pueden dar en el juego.
 * Estas animaciones solo suceden con los textos del título que se muestra encima
 * del tablero.
 */
public enum FadeAnimation {
    NONE(0),
    TITLE_HINT(1),
    TITLE_UNDO(2),
    UNDO_HINT(3);

    FadeAnimation(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    private final int value;
}
