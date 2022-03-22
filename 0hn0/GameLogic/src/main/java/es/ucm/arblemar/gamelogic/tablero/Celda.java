package es.ucm.arblemar.gamelogic.tablero;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.gamelogic.ButtonCallback;
import es.ucm.arblemar.gamelogic.CellCallback;

import static es.ucm.arblemar.gamelogic.tablero.TipoCelda.AZUL;
import static es.ucm.arblemar.gamelogic.tablero.TipoCelda.GRIS;
import static es.ucm.arblemar.gamelogic.tablero.TipoCelda.ROJO;

/**
 * Clase que representa a todas las celdas del juego. Dependiendo de su
 * color podrá tener unos comportamientos u otros.
 */
public class Celda {
    public Celda(TipoCelda tc, Font f, int tamFont, int val, int pos[],
                 float diam, int ind[]) {
        _tipoCelda = tc;
        // La celda se bloquea cuando es distinta de gris
        _lock = _tipoCelda != TipoCelda.GRIS;
        _font = f;
        _tamF = tamFont;
        _value = val;
        _pos = new int[2];
        _pos[0] = pos[0];
        _pos[1] = pos[1];
        _i = ind[0];
        _j = ind[1];
        _diametro = diam;
        switch (_tipoCelda) {
            case GRIS: {
                _color = 0XEEEEEEFF;
                break;
            }
            case AZUL: {
                _color = 0x1CC0E0FF;
                break;
            }
            case ROJO: {
                _color = 0xFF384BFF;
                break;
            }
        }

        _drawText = _value > 0;
        if (!_drawText) return;

        posicionaTexto();
    }

    public void render(es.ucm.arblemar.engine.Graphics g) {
        //Poner animación si la tiene
        //if (anSt == 1) {
        //    g.setColor(color);
        //    g.fillCircle(new Vector2(_pos._x - 5, _pos._y - 5), (int)_diametro + 10);
        //}

        g.setColor(_color);
        g.fillCircle(_pos[0], _pos[1], _diametro);
        g.setColor(0XFFFFFFFF);

        if (!_drawText) return;

        g.drawText(Integer.toString(_value), _textX, _textY, _font, _tamF);
    }

    /**
     * Pone en marcha la funcionalidad
     * asociada al callback
     */
    public void runCallBack() {
        if (_cb != null) {
            _cb.doSomething();
        }
        if (_cbc != null) {
            _cbc.doSomething(_i, _j);
        }
    }

    /**
     * Asocia un nuevo callback a la celda
     */
    public void setCallback(ButtonCallback cb) {
        _cb = cb;
    }

    public void setCellCallback(CellCallback cbc, int i, int j) {
        _cbc = cbc;
        _i = i;
        _j = j;
    }

    /**
     * Devuelve si la celda está bloqueada o no
     */
    public boolean isLock() {
        return _lock;
    }

    /**
     * Determina si la celda ha sido pulsada
     */
    public boolean isClicked(int eventPosX, int eventPosY) {
        double xDiff = (_pos[0] + (_diametro / 2)) - eventPosX;
        double yDiff = (_pos[1] + (_diametro / 2)) - eventPosY;
        double distance = Math.sqrt((Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));
        return distance <= (int) (_diametro / 2);
    }

    /**
     * Devuelve el valor asociado al circulo
     */
    public int getValue() {
        return _value;
    }

    public TipoCelda getTipoCelda() { return _tipoCelda; }

    public int[] getPos() { return _pos; }

    public void setColor() {
        switch (_tipoCelda) {
            case GRIS: {
                _tipoCelda = AZUL;
                _color = 0x1CC0E0FF;
                break;
            }
            case AZUL: {
                _tipoCelda = ROJO;
                _color = 0xFF384BFF;
                break;
            }
            case ROJO: {
                _tipoCelda = GRIS;
                _color = 0XEEEEEEFF;
                break;
            }
        }
    }

    /**
     * Posiciona el texto dentro del circulo en función del número,
     * ya que no todos tienen el mismo tamaño
     */
    private void posicionaTexto() {
        if (_value == 1) {
            _textX = (int) (_pos[0] + (_diametro * 4 / 9));
        } else if (_value == 8) {
            _textX = (int) (_pos[0] + (_diametro * 3 / 8));
        } else if (_value == 7 || _value == 3 || _value == 9) {
            _textX = (int) (_pos[0] + (_diametro * 2 / 5));
        } else {
            _textX = (int) (_pos[0] + (_diametro / 3));
        }

        _textY = (int) (_pos[1] + (_diametro * 5 / 7));
    }

    // ATRIBUTOS DE LAS CELDAS
    //  Tipo de celda
    private TipoCelda _tipoCelda = TipoCelda.GRIS;
    // Determina si esta celda está bloqueada
    private boolean _lock = false;
    // Fuente del texto de la celda
    private Font _font;
    // Tamaño de la fuente
    private int _tamF;
    //  Valor de la celda
    private int _value;
    // Posición donde se va a renderizar la celda
    private int _pos[];
    //  Index dentro del tablero
    private int _i, _j;
    // Diametro del circulo
    private float _diametro;
    // Color inicial del circulo
    private int _color;
    // Determina si la celda escribe texto
    private boolean _drawText = false;
    // Posicion X del texto
    int _textX = 0;
    // Posición Y del texto
    int _textY = 0;
    // Callback de las celdas del menu y del tablero
    private ButtonCallback _cb;
    private CellCallback _cbc;
};