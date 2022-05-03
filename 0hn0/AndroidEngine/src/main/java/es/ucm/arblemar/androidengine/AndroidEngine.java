package es.ucm.arblemar.androidengine;

import android.content.res.AssetManager;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.arblemar.engine.AbstractEngine;
import es.ucm.arblemar.engine.State;

public class AndroidEngine extends AbstractEngine implements Runnable {
    public AndroidEngine() {
    }

    public boolean init(State initState, AppCompatActivity activity, int logicW, int logicH) {
        try {
            // INPUT
            _input = new AndroidInput(this);

            // GRAPHICS
            AssetManager assetMan = activity.getAssets();
            _graphics = new AndroidGraphics(assetMan, activity.getWindowManager(), activity.getWindow(), logicW, logicH);

            // STATE
            _currState = initState;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
        return ((AndroidGraphics) _graphics).init((AndroidInput) _input, activity) && _currState.init();
    }

    @Override
    public void run() {
        _lastFrameTime = System.nanoTime();

        running = true;
        while (running) {
            // Refresco del deltaTime
            updateDeltaTime();

            // Refresco del estado actual
            _currState.handleInput();
            _currState.update(_deltaTime);

            _graphics.updateGraphics();
            _graphics.prepareFrame();
            _graphics.clear(0xFFFFFFFF);
            _currState.render();
            _graphics.restore();

            // Inicializacion del nuevo estado en diferido
            if (_changeState) {
                _changeState = false;
                _currState = _newState;
                _currState.init();
            }
        }
    }

    public void onResume() {
        if (!running) {
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }

    public void onPause() {
        running = false;
        while (true) {
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ATRIBUTOS
    private volatile boolean running = false;
    private Thread thread;
    private AppCompatActivity _activity;

}