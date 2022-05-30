package es.ucm.arblemar.gamelogic.tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.gamelogic.MovementInfo;
import es.ucm.arblemar.gamelogic.enums.TipoUndo;
import es.ucm.arblemar.gamelogic.enums.TipoCelda;
import es.ucm.arblemar.gamelogic.enums.TipoPista;
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
        int i = 0, j = 0;
        while (i < _tam) {
            Celda c = _celdas[i][j];
            if (!c.isLock() && c.getTipoCelda() == TipoCelda.AZUL) {
                _numVisibles = 0;
                int nxt = 1;
                for (int[] d : _dirs) {
                    int row = c.getRow() + d[0] * nxt;
                    int col = c.getCol() + d[1] * nxt;

                    while (row >= 0 && row < _tam && col < _tam && col >= 0 &&
                            _celdas[row][col].getTipoCelda() == TipoCelda.AZUL) {

                        _numVisibles++;

                        // Siguiente
                        nxt++;
                        row = c.getRow() + d[0] * nxt;
                        col = c.getCol() + d[1] * nxt;
                    }

                    nxt = 1;
                }

                _celdas[i][j].setLock(true);
                _celdas[i][j].showText(true, _numVisibles);
            }
            j++;
            if (j >= _tam) {
                j = 0;
                i++;
            }
        }
    }

    /**
     * Reinicia el último movimiento hecho.
     *
     * @return devuelve si hay algo que deshacer o no
     */
    public TipoUndo resetMovement() {
        if (_movements.empty()) {
            return TipoUndo.NONE;
        }

        MovementInfo mov = _movements.peek();
        Celda c = mov.getCelda();
        // Si es distinta de gris, entonces hay que volver a sumar
        // el número de celdas grises del tablero, porque se va a volver
        // a poner en gris
        if (c.getTipoCelda() != TipoCelda.GRIS && mov.getTipoCelda() == TipoCelda.GRIS) {
            _celdasGrises++;
        } else if (c.getTipoCelda() == TipoCelda.GRIS && mov.getTipoCelda() != TipoCelda.GRIS) {
            _celdasGrises--;
        }

        c.setTipoCelda(mov.getTipoCelda());

        _undoCellPos = c.getPos();
        _movements.pop();
        return mov.getTipoUndo();
    }

    private void addStackMov(Celda c) {
        // Si el movimiento es el mismo que el anterior, se devuelve
        if (!_movements.isEmpty() && _movements.peek().getCelda() == c) return;
        MovementInfo mov = new MovementInfo(c);
        _movements.add(mov);
    }

//-----------------------------------BÚSQUEDA-PISTAS----------------------------------------------//

    /**
     * Busca la pista que pueda existir en la casilla seleccionada del tablero que se
     * recibe por parámetro.
     *
     * @param i:   Fila de la celda
     * @param j:   Columna de la celda
     * @param tab: Tablero donde se quieren buscar las pistas
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
     * @param i:   Fila de la celda
     * @param j:   Columna de la celda
     * @param tab: Tablero donde se quieren buscar las pistas
     */
    private void pistaCeldaAzul(int i, int j, Pista pista, Celda[][] tab) {
        _numVisibles = 0;
        boolean yaCerrada;  //Para medir si la celda completa ya esta cerrada
        int[] dirVisibles = new int[4];
        //-----------------DEMASIADAS-AZULES-----------------------//
        yaCerrada = demasiadasAzules(i, j, pista, tab, dirVisibles);
        if (pista.getTipo() != TipoPista.NONE) return;

        //-----------------INSUFICIENTES-AZULES-----------------------//
        if (yaCerrada) {
            //Pueden estar las azules justas o faltar
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
        ponerAzul(i, j, pista, tab, dirVisibles);
    }

    /**
     * Procesa la pista sobre demasiadas azules. Es la 4 del enunciado
     *
     * @param i:           Fila de la celda
     * @param j:           Columna de la celda
     * @param pista:       variable de la pista que se va a modificar
     * @param tab:         Tablero donde se quieren buscar las pistas
     * @param dirVisibles: Array de visibles de la celda en función de las direcciones
     * @return Devuelve true si la celda está cerrada
     * false en caso contrario o si ha sido pista
     */
    private boolean demasiadasAzules(int i, int j, Pista pista, Celda[][] tab, int[] dirVisibles) {
        boolean yaCerrada = true;

        int nxt = 1;
        Celda c = tab[i][j];

        int k = 0;
        for (int[] d : _dirs) {
            int row = c.getRow() + d[0] * nxt;
            int col = c.getCol() + d[1] * nxt;

            // Mientras esté en los límites del tablero
            // Mientras sea (gris y no haya encontrado gris) o mientras se azul
            // es cuando interesa seguir buscando
            while (row >= 0 && row < _tam && col < _tam && col >= 0 &&
                    tab[row][col].getTipoCelda() == TipoCelda.AZUL) {

                dirVisibles[k]++;
                _numVisibles++;
                // Si se excede el valor de la celda, hay demasiadas azules
                if (_numVisibles > tab[i][j].getValue()) {
                    pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                    pista.setPosCelda(tab[i][j].getPos());
                    return false;
                }

                // Siguiente
                nxt++;
                row = c.getRow() + d[0] * nxt;
                col = c.getCol() + d[1] * nxt;
            }

            // El bucle anterior puede haberse salido por haber encontrado una celda != Azul.
            // Si es gris, entonces, la celda a partir de la cual se está procesando la pista no
            // está cerrada.
            // Solo interesa asignarlo una vez.
            if (yaCerrada && row >= 0 && row < _tam && col < _tam && col >= 0 &&
                    tab[row][col].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
            }

            k++;
            nxt = 1;
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
            // Mientras (sea gris y no haya encontrado gris antes) o mientras sea azul
            // es cuando interesa seguir buscando
            while (row >= 0 && row < _tam && col < _tam && col >= 0 &&
                    ((tab[row][col].getTipoCelda() == TipoCelda.GRIS && !gris) ||
                            tab[row][col].getTipoCelda() == TipoCelda.AZUL)) {

                if (tab[row][col].getTipoCelda() == TipoCelda.GRIS) {
                    gris = true;
                    _indRelPista[0] = row;
                    _indRelPista[1] = col;
                    nuevoVal++;
                } else if (gris && tab[row][col].getTipoCelda() == TipoCelda.AZUL)
                    nuevoVal++;

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
    private void ponerAzul(int i, int j, Pista pista, Celda[][] tab, int[] dirVisibles) {
        // Calculo de alcanzables
        Celda c = tab[i][j];
        int[] alcanzables = possibleVisibles(c, tab, dirVisibles);

        // Pista 8 del enunciado. Si la celda solo tiene una dirección, entonces se ponen azules
        // en esa dirección
        boolean oneDir = isOneDir(alcanzables, dirVisibles, c);
        int valorAlcanzable = alcanzables[0] + alcanzables[1] + alcanzables[2] + alcanzables[3] +
                dirVisibles[0] + dirVisibles[1] + dirVisibles[2] + dirVisibles[3];

        if (oneDir) {
            pista.setTipo(TipoPista.UNA_DIRECCION);
            pista.setPosCelda(c.getPos());
        } else if (valorAlcanzable <= c.getValue()) {
            TipoPista type = valorAlcanzable == c.getValue() ?
                    // Pista 9 del enunciado. Si las contiguas suman el valor de la celda,
                    // basta con ponerlas todas en azul para resolverlo
                    TipoPista.AZULES_ALCANZABLES :
                    // Pista 10 del enunciado. Si se ponen en azul las celdas alzanzables,
                    // no se puede llegar a igualar el valor de la celda
                    TipoPista.AZULES_INALCANZABLES;
            pista.setTipo(type);
            pista.setPosCelda(c.getPos());
        }
        // Pista 3 del enunciado. Se escoge la dirección donde hay que poner azules
        else {
            putBlueInDir(alcanzables, pista, c, dirVisibles);
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
        // Arriba
        // Abajo
        // Izquierda
        // Derecha

        // Si se detecta que no está cerrada, entonces no es esta pista
        if ((i - 1 >= 0 && tab[i - 1][j].getTipoCelda() != TipoCelda.ROJO)          // ARRIBA
                || (i + 1 < _tam && tab[i + 1][j].getTipoCelda() != TipoCelda.ROJO) // ABAJO
                || (j - 1 >= 0 && tab[i][j - 1].getTipoCelda() != TipoCelda.ROJO)   // IZQUIERDA
                || (j + 1 < _tam && tab[i][j + 1].getTipoCelda() != TipoCelda.ROJO))// DERECHA
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
    private boolean isOneDir(final int[] alcanzables, final int[] dirVisibles, final Celda c) {
        boolean oneDir = false;
        int d = 0;
        for (int val : alcanzables) {
            if (val > 0) {
                int next = dirVisibles[d] + 1;
                int row = c.getRow() + _dirs.get(d)[0] * next;
                int col = c.getCol() + _dirs.get(d)[1] * next;

                while (row >= 0 && row < _tam && col < _tam && col >= 0) {
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
                    // Siguiente
                    next++;
                    row = c.getRow() + _dirs.get(d)[0] * next;
                    col = c.getCol() + _dirs.get(d)[1] * next;
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
    private int[] possibleVisibles(Celda c, Celda[][] tab, int[] dirVisibles) {
        int[] alcanzables = new int[4];
        int nxt = 1;
        int line = 0;
        for (int[] d : _dirs) {
            int row = c.getRow() + d[0] * nxt;
            int col = c.getCol() + d[1] * nxt;
            int aux = dirVisibles[line];

            // Mientras esté dentro de los límites
            // Mientras la siguiente celda sea distinta de roja, interesa
            while (row >= 0 && row < _tam && col < _tam && col >= 0 &&
                    tab[row][col].getTipoCelda() != TipoCelda.ROJO /*&&
                    alcanzables[line] < c.getValue()*/) {

                if (tab[row][col].getTipoCelda() == TipoCelda.AZUL && aux > 0)
                    aux--;
                else
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

    /**
     * Calcla en qué dirección debe poner una azul
     *
     * @param alcanzables Número de celdas alcanzables desde c en todas direcciones
     * @param pista:      Variable de la pista que se va a modificar
     * @param c           Celda desde la que se inicia la búsqueda
     * @param dirVisibles Número de celdas visibles desde c en todas direcciones
     */
    private void putBlueInDir(int[] alcanzables, Pista pista, Celda c, int[] dirVisibles) {
        // Azules que quedan por colocar
        int leftVisibles = c.getValue() - _numVisibles;
        // Valor máximo alcanzable y el ínice correspondiente
        int max = Math.max(
                Math.max(alcanzables[0], alcanzables[1]),
                Math.max(alcanzables[2], alcanzables[3])
        );
        int[] maximo = getMaximumAndIndex(alcanzables, max);

        // ¿Cuántas direcciones tiene disponibles
        int count = 0;
        int indexDuo = 0;
        int i = 0;
        for (int a : alcanzables) {
            if (a > 0) {
                count++;
                if (maximo[2] != 1 || (maximo[2] == 1 && maximo[0] != a)) {
                    indexDuo = i;
                }
            }
            i++;
        }

        // 1. Solo tiene 2 direcciones
        if (count == 2) {
            // Ambos son iguales y menores que las que quedan por colocar
            boolean bothEqualLess = maximo[2] != 1 && alcanzables[indexDuo] < leftVisibles;
            // Ambas direcciones son diferentes y son más pequeñas que leftVisibles
            // porque el maximo es menor que las que quedan por colocar
            boolean bothLess = maximo[2] == 1 && maximo[0] < leftVisibles;
            if (bothEqualLess || bothLess) {
                // Se puede coger cualquier dirección.
                pista.setTipo(TipoPista.DEBE_SER_AZUL);
                pista.setPosCelda(c.getPos());
                _indRelPista[0] = _dirs.get(indexDuo)[0];
                _indRelPista[1] = _dirs.get(indexDuo)[1];
                return;
            }

            // El valor máximo es único, su valor es mayor que las que quedan por ver
            // y el valor del otro camino es menor que las que quedan por colocar
            boolean isMaxDir = maximo[2] == 1 && alcanzables[maximo[1]] >= leftVisibles &&
                    alcanzables[indexDuo] < leftVisibles;
            if (isMaxDir) {
                pista.setTipo(TipoPista.DEBE_SER_AZUL);
                pista.setPosCelda(c.getPos());
                _indRelPista[0] = _dirs.get(maximo[1])[0];
                _indRelPista[1] = _dirs.get(maximo[1])[1];
                return;
            }
        }

        // 2. Existe una dirección alcanzable más grande que las otras y la suma de las otras
        // es menor que las que quedan por ver, por tanto, se escoge la dirección más grande
        if (maximo[2] == 1) {
            int sumMenores = 0;
            for (int a : alcanzables) {
                if (a != maximo[0]) {
                    sumMenores += a;
                }
            }
            if (sumMenores < leftVisibles) {
                pista.setTipo(TipoPista.DEBE_SER_AZUL);
                pista.setPosCelda(c.getPos());
                _indRelPista[0] = _dirs.get(maximo[1])[0];
                _indRelPista[1] = _dirs.get(maximo[1])[1];
            }
        }
        // Si hay más de un máximo, entonces, deben ser todos menores que
        // los que quedan por colocar
        else {
            int sumMayores = 0;
            for (int a : alcanzables) {
                if (a == max) {
                    sumMayores += a;
                }
            }

            if (sumMayores < leftVisibles) {
                pista.setTipo(TipoPista.DEBE_SER_AZUL);
                pista.setPosCelda(c.getPos());
                _indRelPista[0] = _dirs.get(maximo[1])[0];
                _indRelPista[1] = _dirs.get(maximo[1])[1];
            }
        }
    }

    /**
     * Comprueba si el valor máximos es único y lo devuelve
     * junto con el índice correspondiente y el número de
     * máximos. Si el máximo no es único, entonces se devolverá
     * -1 como primer elemento del array devuelto.
     *
     * @param array    Array del que obtener el máximo
     * @param maxValue Valor máximo del array
     */
    private int[] getMaximumAndIndex(int[] array, int maxValue) {
        int[] maxAndIndex = {-1, 0, 0};
        for (int i = 0; i < 4; i++) {
            int aux = array[i];
            if (aux == maxValue) {
                maxAndIndex[0] = aux;
                maxAndIndex[1] = i;
                maxAndIndex[2]++;
            }
        }

        if (maxAndIndex[2] > 1) {
            maxAndIndex[0] = -1;
        }

        return maxAndIndex;
    }
//-----------------------------------------CELDAS-------------------------------------------------//

    public void changeCellColor(int i, int j) {
        addStackMov(_celdas[i][j]);
        _celdas[i][j].changeColor();
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
    public int[] getUndoCellPos() {
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
    Stack<MovementInfo> _movements;
}
