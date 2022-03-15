package es.ucm.arblemar.desktopengine;

import java.awt.image.BufferStrategy;

import es.ucm.arblemar.engine.AbstractEngine;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input;

public class DesktopEngine extends AbstractEngine {
    public DesktopEngine(){
    }

    public boolean init(State initState, String nameGame, int w, int h) {
        // STATE
        _currState = initState;
        // INPUT
        _input = new DesktopInput(this);
        // GRAPHICS
        _graphics = new DesktopGraphics(nameGame, this, w, h);

        return ((DesktopGraphics)_graphics).init() && ((DesktopInput)_input).init() && _currState.init();
    }

    public void run() {
        BufferStrategy strategy = ((DesktopGraphics)_graphics).getStrategy();
        _lastFrameTime = System.nanoTime();

        while(true) {
            // Refresco del deltaTime
            updateDeltaTime();

            // Refresco del estado actual
            _currState.handleInput();
            _currState.update(_deltaTime);

            // Pintamos el frame con el BufferStrategy
            do {
                do {
                    _graphics.updateGraphics();
                    _graphics.prepareFrame();
                    _graphics.clear(0xFFFFFFFF);
                    try {
                        _currState.render();
                    }
                    finally {
                        _graphics.restore();
                    }
                } while(strategy.contentsRestored());
                strategy.show();
            } while(strategy.contentsLost());

            // Inicializacion del nuevo estado en diferido
            if(_changeState)
            {
                _changeState = false;
                _currState = _newState;
                _currState.init();
            }
        }
    }

    /**
     * Cálculo del deltaTime
     * */
    private void updateDeltaTime(){
        _currentTime = System.nanoTime();
        long nanoElapsedTime = _currentTime - _lastFrameTime;
        _lastFrameTime = _currentTime;
        _deltaTime = (double) nanoElapsedTime / 1.0E9;
    }

    // ATRIBUTOS
    private long _lastFrameTime = 0;
    private long _currentTime = 0;
    private double _deltaTime = 0;
}

