package es.ucm.arblemar.gamelogic.states;
import java.awt.Button;
import java.util.ArrayList;
import java.util.List;

import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Image;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input;
import es.ucm.arblemar.gamelogic.Assets;
import es.ucm.arblemar.gamelogic.ButtonCallback;

public class SelectMenuState implements State {
    public SelectMenuState(Engine engine){
        _engine = engine;
    }

    @Override
    public boolean init() {
        try{
            Graphics g = _engine.getGraphics();

            //final CeldaAzul c1 = new CeldaAzul(Assets.jose, 43,4,new Vector2(0,0), new Vector2(88,230), 70);
            //c1.setCallback(new ButtonCallback() {
            //    @Override
            //    public void doSomething() {
            //        //System.out.println(eventPos._x + " " + eventPos._y);
            //        int numGame = c1.getValue();
            //        Game game = new Game(engine,numGame);
            //        engine.initNewApp(game);
            //    }
            //});
//
            //gameObjects.add(c1);

            //final CeldaRoja c2 = new CeldaRoja(new Vector2(0,0), new Vector2(165,230),5,Assets.jose, 43, 70);
            //c2.setCallback(new ButtonCallback() {
            //    @Override
            //    public void doSomething() {
            //        //System.out.println(eventPos._x + " " + eventPos._y);
            //        int numGame = c2.getValue();
            //        Game game = new Game(engine,numGame);
            //        engine.initNewApp(game);
            //    }
            //});
            //gameObjects.add(c2);

            //final CeldaAzul c3 = new CeldaAzul(Assets.jose, 43, 6,new Vector2(0,0), new Vector2(243,230), 70);
            //c3.setCallback(new ButtonCallback() {
            //    @Override
            //    public void doSomething() {
            //        //System.out.println(eventPos._x + " " + eventPos._y);
            //        int numGame = c3.getValue();
            //        Game game = new Game(engine,numGame);
            //        engine.initNewApp(game);
            //    }
            //});
            //gameObjects.add(c3);

            //final CeldaRoja c4 =  new CeldaRoja(new Vector2(0,0), new Vector2(88,308),7,Assets.jose, 43, 70);
            //c4.setCallback(new ButtonCallback() {
            //    @Override
            //    public void doSomething() {
            //        //System.out.println(eventPos._x + " " + eventPos._y);
            //        int numGame = c4.getValue();
            //        Game game = new Game(engine,numGame);
            //        engine.initNewApp(game);
            //    }
            //});
            //gameObjects.add(c4);

            //final CeldaAzul c5 = new CeldaAzul(Assets.jose, 43, 8,new Vector2(0,0), new Vector2(165,308), 70);
            //c5.setCallback(new ButtonCallback() {
            //    @Override
            //    public void doSomething() {
            //        //System.out.println(eventPos._x + " " + eventPos._y);
            //        int numGame = c5.getValue();
            //        Game game = new Game(engine,numGame);
            //        engine.initNewApp(game);
            //    }
            //});
            //gameObjects.add(c5);

            //final CeldaRoja c6 = new CeldaRoja(new Vector2(0,0), new Vector2(243,308),9, Assets.jose, 43, 70);
            //c6.setCallback(new ButtonCallback() {
            //    @Override
            //    public void doSomething() {
            //        //System.out.println(eventPos._x + " " + eventPos._y);
            //        int numGame = c6.getValue();
            //        Game game = new Game(engine,numGame);
            //        engine.initNewApp(game);
            //    }
            //});
            //gameObjects.add(c6);


            sizeVolver = new int[2];
            sizeVolver[0] = (g.getLogWidth() / 16) * 2 ;
            sizeVolver[1] = (g.getLogWidth() / 16) * 2;
            posVolver = new int[2];
            posVolver[0] = (g.getLogWidth() / 2)  - (sizeVolver[0] / 2);
            posVolver[1] = (g.getLogHeight() / 5) * 4;
            backIm = Assets.close;

            goBack = new ButtonCallback() {
                @Override
                public void doSomething() {
                    MainMenuState main = new MainMenuState(_engine);
                    _engine.reqNewState(main);
                }
            };
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

        // TITULO
        int size [] = new int[2];
        size[0] = (g.getLogWidth() / 2);
        size[1] = (g.getLogWidth() / 8);
        int pos [] = new int [2];
        pos[0] = (g.getLogWidth() / 6);
        pos[1] = (g.getLogHeight() / 10);
        int color = 0x313131FF;
        Font font = Assets.molle;
        int tamF = 80;

        g.setColor(color);
        g.drawText("Oh, no",pos[0],  pos[1] + size[1], font, tamF);

        // INFO
        size[0] = (g.getLogWidth() / 7) * 5;
        size[1] = (g.getLogWidth() / 25);
        pos[0] = (g.getLogWidth() / 7);
        pos[1] = (g.getLogHeight() / 3) - 30;
        color = 0x313131FF;
        font = Assets.jose;
        tamF = 32;

        g.setColor(color);
        g.drawText("Elija el tamaño a jugar", pos[0],  pos[1] + size[1], font, tamF);

        // BOTON VOLVER
        g.drawImage(backIm, posVolver[0], posVolver[1], sizeVolver[0], sizeVolver[1]);
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
                    // BOTON VOLVER
                    if (eventPos[0] > posVolver[0] && eventPos[0] < posVolver[0] + sizeVolver[0]
                            && eventPos[1] > posVolver[1] && eventPos[1] < posVolver[1] + sizeVolver[1])
                    {
                        goBack.doSomething();
                    };
                    break;
                }
            }
        }
    }

    private Engine _engine;

    // Atributos de los botones
    Image backIm;
    int sizeVolver [];
    int posVolver [];
    ButtonCallback goBack;

}