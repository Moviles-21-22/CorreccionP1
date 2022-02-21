package es.ucm.arblemar.engine;

public interface Engine {
    /**
     * Solicita un cambio de estado a newState
     * */
    public void reqNewState(State newState);

    /**
     * Devuelve la clase graphics implementada por la plataforma
     * */
    public Graphics getGraphics();
    /**
     * Devuelve la clase input implementada por la plataforma
     * */
    public Input getInput();
}