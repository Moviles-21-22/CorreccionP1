package es.ucm.arblemar.gamelogic.tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.gamelogic.interfaces.CellCallback;
import es.ucm.arblemar.gamelogic.enums.TipoCelda;
import es.ucm.arblemar.gamelogic.enums.TipoPista;
import es.ucm.arblemar.gamelogic.states.GameState;

/**
 * Generador de tableros
 */
public class CreaTablero {
    public CreaTablero(Tablero t, int tam, Celda[][] celdas) {
        _tab = t;
        _tam = tam;
        _celdas = celdas;
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

                int[] ind = {i, j};
                if ((i == 0 && j == 1) || (i == 1 && j == 0) || (i == 1 && j == 1) || (i == 1 && j == 2) ||
                        (i == 2 && j == 0) || (i == 2 && j == 3) || (i == 3 && j == 0) || (i == 3 && j == 1) ||
                        (i == 3 && j == 3)) {
                    _celdasGrises++;
                    _celdas[i][j] = new Celda(TipoCelda.GRIS, cFont, tamFont,
                            0, pos, diam, ind);
                    _celdas[i][j].setCellCallback(new CellCallback() {
                        @Override
                        public void doSomething(int x, int y, GameState gm) {
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
     * @param cSize   Tama??o de cada celda en la ventana
     * @param cFont   Tipo de fuente usada en las celdas
     * @param tamFont Tama??o de la fuente
     * @param tabPos  Posici??n inicial del tablero
     * @param diam    Di??metro de cada celda
     */
    public void initGreyTab(int[] tabPos, float cSize, Font cFont, int tamFont, float diam) {
        int[] pos;
        pos = new int[2];
        for (int i = 0; i < _tam; i++) {
            pos[1] = (int) (tabPos[1] + (cSize * i) + (cSize * 0.1));
            for (int j = 0; j < _tam; j++) {
                pos[0] = (int) (tabPos[0] + (cSize * j) + (cSize * 0.1));
                int[] ind = {i, j};
                Celda c = new Celda(TipoCelda.GRIS, cFont, tamFont,
                        0, pos, diam, ind);
                c.setCellCallback(new CellCallback() {
                    @Override
                    public void doSomething(int x, int y, GameState gm) {
                        changeCellColor(x, y, gm);
                    }
                });
                _celdas[i][j] = c;
            }
        }
    }

    /**
     * Genera un tablero aleatorio
     *
     * @param tabPos  Posici??n del tablero en la ventana del juego
     * @param cSize   Tama??o de las celdas en la ventana del juego
     * @param cFont   Fuente de las celdas
     * @param tamFont Tama??o de la fuente de las celdas
     * @param diam    Di??metro de las celdas en la ventana del juego
     * @return Deuvelve si ha sido posible la creaci??n de un tablero
     */
    public boolean creaTablero(final int[] tabPos, final float cSize, final Font cFont,
                               final int tamFont, final float diam) {
        calcularCeldas();

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

    public Celda[][] getSolution(){
        return  _solution;
    }
//-------------------------------------RECURSI??N--------------------------------------------------//

    /**
     * Genera un tablero aleatorio de forma recursiva usando BackTracking
     * 1. Se a??ade una nueva celda
     * 2. Se comprueba si es posible a??adirla( rojo o azul)
     * 3. Se mira si est?? lleno el tablero
     * 4. Si est?? lleno, se comprueba que sea soluci??n
     * 5. Si es soluci??n, se asignan las celdas grises
     *
     * @param tabPos  Posici??n del tablero en la ventana del juego
     * @param cSize   Tama??o de las celdas en la ventana del juego
     * @param cFont   Fuente de las celdas
     * @param tamFont Tama??o de la fuente de las celdas
     * @param diam    Di??metro de las celdas en la ventana del juego
     * @return Deuvelve si ha sido posible la creaci??n de un tablero
     */
    private boolean creaTableroRec(final int[] tabPos, final float cSize, final Font cFont,
                                   final int tamFont, final float diam, int i, int j) {
        // 3. ??Est?? lleno?
        if (i >= _tam) {
            Celda last = _celdas[_tam - 1][_tam - 1];
            if(last.getTipoCelda() == TipoCelda.AZUL && last.getValue() == 0){
                last.setTipoCelda(TipoCelda.ROJO);
                last.showText(false);
            }

            // Se ponen en grises las celdas que toquen
            makeGreyCells();
            return true;
        }

        // 1. Asignaci??n de una nueva celda
        Random rn = new Random();
        int tryCell = rn.nextInt(2);
        TipoCelda initTipo = tryCell == 0 ? TipoCelda.AZUL : TipoCelda.ROJO;
        Celda c = _celdas[i][j];
        c.setTipoCelda(initTipo);
        c.showText(false);

        // 2. ??Es posible a??adirla?
        if ((initTipo == TipoCelda.AZUL && !isPossibleToAddBlue(c, initTipo))
                || (initTipo == TipoCelda.ROJO && !isPossibleToAddRed(c, initTipo))) {
            c.setTipoCelda(TipoCelda.GRIS);
            return false;
        }

        // Ha sido posible. ??Qu?? color fue el que se a??adi???
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

        // ??Hay soluci??n con la celda actual en el color actual?
        if (!creaTableroRec(tabPos, cSize, cFont, tamFont, diam, i, j)) {
            // Si falla, entonces se intenta de nuevo con el otro tipo de celda
            return tryAgain(initTipo, c, tabPos, cSize, cFont, tamFont, diam, i, j);
        }

        return true;
    }

    /**
     * Vuelve a intentar la generaci??n del tablero a partir de la celda donde fall??
     *
     * @param initTipo Color de la celda inicialmente
     * @param c        Celda desde la que se quiere mirar
     * @param tabPos   Posici??n del tablero en la ventana del juego
     * @param cSize    Tama??o de las celdas en la ventana del juego
     * @param cFont    Fuente de las celdas
     * @param tamFont  Tama??o de la fuente de las celdas
     * @param diam     Di??metro de las celdas en la ventana del juego
     * @param i        Fila de la siguiente celda en el tablero
     * @param j        Columna de la siguiente celda en el tablero
     * @return Devuelve si ha sido posible la generaci??n
     */
    private boolean tryAgain(TipoCelda initTipo, Celda c, final int[] tabPos, final float cSize, final Font cFont,
                             final int tamFont, final float diam, int i, int j) {
        // ??Ha cambiado el tipoCelda con el que empez?? la celda?
        // No: Entonces no se ha comprobado con el otro tipo
        // S??: Entonces ya se comprob?? con el otro tipo y se cambi?? porque no fue posible
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
            // Si al cambiar de tipo no quedan m??s del otro tipo por colocar
            // entonces no se puede hacer nada m??s
            c.showText(false);
            if (_redLeft == 0) {
                c.setTipoCelda(TipoCelda.GRIS);
                return false;
            }
            c.setTipoCelda(TipoCelda.ROJO);
        } else {
            _redLeft++;
            // Si al cambiar de tipo no quedan m??s del otro tipo por colocar
            // entonces no se puede hacer nada m??s
            if (_blueLeft == 0) {
                c.setTipoCelda(TipoCelda.GRIS);
                return false;
            }
            c.setTipoCelda(TipoCelda.AZUL);
        }

        // ??Es posible a??adir el nuevo tipo?
        if ((initTipo == TipoCelda.ROJO && !isPossibleToAddBlue(c, initTipo))
                || (initTipo == TipoCelda.AZUL && !isPossibleToAddRed(c, initTipo))) {
            c.setTipoCelda(TipoCelda.GRIS);
            return false;
        }

        // ??El nuevo tipo ya prob?? antes?
        // S??: eso significa que el tipo inicial de la celda y el tipo actual son el mismo
        if (initTipo == c.getTipoCelda()) {
            return false;
        }
        // No: Entonces se prueba el nuevo tipo
        if (c.getTipoCelda() == TipoCelda.AZUL) {
            _blueLeft--;
        } else {
            _redLeft--;
        }

        // ??Se puede crear un tablero con el nuevo tipo?
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
        // S??, ha sido posible
        return true;
    }

    /**
     * Comprueba si se puede a??adir una celda azul
     *
     * @param c        Celda que se va a cambiar a roja
     * @param prevTipo Es el tipo de la celda que se prob?? antes
     * @return Devuelve si ha sido posible la asignaci??n a azul
     */
    private boolean isPossibleToAddBlue(Celda c, TipoCelda prevTipo) {
        // No quedan m??s azules por colocar
        if (_blueLeft == 0) {
            // Si vino en azul, se prueba con el rojo
            if (prevTipo == TipoCelda.AZUL) {
                c.setTipoCelda(TipoCelda.ROJO);
                c.showText(false);
                return isPossibleToAddRed(c, TipoCelda.AZUL);
            }
            // Ya se prob?? con el rojo antes, as?? que se descarta
            else if (prevTipo == TipoCelda.ROJO) {
                return false;
            }
        }

        // ??A??adir la nueva celda ha roto el tablero?
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Celda aux = _celdas[i][j];
                if (aux.getTipoCelda() == TipoCelda.AZUL) {
                    int n = numVisibles(aux);
                    // Ninguna celda azul puede ver m??s que el tama??o del tablero
                    if (n > _tam) {
                        // Ya se intent?? hacer el cambio de color, se descarta
                        if (prevTipo == TipoCelda.ROJO) {
                            return false;
                        }
                        c.setTipoCelda(TipoCelda.ROJO);
                        c.showText(false);
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
     * Comprueba si se puede a??adir una celda roja
     *
     * @param c        Celda que se va a cambiar a roja
     * @param prevTipo Es el tipo de la celda que se prob?? antes
     * @return Devuelve si ha sido posible la asignaci??n a rojo
     */
    private boolean isPossibleToAddRed(Celda c, TipoCelda prevTipo) {
        // No quedan m??s rojas por colocar
        if (_redLeft == 0) {
            // Si vino en rojo, se prueba con el azul
            if (prevTipo == TipoCelda.ROJO) {
                c.setTipoCelda(TipoCelda.AZUL);
                return isPossibleToAddBlue(c, TipoCelda.ROJO);
            }
            // Ya se prob?? con el azul antes, as?? que se descarta
            else if (prevTipo == TipoCelda.AZUL) {
                return false;
            }
        }

        // ??A??adir la nueva celda ha roto el tablero?
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Celda aux = _celdas[i][j];
                if (aux.getTipoCelda() == TipoCelda.AZUL) {
                    int n = numVisibles(aux);
                    // Todas las celdas azules deben ver al menos 1 celda contigua
                    if (n < 1) {
                        // Ya se intent?? hacer el cambio de color, se descarta
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

//-------------------------------------TAB-??NICO--------------------------------------------------//

    /**
     * Termina de generar el tablero asignando las celdas grises y azules fijas que
     * correspondan en funci??n de n??meros aleatorios y de las rojas que se hayan generado.
     */
    private void makeGreyCells() {
        // 1. Divisi??n de las azules por grupos - Selecci??n de azules m??nimas
        List<List<Celda>> groups = new ArrayList<>();
        _celdasAzules = 0;
        saveSolution();
        selectBlues(groups);

        _celdasGrises = 0;
        // 2. Asignaci??n de azules como grises
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Celda c = _celdas[i][j];
                if (c.getTipoCelda() == TipoCelda.AZUL && !c.isLock()) {
                    _celdasGrises++;
                    c.setTipoCelda(TipoCelda.GRIS);
                    c.showText(false, 0);
                } else if (c.getTipoCelda() == TipoCelda.ROJO) {
                    c.setLock(true);
                }
            }
        }

        // 2. Selecci??n de rojas m??nimas
        selectReds();
    }

    /**
     * Se hace una copia de la soluci??n generada. Sin grises.
     */
    private void saveSolution() {
        _solution = new Celda[_tam][_tam];
        for (int i = 0; i < _tam; i++) {
            for (int j = 0; j < _tam; j++) {
                Celda c = _celdas[i][j];
                int[] ind = {i, j};
                _solution[i][j] = new Celda(c.getTipoCelda(), null, 0,
                        c.getValue(), c.getPos(), 0, ind);
            }
        }
    }
    //----------------------------AZULES-------------------------------//

    /**
     * Selecciona los azules m??nimos para que el puzzle tenga soluci??n ??nica
     *
     * @param groups Lista de grupos de azules
     */
    private void selectBlues(List<List<Celda>> groups) {
        _celdasAzules = 0;
        int i = 0;
        int j = 0;
        while (i < _tam) {
            Celda c = _celdas[i][j];
            // 1. B??squeda de azules
            if (c.getTipoCelda() == TipoCelda.AZUL) {
                // 2. ??Est?? a??adida en alg??n grupo?
                boolean added;
                added = isAddedInGroup(groups, c);
                // NO
                if (!added) {
                    // 3 Se a??ade nuevo grupo
                    groups.add(new ArrayList<Celda>());
                    int g = groups.size() - 1;
                    // 3.1 Se a??ade la celda
                    groups.get(g).add(c);
                    // 4. Se a??aden todos los contiguos
                    addAllVisiblesFromCell(groups, g);
                    // 5. Se bloquean las azules obligatorias
                    instaLockSomeBlues(groups.get(g));
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
     * Comprueba si la celda est?? a??adida en alg??n grupo de azules
     *
     * @param groups Grupo de azules
     * @param c      Celda que se quiere mirar
     * @return Devuelve si est?? a??adida
     */
    boolean isAddedInGroup(final List<List<Celda>> groups, Celda c) {
        for (int i = 0; i < groups.size(); i++) {
            List<Celda> blues = groups.get(i);
            for (int j = 0; j < blues.size(); j++) {
                if (blues.contains(c)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * A??ade todas las visibles y las contiguas de
     * estas visibles al grupo de azules de la celda origen
     *
     * @param groups Grupo de azules
     * @param g      ??ndice del grupo de azules de la celda origen
     */
    void addAllVisiblesFromCell(List<List<Celda>> groups, int g) {
        // 4.1 A??adir todos los visibles (y los contiguos a ??stos)
        int k = 0;
        Celda currC = groups.get(g).get(k);
        int currRow = currC.getRow();
        int currCol = currC.getCol();
        int d = 0;

        while (true) {
            // 4.2 Reset de la siguiente celda
            int i = 1;
            int row = currRow + _dirs.get(d)[0] * i;
            int col = currCol + _dirs.get(d)[1] * i;

            // 4.3 B??squeda de azules en una direcci??n
            while (row >= 0 && row < _tam && col < _tam && col >= 0
                    && _celdas[row][col].getTipoCelda() == TipoCelda.AZUL) {
                Celda c = _celdas[row][col];
                currC.addVisible(_celdas[row][col]);
                boolean added = isAddedInGroup(groups, c);
                // 4.4 ??Est?? a??adido?
                if (!added) {
                    // 4.5 Se a??ade la celda
                    groups.get(g).add(c);
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
                // 4.6 ??Quedan m??s celdas en el grupo?
                if (k >= groups.get(g).size())
                    break;
                currC = groups.get(g).get(k);
                currRow = currC.getRow();
                currCol = currC.getCol();
            }
        }
    }

    /**
     * Pone en lock algunas azules para que el puzzle
     * pueda tener soluci??n ??nica
     *
     * @param blues Grupo de azules que se va a mirar
     */
    void instaLockSomeBlues(List<Celda> blues) {
        // 1. Celda menos frecuente
        Celda currC = moreFreqCell(blues);
        _celdasAzules++;
        currC.setLock(true);
        currC.setCellCallback(new CellCallback() {
            @Override
            public void doSomething(int x, int y, GameState gm) {
                celdaBloqueada(x, y);
            }
        });
        currC.showText(true);

        int i = 0;
        while (i < blues.size()) {
            Celda cell = blues.get(i);
            boolean canSee;
            // 2. ??Alguna celda lock puede ver a cell?
            canSee = canSeeLockCell(blues, cell);
            if (!canSee && !cell.isLock()) {
                _celdasAzules++;
                cell.setLock(true);
                cell.setCellCallback(new CellCallback() {
                    @Override
                    public void doSomething(int x, int y, GameState gm) {
                        celdaBloqueada(x, y);
                    }
                });
                cell.showText(true);
            }
            i++;
        }
    }

    /**
     * Devuelve la celda que m??s visibles tenga del grupo
     *
     * @param blues Grupo de azules que se va a mirar
     */
    Celda moreFreqCell(List<Celda> blues) {
        Celda c = blues.get(0);
        for (int i = 0; i < blues.size(); i++) {
            Celda newC = blues.get(i);
            if (newC.getValue() > c.getValue()) {
                c = newC;
            }
        }

        return c;
    }

    /**
     * Comprueba si la celda puede ser vista por otra celda
     * bloqueada de su mismo grupo de azules
     *
     * @param blues Grupo de azules de la celda
     * @param cell  Celda que se quiere comprobar
     * @return Devuelve false si ninguna celda lock est?? viendo a cell
     */
    boolean canSeeLockCell(List<Celda> blues, Celda cell) {
        for (int i = 0; i < blues.size(); i++) {
            Celda c = blues.get(i);
            if (c.isLock() && c.getVisibles().contains(cell)) {
                return true;
            }
        }
        return false;
    }

    //----------------------------ROJAS-------------------------------//

    /**
     * Selecciona las celdas rojas m??nimas para que
     * el puzzle sea soluci??n ??nica
     */
    private void selectReds() {
        // Asignaci??n de rojas fijas aleatorias
        int i = 0;
        int j = 0;
        while (i < _tam) {
            Celda c = _celdas[i][j];
            if (c.getTipoCelda() == TipoCelda.ROJO) {
                // 1. Conversi??n de roja a azul
                c.setLock(false);
                c.setTipoCelda(TipoCelda.AZUL);
                // 2. ??Tiene soluci??n o es diferente a la original?
                boolean isNewSol = isNewSolution();
                // Si - Entonces se bloquea, es obligatoria
                if (isNewSol) {
                    c.setLock(true);
                    c.setTipoCelda(TipoCelda.ROJO);
                    _celdas[i][j].setCellCallback(new CellCallback() {
                        @Override
                        public void doSomething(int x, int y, GameState gm) {
                            celdaBloqueada(x, y);
                        }
                    });
                }
                // No - Entonces se puede poner gris
                else {
                    _celdasGrises++;
                    c.setTipoCelda(TipoCelda.GRIS);
                }

                c.showText(false, 0);
            }

            j++;
            if (j >= _tam) {
                j = 0;
                i++;
            }
        }
    }

    /**
     * Soluciona el tablero para comprobar que sea soluci??n ??nica.
     * A partir del tablero generado con backtracking se han seleccionado
     * las azules obligatorias. Luego se han puesto en gris las azules
     * restantes y ahora, se busca soluci??n poniendo una roja como azul
     * siguiendo las pistas
     *
     * @return Devuelve si encontr?? soluci??n o no
     */
    boolean isNewSolution() {
        // 1. Copia del tablero para resolverlo
        Celda[][] tabAux = new Celda[_tam][_tam];
        for (int i = 0; i < _tam; ++i) {
            for (int j = 0; j < _tam; ++j) {
                Celda c = _celdas[i][j];
                int[] indx = {i, j};
                int[] pos = {0, 0};
                tabAux[i][j] = new Celda(c.getTipoCelda(), null, 0,
                        c.getValue(), pos, 0, indx);

                if (c.getTipoCelda() == TipoCelda.AZUL && !c.isLock()) {
                    tabAux[i][j].setLock(false);
                }
            }
        }

        // 2. Procesa pista
        Pista p;
        int i = 0;
        int j = 0;

        while (i < _tam) {
            p = _tab.buscaPista(i, j, tabAux);

            // 3. Clasificaci??n de la pista
            switch (p.getTipo()) {
                case CERRAR_CASILLA:
                    closeCell(tabAux, i, j);
                    break;
                case DEBE_SER_PARED: {
                    int[] pos = _tab.getIndRelPista();
                    tabAux[pos[0]][pos[1]].setTipoCelda(TipoCelda.ROJO);
                    break;
                }
                case DEBE_SER_AZUL:
                case AZULES_INALCANZABLES:
                    putBlueInDir(tabAux, i, j);
                    break;
                case UNA_DIRECCION:
                    closeOneDir(tabAux, i, j);
                    break;
                case AZULES_ALCANZABLES:
                    allBlues(tabAux, i, j);
                    break;
                case AZUL_ES_ROJA:
                case GRIS_ES_ROJA:
                    tabAux[i][j].setTipoCelda(TipoCelda.ROJO);
                    break;
                // PISTAS DE FALLO
                case DEMASIADAS_AZULES:
                case INSUFICIENTES_AZULES:
                    return false;
            }

            // 4. Si no encontramos pista, seguimos buscando
            if (p.getTipo() == TipoPista.NONE) {
                j++;
                if (j >= _tam) {
                    j = 0;
                    i++;
                }
            }
            // Si hay pista reseteamos el indice
            else {
                i = 0;
                j = 0;
            }
        }

        if(i >= _tam)
        {
            return true;
        }

        // 5. ??La souci??n nueva es igual a la original?
        for (int n = 0; n < _tam; n++) {
            for (int m = 0; m < _tam; m++) {
                if (tabAux[n][m].getTipoCelda() != _solution[n][m].getTipoCelda()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Aplica la pista Cerrar Casilla a la celda correspondiente
     *
     * @param tabAux Tablero auxiliar de celdas
     * @param i      Fila de la celda
     * @param j      Columna de la celda
     */
    private void closeCell(Celda[][] tabAux, int i, int j) {
        int nxt = 1;
        Celda c = tabAux[i][j];
        for (int[] d : _dirs) {
            int row = c.getRow() + d[0] * nxt;
            int col = c.getCol() + d[1] * nxt;

            while (row >= 0 && row < _tam && col < _tam && col >= 0) {
                if (tabAux[row][col].getTipoCelda() != TipoCelda.AZUL) {
                    if (tabAux[row][col].getTipoCelda() == TipoCelda.GRIS) {
                        tabAux[row][col].setTipoCelda(TipoCelda.ROJO);
                    }
                    break;
                }

                // Siguiente
                nxt++;
                row = c.getRow() + d[0] * nxt;
                col = c.getCol() + d[1] * nxt;
            }

            nxt = 1;
        }
    }

    /**
     * Aplica la pista 9 del enunciado. Si la suma alcanzable es el valor
     * de la celda, se ponen todas las contiguas en azul
     *
     * @param tabAux Tablero auxiliar de celdas
     * @param i      Fila de la celda
     * @param j      Columna de la celda
     */
    private void allBlues(Celda[][] tabAux, int i, int j) {
        int nxt = 1;
        Celda c = tabAux[i][j];
        for (int[] d : _dirs) {
            int row = c.getRow() + d[0] * nxt;
            int col = c.getCol() + d[1] * nxt;

            while (row >= 0 && row < _tam && col < _tam && col >= 0 &&
                    tabAux[row][col].getTipoCelda() != TipoCelda.ROJO) {

                if (tabAux[row][col].getTipoCelda() == TipoCelda.GRIS) {
                    tabAux[row][col].setTipoCelda(TipoCelda.AZUL);
                }

                // Siguiente
                nxt++;
                row = c.getRow() + d[0] * nxt;
                col = c.getCol() + d[1] * nxt;
            }

            nxt = 1;
        }
    }

    /**
     * Busca en una direcci??n la primera gris que encuentre y la pone en azul
     *
     * @param tabAux Tablero auxiliar de celdas
     * @param i      Fila de la celda
     * @param j      Columna de la celda
     */
    private void putBlueInDir(Celda[][] tabAux, int i, int j) {
        // Direcci??n donde poner la azul
        int[] dir = _tab.getIndRelPista();
        Celda c = tabAux[i][j];
        int next = 1;
        int row = c.getRow() + dir[0] * next;
        int col = c.getCol() + dir[1] * next;
        Celda cAux = tabAux[row][col];
        while (cAux.getTipoCelda() != TipoCelda.GRIS) {
            next++;
            row = c.getRow() + dir[0] * next;
            col = c.getCol() + dir[1] * next;
            cAux = tabAux[row][col];
        }

        cAux.setTipoCelda(TipoCelda.AZUL);
    }


    /**
     * Rellena la direcci??n ??nica de la celda con azules
     * hasta que se alcancen todas las visibles posibles
     * o se encuentre una roja
     *
     * @param tabAux Tablero auxiliar de celdas
     * @param i      Fila de la celda
     * @param j      Columna de la celda
     */
    private void closeOneDir(Celda[][] tabAux, int i, int j) {
        // Direcci??n que se quiere rellenar
        int[] dir = _tab.getIndRelPista();
        // Numero de visibles de la celda
        int _numVisibles = _tab.getNumVisibles();
        Celda c = tabAux[i][j];
        // Visibles que quedan por rellenar
        int leftVisibles = c.getValue() - _numVisibles;
        // Numero de celdas cambiadas
        int changed = 0;
        // Siguiente celda para mirar
        int next = 1;
        int row = c.getRow() + dir[0] * next;
        int col = c.getCol() + dir[1] * next;
        Celda cAux = tabAux[row][col];
        while (changed < leftVisibles) {
            if (cAux.getTipoCelda() == TipoCelda.GRIS) {
                cAux.setTipoCelda(TipoCelda.AZUL);
                changed++;
            }
            else if(cAux.getTipoCelda() == TipoCelda.ROJO){
                break;
            }
            next++;
            row = c.getRow() + dir[0] * next;
            col = c.getCol() + dir[1] * next;
            if(row < 0 || row >= _tam || col < 0 || col >= _tam){
                break;
            }

            cAux = tabAux[row][col];
        }
    }

//---------------------------------------CALLBACK-------------------------------------------------//

    /**
     * Callback para cambiar el color de la celda correspondiente,
     * actualizar el contador de grises del tablero, a??adir movimientos
     * a la pila de movimientos y para detener, si es posible, el temporizador
     * activo del GameState
     *
     * @param i:  Fila de la celda
     * @param j:  Columna de la celda
     * @param gm: Referencia al GameState
     */
    private void changeCellColor(int i, int j, GameState gm) {

        _tab.changeCellColor(i, j);

        // Se a??ade a la pila de movimientos
        int[] coors = {i, j};

        //Si pasamos de gris a azul tenemos una gris menos
        if (_tab.getCelda(i, j).getTipoCelda() == TipoCelda.AZUL)
            _tab.addGreyCell(-1);

        //Si pasamos de roja a gris tenemos gris de nuevo
        if (_tab.getCelda(i, j).getTipoCelda() == TipoCelda.GRIS)
            _tab.addGreyCell(1);

        gm.resetTitleText();
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

//------------------------------------------------------------------------------------------------//

    /**
     * Calcula el n??mero m??ximo de azules y rojas que habr?? en el tablero al completarlo
     */
    private void calcularCeldas() {
        Random rn = new Random();
        int result = rn.nextInt(_maxBlues - _minBlues) + _minBlues;
        _celdasAzules = Math.round(_tam * _tam * (float) result / 100);
        _celdasRojas = _tam * _tam - _celdasAzules;
        _blueLeft = _celdasAzules;
        _redLeft = _celdasRojas;
    }

    /**
     * Cuenta el n??mero de visibles de una celda
     *
     * @param c Celda que se quiere mirar
     * @return N??mero de visibles
     */
    private int numVisibles(Celda c) {
        int n = 0;
        for (int[] d : _dirs) {
            n += visiblesOneDirection(c, d);
        }
        return n;
    }

    /**
     * N??mero de celdas visibles en una direcci??n
     *
     * @param initC Celda desde la que se mira
     * @param d     Direcci??n en la que se mira
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

//------------------------------------------------------------------------------------------------//

    /**
     * Referencia a la clase gestora del tablero
     */
    Tablero _tab;
    /**
     * Tama??o del tablero
     */
    int _tam;
    /**
     * Array de celdas del tablero
     */
    Celda[][] _celdas;
    /**
     * Array de la soluci??n del tablero
     */
    Celda[][] _solution;
    /**
     * Celdas grises que posee el tablero.
     * Se usa para calcular el porcentaje
     * de tablero completado
     */
    int _celdasGrises = 0;
    /**
     * N??mero de azules que puede tener el tablero
     */
    int _celdasAzules;
    /**
     * N??mero de rojas que puede tener el tablero
     */
    int _celdasRojas;
    /**
     * Porcentaje m??nimo de azules que habr?? con el
     * tablero completo
     */
    final int _minBlues = 50;
    /**
     * Porcentaje m??ximo de azules que habr?? con el
     * tablero completo
     */
    final int _maxBlues = 65;
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
