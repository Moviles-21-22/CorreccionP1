package es.ucm.arblemar.engine;

public abstract class AbstractGraphics implements Graphics {
    protected AbstractGraphics(int w, int h) {
        _wLogWindow = w;
        _hLogWindow = h;
        _posLogX = 0.0f;
        _posLogY = 0.0f;
    }

    protected AbstractGraphics() {
    }

    /**
     * Devuelve el factor de escala de la ventana
     */
    private float scaleFactor() {
        float wFactor = getWidth() / _wLogWindow;
        float hFactor = getHeight() / _hLogWindow;

        // Nos interesa el factor más pequeño
        return Math.min(wFactor, hFactor);
    }

    /**
     * Dada una posición P(x, y) se devuelve el vector transformado
     * al sistema de coordenadas de la ventana
     */
    public int[] realPos(int x, int y) {
        _scaleFactor = scaleFactor();
        float offsetX = (getWidth() - (_wLogWindow * _scaleFactor)) / 2.0f;
        float offsetY = (getHeight() - (_hLogWindow) * _scaleFactor) / 2.0f;

        int newPosX = (int) ((x * _scaleFactor) + offsetX);
        int newPosY = (int) ((y * _scaleFactor) + offsetY);

        int[] newPos = new int[2];
        newPos[0] = newPosX;
        newPos[1] = newPosY;
        return newPos;
    }

    /**
     * Dado un tamaño S(w, h) se devuelve el tamaño transformado
     * al sistema de coordenadas de la ventana
     */
    public int[] realSize(float w, float h) {
        _scaleFactor = scaleFactor();
        int newW = (int) (w * _scaleFactor);
        int newH = (int) (h * _scaleFactor);

        int[] newSize = new int[2];
        newSize[0] = newW;
        newSize[1] = newH;
        return newSize;
    }

    /**
     * Dado un tamaño size se devuelve el valor transformado
     * al sistema de coordenadas de la ventana
     */
    public int realSize(float size) {
        _scaleFactor = scaleFactor();
        return (int) (size * _scaleFactor);
    }

    /**
     * Dado una posición P(x, y), se devuelve el valor transformado
     * al sistema de coordenadas lógico
     */
    public int[] logPos(int x, int y) {
        _scaleFactor = scaleFactor();
        float offsetX = (_wLogWindow - (getWidth() / _scaleFactor)) / 2;
        float offsetY = (_hLogWindow - (getHeight() / _scaleFactor)) / 2;

        int newPosX = (int) ((x / _scaleFactor) + offsetX);
        int newPosY = (int) ((y / _scaleFactor) + offsetY);

        int[] newPos = new int[2];
        newPos[0] = newPosX;
        newPos[1] = newPosY;

        return newPos;
    }

    private int[] translateWindow() {
        float offsetX = (getWidth() - (_wLogWindow * _scaleFactor)) / 2.0f;
        float offsetY = (getHeight() - (_hLogWindow) * _scaleFactor) / 2.0f;

        int newPosX = (int) ((_posLogX * _scaleFactor) + offsetX);
        int newPosY = (int) ((_posLogY * _scaleFactor) + offsetY);

        int[] newPos = new int[2];
        newPos[0] = newPosX;
        newPos[1] = newPosY;

        return newPos;
    }

    public void prepareFrame() {
        _scaleFactor = scaleFactor();
        int[] newPos = translateWindow();

        translate(newPos[0], newPos[1]);
        scale(_scaleFactor, _scaleFactor);
    }

    @Override
    public int getLogWidth() {
        return (int) _wLogWindow;
    }

    @Override
    public int getLogHeight() {
        return (int) _hLogWindow;
    }

    // Posición lógica
    protected float _posLogX;
    protected float _posLogY;

    // Dimensión lógica
    protected float _wLogWindow;
    protected float _hLogWindow;

    // Factor de escala
    protected float _scaleFactor;
}