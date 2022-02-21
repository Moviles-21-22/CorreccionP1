package es.ucm.arblemar.gamelogic.states;

import java.util.List;
import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input.TouchEvent;

public class MainMenuState implements State {
    public MainMenuState(Engine engine){
        _engine = engine;
    }

    @Override
    public boolean init() {
        try{
            Graphics g = _engine.getGraphics();
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render() {
        Graphics g = _engine.getGraphics();
    }

    @Override
    //  Gestiona las colisiones del ratón con los objetos de la escena
    public void handleInput() {
        List<TouchEvent> events = _engine.getInput().GetTouchEvents();
        AbstractGraphics g = (AbstractGraphics) _engine.getGraphics();
        for(int i = 0 ; i < events.size() ; i++){
            TouchEvent currEvent = events.get(i);
            // Posición donde se hizo click
            int eventPos [] = g.logPos(currEvent.getX(), currEvent.getY());
            switch (currEvent){
                case touchDown:{
                    //GameObject obj = getObjectClicked(eventPos);
                    //if(obj != null){
                    //    obj.clicked();
                    //}
                    System.out.println("WIP");
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    /**
     * Devuelve el objeto que ha sido pulsado
     * */
    //private GameObject getObjectClicked(Vector2 eventPos){
    //    boolean encontrado = false;
    //    int gameObjIndex = 0;
    //    AbstractGraphics g = (AbstractGraphics) _mainEngine.getGraphics();
    //    while (!encontrado && gameObjIndex < gameObjects.size()){

    //        if(gameObjects.get(gameObjIndex).isClicked(eventPos)){
    //            encontrado = true;
    //            return gameObjects.get(gameObjIndex);
    //        }
    //        else{
    //            gameObjIndex++;
    //        }
    //    }
    //    return null;
    //}

    Engine _engine;
}