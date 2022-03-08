package es.ucm.arblemar.androidengine;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import es.ucm.arblemar.engine.AbstractEngine;
import es.ucm.arblemar.engine.State;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input;

public class AndroidEngine extends AbstractEngine implements  Runnable {
    private SurfaceView surface;
    private volatile boolean running = false;
    private Thread thread;
    private long _lastFrameTime = 0;
    private long _currentTime = 0;
    private double _deltaTime = 0;

    public AndroidEngine(AppCompatActivity activity, int logicW, int logicH){

        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        boolean landScape = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int bufferW = landScape ? logicH : logicW;
        int bufferH = landScape ? logicW : logicH;
        Bitmap buffer = Bitmap.createBitmap(bufferW,bufferH, Bitmap.Config.RGB_565);

        surface = new SurfaceView(activity.getApplicationContext());
        _graphics = new AndroidGraphics(activity,logicW,logicH,buffer);
        _input = new AndroidInput(this, surface);
        activity.setContentView(surface);
    }

    public boolean init(State initState, String nameGame, int w, int h) {
        _currState = initState;
        return _currState.init();
    }

    @Override
    public void run(){

        SurfaceHolder holder = surface.getHolder();
        _lastFrameTime = System.nanoTime();

        running = true;
        while (running){
            // Refresco del deltaTime
            updateDeltaTime();

            // Refresco del estado actual
            _currState.handleInput();
            _currState.update(_deltaTime);

            while (!holder.getSurface().isValid());
            Canvas canvas = holder.lockCanvas();
            ((AndroidGraphics)_graphics).setCanvas(canvas);

            _graphics.prepareFrame();
            _graphics.clear(0xFFFFFFFF);
            _currState.render();
            holder.unlockCanvasAndPost(canvas);

            // Inicializacion del nuevo estado en diferido
            if(_changeState)
            {
                _changeState = false;
                _currState = _newState;
                _currState.init();
            }
        }
    }

    private void updateDeltaTime(){
        _currentTime = System.nanoTime();
        long nanoElapsedTime = _currentTime - _lastFrameTime;
        _lastFrameTime = _currentTime;
        _deltaTime = (double) nanoElapsedTime / 1.0E9;
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
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}