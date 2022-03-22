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

            int val = 0;
            boolean yaCerrada = true;  //Para medir si la celda completa ya esta cerrada
            //-----------------BÚSQUEDA-DEMASIADAS-AZULES-----------------------//
            // i/n --> FILAS // j/m --> COLUMNAS
            //-----------------ABAJO----------------//
            for (int n = i + 1; n < _tam; ++n) {
                if (_celdas[n][j].getTipoCelda() == TipoCelda.AZUL) val++;
                else if (_celdas[n][j].getTipoCelda() == TipoCelda.GRIS) {
                    yaCerrada = false;
                    break;
                } else break;
                // Si se excede el valor de la celda, hay demasiadas azules
                if (val > _celdas[i][j].getValue()) {
                    pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                    return pista;
                }
            }
            //-----------------ARRIBA----------------//
            for (int n = i - 1; n >= 0; --n) {
                if (_celdas[n][j].getTipoCelda() == TipoCelda.AZUL) val++;
                else if (_celdas[n][j].getTipoCelda() == TipoCelda.GRIS) {
                    yaCerrada = false;
                    break;
                } else break;
                // Si se excede el valor de la celda, hay demasiadas azules
                if (val > _celdas[i][j].getValue()) {
                    pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                    return pista;
                }
            }
            //-----------------DERECHA----------------//
            for (int m = j + 1; m < _tam; ++m) {
                if (_celdas[i][m].getTipoCelda() == TipoCelda.AZUL) val++;
                else if (_celdas[i][m].getTipoCelda() == TipoCelda.GRIS) {
                    yaCerrada = false;
                    break;
                } else break;
                // Si se excede el valor de la celda, hay demasiadas azules
                if (val > _celdas[i][j].getValue()) {
                    pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                    return pista;
                }
            }
            //-----------------IZQUIERDA----------------//
            for (int m = j - 1; m >= 0; --m) {
                if (_celdas[i][m].getTipoCelda() == TipoCelda.AZUL) val++;
                else if (_celdas[i][m].getTipoCelda() == TipoCelda.GRIS) {
                    yaCerrada = false;
                    break;
                } else break;
                // Si se excede el valor de la celda, hay demasiadas azules
                if (val > _celdas[i][j].getValue()) {
                    pista.setTipo(TipoPista.DEMASIADAS_AZULES);
                    return pista;
                }
            }

            //-----------------INSUFICIENTES-AZULES-----------------------//
            if(yaCerrada){
                if(val < _celdas[i][j].getValue()){
                    pista.setTipo(TipoPista.INSUFICIENTES_AZULES);
                }

                return pista;
            }

            //-----------------PRIMERA-O-SEGUNDA-PISTA-----------------------//
            if (val == _celdas[i][j].getValue()) {
                pista.setTipo(TipoPista.CERRAR_CASILLA);
                return pista;
            }
            else {      //val < getValue    ->    Puede ser segunda pista
                boolean gris = false;
                int nuevoVal = val;
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
                        return pista;
                    }
                }
                gris = false;
                nuevoVal = val;

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
                        return pista;
                    }
                }
                gris = false;
                nuevoVal = val;

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
                        return pista;
                    }
                }
                gris = false;
                nuevoVal = val;

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
                        return pista;
                    }
                }
            }

            //-----------------TERCERA-PISTA-------------------------//
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
                return pista;
            }
        }

        return pista;
    }

//---------------------------------------------------------------------------------------//
    // ATRIBUTOS DEL TABLERO
    /**
     * Array que contiene las celdas
     */
    Celda[][] _celdas;
    int _tam;
}
