package es.ucm.arblemar.engine;

public abstract class AbstractEngine implements Engine {
    public AbstractEngine(){
    }

    @Override
    public void reqNewState(State newState){
        _changeState = true;
        _newState = newState;
    }

    @Override
    public Graphics getGraphics() {
        return _graphics;
    }

    @Override
    public Input getInput() {
        return _input;
    }

    protected void updateDeltaTime() {
        _currentTime = System.nanoTime();
        long nanoElapsedTime = _currentTime - _lastFrameTime;
        _lastFrameTime = _currentTime;
        _deltaTime = (double) nanoElapsedTime / 1.0E9;
    }

    protected boolean _changeState = false;
    protected State _newState;
    protected State _currState;
    protected Graphics _graphics;
    protected Input _input;

    // DELTA TIME
    protected long _lastFrameTime = 0;
    protected long _currentTime = 0;
    protected double _deltaTime = 0;
}