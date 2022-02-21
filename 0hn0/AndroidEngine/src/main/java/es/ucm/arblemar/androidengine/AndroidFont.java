package es.ucm.arblemar.androidengine;

import android.graphics.Typeface;
import es.ucm.arblemar.engine.Font;

public class AndroidFont implements Font {
    private Typeface font;
    private float size;
    private String fileName;

    public AndroidFont(Typeface _font, float _size, String _filename){
        font = _font;
        size = _size;
        fileName = _filename;
    }

    @Override
    public void setSize(float newSize) {
        size = newSize;
    }

    public Typeface getFont(){
        return font;
    }
}