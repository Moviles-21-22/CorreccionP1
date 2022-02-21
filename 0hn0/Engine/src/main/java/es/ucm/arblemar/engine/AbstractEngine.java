package es.ucm.arblemar.engine;

public abstract class AbstractEngine implements Engine{
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

    protected boolean _changeState = false;
    protected State _newState;
    protected State _currState;
    protected Graphics _graphics;
    protected Input _input;
}
