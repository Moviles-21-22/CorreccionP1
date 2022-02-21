package es.ucm.arblemar.gamelogic.states;
import java.util.List;

import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input;

public class SelectMenuState implements State {
    private Engine _engine;

    public SelectMenuState(Engine engine){
        _engine = engine;
    }

    @Override
    public boolean init() {
        try{
            Graphics g = _engine.getGraphics();
        }
        catch (Exception e){
            System.out.println(e);
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

        //for(GameObject obj : gameObjects){
        //    obj.render(g);
        //}
    }

    @Override
    //  Gestiona las colisiones del ratón con los objetos de la escena
    public void handleInput() {
        List<Input.TouchEvent> events = _engine.getInput().GetTouchEvents();
        AbstractGraphics g = (AbstractGraphics) _engine.getGraphics();

        for(int i = 0 ; i < events.size() ; i++) {
            Input.TouchEvent currEvent = events.get(i);
            int eventPos [] = g.logPos(currEvent.getX(), currEvent.getY());
            switch (currEvent){
                case touchDown:{
                    //GameObject obj = getObjectClicked(eventPos);
                    //if(obj != null) {
                    //    obj.clicked();
                    //}

                    break;
                }
            }
        }
    }

    // Devuelve el objecto que ha sido pulsado
    //private GameObject getObjectClicked(Vector2 eventPos){
    //    boolean encontrado = false;
    //    int gameObjIndex = 0;

    //    while (!encontrado && gameObjIndex < gameObjects.size()){
    //        if(gameObjects.get(gameObjIndex).isInteractive() && (gameObjects.get(gameObjIndex)).isClicked(eventPos)){
    //            encontrado = true;
    //            return gameObjects.get(gameObjIndex);
    //        }
    //        else{
    //            gameObjIndex++;
    //        }
    //    }
    //    return null;
    //}
}