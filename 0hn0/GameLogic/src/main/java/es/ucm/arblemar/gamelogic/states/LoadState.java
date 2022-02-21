package es.ucm.arblemar.gamelogic.states;

import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.gamelogic.Assets;

/**
 * Estado que se encarga de cargar los recursos de la aplicación
 **/
public class LoadState implements State {
    public LoadState(Engine engine) {
        _engine = engine;
    }

    @Override
    public boolean init() {
        try {
            Graphics graphics = _engine.getGraphics();

            //Sprites
            Assets.close = graphics.newImage("close.png");
            Assets.q42 = graphics.newImage("q42.png");
            Assets.history = graphics.newImage("history.png");
            Assets.eye = graphics.newImage("eye.png");
            Assets.lock = graphics.newImage("lock.png");

            //Fuentes
            Assets.molle = graphics.newFont("Molle-Regular.ttf", 1, false);
            Assets.jose = graphics.newFont("JosefinSans-Bold.ttf", 1, true);

            //  Falla al inicializar
            State mainMenu = new MainMenuState(_engine);
            _engine.reqNewState(mainMenu);
        }
        catch (Exception e) {
            System.err.println(e);
            return false;
        }

        return true;
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public void render() {

    }

    @Override
    public void handleInput() {

    }

    Engine _engine;
}