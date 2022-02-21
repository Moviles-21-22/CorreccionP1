package es.ucm.arblemar.androidengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.io.InputStream;
import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Image;
import android.graphics.Rect;

public class AndroidGraphics extends AbstractGraphics {

    private AndroidScreen screen;
    private AndroidInput input;
    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private AssetManager assetManager;
    private int widht ;
    private int height ;


    public AndroidGraphics(AppCompatActivity appCmtAct,int w,int h,Bitmap bt){
        super(w, h);
        this.bitmap = bt;
        this.canvas = new Canvas(bt);
        this.paint = new Paint();
        this.assetManager = appCmtAct.getAssets();
        Point p = new Point();
        appCmtAct.getWindowManager().getDefaultDisplay().getSize(p);
        this.widht = p.x;
        this.height = p.y;
    }

    @Override
    public Image newImage(String name){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = null;
        Bitmap currBitMap = null;
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open(name);
            currBitMap = BitmapFactory.decodeStream(inputStream);
            if(currBitMap == null){
                throw new RuntimeException("No se ha podido cargar el bitmap del asset " + name);
            }
        }
        catch (IOException e){
            throw new RuntimeException("No se ha podido cargar el bitmap del asset " + name);
        }
        finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }
                catch (IOException e){
                    throw new RuntimeException("InputStream null en la carga del asset " + name);
                }
            }
        }
        return new AndroidImage(currBitMap);
    }

    @Override
    public Font newFont(String filename, int size, boolean isBold){
        return new AndroidFont(Typeface.createFromAsset(assetManager,filename),size,filename);
    }

    @Override
    public void clear(int color){
        canvas.drawColor(color);
    }

    @Override
    public void setColor(int color){
        int red = (int) ((color & 0xffffffffL) >> 24);
        int green = (color & 0x00ff0000) >> 16;
        int blue = (color & 0x0000ff00) >> 8;
        int alpha = color & 0x000000ff;
        this.paint.setColor(Color.argb(alpha,red,green,blue));
    }

    @Override
    public void setFont(Font font, int tam) {
        paint.setTypeface(((AndroidFont)font).getFont());
    }

    @Override
    public void drawImage(Image image, int x, int y, float w, float h) {
        Rect source = new Rect(0,0,image.getWidth(),image.getHeight());
        Rect destiny = new Rect(x,y,(int)(x + w), (int)(y + h));
        canvas.drawBitmap(((AndroidImage)image).getBitmap(), source, destiny,null);
    }

    @Override
    public void drawCircle(int x, int y, float radio) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawCircle(x, y, radio, this.paint);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void drawText(String text, int x, int y, Font font, int tam) {
        Typeface currFont = ((AndroidFont)font).getFont();
        paint.setTypeface(currFont);
        paint.setTextSize(tam);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text,x,y,paint);
        paint.reset();
    }

    @Override
    public void drawRect(int x, int y, float width, float height){
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x,y,x + width, y + width, paint);
        paint.reset();
    }

    @Override
    public void fillCircle(int x, int y, float dm){
        float aux = dm / 2;
        canvas.drawCircle(x + aux,y + aux, aux, paint);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x,y,x + width,y +width,paint);
        paint.reset();
    }

    @Override
    public int getWidth() {
        return widht;
    }

    @Override
    public int getHeight() {
        return  height;
    }

    @Override
    public void updateGraphics() {

    }

    @Override
    public void save() {

    }

    @Override
    public void restore() {

    }

    public AndroidScreen getScreen() {
        return screen;
    }

    public void setCanvas(Canvas _canvas) {
        canvas = _canvas;
    }

    @Override
    public void translate(int x, int y) {
        this.canvas.translate(x, y);
    }

    @Override
    public void scale(float x, float y) {
        this.canvas.scale(x,y);
    }
}