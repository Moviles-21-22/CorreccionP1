package es.ucm.arblemar.engine;

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