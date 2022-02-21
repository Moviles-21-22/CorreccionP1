package es.ucm.arblemar.gamelogic.states;

import java.util.List;

import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input;

public class GameState implements State {
    Engine _engine;
    Graphics _graphics;
    private int tam;
    private int posPista [];
    //private Tablero tab;
    //private List<GameObject> objects;
    //private List<Boton> images;
    //private Icon[] candados;
    //private Boton backButton;
    //private Boton restButton;
    //private Boton pistabutton;
    //private Texto textoSuperior;
    //private Texto textoSupDos;
    private boolean _pistaPuesta = false;
    private boolean muestraCandados = false;
    private boolean win = false;

    GameState(Engine _engine,int _tam){
        this._engine = _engine;
        tam = _tam;
        _graphics = this._engine.getGraphics();
        //objects = new ArrayList<>();
        //images = new ArrayList<>();
    }

    @Override
    public boolean init() {
        try {
        }
        catch (Exception e){
            System.out.println("Fallo al intentar crear el juego");
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public void update(double deltaTime) {
        //if (tab != null && tab.EsSolucion() && !win) {
        //    gameWin();
        //}
        //for(GameObject obj : objects){
        //    obj.update(deltaTime);
        //}
    }

    @Override
    public void render() {
        Graphics g = _engine.getGraphics();

        ////  Render de las celdas
        //if (tab != null && _pistaPuesta) {
        //    _graphics.setColor(0x313131FF);
        //    _graphics.fillCircle(new Vector2(posPista._x - 3, posPista._y - 3), (int)tab.GetCeldaSize() + 6);
        //}
        //// Render de los demás objetos
        //for(GameObject obj : objects){
        //    obj.render(g);
        //}
    }

    @Override
    public void handleInput() {
        List<Input.TouchEvent> events = _engine.getInput().GetTouchEvents();
        AbstractGraphics g = (AbstractGraphics) _graphics;

        for(int i = 0 ; i < events.size() ; i++){
            Input.TouchEvent currEvent = events.get(i);
            int eventPos [] = g.logPos(currEvent.getX(), currEvent.getY());
            switch (currEvent) {
                case touchDown: {
                    if (win) {
                        win = false;
                        SelectMenuState menu = new SelectMenuState(_engine);
                        _engine.reqNewState(menu);
                    }
                    //GameObject obj = getObjectClicked(eventPos);
                    //  Es de tipo texto o imagen
                    //if(obj != null){
                    //    obj.clicked();
                    //}
                    break;
                }
            }
        }
    }

    private void gameWin() {
        win = true;
        //objects.remove(textoSuperior);
        //if (textoSupDos != null) {
        //    objects.remove(textoSupDos);
        //    textoSupDos = null;
        //}
        //objects.remove(backButton);
        //objects.remove(restButton);
        //objects.remove(pistabutton);

        //int width = (_graphics.getLogWidth() / 2) * 3, height = (_graphics.getLogWidth() / 7),
        //        posX = (_graphics.getLogWidth() / 3) - 15, posY = (_graphics.getLogHeight() / 12) - 10;

        //textoSuperior = new Texto(new Vector2(posX,posY), new Vector2(width, height), 0X313131FF ,Assets.jose,72,0);
        //textoSuperior.setTexto("Super");
        //objects.add(textoSuperior);
    }

    // Devuelve el objecto que ha sido pulsado
    //private GameObject getObjectClicked(Vector2 eventPos){

    //    boolean encontrado = false;
    //    int gameObjIndex = 0;
    //    while (!encontrado && gameObjIndex < objects.size()){
    //        //  Buscando entre los objetos que son textos, img ,etc (no incluidas las celdas)
    //        if(objects.get(gameObjIndex).isInteractive() && objects.get(gameObjIndex).isClicked(eventPos)) {
    //            encontrado = true;
    //            return objects.get(gameObjIndex);
    //        }
    //        else{
    //            gameObjIndex++;
    //        }
    //    }
    //    return null;
    //}

    //Escribe un texto en función de la pista elegida
    private void stringText(int id) {
        String text = "", text2 = "";
        int width = (_graphics.getLogWidth() / 2) * 3, height = (_graphics.getLogWidth() / 7),
                posX = (_graphics.getLogWidth() / 22), posY = (_graphics.getLogHeight() / 50),
                dist = height / 2;

        switch (id) {
            case 0: {
                posX = (_graphics.getLogWidth() / 4);
                text = "Azul completa,";
                text2 = "se puede cerrar";
                break;
            }
            case 1: {
                posX = (_graphics.getLogWidth() / 6);
                text = "   Se puede poner";
                text2 = "una pared adyacente";
                break;
            }
            case 2: {
                posX = (_graphics.getLogWidth() / 4);
                text = "  Es necesario";
                text2 = "poner una azul";
                break;
            }
            case 3: {
                posX = (_graphics.getLogWidth() / 10);
                text = "Tiene más vecinas azules";
                text2 = "    de las que debería";
                break;
            }
            case 4: {
                text = "Necesita más vecinas azules";
                text2 = " y, en cambio, está cerrada";
                break;
            }
            case 5:
            case 6: {
                posX = (_graphics.getLogWidth() / 8);
                text = "No puede haber celdas";
                text2 = "   azules sin vecinas";
                break;
            }
            case 7: {
                text = "   Se deben poner azules";
                text2 = "en la única dirección abierta";
                break;
            }
            case 8: {
                posX = (_graphics.getLogWidth() / 11);
                text = "   Suma alcanzable de";
                text2 = "adyacentes igual al valor";
                break;
            }
            case 9: {
                text = "No puede alcanzar su valor";
                text2 = "con las adyacentes que tiene";
                break;
            }
        }
        //textoSuperior = new Texto(new Vector2(posX, posY), new Vector2(width, height), 0X313131FF, Assets.jose, 32, 0);
        //textoSuperior.setTexto(text);
        //objects.add(textoSuperior);
        //textoSupDos = new Texto(new Vector2(posX, posY + dist), new Vector2(width, height), 0X313131FF, Assets.jose, 32, 0);
        //textoSupDos.setTexto(text2);
        //objects.add(textoSupDos);
    }
}