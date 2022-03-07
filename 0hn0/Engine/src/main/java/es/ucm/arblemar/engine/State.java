package es.ucm.arblemar.engine;

/**
 * Interfaz de los diferentes estados del juego. Debe ir en Engine
 * porque se usa en otras interfaces y clases Abstractas para la comunicación
 * del cambio de estado pricipalmente
 */
public interface State {
    /**
     * Inicializa el estado del juego
     * @return devuelve false si ha fallado
     * */
    boolean init();
    /**
     * Actualiza la parte de la lógica del estado actual
     * */
    void update(double deltaTime);
    /**
     * Actualiza la parte gráfica del estado actual
     * */
    void render();
    /**
     * Gestiona los eventos entrantes
     * */
    void handleInput();
}