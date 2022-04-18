package es.ucm.arblemar.gamelogic.tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.gamelogic.CellCallback;
import es.ucm.arblemar.gamelogic.states.GameState;

/**
 * Generador de tableros correctos
 */
public class CreaTablero {
    public CreaTablero(Tablero t, int tam, Celda[][] celdas) {
        _tab = t;
        _tam = tam;
        _celdas = celdas;
        // Lista de direcciones
        _dirs = new ArrayList<>();
        int[] dir = new int[2];
        // Arriba
        dir[0] = -1;
        _dirs.add(dir);
        // Abajo
        dir = new int[2];
        dir[0] = 1;
        _dirs.add(dir);
        // Izquierda
        dir = new int[2];
        dir[1] = -1;
        _dirs.add(dir);
        // Derecha
        dir = new int[2];
        dir[1] = 1;
        _dirs.add(dir);
    }

    /**
     * Tablero de 4x4 del enunciado
     */
    public int testTab(final int[] tabPos, final float cSize, final Font cFont,
                       final int tamFont, final float diam) {
        //Tablero
        int[] pos;
        pos = new int[2];
        for (int i = 0; i < _tam; i++) {
            pos[1] = (int) (tabPos[1] + (cSize * i) + (cSize * 0.1));
            for (int j = 0; j < _tam; j++) {
                pos[0] = (int) (tabPos[0] + (cSize * j) + (cSize * 0.1));

                int[] ind = new int[2];
                ind[0] = i;
                ind[1] = j;
                if ((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 1 && j == 1) || (i == 1 && j == 2) ||
                        (i == 2 && j == 0) || (i == 2 && j == 3) || (i == 3 && j == 0) || (i == 3 && j == 1) ||
                        (i == 3 && j == 3)) {
                    _celdasGrises++;
                    _celdas[i][j] = new Celda(TipoCelda.GRIS, cFont, tamFont,
                            0, pos, diam, ind);
                    _celdas[i][j].setCellCallback(new CellCallback() {
                        @Override
                        public void doSomething(int x, int y, GameState gm) {
                            Pista pista = gm.getPista();
                            if (pista.getTipo() != TipoPista.NONE) {
                                pista.setTipo(TipoPista.NONE);
                            }
                            changeCellColor(x, y, gm);
                        }
                    });
                } else if (i == 2 && j == 2) {
                    _celdas[i][j] = new Celda(TipoCelda.ROJO, cFont, tamFont,
                            0, pos, diam, ind);
                    _celdas[i][j].setCellCallback(new CellCallback() {
                        @Override
                        public void doSomething(int x, int y, GameState gm) {
                            celdaBloqueada(x, y);
                        }
                    });
                } else {
                    if ((i == 0 && j == 0) || (i == 2 && j == 1)) {
                        _celdas[i][j] = new Celda(TipoCelda.AZUL, cFont, tamFont,
                                1, pos, diam, ind);
                        _celdas[i][j].setCellCallback(new CellCallback() {
                            @Override
                            public void doSomething(int x, int y, GameState gm) {
                                celdaBloqueada(x, y);
                            }
                        });
                    } else {
                        _celdas[i][j] = new Celda(TipoCelda.AZUL, cFont, tamFont,
                                2, pos, diam, ind);
                        _celdas[i][j].setCellCallback(new CellCallback() {
                            @Override
                            public void doSomething(int x, int y, GameState gm) {
                                celdaBloqueada(x, y);
                            }
                        });
                    }
                }
            }
        }

        return _celdasGrises;
    }

    /**
     * Inicializa un tablero con celdas grises
     *
     * @param cSize   Tamaño de cada celda en la ventana
     * @param cFont   Tipo de fuente usada en las celdas
     * @param tamFont Tamaño de la fuente
     * @param tabPos  Posición inicial del tablero
     * @param diam    Diámetro de cada celda
     */
    public void initGreyTab(int[] tabPos, float cSize, Font cFont, int tamFont, float diam) {
        int[] pos;
        pos = new int[2];
        for (int i = 0; i < _tam; i++) {
            pos[1] = (int) (tabPos[1] + (cSize * i) + (cSize * 0.1));
            for (int j = 0; j < _tam; j++) {
                pos[0] = (int) (tabPos[0] + (cSize * j) + (cSize * 0.1));
                int[] ind = new int[2];
                ind[0] = i;
                ind[1] = j;
                Celda c = new Celda(TipoCelda.GRIS, cFont, tamFont,
                        0, pos, diam, ind);
                c.setCellCallback(new CellCallback() {
                    @Override
                    public void doSomething(int x, int y, GameState gm) {
                        Pista pista = gm.getPista();
                        if (pista.getTipo() != TipoPista.NONE) {
                            pista.setTipo(TipoPista.NONE);
                        }
                        changeCellColor(x, y, gm);
                    }
                });
                _celdas[i][j] = c;
            }
        }
    }

    /**
     * Calcula el número máximo de azules y rojas que habrá en el tablero al completarlo
     */
    public void calcularCeldas() {
        Random rn = new Random();
        int result = rn.nextInt(_maxBlues - _minBlues) + _minBlues;
        _celdasAzules = Math.round(_tam * _tam * (float) result / 100);
        _celdasRojas = _tam * _tam - _celdasAzules;
    }

    /**
     * Genera un tablero aleatorio
     *
     * @param tabPos  Posición del tablero en la ventana del juego
     * @param cSize   Tamaño de las celdas en la ventana del juego
     * @param cFont   Fuente de las celdas
     * @param tamFont Tamaño de la fuente de las celdas
     * @param diam    Diámetro de las celdas en la ventana del juego
     * @return Deuvelve si ha sido posible la creación de un tablero
     */
    public boolean creaTablero(final int[] tabPos, final float cSize, final Font cFont,
                               final int tamFont, final float diam) {
        _blueLeft = _celdasAzules;
        _redLeft = _celdasRojas;

        boolean created = false;
        try {
            created = creaTableroRec(tabPos, cSize, cFont, tamFont, diam, 0, 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return created;
    }

    public int getGrises() {
        return _celdasGrises;
    }

//-------------------------------------RECURSIÓN--------------------------------------------------//

    /**
     * Genera un tablero aleatorio de forma recursiva usando BackTracking
     * 1. Se añade una nueva celda
     * 2. Se comprueba si es posible añadirla( rojo o azul)
     * 3. Se mira si está lleno el tablero
     * 4. Si está lleno, se comprueba que sea solución
     * 5. Si es solución, se asignan las celdas grises
     *
     * @param tabPos  Posición del tablero en la ventana del juego
     * @param cSize   Tamaño de las celdas en la ventana del juego
     * @param cFont   Fuente de las celdas
     * @param tamFont Tamaño de la fuente de las celdas
     * @param diam    Diámetro de las celdas en la ventana del juego
     * @return Deuvelve si ha sido posible la creación de un tablero
     */
    private boolean creaTableroRec(final int[] tabPos, final float cSize, final Font cFont,
                                   final int tamFont, final float diam, int i, int j) {
        // 3. ¿Está lleno?
        if (i >= _tam) {
            // 4. ¿Es solución?
            if (isSolution()) {
                // Se ponen en grises las celdas que toquen
                makeGreyCells();
                return true;
            } else {
                return false;
            }
        }

        // 1. Asignación de una nueva celda
        Random rn = new Random();
        int tryCell = rn.nextInt(2);
        TipoCelda initTipo = tryCell == 0 ? TipoCelda.AZUL : TipoCelda.ROJO;
        Celda c = _celdas[i][j];
        c.setTipoCelda(initTipo);

        // 2. ¿Es posible añadirla?
        if ((initTipo == TipoCelda.AZUL && !isPossibleToAddBlue(c, initTipo))
                || (initTipo == TipoCelda.ROJO && !isPossibleToAddRed(c, initTipo))) {
            c.setTipoCelda(TipoCelda.GRIS);
            return false;
        }

        // Ha sido posible. ¿Qué color fue el que se añadió?
        if (c.getTipoCelda() == TipoCelda.AZUL) {
            _blueLeft--;
        } else {
            _redLeft--;
        }

        // Siguiente celda
        j++;
        if (j >= _tam) {
            j = 0;
            i++;
        }

        // ¿Hay solución con la celda actual en el color actual?
        if (!creaTableroRec(tabPos, cSize, cFont, tamFont, diam, i, j)) {
            // Si falla, entonces se intenta de nuevo con el otro tipo de celda
            return tryAgain(initTipo, c, tabPos, cSize, cFont, tamFont, diam, i, j);
        }

        return true;
    }

    /**
     * Vuelve a intentar la generación del tablero a partir de la celda donde falló
     *
     * @param initTipo Color de la celda inicialmente
     * @param c        Celda desde la que se quiere mirar
     * @param tabPos   Posición del tablero en la ventana del juego
     * @param cSize    Tamaño de las celdas en la ventana del juego
     * @param cFont    Fuente de las celdas
     * @param tamFont  Tamaño de la fuente de las celdas
     * @param diam     Diámetro de las celdas en la ventana del juego
     * @param i        Fila de la siguiente celda en el tablero
     * @param j        Columna de la siguiente celda en el tablero
     * @return Devuelve si ha sido posible la generación
     */
    private boolean tryAgain(TipoCelda initTipo, Celda c, final int[] tabPos, final float cSize, final Font cFont,
                             final int tamFont, final float diam, int i, int j) {
        // ¿Ha cambiado el tipoCelda con el que empezó la celda?
        // No: Entonces no se ha comprobado con el otro tipo
        // Sí: Entonces ya se comprobó con el otro tipo y se cambió porque no fue posible
        if (initTipo != c.getTipoCelda()) {
            if (c.getTipoCelda() == TipoCelda.AZUL) {
                _blueLeft++;
            } else {
                _redLeft++;
            }

            c.setTipoCelda(TipoCelda.GRIS);
            return false;
        }

        // Cambio de tipo
        if (initTipo == TipoCelda.AZUL) {
            _blueLeft++;
            // Si al cambiar de tipo no quedan más del otro tipo por colocar
            // entonces no se puede hacer nada más
            if (_redLeft == 0) {
                c.setTipoCelda(TipoCelda.GRIS);
                return false;
            }
            c.setTipoCelda(TipoCelda.ROJO);
        } else {
            _redLeft++;
            // Si al cambiar de tipo no quedan más del otro tipo por colocar
            // entonces no se puede hacer nada más
            if (_blueLeft == 0) {
                c.setTipoCelda(TipoCelda.GRIS);
                return false;
            }
            c.setTipoCelda(TipoCelda.AZUL);
        }

        // ¿Es posible añadir el nuevo tipo?
        if ((initTipo == TipoCelda.ROJO && !isPossibleToAddBlue(c, initTipo))
                || (initTipo == TipoCelda.AZUL && !isPossibleToAddRed(c, initTipo))) {
            c.setTipoCelda(TipoCelda.GRIS);
            return false;
        }

        // ¿El nuevo tipo ya probó antes?
        // Sí: eso significa que el tipo inicial de la celda y el tipo actual son el mismo
        if (initTipo == c.getTipoCelda()) {
            return false;
        }
        // No: Entonces se prueba el nuevo tipo
        if (c.getTipoCelda() == TipoCelda.AZUL) {
            _blueLeft--;
        } else {
            _redLeft--;
        }

        // ¿Se puede crear un tablero con el nuevo tipo?
        // No: Entonces se descarta y se devuelve false
        if (!creaTableroRec(tabPos, cSize, cFont, tamFont, diam, i, j)) {
            if (c.getTipoCelda() == TipoCelda.AZUL) {
                _blueLeft++;
            } else {
                _redLeft++;
            }
            c.setTipoCelda(TipoCelda.GRIS);
            return false;
        }
        // Sí, ha sido posible
        return true;
    }

    /**
     * Comprueba que el tablero generado sea solución.
     * En caso de que haya pistas significa que no es solución
     */
    private boolean isSolution() {
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Pista p = _tab.procesaPista(i, j);
                if (p.getTipo() != TipoPista.NONE) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Comprueba si se puede añadir una celda azul
     *
     * @param c        Celda que se va a cambiar a roja
     * @param prevTipo Es el tipo de la celda que se probó antes
     * @return Devuelve si ha sido posible la asignación a azul
     */
    private boolean isPossibleToAddBlue(Celda c, TipoCelda prevTipo) {
        // No quedan más azules por colocar
        if (_blueLeft == 0) {
            // Si vino en azul, se prueba con el rojo
            if (prevTipo == TipoCelda.AZUL) {
                c.setTipoCelda(TipoCelda.ROJO);
                return isPossibleToAddRed(c, TipoCelda.AZUL);
            }
            // Ya se probó con el rojo antes, así que se descarta
            else if (prevTipo == TipoCelda.ROJO) {
                return false;
            }
        }

        // ¿Añadir la nueva celda ha roto el tablero?
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Celda aux = _celdas[i][j];
                if (aux.getTipoCelda() == TipoCelda.AZUL) {
                    int n = numVisibles(aux);
                    // Ninguna celda azul puede ver más que el tamaño del tablero
                    if (n > _tam) {
                        // Ya se intentó hacer el cambio de color, se descarta
                        if (prevTipo == TipoCelda.ROJO) {
                            return false;
                        }
                        c.setTipoCelda(TipoCelda.ROJO);
                        return isPossibleToAddRed(c, TipoCelda.AZUL);
                    }
                    // Se le pone un valor a la celda
                    aux.showText(true, n);
                }
            }
        }

        return true;
    }

    /**
     * Comprueba si se puede añadir una celda roja
     *
     * @param c        Celda que se va a cambiar a roja
     * @param prevTipo Es el tipo de la celda que se probó antes
     * @return Devuelve si ha sido posible la asignación a rojo
     */
    private boolean isPossibleToAddRed(Celda c, TipoCelda prevTipo) {
        // No quedan más rojas por colocar
        if (_redLeft == 0) {
            // Si vino en rojo, se prueba con el azul
            if (prevTipo == TipoCelda.ROJO) {
                c.setTipoCelda(TipoCelda.AZUL);
                return isPossibleToAddBlue(c, TipoCelda.ROJO);
            }
            // Ya se probó con el azul antes, así que se descarta
            else if (prevTipo == TipoCelda.AZUL) {
                return false;
            }
        }

        // ¿Añadir la nueva celda ha roto el tablero?
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Celda aux = _celdas[i][j];
                if (aux.getTipoCelda() == TipoCelda.AZUL) {
                    int n = numVisibles(aux);
                    // Todas las celdas azules deben ver al menos 1 celda contigua
                    if (n < 1) {
                        // Ya se intentó hacer el cambio de color, se descarta
                        if (prevTipo == TipoCelda.AZUL) {
                            return false;
                        }
                        c.setTipoCelda(TipoCelda.AZUL);
                        return isPossibleToAddBlue(c, TipoCelda.ROJO);
                    }
                    // Se le pone un valor a la celda
                    aux.showText(true, n);
                }
            }
        }

        return true;
    }
//------------------------------------------------------------------------------------------------//

    /**
     * Callback para cambiar el color de la celda correspondiente,
     * actualizar el contador de grises del tablero, añadir movimientos
     * a la pila de movimientos y para detener, si es posible, el temporizador
     * activo del GameState
     *
     * @param i:  Fila de la celda
     * @param j:  Columna de la celda
     * @param gm: Referencia al GameState
     */
    private void changeCellColor(int i, int j, GameState gm) {

        _tab.changeCellColor(i, j);

        // Se añade a la pila de movimientos
        int[] coors = new int[2];
        coors[0] = i;
        coors[1] = j;
        _tab.addStackMov(coors);

        //Si pasamos de gris a azul tenemos una gris menos
        if (_tab.getCelda(i, j).getTipoCelda() == TipoCelda.AZUL)
            _tab.addGreyCell(-1);

        //Si pasamos de roja a gris tenemos gris de nuevo
        if (_tab.getCelda(i, j).getTipoCelda() == TipoCelda.GRIS)
            _tab.addGreyCell(1);

        // Si hay algún temporizador activo, se para
        gm.stopTimer();
    }

    /**
     * Callback para hacer  vibrar a la celda pulsada y mostrar/ocultar los candados
     * en las celdas bloqueadas rojas
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     */
    private void celdaBloqueada(int i, int j) {
        _tab.getCelda(i, j).activeAnim();

        //Para activar o desactivar los candados
        for (int n = 0; n < _tam; n++) {
            for (int m = 0; m < _tam; m++) {
                _tab.getCelda(n, m).alternaCandado();
            }
        }
    }

    /**
     * Termina de generar el tablero asignando las celdas grises y azules fijas que
     * correspondan en función de números aleatorios y de las rojas que se hayan generado.
     */
    private void makeGreyCells() {
        // Generacción de grupos de azules
        List<List<int[]>> groups = new ArrayList<>();
        selectGroups(groups);

        // Seleccion de grises
        Random rn = new Random();
        float result = (float) (rn.nextInt(_maxGrey - _minGrey) + _minGrey) / 100;
        _celdasGrises = Math.round(_tam * _tam * result);
        // Seleccion de azules y rojas
        int blue_red = Math.round((1 - result) * 100);
        // El mínimo de azules será el número de grupos de azules que haya
        int minBlues = groups.size();
        result = (float) (rn.nextInt(blue_red / 5) + blue_red * 2 / 5) / 100;
        _celdasAzules = Math.round(_tam * _tam * result);
        int tries = 0;
        while (_celdasAzules < minBlues) {
            result = (float) (rn.nextInt(blue_red / 5) + blue_red * 2 / 5) / 100;
            _celdasAzules = Math.round(_tam * _tam * result);
            tries++;
        }
        System.out.println("Azules generadas tras: " + tries + " intentos.");

        _celdasRojas = _tam * _tam - _celdasAzules - _celdasGrises;

        // Que se fije al menos una roja
        if (_celdasRojas == 0) {
            _celdasRojas = 1;
            _celdasAzules -= 1;
        }

        // Asignación de azules fijas en función de los grupos
        // Se seleccionan las azules con mayor valor de cada grupo
        while (_celdasAzules > 0) {
            Celda c = getMostFreqBlue(groups);
            _celdasAzules--;
            c.setLock(true);
            c.setCellCallback(new CellCallback() {
                @Override
                public void doSomething(int x, int y, GameState gm) {
                    celdaBloqueada(x, y);
                }
            });
            int value = numVisibles(c);
            c.showText(true, value);
        }

        // Asignación de rojas fijas aleatorias
        while (_celdasRojas > 0) {
            int i = rn.nextInt(_tam);
            int j = rn.nextInt(_tam);
            Celda c = _celdas[i][j];
            if (c.getTipoCelda() == TipoCelda.ROJO && !c.isLock()) {
                _celdasRojas--;
                c.setLock(true);
                c.setCellCallback(new CellCallback() {
                    @Override
                    public void doSomething(int x, int y, GameState gm) {
                        celdaBloqueada(x, y);
                    }
                });
                c.showText(false, 0);
            }
        }

        // Asignación de grises
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Celda c = _celdas[i][j];
                if (!c.isLock()) {
                    c.setTipoCelda(TipoCelda.GRIS);
                    c.showText(false, 0);
                }
            }
        }
    }

    /**
     * Devuelve la celda con mayor frecuencia dentro de
     * los grupos de azules para ponerla como fija
     *
     * @param groups Grupo de azules generado anteriormente
     */
    private Celda getMostFreqBlue(List<List<int[]>> groups) {
        // 1. ¿Todos los grupos tienen al menos una fija?
        int numPerman = 0;
        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < groups.get(i).size(); j++) {
                int[] aux = groups.get(i).get(j);
                Celda c = _celdas[aux[0]][aux[1]];
                if (c.isLock()) {
                    numPerman++;
                    break;
                }
            }
            // Si en la primera iteración no hay ninguna bloqueada
            // entonces se puede trabajar con ella directamente
            if (numPerman == 0)
                break;
        }

        int value = 0;
        // Azul
        Celda blue = _celdas[groups.get(0).get(0)[0]][groups.get(0).get(0)[1]];

        // 2. Si no están todos con fijas, se trabaja con la que toque
        if (numPerman < groups.size()) {
            for (int j = 0; j < groups.get(numPerman).size(); j++) {
                int[] cell = groups.get(numPerman).get(j);
                Celda c = _celdas[cell[0]][cell[1]];
                if (c.getValue() > value) {
                    // Nuevo valor maximo
                    value = c.getValue();
                    // Nuevo azul
                    blue = c;
                }
            }
        }
        // 3. Si están todos, entonces se coge cualquiera no bloqueada
        else {
            for (int i = 0; i < groups.size(); i++) {
                for (int j = 0; j < groups.get(i).size(); j++) {
                    int[] cell = groups.get(i).get(j);
                    Celda c = _celdas[cell[0]][cell[1]];
                    if (c.getValue() > value && !c.isLock()) {
                        // Nuevo valor maximo
                        value = c.getValue();
                        // Nuevo azul
                        blue = c;
                    }
                }
            }
        }

        return blue;
    }

    /**
     * Cuenta el número de visibles de una celda
     *
     * @param c Celda que se quiere mirar
     * @return Número de visibles
     */
    private int numVisibles(Celda c) {
        int n = 0;
        for (int[] d : _dirs) {
            n += visiblesOneDirection(c, d);
        }
        return n;
    }

    /**
     * Número de celdas visibles en una dirección
     *
     * @param initC Celda desde la que se mira
     * @param d     Dirección en la que se mira
     * @return Cantidad de visibles
     */
    private int visiblesOneDirection(Celda initC, int[] d) {
        int visibles = 0;
        int i = 1;
        int row = initC.getRow() + d[0] * i;
        int col = initC.getCol() + d[1] * i;

        while (row >= 0 && row < _tam && col < _tam && col >= 0
                && _celdas[row][col].getTipoCelda() == TipoCelda.AZUL) {
            visibles++;
            i++;
            // Siguiente
            row = initC.getRow() + d[0] * i;
            col = initC.getCol() + d[1] * i;
        }
        return visibles;
    }

    /**
     * Genera una lista de grupos donde cada grupo
     * contiene las azules que son visibles entre sí
     *
     * @param groups Lista de grupos de azules
     */
    private void selectGroups(List<List<int[]>> groups) {
        int i = 0;
        int j = 0;
        while (i < _tam) {
            Celda c = _celdas[i][j];
            // 1. Búsqueda de azules
            if (c.getTipoCelda() == TipoCelda.AZUL) {
                int[] index = new int[2];
                index[0] = c.getRow();
                index[1] = c.getCol();
                // 2. ¿Está añadida en algún grupo?
                boolean added;
                added = isAddedInGroup(groups, index[0], index[1]);
                // NO
                if (!added) {
                    // 3 Se añade nuevo grupo
                    groups.add(new ArrayList<int[]>());
                    int g = groups.size() - 1;
                    // 3.1 Se añade la celda
                    groups.get(g).add(index);
                    // 4. Se añaden todos los contiguos
                    addAllVisiblesFromCell(groups, g);
                }
            }
            // NEXT
            j++;
            if (j == _tam) {
                j = 0;
                i++;
            }
        }
    }

    /**
     * Comprueba si la celda está añadida en algún grupo de azules
     *
     * @param groups Grupo de azules
     * @param row    Fila de la celda
     * @param col    Columna de la celda
     * @return Devuelve si está añadida
     */
    boolean isAddedInGroup(final List<List<int[]>> groups, final int row, final int col) {
        for (int i = 0; i < groups.size(); i++) {
            List<int[]> blues = groups.get(i);
            for (int j = 0; j < blues.size(); j++) {
                int[] pair = blues.get(j);
                if (pair[0] == row && pair[1] == col) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Añade todas las visibles y las contiguas de
     * estas visibles al grupo de azules de la celda origen
     *
     * @param groups Grupo de azules
     * @param g      Índice del grupo de azules de la celda origen
     */
    void addAllVisiblesFromCell(List<List<int[]>> groups, int g) {
        // 4.1 Añadir todos los visibles (y los contiguos a éstos)
        int k = 0;
        int currRow = groups.get(g).get(k)[0];
        int currCol = groups.get(g).get(k)[1];
        int d = 0;

        while (true) {
            // 4.2 Reset de la siguiente celda
            int i = 1;
            int row = currRow + _dirs.get(d)[0] * i;
            int col = currCol + _dirs.get(d)[1] * i;

            // 4.3 Búsqueda de azules en una dirección
            while (row >= 0 && row < _tam && col < _tam && col >= 0
                    && _celdas[row][col].getTipoCelda() == TipoCelda.AZUL) {
                boolean added = isAddedInGroup(groups, row, col);
                // 4.4 ¿Está añadido?
                if (!added) {
                    // 4.5 Se añade la celda
                    int[] index = new int[2];
                    index[0] = row;
                    index[1] = col;
                    groups.get(g).add(index);
                }

                // Next cell
                i++;
                row = currRow + _dirs.get(d)[0] * i;
                col = currCol + _dirs.get(d)[1] * i;
            }

            // Cambio de direccion
            d++;
            if (d == _dirs.size()) {
                d = 0;
                k++;
                // 4.6 ¿Quedan más celdas en el grupo?
                if (k >= groups.get(g).size())
                    break;
                currRow = groups.get(g).get(k)[0];
                currCol = groups.get(g).get(k)[1];
            }
        }
    }

//------------------------------------------------------------------------------------------------//

    /**
     * Referencia a la clase gestora del tablero
     */
    Tablero _tab;
    /**
     * Tamaño del tablero
     */
    int _tam;
    /**
     * Array de celdas del tablero
     */
    Celda[][] _celdas;
    /**
     * Celdas grises que posee el tablero.
     * Se usa para calcular el porcentaje
     * de tablero completado
     */
    int _celdasGrises = 0;
    /**
     * Número de azules que puede tener el tablero
     */
    int _celdasAzules;
    /**
     * Número de rojas que puede tener el tablero
     */
    int _celdasRojas;
    /**
     * Porcentaje mínimo de azules que habrá con el
     * tablero completo
     */
    final int _minBlues = 50;
    /**
     * Porcentaje máximo de azules que habrá con el
     * tablero completo
     */
    final int _maxBlues = 65;
    /**
     * Porcentaje mínimo de grises que habrá con el tablero
     */
    final int _minGrey = 55;
    /**
     * Porcentaje máximo de grises que habrá con el tablero
     */
    final int _maxGrey = 65;
    /**
     * Cuenta las azules que quedan por asignar
     */
    int _blueLeft;
    /**
     * Cuenta las rojas que quedan por asignar
     */
    int _redLeft;
    /**
     * Lista de direcciones
     */
    public List<int[]> _dirs;
}
