package es.ucm.arblemar.engine;

public class AbstractGraphics {
    protected AbstractGraphics(int w, int h){
        _wLogWindow = w;
        _hLogWindow = h;
        _posLogX = 0.0f;
        _posLogY = 0.0f;
    }
    protected AbstractGraphics() {}

    /**
     * Devuelve el factor de escala de la ventana
     * */
    private float scaleFactor(){
        float wFactor = getWidth() / _wLogWindow;
        float hFactor = getHeight() / _hLogWindow;

        // Nos interesa el factor más pequeño
        return wFactor < hFactor ? wFactor : hFactor;
    }

    /**
     * Dada una posición (x, y) se devuelve el Vector2 transformado
     * al sistema de coordenadas de la ventana
     * */
    public Vector2 realPos(Vector2 pos){
        _scaleFactor = scaleFactor();
        float offsetX = (getWidth() - (_wLogWindow * _scaleFactor)) / 2.0f;
        float offsetY = (getHeight() - (_hLogWindow) * _scaleFactor) / 2.0f;

        int newPosX = (int)((pos._x * _scaleFactor) + offsetX);
        int newPosY = (int)((pos._y * _scaleFactor) + offsetY);

        return new Vector2((int)(newPosX), (int)(newPosY));
    }

    /**
     * Dado tamaño size se devuelve el Vector2 transformado
     * al sistema de coordenadas de la ventana
     * */
    public Vector2 realSize(Vector2 size){
        _scaleFactor = scaleFactor();
        int newW = (int)(size._x * _scaleFactor);
        int newH = (int)(size._y * _scaleFactor);

        return new Vector2(newW, newH);
    }

    /**
     * Dado un tamaño size se devuelve el valor transformado
     * al sistema de coordenadas de la ventana
     * */
    public int realSize(int size){
        _scaleFactor = scaleFactor();
        return (int)(size * _scaleFactor);
    }

    /**
     * Dado una posición pos, se devuelve el valor transformado
     * al sistema de coordenadas lógico
     * */
    public Vector2 logPos(Vector2 pos){
        _scaleFactor = scaleFactor();
        float offsetX = (_wLogWindow - (getWidth() / _scaleFactor)) / 2;
        float offsetY = (_hLogWindow - (getHeight() / _scaleFactor)) / 2;

        int newPosX = (int)((pos._x / _scaleFactor) + offsetX);
        int newPosY = (int)((pos._y / _scaleFactor) + offsetY);

        return new Vector2(newPosX, newPosY);
    }

    //TODO: Borrar esto si no es necesario
    private Vector2 translateWindow() {
        float offsetX = (getWidth() - (_wLogWindow * _scaleFactor)) / 2.0f;
        float offsetY = (getHeight() - (_hLogWindow) * _scaleFactor) / 2.0f;

        int newPosX = (int)((_posLogX * _scaleFactor) + offsetX);
        int newPosY = (int)((_posLogY * _scaleFactor) + offsetY);

        return new Vector2((int)(newPosX), (int)(newPosY));
    }

    public void prepareFrame(){
        _scaleFactor = scaleFactor();
        Vector2 newPos = translateWindow();

        translate(newPos._x, newPos._y);
        scale(_scaleFactor, _scaleFactor);
    }

    @Override
    public int getLogWidth(){
        return (int)_wLogWindow;
    }

    @Override
    public int getLogHeight(){
        return (int)_hLogWindow;
    }

    // Posaición lógica
    protected float _posLogX;
    protected float _posLogY;

    // Dimensión lógica
    protected float _wLogWindow;
    protected float _hLogWindow;

    // Factor de escala
    protected float _scaleFactor;
}