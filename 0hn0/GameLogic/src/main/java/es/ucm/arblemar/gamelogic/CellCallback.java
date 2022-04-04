package es.ucm.arblemar.gamelogic;

import es.ucm.arblemar.gamelogic.states.GameState;

public interface CellCallback {
    /**
     * Callback del botón para que haga algo
     * */
    void doSomething(int x, int y, GameState gm);
}