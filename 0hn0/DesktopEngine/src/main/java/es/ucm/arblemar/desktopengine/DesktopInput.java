package es.ucm.arblemar.desktopengine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import es.ucm.arblemar.engine.AbstractInput;
import es.ucm.arblemar.engine.Engine;

public class DesktopInput extends AbstractInput implements MouseListener, MouseMotionListener {
    public DesktopInput(Engine e) {
        super(e);
        events = new ArrayList<>();
    }

    public DesktopInput GetInput() {
        return this;
    }

    public boolean init() {
        return true;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        onTouchDownEvent(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
}
