package es.ucm.arblemar.gamelogic;

import es.ucm.arblemar.gamelogic.enums.TipoUndo;
import es.ucm.arblemar.gamelogic.tablero.Celda;
import es.ucm.arblemar.gamelogic.enums.TipoCelda;

/**
 * Clase que guarda una represenación de la Celda y el color
 * con el que se añadió a la lista de movimientos
 */
public class MovementInfo {
    public MovementInfo(Celda c) {
        _cell = c;
        _prevType = c.getTipoCelda();
        switch (_prevType){
            case GRIS:
                _undoType = TipoUndo.GRIS;
                break;
            case AZUL:
                _undoType = TipoUndo.AZUL;
                break;
            case ROJO:
                _undoType = TipoUndo.ROJO;
                break;
        }
    }

    public Celda getCelda() {
        return _cell;
    }

    public TipoCelda getTipoCelda() {
        return _prevType;
    }

    public TipoUndo getTipoUndo(){
        return _undoType;
    }

    Celda _cell;
    TipoCelda _prevType;
    TipoUndo _undoType;
}
