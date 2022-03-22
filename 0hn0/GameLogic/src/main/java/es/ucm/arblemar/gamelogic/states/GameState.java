package es.ucm.arblemar.gamelogic.states;

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
import es.ucm.arblemar.gamelogic.CellCallback;
import es.ucm.arblemar.gamelogic.tablero.Tablero;
import es.ucm.arblemar.gamelogic.tablero.TipoCelda;
import es.ucm.arblemar.gamelogic.tablero.TipoPista;
import es.ucm.arblemar.gamelogic.tablero.Celda;
import es.ucm.arblemar.gamelogic.tablero.Pista;

public class GameState implements State {
    GameState(Engine _engine, int t) {
        this._engine = _engine;
        _tam = t;
        _graphics = this._engine.getGraphics();
    }

    @Override
    public boolean init() {
        try {
            Graphics g = _engine.getGraphics();

            // INICIALIZACION DEL TABLERO
            _sizeTab = (int) (_graphics.getLogWidth() * 0.85);
            _tabX = (int) (_graphics.getLogWidth() - _sizeTab) / 2;
            _tabY = (int) (_graphics.getLogHeight() - _sizeTab) / 2;
            _celdaSize = (_sizeTab / _tam);
            _diam = _celdaSize * 0.9f;
            _tabFont = Assets.jose;
            _tabTamFont = (int) Math.round(_diam * 0.614);

            Celda[][] celdas;
            if (_tam > 4) {
                celdas = randomTab();
            } else {
                celdas = testTab();
            }

            _tablero = new Tablero(celdas);

            // BOTON VOLVER
            _sizeVolver = new int[2];
            _sizeVolver[0] = (g.getLogWidth() / 15);
            _sizeVolver[1] = (g.getLogWidth() / 15);
            _posVolver = new int[2];
            _posVolver[0] = (g.getLogWidth() / 4) + (_sizeVolver[0] / 3);
            _posVolver[1] = (g.getLogHeight() / 8) * 7;
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
            _sizeReset[0] = (g.getLogWidth() / 14);
            _sizeReset[1] = (g.getLogWidth() / 14);
            _posReset = new int[2];
            _posReset[0] = (g.getLogWidth() / 2) - (_sizeReset[0] / 2);
            _posReset[1] = (g.getLogHeight() / 8) * 7;
            _imReset = Assets.history;
            _reset = new ButtonCallback() {
                @Override
                public void doSomething() {
                    System.out.println("Retrocedemos movimiento... Falta por hacer");
                }
            };

            // BOTON PISTA
            _sizePista = new int[2];
            _sizePista[0] = (g.getLogWidth() / 13);
            _sizePista[1] = (g.getLogWidth() / 13);
            _posPista = new int[2];
            _posPista[0] = (g.getLogWidth() / 3) * 2;
            _posPista[1] = (g.getLogHeight() / 8) * 7;
            _imPista = Assets.eye;
            _verPista = new ButtonCallback() {
                @Override
                public void doSomething() {
                    System.out.println("Generamos una pista...");
                    if (_pista.getTipo() == TipoPista.NONE) {
                        primerFor:
                        for (int i = 0; i < _tam; ++i) {
                            for (int j = 0; j < _tam; ++j) {
                                _pista = _tablero.procesaPista(i, j);
                                if (_pista.getTipo() != TipoPista.NONE) break primerFor;
                            }
                        }
                    } else {
                        _pista.setTipo(TipoPista.NONE);
                    }
                }
            };

            // PORCENTAJE
            _sizePorcent = new int[2];
            _sizePorcent[0] = (g.getLogWidth() / 12);
            _sizePorcent[1] = (g.getLogWidth() / 12);
            _posPorcent = new int[2];
            _posPorcent[0] = (g.getLogWidth() / 2) - (_sizePorcent[0] / 2);
            _posPorcent[1] = (g.getLogHeight() / 80) * 67;
            _colorPorcent = 0X999999FF;
            _fontPorcent = Assets.jose;
            _tamFPorcent = 24;
            _porcent = 0;

            // TITULO
            _sizeTitulo = new int[2];
            _sizeTitulo[0] = (g.getLogWidth() / 9) * 3;
            _sizeTitulo[1] = (g.getLogWidth() / 8);
            _posTitulo = new int[2];
            _posTitulo[0] = (g.getLogWidth() / 2) - (_sizeTitulo[0] / 2);
            _posTitulo[1] = (g.getLogHeight() / 10);
            _colorTitulo = 0X313131FF;
            _fontTitulo = Assets.jose;
            _tamFTitulo = 64;
            _titulo = _tam + " x " + _tam;

            // PISTAS
            _pista = new Pista();
            _altoPistaTxt = (g.getLogWidth() / 10);                 //Tamaño del texto a lo alto
            _posPistaTxt = new int[3];
            _posPistaTxt[0] = (g.getLogWidth() / 2);                //A lo ancho
            _posPistaTxt[1] = (g.getLogHeight() / 8);               //A lo alto primera linea
            _posPistaTxt[2] = _posPistaTxt[1] + _altoPistaTxt;    //A lo alto segunda linea
            _colorPistaTxt = 0X313131FF;
            _fontPistaTxt = Assets.jose;
            _tamFPistaTxt = 32;

        } catch (Exception e) {
            System.out.println("Fallo al intenar generar GameState");
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public void update(double deltaTime) {
        if (win) {
            win = false;
            SelectMenuState menu = new SelectMenuState(_engine);
            _engine.reqNewState(menu);
        }
    }

    @Override
    public void render() {
        Graphics g = _engine.getGraphics();

        // BOTONES
        g.drawImage(_imVolver, _posVolver[0], _posVolver[1], _sizeVolver[0], _sizeVolver[1]);
        g.drawImage(_imReset, _posReset[0], _posReset[1], _sizeReset[0], _sizeReset[1]);
        g.drawImage(_imPista, _posPista[0], _posPista[1], _sizePista[0], _sizePista[1]);

        //PORCENTAJE
        g.setColor(_colorPorcent);
        g.drawText(_porcent + "%", _posPorcent[0], _posPorcent[1] + _sizePorcent[1], _fontPorcent, _tamFPorcent);

        // TITULO O TEXTO DE PISTA
        if (_pista.getTipo() == TipoPista.NONE) {
            g.setColor(_colorTitulo);
            g.drawText(_titulo, _posTitulo[0], _posTitulo[1] + _sizeTitulo[1], _fontTitulo, _tamFTitulo);
        } else {
            g.setColor(_colorPistaTxt);
            g.drawText(_pista.getTextoPista()[0], _posPistaTxt[0] - (_pista.getSize(g.getLogWidth())[0]), _posPistaTxt[1], _fontPistaTxt, _tamFPistaTxt);
            g.drawText(_pista.getTextoPista()[1], _posPistaTxt[0] - (_pista.getSize(g.getLogWidth())[1]), _posPistaTxt[2], _fontPistaTxt, _tamFPistaTxt);

            g.setColor(0X313131FF);
            int difTam = (int) (_celdaSize * 0.06);
            g.fillCircle(_pista.getPos()[0] - difTam, _pista.getPos()[1] - difTam, _diam + (difTam * 2));
            g.setColor(0XFFFFFFFF);
        }

        // CELDAS
        _tablero.render(g);
    }

    @Override
    public void handleInput() {
        List<Input.TouchEvent> events = _engine.getInput().GetTouchEvents();

        for (int i = 0; i < events.size(); i++) {
            Input.TouchEvent currEvent = events.get(i);
            if (currEvent == Input.TouchEvent.touchDown) {
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
                } else {
                    _tablero.handleInput(currEvent.getX(), currEvent.getY());
                    break;
                }
            }
        }
    }

//------------------------------------------------------------------------------------------------//

    /**
     * Tablero random de pruebas
     */
    private Celda[][] randomTab() {
        Random rn = new Random();
        Celda[][] celdas;
        celdas = new Celda[_tam][_tam];

        //Tablero
        int[] pos;
        pos = new int[2];
        for (int i = 0; i < _tam; i++) {
            pos[1] = (int) (_tabY + (_celdaSize * i) + (_celdaSize * 0.1));
            for (int j = 0; j < _tam; j++) {
                pos[0] = (int) (_tabX + (_celdaSize * j) + (_celdaSize * 0.1));

                int choice = rn.nextInt(3);
                int[] ind = new int[2];
                ind[0] = i;
                ind[1] = j;
                switch (choice) {
                    case 0: {
                        celdas[i][j] = new Celda(TipoCelda.GRIS, _tabFont, _tabTamFont,
                                0, pos, _diam, ind);
                        celdas[i][j].setCellCallback(new CellCallback() {
                            @Override
                            public void doSomething(int x, int y) {
                                if (_pista.getTipo() != TipoPista.NONE) {
                                    _pista.setTipo(TipoPista.NONE);
                                }
                                _tablero.changeCellColor(x, y);
                            }
                        }, i, j);
                        break;
                    }
                    case 1: {
                        celdas[i][j] = new Celda(TipoCelda.AZUL, _tabFont, _tabTamFont,
                                rn.nextInt(9) + 1, pos, _diam, ind);
                        celdas[i][j].setCallback(new ButtonCallback() {
                            @Override
                            public void doSomething() {
                                System.out.println("\nAnimación Azul grande-pequeño.\nAlternar candado Rojas");
                            }
                        });
                        break;
                    }
                    case 2: {
                        celdas[i][j] = new Celda(TipoCelda.ROJO, _tabFont, _tabTamFont,
                                0, pos, _diam, ind);
                        celdas[i][j].setCallback(new ButtonCallback() {
                            @Override
                            public void doSomething() {
                                System.out.println("Animación Roja grande-pequeño.\nAlternar candado Rojas");
                            }
                        });
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + choice);
                }
            }
        }

        return celdas;
    }

    /**
     * Tablero de 4x4 del enunciado
     */
    private Celda[][] testTab() {
        Celda[][] celdas;
        celdas = new Celda[_tam][_tam];

        //Tablero
        int[] pos;
        pos = new int[2];
        for (int i = 0; i < _tam; i++) {
            pos[1] = (int) (_tabY + (_celdaSize * i) + (_celdaSize * 0.1));
            for (int j = 0; j < _tam; j++) {
                pos[0] = (int) (_tabX + (_celdaSize * j) + (_celdaSize * 0.1));

                int[] ind = new int[2];
                ind[0] = i;
                ind[1] = j;
                if ((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 1 && j == 1) || (i == 1 && j == 2) ||
                        (i == 2 && j == 0) || (i == 2 && j == 3) || (i == 3 && j == 0) || (i == 3 && j == 1) ||
                        (i == 3 && j == 3)) {
                    celdas[i][j] = new Celda(TipoCelda.GRIS, _tabFont, _tabTamFont,
                            0, pos, _diam, ind);
                    celdas[i][j].setCellCallback(new CellCallback() {
                        @Override
                        public void doSomething(int x, int y) {
                            if (_pista.getTipo() != TipoPista.NONE) {
                                _pista.setTipo(TipoPista.NONE);
                            }
                            _tablero.changeCellColor(x, y);
                        }
                    }, i, j);
                } else if (i == 2 && j == 2) {
                    celdas[i][j] = new Celda(TipoCelda.ROJO, _tabFont, _tabTamFont,
                            0, pos, _diam, ind);
                    celdas[i][j].setCallback(new ButtonCallback() {
                        @Override
                        public void doSomething() {
                            System.out.println("Animación Roja grande-pequeño.\nAlternar candado Rojas");
                        }
                    });
                } else {
                    if ((i == 0 && j == 0) || (i == 2 && j == 1)) {
                        celdas[i][j] = new Celda(TipoCelda.AZUL, _tabFont, _tabTamFont, 1, pos, _diam, ind);
                        celdas[i][j].setCallback(new ButtonCallback() {
                            @Override
                            public void doSomething() {
                                System.out.println("\nAnimación Azul grande-pequeño.\nAlternar candado Rojas");
                            }
                        });
                    } else {
                        celdas[i][j] = new Celda(TipoCelda.AZUL, _tabFont, _tabTamFont, 2, pos, _diam, ind);
                        celdas[i][j].setCallback(new ButtonCallback() {
                            @Override
                            public void doSomething() {
                                System.out.println("\nAnimación Azul grande-pequeño.\nAlternar candado Rojas");
                            }
                        });
                    }
                }
            }
        }

        return celdas;
    }

//------------------------------------------------------------------------------------------------//

    // ATRIBUTOS DEL ESTADO
    Engine _engine;
    Graphics _graphics;
    boolean win = false;
    Timer timer;
    TimerTask timerTask;

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
    int[] _sizeTitulo;
    int[] _posTitulo;
    int _tamFTitulo;
    int _colorTitulo;
    String _titulo;
    Font _fontTitulo;

    // ATRIBUTOS PARA EL TEXTO DE PISTAS
    int _altoPistaTxt;
    int[] _posPistaTxt;
    int _tamFPistaTxt;
    int _colorPistaTxt;
    Font _fontPistaTxt;
    Pista _pista;

    // ATRIBUTOS DEL TABLERO
    /**
     * Tipo de tablero: 4x4, 5x5, 6x6...
     */
    int _tam;
    /**
     * Tamaño de la cuadrícula del tablero dentro
     * del canvas
     */
    float _sizeTab;
    /**
     * Coordenadas x del tablero
     */
    int _tabX;
    /**
     * Coordenadas y del tablero
     */
    int _tabY;
    /**
     * Fuente de la letra de las celdas
     */
    Font _tabFont;
    /**
     * Tamaño de la fuente de las celdas
     */
    int _tabTamFont;
    /**
     * Diámetro de las celdas
     */
    float _celdaSize;
    /**
     * Diámetro de las celdas
     */
    float _diam;
    /**
     * Referencia al tablero del juego
     */
    Tablero _tablero;
    /**
     * Posicion temporal en _celdas que tiene una celda gris
     */
    //int _i, _j;
}