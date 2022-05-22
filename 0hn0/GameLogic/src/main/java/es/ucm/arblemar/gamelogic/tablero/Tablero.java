package es.ucm.arblemar.gamelogic.tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.gamelogic.states.GameState;

/**
 * Clase gestora de las pistas y del tablero
 */
public class Tablero {

    /**
     * Constructora del tablero para asignar atributos
     *
     * @param tam:        Dimensiones del tablero 4x4, 5x5...
     * @param posTabX:    Posición X del tablero en pantalla
     * @param posTabY:    Posición Y del tablero en pantalla
     * @param celdaSize:  Tamaño de cada celda en pantalla
     * @param celdaDiam:  Diámetro del círculo de cada celda en pantalla
     * @param tabTamFont: Tamaño de la fuente de las celdas
     * @param celdaFont:  Fuente de las celdas
     */
    public Tablero(int tam, int posTabX, int posTabY, float celdaSize, float celdaDiam,
                   int tabTamFont, Font celdaFont) {
        _tam = tam;
        _tabPos = new int[2];
        _tabPos[0] = posTabX;
        _tabPos[1] = posTabY;
        _celdaSize = celdaSize;
        _celdaDiam = celdaDiam;
        _celdaTamFont = tabTamFont;
        _celdaFont = celdaFont;
        _movements = new Stack<>();
        _celdas = new Celda[_tam][_tam];
        _creation = new CreaTablero(this, _tam, _celdas);
        _indRelPista = new int[2];
        _undoCellPos = new int[2];

        // Lista de direcciones
        _dirs = new ArrayList<>();
        int[] u = {-1, 0};
        // Arriba
        _dirs.add(u);
        // Abajo
        int[] d = {1, 0};
        _dirs.add(d);
        // Izquierda
        int[] l = {0, -1};
        _dirs.add(l);
        // Derecha
        int[] r = {0, 1};
        _dirs.add(r);
    }

    /**
     * Inicializa tablero de pruebas
     *
     * @return Devuelve el número de grises generados
     */
    public int initTestTab() {
        if (_tam > 4) {
            System.out.println("Para hacer test el de 4x4");
            _celdasGrises = 100;
        }

        _celdasGrises = _creation.testTab(_tabPos, _celdaSize, _celdaFont, _celdaTamFont, _celdaDiam);
        return _celdasGrises;
    }

    /**
     * Generador del tablero
     *
     * @return Devuelve el número de celdas grises generadas
     */
    public int generateTab() {
        while (true) {
            // Se inicializan todas las celdas en gris
            _creation.initGreyTab(_tabPos, _celdaSize, _celdaFont, _celdaTamFont, _celdaDiam);
            // Se busca un tablero con solución
            if (_creation.creaTablero(_tabPos, _celdaSize, _celdaFont, _celdaTamFont, _celdaDiam))
                break;
            System.out.println("Tablero descartado");
        }

        _solution = _creation.getSolution();
        _totalGrises = _creation.getGrises();
        _celdasGrises = _totalGrises;
        return _totalGrises;
    }

    /**
     * Comprueba si el tablero es la solución final
     */
    public boolean isSolution() {
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                if (_celdas[i][j].getTipoCelda() != _solution[i][j].getTipoCelda()) {
                    return false;
                }
            }
        }

        return true;
    }
//---------------------------------------------------------------------------------------//

    /**
     * Actualiza cada una de las celdas
     */
    public void update(double deltaTime) {
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                _celdas[i][j].update(deltaTime);
            }
        }
    }

    /**
     * Renderiza cada una de las celdas
     */
    public void render(Graphics g) {
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                _celdas[i][j].render(g);
            }
        }
    }

    /**
     * Procesa el input en cada una de las celdas
     */
    public void handleInput(int x, int y, GameState gm) {
        findingCelda:
        for (int n = 0; n < _tam; n++) {
            for (int m = 0; m < _tam; m++) {
                if (_celdas[n][m].isClicked(x, y)) {
                    _celdas[n][m].runCallBack(gm);
                    break findingCelda;
                }
            }
        }
    }

    /**
     * Muestra el valor de todas las celdas azules
     */
    public void showAllValues() {
        int a = 0;
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                if (!_celdas[i][j].isLock() && _celdas[i][j].getTipoCelda() == TipoCelda.AZUL) {
                    _numVisibles = 0;
                    //-----------------ABAJO----------------//
                    for (int n = i + 1; n < _tam; ++n) {
                        if (_celdas[n][j].getTipoCelda() == TipoCelda.ROJO) break;
                        _numVisibles++;
                    }
                    //-----------------ARRIBA----------------//
                    for (int n = i - 1; n >= 0; --n) {
                        if (_celdas[n][j].getTipoCelda() == TipoCelda.ROJO) break;
                        _numVisibles++;
                    }
                    //-----------------DERECHA----------------//
                    for (int m = j + 1; m < _tam; ++m) {
                        if (_celdas[i][m].getTipoCelda() == TipoCelda.ROJO) break;
                        _numVisibles++;
                    }
                    //-----------------IZQUIERDA----------------//
                    for (int m = j - 1; m >= 0; --m) {
                        if (_celdas[i][m].getTipoCelda() == TipoCelda.ROJO) break;
                        _numVisibles++;
                    }

                    _celdas[i][j].setLock(true);
                    _celdas[i][j].showText(true, _numVisibles);
                }
            }
        }
    }

    /**
     * Reinicia el último movimiento hecho.
     *
     * @return devuelve si hay algo que deshacer o no
     */
    public boolean resetMovement() {
        if (_movements.empty()) {
            return false;
        }

        Celda c = _movements.peek();
        // Si es distinta de gris, se resetea a gris
        if (c.getTipoCelda() != TipoCelda.GRIS) {
            c.setTipoCelda(TipoCelda.GRIS);
            _celdasGrises++;
        }

        _undoCellPos = c.getPos();
        _movements.pop();
        return true;
    }

    private void addStackMov(Celda c) {
        if(!_movements.isEmpty() && _movements.peek() == c) return;
        _movements.add(c);
    }

//-----------------------------------BÚSQUEDA-PISTAS----------------------------------------------//

    /**
     * Busca la pista que pueda existir en la casilla seleccionada
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     * @return Devuelve la pista generada. Puede ser NONE.
     */
    public Pista buscaPista(int i, int j, Celda[][] tab) {
        if (tab == null) {
            tab = _celdas;
        }

        Pista pista = new Pista();
        pista.setPosCelda(tab[i][j].getPos());

        if (tab[i][j].isLock() && tab[i][j].getTipoCelda() == TipoCelda.AZUL) {
            pistaCeldaAzul(i, j, pista, tab);
        } else if (!tab[i][j].isLock() && (tab[i][j].getTipoCelda() == TipoCelda.GRIS ||
                tab[i][j].getTipoCelda() == TipoCelda.AZUL)) {
            ponerRoja(i, j, pista, tab);
        }

        return pista;
    }

    /**
     * Procesa las pistas que estén asociadas a una celda azul
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     */
    private void pistaCeldaAzul(int i, int j, Pista pista, Celda[][] tab) {
        _numVisibles = 0;
        boolean yaCerrada;  //Para medir si la celda completa ya esta cerrada
        //-----------------DEMASIADAS-AZULES-----------------------//
        yaCerrada = demasiadasAzules(i, j, pista, tab);
        if (pista.getTipo() != TipoPista.NONE) return;

        //-----------------INSUFICIENTES-AZULES-----------------------//
        if (yaCerrada) {    //Pueden estar las azules justas o faltar
            if (_numVisibles < tab[i][j].getValue()) {
                pista.setTipo(TipoPista.INSUFICIENTES_AZULES);
                pista.setPosCelda(tab[i][j].getPos());
            }
            //En caso de estar la celda completa y cerrada no puede tener pista
            return;
        }

        //-----------------CERRAR-CASILLA-----------------------//
        if (_numVisibles == tab[i][j].getValue()) {
            pista.setTipo(TipoPista.CERRAR_CASILLA);
            return;
        }

        //-----------------DEBE-SER-PARED-----------------------//
        ponerPared(i, j, pista, tab);
        if (pista.getTipo() != TipoPista.NONE) return;

        //-----------------DEBE-SER-AZUL-------------------------//
        ponerAzul(i, j, pista, tab);
    }

    /**
     * Procesa la pista sobre demasiadas azules. Es la 4 del enunciado
     *
     * @param i:     Fila de la celda
     * @param j:     Columna de la celda
     * @param pista: variable de la pista que se va a modificar
     * @return Devuelve true si la celda está cerrada
     * false en caso contrario o si ha sido pista
     */
    private boolean demasiadasAzules(int i, int j, Pista pista, Celda[][] tab) {
        boolean yaCerrada = true;

        // i/n --> FILAS // j/m --> COLUMNAS
        //-----------------ABAJO----------------//
        for (int n = i + 1; n < _tam; ++n) {
            if (tab[n][j].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
                // En cuanto haya una gris, se deja de contar
            else if (tab[n][j].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > tab[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPosCelda(tab[i][j].getPos());
                return false;
            }
        }
        //-----------------ARRIBA----------------//
        for (int n = i - 1; n >= 0; --n) {
            if (tab[n][j].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
            else if (tab[n][j].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > tab[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPosCelda(tab[i][j].getPos());
                return false;
            }
        }
        //-----------------DERECHA----------------//
        for (int m = j + 1; m < _tam; ++m) {
            if (tab[i][m].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
            else if (tab[i][m].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > tab[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPosCelda(tab[i][j].getPos());
                return false;
            }
        }
        //-----------------IZQUIERDA----------------//
        for (int m = j - 1; m >= 0; --m) {
            if (tab[i][m].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
            else if (tab[i][m].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > tab[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPosCelda(tab[i][j].getPos());
                return false;
            }
        }

        //Si ha salido siempre por celdas rojas devolvera true
        return yaCerrada;
    }

    /**
     * Procesa la pista sobre si hay que poner pared en la situación en la que se pone una
     * celda gris en azul. Es la 2 del enunciado.
     *
     * @param i:     Fila de la celda
     * @param j:     Columna de la celda
     * @param pista: Variable de la pista que se va a modificar
     */
    private void ponerPared(int i, int j, Pista pista, Celda[][] tab) {
        boolean gris = false;
        int nuevoVal = _numVisibles;

        int nxt = 1;
        Celda c = tab[i][j];
        for (int[] d : _dirs) {
            int row = c.getRow() + d[0] * nxt;
            int col = c.getCol() + d[1] * nxt;

            // Mientras esté en los límites del tablero
            // Mientras sea (gris y no haya encontrado gris) o mientras se azul
            // es cuando interesa seguir buscando
            while (row >= 0 && row < _tam && col < _tam && col >= 0 &&
                    ((tab[row][col].getTipoCelda() == TipoCelda.GRIS && !gris) ||
                            tab[row][col].getTipoCelda() == TipoCelda.AZUL)) {

                if (tab[row][col].getTipoCelda() == TipoCelda.GRIS) {
                    gris = true;
                    _indRelPista[0] = row;
                    _indRelPista[1] = col;
                    nuevoVal++;
                } else if (gris && tab[row][col].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;

                if (nuevoVal > tab[i][j].getValue()) {
                    pista.setTipo(TipoPista.DEBE_SER_PARED);
                    pista.setPosCelda(tab[i][j].getPos());
                    return;
                }

                // Siguiente
                nxt++;
                row = c.getRow() + d[0] * nxt;
                col = c.getCol() + d[1] * nxt;
            }

            gris = false;
            nuevoVal = _numVisibles;
            nxt = 1;
        }
    }

    /**
     * Procesa la pista sobre si hay que poner una celda azul. Es la 3 del enunciado
     *
     * @param i:     Fila de la celda
     * @param j:     Columna de la celda
     * @param pista: Variable de la pista que se va a modificar
     */
    private void ponerAzul(int i, int j, Pista pista, Celda[][] tab) {
        // Calculo de alcanzables
        Celda c = tab[i][j];
        int[] alcanzables = possibleVisibles(c, tab);

        // Pista 8 del enunciado. Si la celda solo tiene una dirección, entonces se ponen azules
        // en esa dirección
        boolean oneDir = isOneDir(alcanzables, c);
        if (oneDir) {
            pista.setTipo(TipoPista.UNA_DIRECCION);
            pista.setPosCelda(c.getPos());
        }
        // Pista 9 del enunciado. Si las contiguas suman el valor de la celda, basta con ponerlas
        // todas en azul para resolverlo
        else if (alcanzables[0] + alcanzables[1] + alcanzables[2] + alcanzables[3] == c.getValue()) {
            pista.setTipo(TipoPista.AZULES_ALCANZABLES);
            pista.setPosCelda(c.getPos());
        }
        // Pista 3 del enunciado. Se escoge la dirección donde hay que poner azules
        else {
            putBlueInDir(alcanzables, pista, c);
        }
    }

    /**
     * Procesa la pista sobre la celda gris que ya esté cerrada. Solo mira las que están
     * a su lado directamente. Son las pistas 6 y 7 del enunciado.
     *
     * @param i:     Fila de la celda
     * @param j:     Columna de la celda
     * @param pista: Variable de la pista que se va a modificar
     */
    private void ponerRoja(int i, int j, Pista pista, Celda[][] tab) {
        // Si se detecta que no está cerrada, entonces no es esta pista
        if ((i + 1 < _tam && tab[i + 1][j].getTipoCelda() != TipoCelda.ROJO)        // ABAJO
                || (i - 1 >= 0 && tab[i - 1][j].getTipoCelda() != TipoCelda.ROJO)   // ARRIBA
                || (j + 1 < _tam && tab[i][j + 1].getTipoCelda() != TipoCelda.ROJO) // DERECHA
                || (j - 1 >= 0 && tab[i][j - 1].getTipoCelda() != TipoCelda.ROJO))  // IZQUIERDA
        {
            return;
        }

        TipoPista tipo = tab[i][j].getTipoCelda() == TipoCelda.GRIS ?
                TipoPista.GRIS_ES_ROJA : TipoPista.AZUL_ES_ROJA;
        pista.setTipo(tipo);
        pista.setPosCelda(tab[i][j].getPos());
        _indRelPista[0] = i;
        _indRelPista[1] = j;
    }

    /**
     * Comprueba si la celda tiene una sola dirección
     * para poner celdas azules
     *
     * @param alcanzables Visibles alcanzables para cada dirección
     * @param c           Celda desde la que quiere hacer la comprobación
     * @return Devuelve si tiene una sola dirección o no
     */
    private boolean isOneDir(final int[] alcanzables, final Celda c) {
        boolean oneDir = false;
        int d = 0;
        for (int val : alcanzables) {
            if (val > 0) {
                int next = 1;
                int row;
                int col;
                while (next <= val) {
                    row = c.getRow() + _dirs.get(d)[0] * next;
                    col = c.getCol() + _dirs.get(d)[1] * next;

                    // 1. La siguiente celda es gris, por tanto se puede rellenar por ahí
                    if (_celdas[row][col].getTipoCelda() == TipoCelda.GRIS) {
                        if (!oneDir) {
                            oneDir = true;
                            _indRelPista[0] = _dirs.get(d)[0];
                            _indRelPista[1] = _dirs.get(d)[1];
                            break;
                        }
                        // 2. Se vuelve a encontrar otro camino en gris, por tanto no es
                        // una única dirección
                        return false;
                    }
                    next++;
                }
            }
            d++;
        }

        return oneDir;
    }

    /**
     * Busca las posibles visibles para cada dirección de la celda
     *
     * @param c   Celda desde la que se inicia la búsqueda
     * @param tab Tablero donde se realiza la búsqueda
     * @return Devuelve el número de posibles visibles para cada dirección
     */
    private int[] possibleVisibles(Celda c, Celda[][] tab) {
        int[] alcanzables = new int[4];
        int nxt = 1;
        int line = 0;
        for (int[] d : _dirs) {
            int row = c.getRow() + d[0] * nxt;
            int col = c.getCol() + d[1] * nxt;

            while (row >= 0 && row < _tam && col < _tam && col >= 0 &&
                    tab[row][col].getTipoCelda() != TipoCelda.ROJO &&
                    alcanzables[line] < c.getValue()) {

                alcanzables[line]++;

                // Siguiente
                nxt++;
                row = c.getRow() + d[0] * nxt;
                col = c.getCol() + d[1] * nxt;
            }

            line++;
            nxt = 1;
        }

        return alcanzables;
    }

    private void putBlueInDir(int[] alcanzables, Pista pista, Celda c) {
        // 1. Se escoge el camino que tenga más alcanzables
        int max = Math.max(
                Math.max(alcanzables[0], alcanzables[1]),
                Math.max(alcanzables[2], alcanzables[3])
        );
        // 2. Cálculo de celdas que quedan por ver
        int leftVisibles = c.getValue() - _numVisibles;
        boolean unique = false;
        for (int k = 0; k < 4; k++) {
            if (max == alcanzables[k] && alcanzables[k] <= leftVisibles) {
                // 3. ¿Las alcanzables en la dirección k son las que quedan por ver?
                if (alcanzables[k] == leftVisibles) {
                    // 3.1 Si ya se asignó un caminó entonces no es único. Por tanto,
                    // no forma parte de la pista. Si hay más de un camino con EXACTAMENTE
                    // el número de celdas alcanzables igual a las que quedan por ver
                    // entonces no se puede dar una pista de este tipo
                    if (unique) {
                        pista.setTipo(TipoPista.NONE);
                        return;
                    }

                    unique = true;
                }

                // 4. Asignación de la pista
                if (pista.getTipo() != TipoPista.NONE)
                    continue;
                pista.setTipo(TipoPista.DEBE_SER_AZUL);
                pista.setPosCelda(c.getPos());
                _indRelPista[0] = _dirs.get(k)[0];
                _indRelPista[1] = _dirs.get(k)[1];
            }
        }
    }
//-----------------------------------------CELDAS-------------------------------------------------//

    public void changeCellColor(int i, int j) {
        _celdas[i][j].changeColor();
        addStackMov(_celdas[i][j]);
    }

    public void addGreyCell(int grey) {
        _celdasGrises += grey;
    }

//--------------------------------------------GET-SET---------------------------------------------//

    /**
     * Devuelve el numero de grises que tiene actualmente el tablero
     */
    public int getGrises() {
        return _celdasGrises;
    }

    /**
     * Devuelve una celda del tablero
     *
     * @param i Fila de la celda
     * @param j Columna de la celda
     */
    public Celda getCelda(int i, int j) {
        return _celdas[i][j];
    }

    /**
     * Devuelve el índce de la celda relacionada a la pista
     * que se haya generado
     */
    public int[] getIndRelPista() {
        return _indRelPista;
    }

    /**
     * Devuelve el número de celdas visibles de la
     * última celda que se proceso en procesaPista
     */
    public int getNumVisibles() {
        return _numVisibles;
    }

    /**
     * Devuelve el índice de la celda relacionada al
     * último movimiento deshecho
     */
    public int[] getUndoCellPos(){
        return _undoCellPos;
    }

//------------------------------------------------------------------------------------------------//

    // ATRIBUTOS DEL TABLERO
    /**
     * Clase que se encarga de la creación del tablero
     */
    CreaTablero _creation;
    /**
     * Array que contiene las celdas
     */
    Celda[][] _celdas;
    /**
     * Solución final del tablero
     */
    Celda[][] _solution;
    /**
     * Dimensión del tablero
     * 4x4, 5x5, 6x6...
     */
    int _tam;
    /**
     * Auxiliar para contar las celdas
     * visibles de una celda
     */
    int _numVisibles = 0;
    /**
     * Celdas grises que posee el tablero.
     * Se usa para calcular el porcentaje
     * de tablero completado
     */
    int _celdasGrises = 0;
    /**
     * Número total de grises que tenía
     * el tablero al incio
     */
    int _totalGrises = 0;
    /**
     * Posición del tablero en el juego
     */
    int[] _tabPos;
    /**
     * Tamaño de cada celda del tablero
     */
    float _celdaSize;
    /**
     * Diametro de los circulos de la celda
     */
    float _celdaDiam;
    /**
     * Tamaño de la fuente de las celdas
     */
    int _celdaTamFont;
    /**
     * Lista de direcciones
     */
    public List<int[]> _dirs;
    /**
     * Integer auxiliar relacionado con
     * la pista generada. Puede guardar
     * posiciones o direcciones
     */
    int[] _indRelPista;
    /**
     * Fuente de las celdas
     */
    Font _celdaFont;
    /**
     * Indice de la celda que se ha deshecho
     */
    int[] _undoCellPos;
    // PILA DE MOVIMIENTOS
    /**
     * Registro de los movimiento de la partida
     */
    Stack<Celda> _movements;
}
