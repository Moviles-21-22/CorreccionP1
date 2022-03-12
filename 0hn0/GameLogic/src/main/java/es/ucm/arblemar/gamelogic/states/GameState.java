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
import es.ucm.arblemar.gamelogic.TipoCelda;
import es.ucm.arblemar.gamelogic.gameobjects.Celda;

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
            _celdas = new Celda[_tam][_tam];
            _celdaSize = (_sizeTab / _tam);
            _diam = _celdaSize * 0.8f;
            _tabFont = Assets.jose;
            _tabTamFont = (int) Math.round(_diam * 0.614);

            testTab();

            // BOTON VOLVER
            _sizeVolver = new int[2];
            _sizeVolver[0] = (g.getLogWidth() / 16) * 2;
            _sizeVolver[1] = (g.getLogWidth() / 16) * 2;
            _posVolver = new int[2];
            _posVolver[0] = (g.getLogWidth() / 2) - (_sizeVolver[0] / 2);
            _posVolver[1] = (g.getLogHeight() / 5) * 4;
            _backIm = Assets.close;

            _goBack = new ButtonCallback() {
                @Override
                public void doSomething() {
                    SelectMenuState main = new SelectMenuState(_engine);
                    _engine.reqNewState(main);
                }
            };
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
        // CELDAS
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                _celdas[i][j].render(g);
            }
        }

        // BOTON VOLVER
        g.drawImage(_backIm, _posVolver[0], _posVolver[1], _sizeVolver[0], _sizeVolver[1]);
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
                } else{
                    findingCelda:
                    for(int n = 0; n < _tam; n++){
                        for(int m = 0; m < _tam; m++){
                            if (_celdas[n][m].isClicked(currEvent.getX(), currEvent.getY())) {
                                _celdas[n][m].runCallBack();
                                break findingCelda;
                            }
                        }
                    }
                }
            }
        }
    }

//------------------------------------------------------------------------------------------------//

    /**
     * Tablero de pruebas
     */
    private void testTab() {
        Random rn = new Random();
        int[] pos = new int[2];
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
                        _celdas[i][j] = new Celda(TipoCelda.GRIS, _tabFont, _tabTamFont,
                                0, pos, _diam, ind);
                        _celdas[i][j].setCallback(new ButtonCallback() {
                            @Override
                            public void doSomething() {

                                // INICIALIZACIÓN DEL TIMER
                                timer = new Timer();
                                timerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        win = true;
                                    }
                                };
                                System.out.println("Timer empezado...");
                                timer.schedule(timerTask, 2000);
                            }
                        });
                        break;
                    }
                    case 1: {
                        _celdas[i][j] = new Celda(TipoCelda.AZUL, _tabFont, _tabTamFont,
                                rn.nextInt(9) + 1, pos, _diam, ind);
//                        _celdas[i][j].setCallback(new ButtonCallback() {
//                            @Override
//                            public void doSomething() {
//                                System.out.println("Timer cancelado...");
//                                timerTask.cancel();
//                            }
//                        });
                        break;
                    }
                    case 2: {
                        _celdas[i][j] = new Celda(TipoCelda.ROJO, _tabFont, _tabTamFont,
                                0, pos, _diam, ind);
//                        _celdas[i][j].setCallback(new ButtonCallback() {
//                            @Override
//                            public void doSomething() {
//                                System.out.println("Timer reanudado...");
//                                timerTask.run();
//                            }
//                        });
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + choice);
                }
            }
        }
    }

//    private void gameWin() {
//        win = true;
//        objects.remove(textoSuperior);
//        if (textoSupDos != null) {
//            objects.remove(textoSupDos);
//            textoSupDos = null;
//        }
//        objects.remove(backButton);
//        objects.remove(restButton);
//        objects.remove(pistabutton);
//
//        int width = (_graphics.getLogWidth() / 2) * 3, height = (_graphics.getLogWidth() / 7),
//                posX = (_graphics.getLogWidth() / 3) - 15, posY = (_graphics.getLogHeight() / 12) - 10;
//
//        textoSuperior = new Texto(new Vector2(posX,posY), new Vector2(width, height), 0X313131FF ,Assets.jose,72,0);
//        textoSuperior.setTexto("Super");
//        objects.add(textoSuperior);
//    }

    //Escribe un texto en función de la pista elegida
//    private void stringText(int id) {
//        String text = "", text2 = "";
//        int width = (_graphics.getLogWidth() / 2) * 3, height = (_graphics.getLogWidth() / 7),
//                posX = (_graphics.getLogWidth() / 22), posY = (_graphics.getLogHeight() / 50),
//                dist = height / 2;
//
//        switch (id) {
//            case 0: {
//                posX = (_graphics.getLogWidth() / 4);
//                text = "Azul completa,";
//                text2 = "se puede cerrar";
//                break;
//            }
//            case 1: {
//                posX = (_graphics.getLogWidth() / 6);
//                text = "   Se puede poner";
//                text2 = "una pared adyacente";
//                break;
//            }
//            case 2: {
//                posX = (_graphics.getLogWidth() / 4);
//                text = "  Es necesario";
//                text2 = "poner una azul";
//                break;
//            }
//            case 3: {
//                posX = (_graphics.getLogWidth() / 10);
//                text = "Tiene más vecinas azules";
//                text2 = "    de las que debería";
//                break;
//            }
//            case 4: {
//                text = "Necesita más vecinas azules";
//                text2 = " y, en cambio, está cerrada";
//                break;
//            }
//            case 5:
//            case 6: {
//                posX = (_graphics.getLogWidth() / 8);
//                text = "No puede haber celdas";
//                text2 = "   azules sin vecinas";
//                break;
//            }
//            case 7: {
//                text = "   Se deben poner azules";
//                text2 = "en la única dirección abierta";
//                break;
//            }
//            case 8: {
//                posX = (_graphics.getLogWidth() / 11);
//                text = "   Suma alcanzable de";
//                text2 = "adyacentes igual al valor";
//                break;
//            }
//            case 9: {
//                text = "No puede alcanzar su valor";
//                text2 = "con las adyacentes que tiene";
//                break;
//            }
//        }
//        //textoSuperior = new Texto(new Vector2(posX, posY), new Vector2(width, height), 0X313131FF, Assets.jose, 32, 0);
//        //textoSuperior.setTexto(text);
//        //objects.add(textoSuperior);
//        //textoSupDos = new Texto(new Vector2(posX, posY + dist), new Vector2(width, height), 0X313131FF, Assets.jose, 32, 0);
//        //textoSupDos.setTexto(text2);
//        //objects.add(textoSupDos);
//    }

    // ATRIBUTOS DEL ESTADO
    Engine _engine;
    Graphics _graphics;
    boolean win = false;
    Timer timer;
    TimerTask timerTask;

    // ATRIBUTOS BOTON VOLVER
    Image _backIm;
    int[] _sizeVolver;
    int[] _posVolver;
    ButtonCallback _goBack;

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
     * Array que contiene las celdas
     */
    Celda[][] _celdas;
}