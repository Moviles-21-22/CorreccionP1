package es.ucm.arblemar.gamelogic.tablero;

import es.ucm.arblemar.engine.Graphics;

public class Tablero {
    /**
     * Constructora del tablero mediante un tablero externo
     *
     * @param c: tablero
     */
    public Tablero(Celda[][] c) {
        _celdas = c;
        _tam = _celdas.length;
    }

    /**
     * Constructora por defecto
     */
    public Tablero() {

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
    public void handleInput(int x, int y) {
        findingCelda:
        for (int n = 0; n < _tam; n++) {
            for (int m = 0; m < _tam; m++) {
                if (_celdas[n][m].isClicked(x, y)) {
                    _celdas[n][m].runCallBack();
                    break findingCelda;
                }
            }
        }
    }

    /**
     * Cambia el color de la celda correspondiente
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     */
    public void changeCellColor(int i, int j) {
        _celdas[i][j].setColor();
    }

    /**
     * Hace vibrar a la celda pulsada y muestra/oculta los candados
     * en las celdas bloqueadas rojas
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     */
    public void celdaBloqueada(int i, int j) {
        _celdas[i][j].activeAnim();

        //Para activar o desactivar los candados
        for (int x = 0; x < _tam; x++) {
            for (int y = 0; y < _tam; y++) {
                _celdas[x][y].alternaCandado();
            }
        }
    }

//-----------------------------------BÚSQUEDA-PISTAS----------------------------------------------//

    /**
     * Busca la pista que pueda existir en la casilla seleccionada
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     * @return Devuelve la pista generada. Puede ser NONE.
     */
    public Pista procesaPista(int i, int j) {
        Pista pista = new Pista();
        pista.setPos(_celdas[i][j].getPos());

        if (_celdas[i][j].isLock() && _celdas[i][j].getTipoCelda() == TipoCelda.AZUL) {
            pistaCeldaAzul(i, j, pista);
        } else if (!_celdas[i][j].isLock() && (_celdas[i][j].getTipoCelda() == TipoCelda.GRIS ||
                _celdas[i][j].getTipoCelda() == TipoCelda.AZUL)) {
            celdaSola(i, j, pista);
        }

        return pista;
    }

    /**
     * Procesa las pistas que estén asociadas a una celda azul
     *
     * @param i: Fila de la celda
     * @param j: Columna de la celda
     */
    private void pistaCeldaAzul(int i, int j, Pista pista) {
        _numVisibles = 0;
        boolean yaCerrada;  //Para medir si la celda completa ya esta cerrada
        //-----------------DEMASIADAS-AZULES-----------------------//
        yaCerrada = demasiadasAzules(i, j, pista);
        if (pista.getTipo() != TipoPista.NONE) return;

        //-----------------INSUFICIENTES-AZULES-----------------------//
        if (yaCerrada) {    //Pueden estar las azules justas o faltar
            if (_numVisibles < _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.INSUFICIENTES_AZULES);
                pista.setPos(_celdas[i][j].getPos());
            }
            //En caso de estar la celda completa y cerrada tb saldría
            return;
        }

        //-----------------CERRAR-CASILLA-----------------------//
        if (_numVisibles == _celdas[i][j].getValue()) {
            pista.setTipo(TipoPista.CERRAR_CASILLA);
            return;
        }

        //-----------------DEBE-SER-PARED-----------------------//
        ponerPared(i, j, pista);
        if (pista.getTipo() != TipoPista.NONE) return;

        //-----------------DEBE-SER-AZUL-------------------------//
        ponerAzul(i, j, pista);
    }

    /**
     * Procesa la pista sobre demasiadas azules. Es la 4 del enunciado
     *
     * @param i:     Fila de la celda
     * @param j:     Columna de la celda
     * @param pista: variable de la pista que se va a modificar
     * @return Devuelve true si la celda está cerrada
     *                  false en caso contrario o si ha sido pista
     */
    private boolean demasiadasAzules(int i, int j, Pista pista) {
        boolean yaCerrada = true;

        // i/n --> FILAS // j/m --> COLUMNAS
        //-----------------ABAJO----------------//
        for (int n = i + 1; n < _tam; ++n) {
            if (_celdas[n][j].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
                // En cuanto haya una gris, se deja de contar
            else if (_celdas[n][j].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPos(_celdas[i][j].getPos());
                return false;
            }
        }
        //-----------------ARRIBA----------------//
        for (int n = i - 1; n >= 0; --n) {
            if (_celdas[n][j].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
            else if (_celdas[n][j].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPos(_celdas[i][j].getPos());
                return false;
            }
        }
        //-----------------DERECHA----------------//
        for (int m = j + 1; m < _tam; ++m) {
            if (_celdas[i][m].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
            else if (_celdas[i][m].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPos(_celdas[i][j].getPos());
                return false;
            }
        }
        //-----------------IZQUIERDA----------------//
        for (int m = j - 1; m >= 0; --m) {
            if (_celdas[i][m].getTipoCelda() == TipoCelda.AZUL) _numVisibles++;
            else if (_celdas[i][m].getTipoCelda() == TipoCelda.GRIS) {
                yaCerrada = false;
                break;
            } else break;
            // Si se excede el valor de la celda, hay demasiadas azules
            if (_numVisibles > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                pista.setPos(_celdas[i][j].getPos());
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
    private void ponerPared(int i, int j, Pista pista) {
        boolean gris = false;
        int nuevoVal = _numVisibles;

        // i/n --> FILAS // j/m --> COLUMNAS
        //-----------------ABAJO----------------//
        for (int n = i + 1; n < _tam; ++n) {
            if (!gris && _celdas[n][j].getTipoCelda() == TipoCelda.GRIS) {
                gris = true;
                nuevoVal++;
            } else if (gris && _celdas[n][j].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
            else if (!gris && _celdas[n][j].getTipoCelda() == TipoCelda.AZUL) continue;
            else break;
            if (nuevoVal > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEBE_SER_PARED);
                pista.setPos(_celdas[i][j].getPos());
                return;
            }
        }
        gris = false;
        nuevoVal = _numVisibles;

        //-----------------ARRIBA----------------//
        for (int n = i - 1; n >= 0; --n) {
            if (!gris && _celdas[n][j].getTipoCelda() == TipoCelda.GRIS) {
                gris = true;
                nuevoVal++;
            } else if (gris && _celdas[n][j].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
            else if (!gris && _celdas[n][j].getTipoCelda() == TipoCelda.AZUL) continue;
            else break;
            if (nuevoVal > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEBE_SER_PARED);
                pista.setPos(_celdas[i][j].getPos());
                return;
            }
        }
        gris = false;
        nuevoVal = _numVisibles;

        //-----------------DERECHA----------------//
        for (int m = j + 1; m < _tam; ++m) {
            if (!gris && _celdas[i][m].getTipoCelda() == TipoCelda.GRIS) {
                gris = true;
                nuevoVal++;
            } else if (gris && _celdas[i][m].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
            else if (!gris && _celdas[i][m].getTipoCelda() == TipoCelda.AZUL) continue;
            else break;
            if (nuevoVal > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEBE_SER_PARED);
                pista.setPos(_celdas[i][j].getPos());
                return;
            }
        }
        gris = false;
        nuevoVal = _numVisibles;

        //-----------------IZQUIERDA----------------//
        for (int m = j - 1; m >= 0; --m) {
            if (!gris && _celdas[i][m].getTipoCelda() == TipoCelda.GRIS) {
                gris = true;
                nuevoVal++;
            } else if (gris && _celdas[i][m].getTipoCelda() == TipoCelda.AZUL) nuevoVal++;
            else if (!gris && _celdas[i][m].getTipoCelda() == TipoCelda.AZUL) continue;
            else break;
            if (nuevoVal > _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.DEBE_SER_PARED);
                pista.setPos(_celdas[i][j].getPos());
                return;
            }
        }
    }

    /**
     * Procesa la pista sobre si hay que poner una celda azul. Es la 3 del enunciado
     *
     * @param i:     Fila de la celda
     * @param j:     Columna de la celda
     * @param pista: Variable de la pista que se va a modificar
     */
    private void ponerAzul(int i, int j, Pista pista) {
        int[] valLinea = new int[4];

        // i/n --> FILAS // j/m --> COLUMNAS
        //-----------------ABAJO----------------//
        for (int n = i + 1; n < _tam; ++n) {
            if (_celdas[n][j].getTipoCelda() != TipoCelda.ROJO &&
                    valLinea[0] < _celdas[i][j].getValue())
                valLinea[0]++;
            else break;
        }
        //-----------------ARRIBA----------------//
        for (int n = i - 1; n >= 0; --n) {
            if (_celdas[n][j].getTipoCelda() != TipoCelda.ROJO &&
                    valLinea[1] < _celdas[i][j].getValue())
                valLinea[1]++;
            else break;
        }
        //-----------------DERECHA----------------//
        for (int m = j + 1; m < _tam; ++m) {
            if (_celdas[i][m].getTipoCelda() != TipoCelda.ROJO &&
                    valLinea[2] < _celdas[i][j].getValue())
                valLinea[2]++;
            else break;
        }
        //-----------------IZQUIERDA----------------//
        for (int m = j - 1; m >= 0; --m) {
            if (_celdas[i][m].getTipoCelda() != TipoCelda.ROJO &&
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
            pista.setTipo(TipoPista.DEBE_SER_AZUL);
            pista.setPos(_celdas[i][j].getPos());
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
    private void celdaSola(int i, int j, Pista pista) {
        // Si se detecta que no está cerrada, entonces no es esta pista
        if ((i + 1 < _tam && _celdas[i + 1][j].getTipoCelda() != TipoCelda.ROJO)        // ABAJO
                || (i - 1 >= 0 && _celdas[i - 1][j].getTipoCelda() != TipoCelda.ROJO)   // ARRIBA
                || (j + 1 < _tam && _celdas[i][j + 1].getTipoCelda() != TipoCelda.ROJO) // DERECHA
                || (j - 1 >= 0 && _celdas[i][j - 1].getTipoCelda() != TipoCelda.ROJO))  // IZQUIERDA
        {
            return;
        }

        TipoPista tipo = _celdas[i][j].getTipoCelda() == TipoCelda.GRIS ?
                TipoPista.GRIS_ES_ROJA : TipoPista.AZUL_ES_ROJA;
        pista.setTipo(tipo);
        pista.setPos(_celdas[i][j].getPos());
    }
//---------------------------------------------------------------------------------------//

    // ATRIBUTOS DEL TABLERO
    /**
     * Array que contiene las celdas
     */
    Celda[][] _celdas;
    int _tam;
    /**
     * Auxiliar para contar las celdas
     * visibles de una celda
     */
    int _numVisibles = 0;
}
