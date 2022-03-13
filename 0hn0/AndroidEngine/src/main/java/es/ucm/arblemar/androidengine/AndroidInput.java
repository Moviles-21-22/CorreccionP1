package es.ucm.arblemar.androidengine;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import es.ucm.arblemar.engine.AbstractInput;
import es.ucm.arblemar.engine.Engine;

public class AndroidInput extends AbstractInput implements View.OnTouchListener {

    private List<TouchEvent> events;

    public AndroidInput(Engine e, View view) {
        super(e);
        events = new ArrayList<>();
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDownEvent((int) event.getX(), (int) event.getY());
                break;

            //case MotionEvent.ACTION_MOVE:
            //    break;

            //case MotionEvent.ACTION_UP:
            //    break;
        }
        return true;
    }
}