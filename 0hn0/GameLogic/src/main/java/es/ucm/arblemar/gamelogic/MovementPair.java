package es.ucm.arblemar.gamelogic;

import es.ucm.arblemar.gamelogic.tablero.Celda;
import es.ucm.arblemar.gamelogic.tablero.TipoCelda;

/**
 * Clase que guarda una represenación de la Celda y el color
 * con el que se añadió a la lista de movimientos
 */
public class MovementPair {
    public MovementPair(Celda c) {
        _cell = c;
        _cellType = c.getTipoCelda();
    }

    public Celda getCelda() {
        return _cell;
    }

    public TipoCelda getTipoCelda() {
        return _cellType;
    }

    Celda _cell;
    TipoCelda _cellType;
}
