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
import es.ucm.arblemar.gamelogic.gameobjects.TipoCelda;
import es.ucm.arblemar.gamelogic.gameobjects.TipoPista;
import es.ucm.arblemar.gamelogic.gameobjects.Celda;
import es.ucm.arblemar.gamelogic.gameobjects.Pista;

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
            _diam = _celdaSize * 0.9f;
            _tabFont = Assets.jose;
            _tabTamFont = (int) Math.round(_diam * 0.614);

            testTab();

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
                                if (procesaPista(i, j)) break primerFor;
                            }
                        }
                    }
                    else {
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
            _porcent = Integer.toString(0) + "%";

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
            _titulo = Integer.toString(_tam) + " x " + Integer.toString(_tam);

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
        // CELDAS
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                _celdas[i][j].render(g);
            }
        }

        // BOTONES
        g.drawImage(_imVolver, _posVolver[0], _posVolver[1], _sizeVolver[0], _sizeVolver[1]);
        g.drawImage(_imReset, _posReset[0], _posReset[1], _sizeReset[0], _sizeReset[1]);
        g.drawImage(_imPista, _posPista[0], _posPista[1], _sizePista[0], _sizePista[1]);

        //PORCENTAJE
        g.setColor(_colorPorcent);
        g.drawText(_porcent, _posPorcent[0], _posPorcent[1] + _sizePorcent[1], _fontPorcent, _tamFPorcent);

        // TITULO O TEXTO DE PISTA
        if (_pista.getTipo() == TipoPista.NONE) {
            g.setColor(_colorTitulo);
            g.drawText(_titulo, _posTitulo[0], _posTitulo[1] + _sizeTitulo[1], _fontTitulo, _tamFTitulo);
        }
        else {
            g.setColor(_colorPistaTxt);
            g.drawText(_pista.getTextoPista()[0], _posPistaTxt[0] - (_pista.getSize(g.getLogWidth())[0]), _posPistaTxt[1], _fontPistaTxt, _tamFPistaTxt);
            g.drawText(_pista.getTextoPista()[1], _posPistaTxt[0] - (_pista.getSize(g.getLogWidth())[1]), _posPistaTxt[2], _fontPistaTxt, _tamFPistaTxt);
        }
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
                } else if (currEvent.getX() > _posReset[0] &&
                        currEvent.getX() < _posReset[0] + _sizeReset[0] &&
                        currEvent.getY() > _posReset[1] &&
                        currEvent.getY() < _posReset[1] + _sizeReset[1]) {
                    _reset.doSomething();
                    break;
                } else if (currEvent.getX() > _posPista[0] &&
                        currEvent.getX() < _posPista[0] + _sizePista[0] &&
                        currEvent.getY() > _posPista[1] &&
                        currEvent.getY() < _posPista[1] + _sizePista[1]) {
                    _verPista.doSomething();
                    break;
                } else {
                    findingCelda:
                    for(int n = 0; n < _tam; n++) {
                        for(int m = 0; m < _tam; m++) {
                            if (_celdas[n][m].isClicked(currEvent.getX(), currEvent.getY())) {
                                _celdas[n][m].runCallBack();
                                break findingCelda;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public boolean procesaPista(int i, int j) {
        if (!_celdas[i][j].isLock() || _celdas[i][j].getTipoCelda() != TipoCelda.AZUL) return false;

        int val = 0;
        boolean yaCerrada = true;  //Para medir si la celda completa ya esta cerrada
        //Abajo
        for (int x = i + 1; x < _tam; ++x) {
            if (_celdas[x][j].getTipoCelda() == TipoCelda.AZUL) val++;
            else if (_celdas[x][j].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            }
            else break;
            if (val > _celdas[i][j].getValue()) {
                _pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                return true;
            }
        }
        //Arriba
        for (int x = i - 1; x >= 0; --x) {
            if (_celdas[x][j].getTipoCelda() == TipoCelda.AZUL) val++;
            else if (_celdas[x][j].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            }
            else break;
            if (val > _celdas[i][j].getValue()) {
                _pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                return true;
            }
        }
        //Derecha
        for (int y = j + 1; y < _tam; ++y) {
            if (_celdas[i][y].getTipoCelda() == TipoCelda.AZUL) val++;
            else if (_celdas[i][y].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            }
            else break;
            if (val > _celdas[i][j].getValue()) {
                _pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                return true;
            }
        }
        //Izquierda
        for (int y = j - 1; y >= 0; --y) {
            if (_celdas[i][y].getTipoCelda() == TipoCelda.AZUL) val++;
            else if (_celdas[i][y].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            }
            else break;
            if (val > _celdas[i][j].getValue()) {
                _pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                return true;
            }
        }

        if (val == _celdas[i][j].getValue()) {      //Primera pista
            if (yaCerrada) return false;
            _pista.setTipo(TipoPista.CERRAR_CASILLA);
            return true;
        }
        else {      //val < getValue    ->    Puede ser segunda pista
            boolean gris = false;
            int nuevoVal = val;
            //Abajo
            for (int x = i + 1; x < _tam; ++x) {
                if (!gris && _celdas[x][j].getTipoCelda() == TipoCelda.GRIS) {
                    gris = true;
                    nuevoVal++;
                }
                else if (gris && _celdas[x][j].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
                else if (!gris &&  _celdas[x][j].getTipoCelda() == TipoCelda.AZUL) continue;
                else break;
                if (nuevoVal > _celdas[i][j].getValue()) {
                    _pista.setTipo(TipoPista.DEBE_SER_PARED);
                    return true;
                }
            }
            gris = false;
            nuevoVal = val;

            //Arriba
            for (int x = i - 1; x >= 0; --x) {
                if (!gris && _celdas[x][j].getTipoCelda() == TipoCelda.GRIS) {
                    gris = true;
                    nuevoVal++;
                }
                else if (gris && _celdas[x][j].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
                else if (!gris &&  _celdas[x][j].getTipoCelda() == TipoCelda.AZUL) continue;
                else break;
                if (nuevoVal > _celdas[i][j].getValue()) {
                    _pista.setTipo(TipoPista.DEBE_SER_PARED);
                    return true;
                }
            }
            gris = false;
            nuevoVal = val;

            //Derecha
            for (int y = j + 1; y < _tam; ++y) {
                if (!gris && _celdas[i][y].getTipoCelda() == TipoCelda.GRIS) {
                    gris = true;
                    nuevoVal++;
                }
                else if (gris && _celdas[i][y].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
                else if (!gris &&  _celdas[i][y].getTipoCelda() == TipoCelda.AZUL) continue;
                else break;
                if (nuevoVal > _celdas[i][j].getValue()) {
                    _pista.setTipo(TipoPista.DEBE_SER_PARED);
                    return true;
                }
            }
            gris = false;
            nuevoVal = val;

            //Izquierda
            for (int y = j - 1; y >= 0; --y) {
                if (!gris && _celdas[i][y].getTipoCelda() == TipoCelda.GRIS) {
                    gris = true;
                    nuevoVal++;
                }
                else if (gris && _celdas[i][y].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
                else if (!gris &&  _celdas[i][y].getTipoCelda() == TipoCelda.AZUL) continue;
                else break;
                if (nuevoVal > _celdas[i][j].getValue()) {
                    _pista.setTipo(TipoPista.DEBE_SER_PARED);
                    return true;
                }
            }
        }

        //Si se sale es porque no es ni primera ni segunda pista    ->    puede ser tercera
        int valLinea[] = new int[4];
        boolean dirCompl = false;

        //Abajo
        for (int x = i + 1; x < _tam; ++x) {
            if (_celdas[x][j].getTipoCelda() != TipoCelda.ROJO &&
                    valLinea[0] < _celdas[i][j].getValue())
                valLinea[0]++;
            else break;
        }
        //Arriba
        for (int x = i - 1; x >= 0; --x) {
            if (_celdas[x][j].getTipoCelda() != TipoCelda.ROJO &&
                    valLinea[1] < _celdas[i][j].getValue())
                valLinea[1]++;
            else break;
        }
        //Derecha
        for (int y = j + 1; y < _tam; ++y) {
            if (_celdas[i][y].getTipoCelda() != TipoCelda.ROJO &&
                    valLinea[2] < _celdas[i][j].getValue())
                valLinea[2]++;
            else break;
        }
        //Izquierda
        for (int y = j - 1; y >= 0; --y) {
            if (_celdas[i][y].getTipoCelda() != TipoCelda.ROJO &&
                    valLinea[3] < _celdas[i][j].getValue())
                valLinea[3]++;
            else break;
        }

        //Para que sea pista tres, el valor maximo entre tres de las direcciones debe ser menor
        //estricto que el valor buscado por la celda
        if (valLinea[0] + valLinea[1] + valLinea[2] < _celdas[i][j].getValue() ||
                valLinea[0] + valLinea[1] + valLinea[3] < _celdas[i][j].getValue() ||
                valLinea[0] + valLinea[2] + valLinea[3] < _celdas[i][j].getValue() ||
                valLinea[1] + valLinea[2] + valLinea[3] < _celdas[i][j].getValue()) {
            _pista.setTipo(TipoPista.DEBE_SER_AZUL);
            return true;
        }
        return false;
    }

//------------------------------------------------------------------------------------------------//

    /**
     * Tablero de pruebas
     */
    private void testTab() {
        Random rn = new Random();
        Graphics g = _engine.getGraphics();

        //Tablero
        int pos[] = new int[2];
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
                        _i = i; _j = j;
                        _celdas[i][j] = new Celda(TipoCelda.GRIS, _tabFont, _tabTamFont,
                                0, pos, _diam, ind);
                        _celdas[i][j].setCellCallback(new CellCallback() {
                            @Override
                            public void doSomething(int x, int y) {
                                //// INICIALIZACIÓN DEL TIMER
                                //timer = new Timer();
                                //timerTask = new TimerTask() {
                                //    @Override
                                //    public void run() {
                                //        win = true;
                                //    }
                                //};
                                //System.out.println("Timer empezado...");
                                //timer.schedule(timerTask, 2000);

                                _celdas[x][y].setColor();
                            }
                        }, _i, _j);
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
    String _porcent;
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
     * Array que contiene las celdas
     */
    Celda[][] _celdas;
    /**
     * Posicion temporal en _celdas que tiene una celda gris
     */
    int _i, _j;
}