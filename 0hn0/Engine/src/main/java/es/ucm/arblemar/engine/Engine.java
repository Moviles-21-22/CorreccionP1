package es.ucm.arblemar.engine;

public interface Engine {
    /**
     * Inicializa un nuevo estado
     * */
    public boolean initNewState(State newState);

    /**
     * Devuelve la clase graphics implementada por la plataforma
     * */
    public Graphics getGraphics();
    /**
     * Devuelve la clase input implementada por la plataforma
     * */
    public Input getInput();
}