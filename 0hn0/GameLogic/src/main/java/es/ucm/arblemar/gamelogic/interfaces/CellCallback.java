package es.ucm.arblemar.gamelogic.interfaces;

import es.ucm.arblemar.gamelogic.states.GameState;

/**
 * Callback dedicado a las celdas
 */
public interface CellCallback {
    /**
     * Callback de la celda para que haga algo
     * */
    void doSomething(int x, int y, GameState gm);
}