package es.ucm.arblemar.gamelogic.states;

import java.util.List;

import es.ucm.arblemar.engine.Image;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Input.TouchEvent;
import es.ucm.arblemar.gamelogic.Assets;
import es.ucm.arblemar.gamelogic.ButtonCallback;

public class MainMenuState implements State {
    public MainMenuState(Engine engine) {
        _engine = engine;
    }

    @Override
    public boolean init() {
        try {
            Graphics g = _engine.getGraphics();

            // Atributos del botón jugar
            _sizeJugar = new int[2];
            _sizeJugar[0] = (g.getLogWidth() / 10) * 3;
            _sizeJugar[1] = (g.getLogHeight() / 10);
            _posJugar = new int[2];
            _posJugar[0] = (g.getLogWidth() / 2) - (_sizeJugar[0] / 2);
            _posJugar[1] = (g.getLogHeight() / 10) * 3;
            _play = new ButtonCallback() {
                @Override
                public void doSomething() {
                    SelectMenuState sMenu = new SelectMenuState(_engine);
                    _engine.reqNewState(sMenu);
                }
            };
        } catch (Exception e) {
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
        int size[] = new int[2];
        size[0] = (g.getLogWidth() / 10) * 7;
        size[1] = (g.getLogWidth() / 6);
        int pos[] = new int[2];
        pos[0] = (g.getLogWidth() / 2) - (size[0] / 2);
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
        g.drawText("Jugar", _posJugar[0], _posJugar[1] + _sizeJugar[1], font, tamF);

        // CREDITOS
        size[0] = (g.getLogWidth() / 2);
        size[1] = (g.getLogHeight() / 20);
        pos[0] = (g.getLogWidth() / 2) - (size[0] / 2);
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
        pos[0] = (g.getLogWidth() / 2) - (size[0] / 2);
        pos[1] = (g.getLogHeight() / 4) * 3;
        Image im = Assets.q42;

        g.drawImage(im, pos[0], pos[1], size[0], size[1]);
    }

    @Override
    //  Gestiona las colisiones del ratón con los objetos de la escena
    public void handleInput() {
        List<TouchEvent> events = _engine.getInput().GetTouchEvents();
        for (int i = 0; i < events.size(); i++) {
            TouchEvent currEvent = events.get(i);
            if (currEvent == TouchEvent.touchDown) {
                if (currEvent.getX() > _posJugar[0] &&
                        currEvent.getX() < _posJugar[0] + _sizeJugar[0] &&
                        currEvent.getY() > _posJugar[1] &&
                        currEvent.getY() < _posJugar[1] + _sizeJugar[1]) {
                    _play.doSomething();
                }
            }
        }
    }

    Engine _engine;

    // Atributos del botón JUGAR
    int _posJugar[];
    int _sizeJugar[];
    ButtonCallback _play;
}