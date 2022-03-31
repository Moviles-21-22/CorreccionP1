package es.ucm.arblemar.gamelogic.tablero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.gamelogic.CellCallback;
import es.ucm.arblemar.gamelogic.states.GameState;

public class CreaTablero {
    public CreaTablero(Tablero t, int tam) {
        _tab = t;
        _tam = tam;
        _celdas = new Celda[_tam][_tam];

        // Lista de direcciones
        _dirs = new ArrayList<>();
        int[] dir = new int[2];
        // Arriba
        dir[0] = -1;
        dir[1] = 0;
        _dirs.add(dir);
        // Abajo
        dir[0] = 1;
        _dirs.add(dir);
        // Izquierda
        dir[0] = 0;
        dir[1] = -1;
        _dirs.add(dir);
        // Derecha
        dir[1] = 1;
        _dirs.add(dir);
    }

    /**
     * Devuelve el tablero generado
     */
    public Celda[][] getCeldas() {
        return _celdas;
    }

    /**
     * Tablero de 4x4 del enunciado
     */
    public int testTab(final int[] tabPos, final float cSize, final Font cFont,
                       final int tamFont, final float diam, final GameState gm) {
        _celdas = new Celda[_tam][_tam];

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
                        public void doSomething(int x, int y) {
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
                        public void doSomething(int x, int y) {
                            //System.out.println("Animación Roja grande-pequeño.\nAlternar candado Rojas");
                            celdaBloqueada(x, y);
                        }
                    });
                } else {
                    if ((i == 0 && j == 0) || (i == 2 && j == 1)) {
                        _celdas[i][j] = new Celda(TipoCelda.AZUL, cFont, tamFont,
                                1, pos, diam, ind);
                        _celdas[i][j].setCellCallback(new CellCallback() {
                            @Override
                            public void doSomething(int x, int y) {
                                //System.out.println("\nAnimación Azul grande-pequeño.\nAlternar candado Rojas");
                                celdaBloqueada(x, y);
                            }
                        });
                    } else {
                        _celdas[i][j] = new Celda(TipoCelda.AZUL, cFont, tamFont,
                                2, pos, diam, ind);
                        _celdas[i][j].setCellCallback(new CellCallback() {
                            @Override
                            public void doSomething(int x, int y) {
                                //System.out.println("\nAnimación Azul grande-pequeño.\nAlternar candado Rojas");
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
     * Genera un tablero random a partir siguiendo una serie de restricciones
     */
    public Celda[][] randomTab(int[] tabPos, float cSize, Font cFont, int tamFont, float diam) {
        Random r = new Random();

        initRedTab(tabPos, cSize, cFont, tamFont, diam);

        while (_celdasAzules > 0) {
            int n = r.nextInt(_tam);
            int m = r.nextInt(_tam);
            Celda originalCell = _celdas[n][m];

            if (!esPosibleAzul(originalCell)) {
                continue;
            }
            // Número de visibles de la celda
            int numVisibles = r.nextInt(_tam) + 1;
            int initNumVisibles = numVisibles;
            int j = 0;

            // Intenta poner en azul celdas aleatorias de una dirección
            while (numVisibles > 0 && j < 4 && _celdasAzules > 0) {
                int totalNeighsInDir;
                if (numVisibles > 1) {
                    totalNeighsInDir = r.nextInt(numVisibles - 1) + 1;
                } else totalNeighsInDir = 1;
                int[] dir = _dirs.get(j);
                int step = 1;
                while (step <= totalNeighsInDir) {
                    Celda c = _celdas[n + (dir[0] * step)][m + (dir[1] * step)];

                    // Si se puede poner en azul, se hace
                    if (c != null && esPosibleAzul(c)) {
                        step++;
                        _celdasAzules--;
                    }
                    // Si no, entonces esta dirección deja de ser una opción
                    else break;
                }
                j++;
                numVisibles -= (step - 1);
            }

            // Si no se han podido añadir azules, se resetea la celda
            if (initNumVisibles == numVisibles)
                originalCell.setTipoCelda(TipoCelda.ROJO);
        }

        // Después de asignar nuevos valores para cada celda, hay que actualizar
        //for (int i = 0; i < _tam; i++) {
        //    for (int j = 0; j < _tam; j++) {
        //        Celda c = _celdas[i][j];
        //        if (c.getTipoCelda() != TipoCelda.ROJO.Red) {
        //            // Se calculan los vecinos de una celda
        //            int n = solver.visibleNeighbours(c);
        //            c.setNeigh(n);
        //        }
        //    }
        //}

        // Finalmente se ponen en gris aquellas celdas visibles de las azules
        // and we need to reset the visible cells to a grey state for the next step
        //for (int i = 0; i < _tam; i++) {
        //    for (int j = 0; j < _tam; j++) {
        //        solver._visibleCells.add(c);
        //        solver._freeCells++;
        //        _celdas[i][j].setTipoCelda(TipoCelda.GRIS);
        //        _celdas[i][j].setLock(false);
        //        _celdas[i][j].setCellCallback(new CellCallback() {
        //            @Override
        //            public void doSomething(int x, int y) {
        //                Pista pista = _gm.getPista();
        //                if (pista.getTipo() != TipoPista.NONE) {
        //                    pista.setTipo(TipoPista.NONE);
        //                }
        //                changeCellColor(x, y);
        //            }
        //        }, i, j);
        //    }
        //}

        return _celdas;
    }

    /**
     * Calcula el número de celdas que se pondrán en el tablero de cada tipo
     */
    public int calcularCeldas() {
        Random rn = new Random();
        // Se escoge un porcentaje del tablero aleatorio que puede quedar resuleto mediante
        // la generación de azules y rojas
        int porc = rn.nextInt(_maxSolved - _minSolved) + _minSolved;
        // Porcentaje azules y rojas
        int azules = rn.nextInt(porc / 2) + porc / 2;
        int rojas = porc - azules;
        // Número de azules y rojas tel tablero
        _celdasAzules = Math.round(_tam * _tam * ((float) azules) / 100);
        _celdasRojas = Math.round(_tam * _tam * ((float) rojas) / 100);
        // En caso de que no se generen rojas, forzamos que salga una a costa de otra azul
        if (_celdasRojas == 0) {
            _celdasRojas = 1;
            _celdasAzules--;
        }
        _celdasGrises = _tam * _tam - (_celdasAzules + _celdasRojas);

        //System.out.println("----------");
        //System.out.println("PORC: " + porc);
        //System.out.println("Azules: " + _celdasAzules);
        //System.out.println("Rojas: " + _celdasRojas);
        //System.out.println("Grises: " + _celdasGrises);
        //System.out.println("----------");

        return _celdasGrises;
    }

    public Celda[][] generateTab() {

//        if (!isSolution(solver)) {
//            // if its not able, load it from file
//            solver.reset();
//        }
//
//        finishGeneration(solver);

        return _celdas;
    }

    /**
     * Checks if a puzzle can be solved, following the tips a player would receive
     * @param solver solver the solver to test
     * @return whether the grid corresponding to the solver is solvable or no
     */
//    public boolean isSolution()
//    {
//        Random r = new Random();
//        boolean solved = false;
//
//        // while the game isn't solved yet
//        while(!solved){
//            int i= 0;
//            int j = 0;
//            Pista p = new Pista();
//            while(i < _tam && p.getTipo() == TipoPista.NONE) {
//                p = _tab.procesaPista(i, j);
//                // see if there is any clue remaining
//                if (p.getTipo() == TipoPista.NONE) {
//                    // if there is not any new clue, add a new grey cell as locked
//                    int rand = r.nextInt(solver._visibleCells.size());
//                    Celda c = solver._visibleCells.get(rand);
//                    c.setLock(true);
//                    if (c.getNeigh() > 0)
//                        c.setTipoCelda(TipoCelda.AZUL);
//                    else c.setTipoCelda(TipoCelda.ROJO);
//                } else {
//                    // if there are still clues, keep following them until the game is over or there isn't any
//                    int x = p.correctState().col();
//                    int y = clue.correctState().row();
//                    Cell c = solver._grid.getCell(x, y);
//                    if (!c.isLocked()) {
//                        solver._visibleCells.remove(c);
//                        c.setState(clue.correctState().getState());
//                    } else {
//                        // if the clue returns a locked cell to edit
//                        System.err.println("Clue should not be editable: ");
//                        System.err.println(c.col() + " " + c.row());
//                        System.err.println(clue.message());
//                    }
//
//                }
//
//                j++;
//                if(j == _tam){
//                    j = 0;
//                    i++;
//                }
//            }
//
//            solved = solver._visibleCells.size() == 0;
//        }
//
//        Clue c = solver.getClue();
//        return c == null || c.correctState() == null;
//    }

//------------------------------------------------------------------------------------------------//

    /**
     * Inicializa un tablero con celdas rojas
     *
     * @param cSize   Tamaño de cada celda en la ventana
     * @param cFont   Tipo de fuente usada en las celdas
     * @param tamFont Tamaño de la fuente
     * @param tabPos  Posición inicial del tablero
     * @param diam    Diámetro de cada celda
     */
    private void initRedTab(int[] tabPos, float cSize, Font cFont, int tamFont, float diam) {
        int[] pos;
        pos = new int[2];
        for (int i = 0; i < _tam; i++) {
            pos[1] = (int) (tabPos[1] + (cSize * i) + (cSize * 0.1));
            for (int j = 0; j < _tam; j++) {
                pos[0] = (int) (tabPos[0] + (cSize * j) + (cSize * 0.1));
                int[] ind = new int[2];
                ind[0] = i;
                ind[1] = j;
                _celdas[i][j] = new Celda(TipoCelda.ROJO, cFont, tamFont,
                        0, pos, diam, ind);
                _celdas[i][j].setCellCallback(new CellCallback() {
                    @Override
                    public void doSomething(int x, int y) {
                        celdaBloqueada(x, y);
                    }
                });
            }
        }
    }

    /**
     * Intenta asignar la celda como azul
     *
     * @param c La celda que se va a probar
     * @return Devuelve si es posible o no
     */
    private boolean esPosibleAzul(Celda c) {
        c.setTipoCelda(TipoCelda.AZUL);
        c.setCellCallback(new CellCallback() {
            @Override
            public void doSomething(int x, int y) {
                celdaBloqueada(x, y);
            }
        });
        int value;
        for (int i = 0; i < _dirs.size(); i++) {
            // and search if adding to blue the cell, will exceed the neighbours of one of its neighbours
            for (int j = 0; j < _dirs.size(); j++) {
                int row = c.getRow() + _dirs.get(i)[0] * j;
                int col = c.getCol() + _dirs.get(j)[1] * j;
                if (row >= _tam || row < 0 || col >= _tam || col < 0) break;

                Celda ady = _celdas[row][col];
                // Si se excede el número de vecinos, se resetea
                value = numVisibles(ady);
                if (value > _tam) {
                    c.setTipoCelda(TipoCelda.ROJO);
                    c.setCellCallback(new CellCallback() {
                        @Override
                        public void doSomething(int x, int y) {
                            celdaBloqueada(x, y);
                        }
                    });
                    return false;
                } else {
                    c.setValue(value);
                }
            }
        }

        return true;
    }

    /**
     * Cambia el color de la celda correspondiente
     * Actualiza el contador de grises que tenemos en el tablero
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     */
    private void changeCellColor(int i, int j, GameState gm) {

        _tab.changeCellColor(i, j);

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

        gm.stopTimer();
    }

    /**
     * Hace vibrar a la celda pulsada y muestra/oculta los candados
     * en las celdas bloqueadas rojas
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     */
    private void celdaBloqueada(int i, int j) {
        _tab.getCelda(i, j).activeAnim();

        //Para activar o desactivar los candados
        for (int x = 0; x < _tam; x++) {
            for (int y = 0; y < _tam; y++) {
                _tab.getCelda(i, j).alternaCandado();
            }
        }
    }

//------------------------------------------------------------------------------------------------//

    /**
     * Cuenta el número de visibles de una celda
     *
     * @param c Celda que se quiere mirar
     * @return Número de visibles
     */
    public int numVisibles(Celda c) {
        int n = 0;
        for (int[] d : _dirs) {
            n += visiblesOneDirection(c, d);
        }
        return n;
    }

    /**
     * Número de celdas visibles en una dirección
     *
     * @param c Celda desde la que se mira
     * @param d Dirección en la que se mira
     * @return Cantidad de visibles
     */
    private int visiblesOneDirection(Celda c, int[] d) {
        int visibles = -1;
        int i = 1;
        while (c.getTipoCelda() == TipoCelda.AZUL) {
            int row = c.getRow() + d[0] * i;
            int col = c.getCol() + d[1] * i;
            if (row >= _tam || row < 0 || col >= _tam || col < 0) break;

            c = _celdas[row][col];
            visibles++;
            i++;
        }
        return visibles;
    }

//------------------------------------------------------------------------------------------------//

    Tablero _tab;
    int _tam;
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
     * Porcentaje mínimo del tablero que puede
     * quedar resuleto al generarlo
     */
    final int _minSolved = 30;
    /**
     * Porcentaje máximo del tablero que puede
     * quedar resuleto al generarlo
     */
    final int _maxSolved = 45;
    /**
     * Lista de direcciones
     */
    public List<int[]> _dirs;
}
