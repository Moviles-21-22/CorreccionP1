package es.ucm.arblemar.desktopgame;
import es.ucm.arblemar.desktopengine.DesktopEngine;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.gamelogic.states.LoadState;

class Main {
    public static void main (String [] args){
        DesktopEngine engine = new DesktopEngine();
        State loadAssets = new LoadState(engine);
        if(!engine.init(loadAssets, "0hn0", 400, 600)) {
            System.out.println("Error al inicializar el engine");
            return;
        }
        engine.run();
    }
}