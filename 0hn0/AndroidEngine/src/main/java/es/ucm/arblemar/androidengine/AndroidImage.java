package es.ucm.arblemar.androidengine;

import android.graphics.Bitmap;

import es.ucm.arblemar.engine.Image;

public class AndroidImage implements Image {
    private Bitmap bitmap;

    public AndroidImage(Bitmap _bitmap){
        bitmap = _bitmap;
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    public Bitmap getBitmap(){
        return bitmap;
    }
}
