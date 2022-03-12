package es.ucm.arblemar.engine;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInput implements Input {
    protected AbstractInput(Engine e) {
        _engine = e;
        events = new ArrayList<>();
    }

    @Override
    public synchronized List<TouchEvent> GetTouchEvents() {
        if (!events.isEmpty()) {
            List<TouchEvent> touchEvents = new ArrayList<>();
            touchEvents.addAll(events);
            events.clear();
            return touchEvents;
        } else return events;
    }

    protected void onTouchEvent(int x, int y) {
        AbstractGraphics g = (AbstractGraphics) _engine.getGraphics();

        TouchEvent currEvent = TouchEvent.touchDown;
        int eventPos[] = g.logPos(x, y);
        currEvent.setX(eventPos[0]);
        currEvent.setY(eventPos[1]);

        events.add(currEvent);
    }

    protected Engine _engine;
    //  Lista de los eventos
    protected List<TouchEvent> events;
}