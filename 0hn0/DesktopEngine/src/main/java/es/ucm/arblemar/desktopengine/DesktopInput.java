package es.ucm.arblemar.desktopengine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.Engine;
import es.ucm.arblemar.engine.Graphics;
import es.ucm.arblemar.engine.Input;

public class DesktopInput implements Input, MouseListener, MouseMotionListener {
    public DesktopInput(Engine e){
        super();
        _engine = e;
        events = new ArrayList<>();
    }

    public DesktopInput GetInput(){
        return this;
    }

    public boolean init() {
        return true;
    }

    @Override
    public List<TouchEvent> GetTouchEvents() {
        synchronized (events){
            if(!events.isEmpty()){
                List<TouchEvent> touchEvents = new ArrayList<>();
                touchEvents.addAll(events);
                events.clear();
                return touchEvents;
            }
            else return events;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        synchronized (events){
            AbstractGraphics g = (AbstractGraphics) _engine.getGraphics();

            TouchEvent currEvent = TouchEvent.touchDown;
            int eventPos [] = g.logPos(e.getX(), e.getY());
            currEvent.setX(eventPos[0]);
            currEvent.setY(eventPos[1]);

            events.add(currEvent);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        synchronized (events){
            AbstractGraphics g = (AbstractGraphics) _engine.getGraphics();

            TouchEvent currEvent = TouchEvent.touchUp;
            int eventPos [] = g.logPos(e.getX(), e.getY());
            currEvent.setX(eventPos[0]);
            currEvent.setY(eventPos[1]);

            events.add(currEvent);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {


    }

    @Override
    public void mouseMoved(MouseEvent e) {


    }

    private Engine _engine;
    //  Lista de los eventos que maneja deskopPc
    private List<TouchEvent> events;
}
