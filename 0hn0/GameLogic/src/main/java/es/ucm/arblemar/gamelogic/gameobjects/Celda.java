package es.ucm.arblemar.gamelogic.gameobjects;

import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.gamelogic.ButtonCallback;
import es.ucm.arblemar.gamelogic.TipoCelda;

/**
 * Clase que representa a todas las celdas del juego. Dependiendo de su
 * color podrá tener unos comportamientos u otros.
 */
public class Celda {
    public Celda(TipoCelda tc, Font f, int tamFont, int val, int pos [],
                 float diam, int ind []) {
        _tipoCelda = tc;
        // La celda se bloquea cuando es distinta de gris
        _lock = _tipoCelda != TipoCelda.GRIS ? true : false;
        _font = f;
        _tamF = tamFont;
        _value = val;
        _pos = new int[2];
        _pos[0] = pos[0];
        _pos[1] = pos[1];
        _index = ind;
        _diametro = diam;
        switch (_tipoCelda)
        {
            case GRIS: {
                _color = 0XEEEEEEFF;
                break;
            }
            case AZUL: {
                _color = 0x1CC0E0FF;
                break;
            }
            case ROJO: {
                _color = 0xFF384BFF;
                break;
            }
        }
    }

    public void render(es.ucm.arblemar.engine.Graphics g) {
        //Poner animación si la tiene
        //if (anSt == 1) {
        //    g.setColor(color);
        //    g.fillCircle(new Vector2(_pos._x - 5, _pos._y - 5), (int)_diametro + 10);
        //}

        g.setColor(_color);
        g.fillCircle(_pos[0], _pos[1], _diametro);
        g.setColor(0XFFFFFFFF);
        int x = (int)(_pos[0] + (_diametro / 3));
        int y = (int)(_pos[1] + (_diametro * 2 / 3));
        g.drawText(Integer.toString(_value), x, y, _font, _tamF);
    }

    /**
     * Pone en marcha la funcionalidad
     * asociada al callback
     */
    public void runCallBack(){
        if(_cb != null){
            _cb.doSomething();
        }
    };

    /**
     * Asocia un nuevo callback a la celda
     */
    public void setCallback(ButtonCallback cb)
    {
        _cb = cb;
    }

    /**
     * Determina si la celda ha sido "clickeada"
     */
    public boolean isClicked(int eventPos []){
        double xDiff = (_pos[0] + (_diametro / 2)) - eventPos[0];
        double yDiff = (_pos[1] + (_diametro / 2)) - eventPos[1];
        double distance = Math.sqrt((Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));
        return distance <= (int)(_diametro / 2);
    }

    /**
     * Devuelve si la celda está bloqueada o no
     */
    public boolean isLock(){
        return _lock;
    }

    /**
     * Devuelve el valor asociado al circulo
     */
    public int getValue(){
        return _value;
    }


    //public void setTypeColor(TipoCelda type) {
    //    TipoCelda antType = _tipoCelda;
    //    _tipoCelda = type;
    //    timer = 0;
    //    switch (type){
    //        case GRIS:
    //            targetColor = 0XEEEEEEFF;
    //            if (antType == TipoCelda.AZUL)
    //                newColor = 0xbee7eeFF;
    //            else if (antType == TipoCelda.ROJO)
    //                newColor = 0xf6c5caFF;
    //            break;
    //        case AZUL:
    //            targetColor = 0x1CC0E0FF;
    //            if (antType == TipoCelda.GRIS)
    //                newColor = 0xbee7eeFF;
    //            else if (antType == TipoCelda.ROJO)
    //                newColor = 0xc68b9dFF;
    //            break;
    //        case ROJO:
    //            targetColor = 0xFF384BFF;
    //            if (antType == TipoCelda.AZUL)
    //                newColor = 0xc68b9dFF;
    //            else if (antType == TipoCelda.GRIS)
    //                newColor = 0xf6c5caFF;
    //            break;
    //    }
    //}

    //public void set_color(int c) {
    //    _color = c;
    //}

    //public void setValue(int v){
    //    valor = v;
    //}

    //public void setAnimState(int s) {
    //    anSt = s;
    //}

    //public TipoCelda getTypeColor(){
    //    return _tipoCelda;
    //}

    //public int get_color(){
    //    return _color;
    //}

    //public Vector2 get_index(){
    //    return _index;
    //}


    // ATRIBUTOS DE LAS CELDAS
    //  Tipo de celda
    private TipoCelda _tipoCelda = TipoCelda.GRIS;
    // Determina si esta celda está bloqueada
    private boolean _lock = false;
    // Fuente del texto de la celda
    private Font _font;
    // Tamaño de la fuente
    private int _tamF;
    //  Valor de la celda
    private int _value;
    // Posición donde se va a renderizar la celda
    private int _pos [];
    //  Index dentro del tablero
    private int _index[];
    // Diametro del circulo
    private float _diametro;
    // Color inicial del circulo
    private int _color;
    // Callback del circulo
    private ButtonCallback _cb;

    // TODO: Revisar si es necesario tener estos atributos o no
    //private int targetColor;
    //private int newColor;
    //private int anSt;  //0: no anim; 1: anim grande; 2: anim pequeña
    //private int contAnim;
    //private double timer;
    //private int cont;
};