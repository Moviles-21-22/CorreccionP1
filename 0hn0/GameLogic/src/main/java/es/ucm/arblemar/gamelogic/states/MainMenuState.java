package es.ucm.arblemar.gamelogic.states;

import java.awt.Button;
import java.util.List;
import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.Image;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Input.TouchEvent;
import es.ucm.arblemar.gamelogic.Assets;
import es.ucm.arblemar.gamelogic.ButtonCallback;

public class MainMenuState implements State {
    public MainMenuState(Engine engine){
        _engine = engine;
    }

    @Override
    public boolean init() {
        try{
            Graphics g = _engine.getGraphics();

            // Atributos del botón jugar
            sizeJugar = new int[2];
            sizeJugar[0] = (g.getLogWidth() / 10) * 3;
            sizeJugar[1] = (g.getLogHeight() / 10);
            posJugar = new int[2];
            posJugar[0] = (g.getLogWidth() / 2)  - (sizeJugar[0] / 2);
            posJugar[1] = (g.getLogHeight() / 10) * 3;
            play = new ButtonCallback() {
                @Override
                public void doSomething() {
                    SelectMenuState sMenu = new SelectMenuState(_engine);
                    _engine.reqNewState(sMenu);
                }
            };
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

        // TITULO
        int size [] = new int[2];
        size[0] = (g.getLogWidth() / 10) * 7;
        size[1] = (g.getLogWidth() / 6);
        int pos [] = new int[2];
        pos[0] = (g.getLogWidth() / 2)  - (size[0] / 2);
        pos[1] = (g.getLogHeight() / 10);
        int color = 0X313131FF;
        Font font = Assets.molle;
        int tamF = 100;

        g.setColor(color);
        g.drawText("Oh no", pos[0], pos[1] + size[1], font, tamF);

        // BOTON JUGAR
        color = 0X313131FF;
        font = Assets.jose;
        tamF = 56;

        g.setColor(color);
        g.drawText("Jugar", posJugar[0], posJugar[1] + sizeJugar[1], font, tamF);

        // CREDITOS
        size[0] = (g.getLogWidth() / 2);
        size[1] = (g.getLogHeight() / 20);
        pos[0] = (g.getLogWidth() / 2)  - (size[0] / 2);
        pos[1] = (g.getLogHeight() / 2);
        color = 0XB6B3B6FF;
        tamF = 20;

        g.setColor(color);
        g.drawText("Un juego copiado a Q42", pos[0], pos[1] + size[1], font, tamF);

        pos[1] += size[1];
        g.drawText("Creado por Martin Kool", pos[0], pos[1] + size[1], font, tamF);

        // ICONO
        size[0] = (g.getLogWidth() / 16) * 2;
        size[1] = (g.getLogWidth() / 11) * 2;
        pos[0] = (g.getLogWidth() / 2)  - (size[0] / 2);
        pos[1] = (g.getLogHeight() / 4) * 3;
        Image im = Assets.q42;

        g.drawImage(im, pos[0] , pos[1], size[0], size[1]);
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
                    if (eventPos[0] > posJugar[0] && eventPos[0] < posJugar[0] + sizeJugar[0]
                            && eventPos[1] > posJugar[1] && eventPos[1] < posJugar[1] + sizeJugar[1])
                    {
                        play.doSomething();
                    };
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    Engine _engine;

    // Atributos del botón JUGAR
    int posJugar[];
    int sizeJugar[];
    ButtonCallback play;
}