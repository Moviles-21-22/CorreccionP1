package es.ucm.arblemar.engine;

public interface Engine {
    /**
     * Inicializa el engine
     * */
    public boolean init(State initState, String nameGame, int w, int h);
    /**
     * Inicializa un nuevo estado
     * */
    public boolean initNewState(State newState);

    /**
     * Bucle principal del juego. Encargado de actualizar render y update de los
     * estados del juego, así como de llevar la cuenta del deltaTime
     * */
    public void run();

    /**
     * Devuelve la clase graphics implementada por la plataforma
     * */
    public Graphics getGraphics();
    /**
     * Devuelve la clase input implementada por la plataforma
     * */
    public Input getInput();
}