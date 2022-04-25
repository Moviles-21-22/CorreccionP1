package es.ucm.arblemar.gamelogic.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Image;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input;
import es.ucm.arblemar.gamelogic.Assets;
import es.ucm.arblemar.gamelogic.ButtonCallback;
import es.ucm.arblemar.gamelogic.tablero.Tablero;
import es.ucm.arblemar.gamelogic.tablero.TipoPista;
import es.ucm.arblemar.gamelogic.tablero.Pista;

public class GameState implements State {
    GameState(Engine engine, int t) {
        _engine = engine;
        _tam = t;
        _graphics = _engine.getGraphics();
    }

    @Override
    public boolean init() {
        try {
            // INICIALIZACION DEL TABLERO
            Font tabFont = Assets.jose;
            float sizeTab = (float) (_graphics.getLogWidth() * 0.85);
            int posTabX = (int) (_graphics.getLogWidth() - sizeTab) / 2;
            int posTabY = (int) (_graphics.getLogHeight() - sizeTab) / 2;
            _celdaSize = (sizeTab / _tam);
            _celdaDiam = _celdaSize * 0.9f;
            int tabTamFont = (int) Math.round(_celdaDiam * 0.614);

            _tablero = new Tablero(_tam, posTabX, posTabY, _celdaSize, _celdaDiam, tabTamFont, tabFont);
            _totalGrises = _tablero.initTestTab();
//            _totalGrises = _tablero.generateTab();

            // BOTON VOLVER
            _sizeVolver = new int[2];
            _sizeVolver[0] = (_graphics.getLogWidth() / 15);
            _sizeVolver[1] = (_graphics.getLogWidth() / 15);
            _posVolver = new int[2];
            _posVolver[0] = (_graphics.getLogWidth() / 4) + (_sizeVolver[0] / 3);
            _posVolver[1] = (_graphics.getLogHeight() / 8) * 7;
            _imVolver = Assets.close;
            _goBack = new ButtonCallback() {
                @Override
                public void doSomething() {
                    SelectMenuState main = new SelectMenuState(_engine);
                    _engine.reqNewState(main);
                }
            };

            // BOTON RESET
            _sizeReset = new int[2];
            _sizeReset[0] = (_graphics.getLogWidth() / 14);
            _sizeReset[1] = (_graphics.getLogWidth() / 14);
            _posReset = new int[2];
            _posReset[0] = (_graphics.getLogWidth() / 2) - (_sizeReset[0] / 2);
            _posReset[1] = (_graphics.getLogHeight() / 8) * 7;
            _imReset = Assets.history;

            _reset = new ButtonCallback() {
                @Override
                public void doSomething() {
                    _tablero.devuelveMovimiento();
                }
            };

            // BOTON PISTA
            _sizePista = new int[2];
            _sizePista[0] = (_graphics.getLogWidth() / 13);
            _sizePista[1] = (_graphics.getLogWidth() / 13);
            _posPista = new int[2];
            _posPista[0] = (_graphics.getLogWidth() / 3) * 2;
            _posPista[1] = (_graphics.getLogHeight() / 8) * 7;
            _imPista = Assets.eye;
            _verPista = new ButtonCallback() {
                @Override
                public void doSomething() {
                    if (_pista.getTipo() == TipoPista.NONE) {
                        List<Pista> pistasList = new ArrayList<>();

                        for (int i = 0; i < _tam; ++i) {
                            for (int j = 0; j < _tam; ++j) {
                                Pista p = _tablero.procesaPista(i, j, null);
                                if (p.getTipo() != TipoPista.NONE) pistasList.add(p);
                            }
                        }
                        // Escoge una pista aleatoria de las que se encontraron
                        if (!pistasList.isEmpty()) {
                            // animación fade-in/out
                            _animFadeText = true;
                            Random rn = new Random();
                            int max = pistasList.size();
                            int choice = rn.nextInt(max);
                            _pista = pistasList.get(choice);
                            _lastPistaTxt[0] = _pista.getTextoPista()[0];
                            _lastPistaTxt[1] = _pista.getTextoPista()[1];
                            pistasList.clear();
                        }
                    } else {
                        // animación fade-in/out
                        _animFadeText = true;
                        _pista.setTipo(TipoPista.NONE);
                    }
                }
            };

            // PORCENTAJE
            _sizePorcent = new int[2];
            _sizePorcent[0] = (_graphics.getLogWidth() / 12);
            _sizePorcent[1] = (_graphics.getLogWidth() / 12);
            _posPorcent = new int[2];
            _posPorcent[0] = (_graphics.getLogWidth() / 2) - (_sizePorcent[0] / 2);
            _posPorcent[1] = (_graphics.getLogHeight() / 80) * 67;
            _colorPorcent = 0X999999FF;
            _fontPorcent = Assets.jose;
            _tamFPorcent = 24;
            _porcent = 0;

            // TITULO
            _sizeTitulo = new int[2];
            _sizeTitulo[0] = (_graphics.getLogWidth() / 9) * 3;
            _sizeTitulo[1] = (_graphics.getLogWidth() / 8);
            _posTitulo = new int[2];
            _posTitulo[0] = (_graphics.getLogWidth() / 2) - (_sizeTitulo[0] / 2);
            _posTitulo[1] = (_graphics.getLogHeight() / 10);
            _currColorTitulo = 0X313131FF;
            _colorTitulo = 0X313131FF;
            _fontTitulo = Assets.jose;
            _tamFTitulo = 64;
            _titulo = _tam + " x " + _tam;

            // PISTAS
            _pista = new Pista();
            _heightPistaTxt = (_graphics.getLogWidth() / 10);     //Tamaño del texto a lo alto
            _posPistaTxt = new int[3];
            _posPistaTxt[0] = (_graphics.getLogWidth() / 2);    //A lo ancho
            _posPistaTxt[1] = (_graphics.getLogHeight() / 8);   //A lo alto primera linea
            _posPistaTxt[2] = _posPistaTxt[1] + _heightPistaTxt;  //A lo alto segunda linea
            _currColorPista = 0X31313100;
            _fontPistaTxt = Assets.jose;
            _tamFPistaTxt = 32;
            _lastPistaTxt = new String[2];
            _lastPistaTxt[0] = " ";
            _lastPistaTxt[1] = " ";
            _offsetPista = (int) (_celdaSize * 0.06);
            _diamPista = _celdaDiam + (_offsetPista * 2);
        } catch (Exception e) {
            System.out.println("Fallo al intenar generar GameState");
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public void update(double deltaTime) {
        if (win && _timer == null) {
            _timer = new Timer();
            _delay = 2000;
            _timerTask = new TimerTask() {
                @Override
                public void run() {
                    SelectMenuState menu = new SelectMenuState(_engine);
                    _engine.reqNewState(menu);
                }
            };
            _timer.schedule(_timerTask, _delay);
        } else {
            _tablero.update(deltaTime);
            _porcent = 100 - ((_tablero.getGrises() * 100) / _totalGrises);
            procesaPorcentaje();
        }

        // FADE IN/OUT TEXTO
        if (_animFadeText) {
            _ticksAnimText += _velFadeAnim * deltaTime;
            if (_ticksAnimText >= 1) {
                // PISTA: Si hay una pista +1 - Si no -1
                _currColorPista += _pista.getTipo() != TipoPista.NONE ? 1 : -1;
                // TITULO: Si hay una pista -1 - Si no +1
                _currColorTitulo += _pista.getTipo() != TipoPista.NONE ? -1 : 1;
                if (_currColorPista == _colorTitulo || _currColorTitulo == _colorTitulo) {
                    // PISTA: Si hay una pista color del titulo - Si no color sin alpha
                    _currColorPista = _pista.getTipo() != TipoPista.NONE ? _colorTitulo : 0X31313100;
                    // Titulo: Si hay una pista color sin alpha - Si no color del titulo
                    _currColorTitulo = _pista.getTipo() != TipoPista.NONE ? 0X31313100 : _colorTitulo;
                    _animFadeText = false;
                }

                _ticksAnimText = 0;
            }
        }
    }

    @Override
    public void render() {
        if (win) {
            _graphics.setColor(_colorTitulo);
            _graphics.drawText("Super", _posTitulo[0], _posTitulo[1] + _sizeTitulo[1], _fontTitulo, _tamFTitulo);
        } else {
            // BOTONES
            _graphics.drawImage(_imVolver, _posVolver[0], _posVolver[1], _sizeVolver[0], _sizeVolver[1]);
            _graphics.drawImage(_imReset, _posReset[0], _posReset[1], _sizeReset[0], _sizeReset[1]);
            _graphics.drawImage(_imPista, _posPista[0], _posPista[1], _sizePista[0], _sizePista[1]);

            //PORCENTAJE
            _graphics.setColor(_colorPorcent);
            _graphics.drawText(String.format("%s %s", _porcent, "%"), _posPorcent[0], _posPorcent[1] + _sizePorcent[1], _fontPorcent, _tamFPorcent);

            // TITULO
            _graphics.setColor(_currColorTitulo);
            _graphics.drawText(_titulo, _posTitulo[0], _posTitulo[1] + _sizeTitulo[1], _fontTitulo, _tamFTitulo);

            // PISTA
            _graphics.setColor(_currColorPista);
            int x = _posPistaTxt[0] - (_pista.getSize(_graphics.getLogWidth())[0]);
            int y = _posPistaTxt[1];
            _graphics.drawText(_lastPistaTxt[0], x, y, _fontPistaTxt, _tamFPistaTxt);
            x = _posPistaTxt[0] - (_pista.getSize(_graphics.getLogWidth())[1]);
            y = _posPistaTxt[2];
            _graphics.drawText(_lastPistaTxt[1], x, y, _fontPistaTxt, _tamFPistaTxt);

            // CELDA ASOCIADA A LA PISTA
            if (_pista.getTipo() != TipoPista.NONE) {
                _graphics.setColor(_currColorPista);
                x = _pista.getIndexCelda()[0] - _offsetPista;
                y = _pista.getIndexCelda()[1] - _offsetPista;
                _graphics.fillCircle(x, y, _diamPista);
                _graphics.setColor(0XFFFFFFFF);
            }
        }

        // CELDAS
        _tablero.render(_graphics);
    }

    @Override
    public void handleInput() {
        List<Input.TouchEvent> events = _engine.getInput().GetTouchEvents();

        for (int i = 0; i < events.size(); i++) {
            Input.TouchEvent currEvent = events.get(i);
            if (currEvent == Input.TouchEvent.touchDown) {
                if (win) return;

                // BOTON VOLVER
                if (currEvent.getX() > _posVolver[0] &&
                        currEvent.getX() < _posVolver[0] + _sizeVolver[0] &&
                        currEvent.getY() > _posVolver[1] &&
                        currEvent.getY() < _posVolver[1] + _sizeVolver[1]) {
                    _goBack.doSomething();
                    break;
                }
                // BOTON RESET MOVIMIENTO
                else if (currEvent.getX() > _posReset[0] &&
                        currEvent.getX() < _posReset[0] + _sizeReset[0] &&
                        currEvent.getY() > _posReset[1] &&
                        currEvent.getY() < _posReset[1] + _sizeReset[1]) {
                    _reset.doSomething();
                    break;
                }
                // BOTÓN PISTA
                else if (currEvent.getX() > _posPista[0] &&
                        currEvent.getX() < _posPista[0] + _sizePista[0] &&
                        currEvent.getY() > _posPista[1] &&
                        currEvent.getY() < _posPista[1] + _sizePista[1]) {
                    _verPista.doSomething();
                    break;
                }
                // CELDAS
                else {
                    _tablero.handleInput(currEvent.getX(), currEvent.getY(), this);
                    break;
                }
            }
        }
    }

    /**
     * Desactiva la pista actual y activa la animación
     * para mostrar el título del tablero
     */
    public void disablePista() {
        if (_pista.getTipo() != TipoPista.NONE) {
            // animación fade-in/out
            _animFadeText = true;
            _pista.setTipo(TipoPista.NONE);
        }
    }

    /**
     * Devuelve la pista actual
     */
    public Pista getPista() {
        return _pista;
    }

    /**
     * Detiene el timer que haya en marcha
     */
    public void stopTimer() {
        if (_timer != null) {
            _timer.cancel();
            _timer = null;
        }
    }

    /**
     * Procesa el porcentaje de tablero que se ha completado.
     * En caso de llegar a 100 se mira que se haya completado
     * correctamente
     */
    private void procesaPorcentaje() {
        if (_porcent < 100 || _pista.getTipo() != TipoPista.NONE || _timer != null || win) return;

        // Búsqueda de posibles pistas activas
        _pistasList = new ArrayList<>();
        for (int i = 0; i < _tam; ++i) {
            for (int j = 0; j < _tam; ++j) {
                Pista p = _tablero.procesaPista(i, j, null);
                if (p.getTipo() != TipoPista.NONE) _pistasList.add(p);
            }
        }

        _timer = new Timer();
        // Si se encuentran pistas, se escoge una aleatoria y se muestra
        if (!_pistasList.isEmpty()) {
            _delay = 2000;
            _timerTask = new TimerTask() {
                @Override
                public void run() {
                    Random rn = new Random();
                    int max = _pistasList.size();
                    int choice = rn.nextInt(max);
                    _pista = _pistasList.get(choice);
                    _pistasList.clear();
                }
            };
        }
        // En caso de no encontrar pistas, significa que el nivel está completo
        else {
            _delay = 500;
            _timerTask = new TimerTask() {
                @Override
                public void run() {
                    win = true;
                    _tablero.showAllValues();
                    _timer = null;
                }
            };
        }

        _timer.schedule(_timerTask, _delay);
    }

    // ATRIBUTOS DEL ESTADO
    Engine _engine;
    Graphics _graphics;
    boolean win = false;
    Timer _timer;
    TimerTask _timerTask;
    int _delay;

    // ATRIBUTOS BOTON VOLVER
    Image _imVolver;
    int[] _sizeVolver;
    int[] _posVolver;
    ButtonCallback _goBack;

    // ATRIBUTOS BOTON RESET
    Image _imReset;
    int[] _sizeReset;
    int[] _posReset;
    ButtonCallback _reset;

    // ATRIBUTOS BOTON PISTA
    Image _imPista;
    int[] _sizePista;
    int[] _posPista;
    ButtonCallback _verPista;

    // ATRIBUTOS PORCENTAJE
    int[] _sizePorcent;
    int[] _posPorcent;
    int _tamFPorcent;
    int _colorPorcent;
    int _porcent;
    Font _fontPorcent;

    // ATRIBUTOS TITULO
    /**
     * Cuenta los ticks para aplicar el fade-in/out
     */
    float _ticksAnimText = 0;
    /**
     * Velocidad de los ticks de la animación fade-int/out
     */
    final float _velFadeAnim = 1200;
    int[] _sizeTitulo;
    int[] _posTitulo;
    int _tamFTitulo;
    int _currColorTitulo;
    int _colorTitulo;
    String _titulo;
    Font _fontTitulo;
    boolean _animFadeText = false;

    // ATRIBUTOS PARA EL TEXTO DE PISTAS
    int _heightPistaTxt;
    int[] _posPistaTxt;
    String[] _lastPistaTxt;
    int _tamFPistaTxt;
    int _currColorPista;
    Font _fontPistaTxt;
    Pista _pista;
    /**
     * Diferencia de tamaño de la celda
     * asociada a la pista y la circunferencia
     * negra que la rodea
     */
    int _offsetPista;
    /**
     * Diámetro de la celda negra que rodea
     * a la celda asociada a la pista
     */
    float _diamPista;

    // ATRIBUTOS DEL TABLERO
    /**
     * Lista de las pistas disponibles en el tablero
     */
    List<Pista> _pistasList;
    /**
     * Tipo de tablero: 4x4, 5x5, 6x6...
     */
    int _tam;
    /**
     * Diámetro de las celdas
     */
    float _celdaSize;
    /**
     * Diámetro de las celdas
     */
    float _celdaDiam;
    /**
     * Total de grises del tablero al empezar
     */
    int _totalGrises;
    /**
     * Referencia al tablero del juego
     */
    Tablero _tablero;
}