package es.ucm.arblemar.gamelogic.states;

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
import es.ucm.arblemar.gamelogic.TipoCelda;
import es.ucm.arblemar.gamelogic.gameobjects.Celda;

public class SelectMenuState implements State {
    public SelectMenuState(Engine engine){
        _engine = engine;
    }

    @Override
    public boolean init() {
        try{
            Graphics g = _engine.getGraphics();

            // INICIALIZACIÓN DE LAS CELDAS
            celdas = new ArrayList<>();

            int ind [] = new int[2];
            ind[0] = ind[1] = -1;
            float diam = 70;
            int pos [] = new int[2];
            pos[0] = (g.getLogWidth() * 2 / 7)  - ((int)diam / 2);
            pos[1] = (g.getLogHeight() * 2 / 5);
            Font f = Assets.jose;
            int tamF = 43;
            final Celda c1 = new Celda(TipoCelda.AZUL, f, tamF,4, pos, diam, ind);
            c1.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    //System.out.println(eventPos._x + " " + eventPos._y);
                    int numGame = c1.getValue();
                    GameState game = new GameState(_engine, numGame);
                    _engine.reqNewState(game);
                }
            });
            celdas.add(c1);

            pos[0] = (g.getLogWidth() / 2)  - ((int)diam / 2);
            final Celda c2 = new Celda(TipoCelda.ROJO, f, tamF, 5, pos, diam, ind);
            c2.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    //System.out.println(eventPos._x + " " + eventPos._y);
                    int numGame = c2.getValue();
                    GameState game = new GameState(_engine, numGame);
                    _engine.reqNewState(game);
                }
            });
            celdas.add(c2);

            pos[0] = (g.getLogWidth() * 5 / 7)  - ((int)diam / 2);
            final Celda c3 = new Celda(TipoCelda.AZUL, f, tamF,6, pos, diam, ind);
            c3.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    //System.out.println(eventPos._x + " " + eventPos._y);
                    int numGame = c3.getValue();
                    GameState game = new GameState(_engine, numGame);
                    _engine.reqNewState(game);
                }
            });
            celdas.add(c3);

            pos[0] = (g.getLogWidth() * 2 / 7)  - ((int)diam / 2);
            pos[1] += (int)(diam * 11 / 10);
            final Celda c4 =  new Celda(TipoCelda.ROJO, f, tamF, 7, pos, diam, ind);
            c4.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    //System.out.println(eventPos._x + " " + eventPos._y);
                    int numGame = c4.getValue();
                    GameState game = new GameState(_engine, numGame);
                    _engine.reqNewState(game);
                }
            });
            celdas.add(c4);

            pos[0] = (g.getLogWidth() / 2)  - ((int)diam / 2);
            final Celda c5 = new Celda(TipoCelda.AZUL, f, tamF,8, pos, diam, ind);
            c5.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    //System.out.println(eventPos._x + " " + eventPos._y);
                    int numGame = c5.getValue();
                    GameState game = new GameState(_engine, numGame);
                    _engine.reqNewState(game);
                }
            });
            celdas.add(c5);

            pos[0] = (g.getLogWidth() * 5 / 7)  - ((int)diam / 2);
            final Celda c6 = new Celda(TipoCelda.ROJO, f, tamF, 9, pos, diam, ind);
            c6.setCallback(new ButtonCallback() {
                @Override
                public void doSomething() {
                    //System.out.println(eventPos._x + " " + eventPos._y);
                    int numGame = c6.getValue();
                    GameState game = new GameState(_engine, numGame);
                    _engine.reqNewState(game);
                }
            });
            celdas.add(c6);

            // BOTON VOLVER
            sizeVolver = new int[2];
            sizeVolver[0] = (g.getLogWidth() / 16) * 2;
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

        // Celdas
        for(Celda c : celdas){
            c.render(g);
        }

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
                    }
                    else
                    {
                        boolean isClick = false;
                        int j = 0;
                        while(!isClick && j < celdas.size())
                        {
                            if(celdas.get(j).isClicked(eventPos))
                            {
                                System.out.println("Celda pulsada: " + Integer.toString(celdas.get(j).getValue()));
                                //celdas.get(j).runCallBack();
                                isClick = true;
                            }
                            j++;
                        }
                        for(Celda c : celdas){
                            c.render(g);
                        }
                    }

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
    private List<Celda> celdas;
}