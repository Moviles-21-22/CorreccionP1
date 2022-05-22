package es.ucm.arblemar.gamelogic.tablero;

import java.util.ArrayList;
import java.util.List;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.gamelogic.Assets;
import es.ucm.arblemar.gamelogic.interfaces.ButtonCallback;
import es.ucm.arblemar.gamelogic.interfaces.CellCallback;
import es.ucm.arblemar.gamelogic.enums.TipoCelda;
import es.ucm.arblemar.gamelogic.states.GameState;

import static es.ucm.arblemar.gamelogic.enums.TipoCelda.AZUL;
import static es.ucm.arblemar.gamelogic.enums.TipoCelda.GRIS;
import static es.ucm.arblemar.gamelogic.enums.TipoCelda.ROJO;

/**
 * Clase que representa a todas las celdas del juego. Dependiendo de su
 * color podrá tener unos comportamientos u otros.
 */
public class Celda {
    public Celda(TipoCelda tc, Font f, int tamFont, int val, int[] pos, float diam, int[] ind) {
        _tipoCelda = tc;
        _visibles = new ArrayList<>();

        // La celda se bloquea cuando es distinta de gris
        _lock = _tipoCelda != TipoCelda.GRIS;
        _font = f;
        _tamF = tamFont;
        _value = val;
        _pos = new int[2];
        _pos[0] = pos[0];
        _pos[1] = pos[1];
        _iniPos = new int[2];
        _iniPos[0] = pos[0];
        _iniPos[1] = pos[1];
        _diametro = diam;
        _i = ind[0];
        _j = ind[1];

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

        //ANIMACIONES
        initAnims();

        _drawText = _value > 0;
        if (!_drawText) return;

        posicionaTexto();
    }

    public void update(double deltaTime) {
        //En caso de tener animación, la procesa
        if (_actAnim) {
            _currFrame += deltaTime * _diametroMin;
            if (_currFrame - _lastFrame >= _nextFrame) {
                _diametro += (_dirAnim * 2);
                _pos[0] -= _dirAnim;
                _pos[1] -= _dirAnim;
                if (_diametro >= _diametroMax || _diametro <= _diametroMin) {
                    _dirAnim *= -1;
                    _numAnims++;

                    //Terminamos la animacion
                    if (_numAnims == 6) {
                        _actAnim = false;
                        _lastFrame = 0;
                        _currFrame = 0;
                        _dirAnim = 1;
                        _numAnims = 0;
                        _diametro = _diametroMin;
                        _pos[0] = _iniPos[0];
                        _pos[1] = _iniPos[1];
                    }
                }
                _lastFrame = _currFrame;
            }
        }
    }

    public void render(es.ucm.arblemar.engine.Graphics g) {
        g.setColor(_color);
        g.fillCircle(_pos[0], _pos[1], _diametro);
        g.setColor(0XFFFFFFFF);

        if (_candado) {
            g.drawImage(Assets.lock, _posCand[0], _posCand[1], _tamF, _tamF);
            return;
        }

        if (!_drawText) return;

        g.drawText(Integer.toString(_value), _textX, _textY, _font, _tamF);
    }

//-------------------------------------------CALLBACKS--------------------------------------------//

    /**
     * Pone en marcha la funcionalidad
     * asociada al callback
     */
    public void runCallBack(GameState gm) {
        if (_cb != null) {
            _cb.doSomething();
        }

        if (_cbc != null) {
            _cbc.doSomething(_i, _j, gm);
        }
    }

    /**
     * Asocia un nuevo callback a la celda
     *
     * @param cb: Callback de tipo ButtonCallback
     */
    public void setCallback(ButtonCallback cb) {
        _cb = cb;
    }

    /**
     * Asocia el nuevo callback a la celda
     *
     * @param cbc: Callback de tipo CellCallback
     */
    public void setCellCallback(CellCallback cbc) {
        _cbc = cbc;
    }

//------------------------------------------------------------------------------------------------//

//--------------------------------------------GET-SET---------------------------------------------//

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

    /**
     * Devuelve el tipo de la celda
     */
    public TipoCelda getTipoCelda() {
        return _tipoCelda;
    }

    /**
     * Devuelve la posición de renderizado de la celda
     */
    public int[] getPos() {
        return _pos;
    }

    /**
     * Devuelve la fila de la celda en el tablero
     */
    public int getRow() {
        return _i;
    }

    /**
     * Devuelve la columna de la celda en el tablero
     */
    public int getCol() {
        return _j;
    }

    /**
     * Devuelve la lista de visibles
     */
    public List<Celda> getVisibles() {
        return _visibles;
    }

    /**
     * Asigna un nuevo tipo de celda
     *
     * @param c: El nuevo tipo de celda
     */
    public void setTipoCelda(TipoCelda c) {
        _tipoCelda = c;
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
    }

    /**
     * Cambia el estado lock de la celda
     */
    public void setLock(boolean isLock) {
        _lock = isLock;
    }

    /**
     * Añade una nueva celda a la lista de visibles
     *
     * @param c Celda visible
     */
    public void addVisible(Celda c) {
        if (_visibles.contains(c))
            return;
        _visibles.add(c);
    }

    /**
     * Limpia la lista de visibles
     */
    public void clearVisibles() {
        _visibles.clear();
    }
//------------------------------------------------------------------------------------------------//

//------------------------------------------ANIMACIONES-------------------------------------------//

    /**
     * Inicializa los atributos relacionados con animaciones
     */
    private void initAnims() {
        _actAnim = false;
        _lastFrame = 0;
        _currFrame = 0;
        _nextFrame = 1;
        _diametroMin = _diametro;
        _diametroMax = (float) (_diametro * 1.1);
        _dirAnim = (int) (_diametroMax / _diametro);
        _numAnims = 0;
        _candado = false;
        _posCand = new int[2];
        _posCand[0] = (int) (_pos[0] + (_diametro / 24) * 5);
        _posCand[1] = (int) (_pos[1] + (_diametro / 5));
    }

    /**
     * Cambia el color de la celda
     */
    public void changeColor() {
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

    public void backColor() {
        switch (_tipoCelda) {
            case AZUL: {
                _tipoCelda = GRIS;
                _color = 0XEEEEEEFF;
                break;
            }
            case ROJO: {
                _tipoCelda = AZUL;
                _color = 0x1CC0E0FF;
                break;
            }
        }
    }

    /**
     * Activa la animación de celda bloqueada
     */
    public void activeAnim() {
        _actAnim = true;
    }

    /**
     * Alterna entre poner o no el candado
     * Solo lo cambia para las celdas rojas
     */
    public void alternaCandado() {
        if (_tipoCelda == ROJO && _lock)
            _candado = !_candado;
    }

//------------------------------------------------------------------------------------------------//

    /**
     * Muestra el valor de la celda o no en función de show
     *
     * @param show Determina si se debe mostrar el valor de la celda
     * @param v    Valor que se quiere mostrar de la celda
     */
    public void showText(boolean show, int v) {
        _drawText = show;
        if (show) {
            _value = v;
            posicionaTexto();
        }
    }

    /**
     * Activa/Desactiva el valor de la celda
     *
     * @param show Determina si se debe mostrar el valor de la celda
     */
    public void showText(boolean show) {
        _drawText = show;
        if (show) {
            _value = _visibles.size();
            posicionaTexto();
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
    /**
     * Tipo de la celda
     */
    private TipoCelda _tipoCelda;
    /**
     * Determina si esta celda está bloqueada
     */
    private boolean _lock;
    /**
     * Fuente del texto de la celda
     */
    private final Font _font;
    /**
     * Tamaño de la fuente
     */
    private final int _tamF;
    /**
     * Valor de la celda
     */
    private int _value;
    /**
     * Posición donde se va a renderizar la celda
     */
    private final int[] _pos;
    /**
     * Índice dentro del tablero
     * _i -> Fila
     * _j -> Columna
     */
    private final int _i;
    private final int _j;
    /**
     * Diámetro del circulo
     */
    private float _diametro;
    /**
     * Color inicial del circulo
     */
    private int _color;
    /**
     * Determina si la celda escribe texto
     */
    private boolean _drawText;
    /**
     * Posicion X del texto
     */
    int _textX = 0;
    /**
     * Posición Y del texto
     */
    int _textY = 0;
    /**
     * Callback de tipo ButtonCallback
     */
    private ButtonCallback _cb;
    /**
     * Callback de tipo CellCallback
     */
    private CellCallback _cbc;

    //ANIMACIONES
    /**
     * Posición inicial de la celda. Para animaciones
     */
    private final int[] _iniPos;
    /**
     * Diámetro del circulo mínimo.
     * Para animaciones
     */
    private float _diametroMin;
    /**
     * Diámetro del circulo máximo.
     * Para animaciones
     */
    private float _diametroMax;
    /**
     * Para activar y desactivar animaciones
     */
    private boolean _actAnim;
    /**
     * Auxiliar para reposicionar las celdas
     * durante las animaciones
     */
    private int _dirAnim;
    /**
     * Número de animaciones
     */
    private int _numAnims;
    // Frames de las animaciones
    private double _lastFrame;
    private double _currFrame;
    private double _nextFrame;
    /**
     * Determina si hay o no que enseñar candados
     */
    private boolean _candado;
    /**
     * Posicion del candado
     */
    private int[] _posCand;
    /**
     * Lista de las celdas visibles
     */
    private final List<Celda> _visibles;
}