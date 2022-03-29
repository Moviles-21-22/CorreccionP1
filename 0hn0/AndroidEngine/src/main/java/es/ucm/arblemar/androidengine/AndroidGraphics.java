package es.ucm.arblemar.androidengine;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.view.Window;

import java.io.IOException;
import java.io.InputStream;

import es.ucm.arblemar.engine.AbstractGraphics;
import es.ucm.arblemar.engine.Font;
import es.ucm.arblemar.engine.Image;

import android.graphics.Rect;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class AndroidGraphics extends AbstractGraphics {
    public AndroidGraphics(AssetManager assetMng, WindowManager winMan, Window win,
                           int logW, int logH) {
        super(logW, logH);
        _assetMng = assetMng;
        _mainWin = win;
        _winManager = winMan;
        _paint = new Paint();
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean init(AndroidInput input, AppCompatActivity activity) {
        try {
            _mainWin.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            _mainWin.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            _surface = new SurfaceView(activity.getApplicationContext());
            _surface.setOnTouchListener(input);
            activity.setContentView(_surface);
            Point winSize = new Point();
            _winManager.getDefaultDisplay().getSize(winSize);
            _winSize = winSize;

            _holder = _surface.getHolder();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public Image newImage(String name) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = null;
        Bitmap currBitMap = null;
        InputStream inputStream = null;

        try {
            inputStream = _assetMng.open(name);
            currBitMap = BitmapFactory.decodeStream(inputStream);
            if (currBitMap == null) {
                throw new RuntimeException("No se ha podido cargar el bitmap del asset " + name);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido cargar el bitmap del asset " + name);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("InputStream null en la carga del asset " + name);
                }
            }
        }
        return new AndroidImage(currBitMap);
    }

    @Override
    public Font newFont(String filename, int size, boolean isBold) {
        return new AndroidFont(Typeface.createFromAsset(_assetMng, filename), size, filename);
    }

    @Override
    public void clear(int color) {
        _canvas.drawColor(color);
    }

    @Override
    public void setColor(int color) {
        int red = (int) ((color & 0xffffffffL) >> 24);
        int green = (color & 0x00ff0000) >> 16;
        int blue = (color & 0x0000ff00) >> 8;
        int alpha = color & 0x000000ff;
        this._paint.setColor(Color.argb(alpha, red, green, blue));
    }

    @Override
    public void setFont(Font font, int tam) {
        _paint.setTypeface(((AndroidFont) font).getFont());
    }

    @Override
    public void drawImage(Image image, int x, int y, float w, float h) {
        Rect source = new Rect(0, 0, image.getWidth(), image.getHeight());
        Rect destiny = new Rect(x, y, (int) (x + w), (int) (y + h));
        _canvas.drawBitmap(((AndroidImage) image).getBitmap(), source, destiny, null);
    }

    @Override
    public void drawCircle(int x, int y, float radio) {
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(2);
        _canvas.drawCircle(x, y, radio, this._paint);
        _paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void drawText(String text, int x, int y, Font font, int tam) {
        Typeface currFont = ((AndroidFont) font).getFont();
        _paint.setTypeface(currFont);
        _paint.setTextSize(tam);
        _paint.setTextAlign(Paint.Align.LEFT);
        _canvas.drawText(text, x, y, _paint);
        _paint.reset();
    }

    @Override
    public void drawRect(int x, int y, float width, float height) {
        _paint.setStyle(Paint.Style.STROKE);
        _canvas.drawRect(x, y, x + width, y + width, _paint);
        _paint.reset();
    }

    @Override
    public void fillCircle(int x, int y, float dm) {
        float aux = dm / 2;
        _canvas.drawCircle(x + aux, y + aux, aux, _paint);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        _paint.setStyle(Paint.Style.STROKE);
        _canvas.drawRect(x, y, x + width, y + width, _paint);
        _paint.reset();
    }

    @Override
    public int getWidth() {
        return _winSize.x;
    }

    @Override
    public int getHeight() {
        return _winSize.y;
    }

    @Override
    public void updateGraphics() {
        while (!_holder.getSurface().isValid()) ;
        _canvas = _holder.lockCanvas();
    }

    @Override
    public void save() {

    }

    @Override
    public void restore() {
        _holder.unlockCanvasAndPost(_canvas);
    }

    @Override
    public void translate(int x, int y) {
        try {
            _canvas.translate(x, y);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void scale(float x, float y) {
        _canvas.scale(x, y);
    }

    // ATRIBUTOS
    private final WindowManager _winManager;
    private final Window _mainWin;
    private final Paint _paint;
    private final AssetManager _assetMng;

    private SurfaceView _surface;
    private Canvas _canvas;
    private Point _winSize;
    private SurfaceHolder _holder;
}