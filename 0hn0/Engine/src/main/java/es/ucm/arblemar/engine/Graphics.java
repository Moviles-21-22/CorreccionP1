package es.ucm.arblemar.engine;

public interface Graphics {
    /**
     * Carga una imagen almacenada en el contenedor de recursos de la aplicación a partir de su nombre
     * */
    Image newImage(String name) throws Exception;
    /**
     * Crea una nueva fuente del tamaño especificado a partir de un fichero .ttf. Se indica si se desea o no fuente
     * en negrita
     * */
    Font newFont(String filename, int size, boolean isBold) throws Exception;
    /**
     * Borra el contenido completo de la ventana, rellenándolo con un color recibido como parámetro
     * */
    void clear(int color);
    /**
     * Actualiza el color de las operaciones de renderizado posteriores
     * @param color se espera un color en hexadecimal
     * */
    void setColor(int color);
    /**
     * Actualiza la fuente de las operaciones de renderizado posteriores
     * @param font se espera una fuente tipo Font
     * */
    void setFont(Font font, int tam);
    /**
     * Dibuja una imagen
     * @param x posición X en pantalla
     * @param y posición Y en pantalla
     * */
    void drawImage(Image image, int x, int y, float w, float h);
    /**
     * Dibuja un rectángulo sin relleno
     * @param x Posición X esquina superior izquierda
     * @param y Posición Y esquina superior izquierda
     * @param width Ancho del rectángulo
     * @param height Alto del rectángulo
     * */
    void drawRect(int x, int y, float width, float height);
    /**
     * Dibuja una circunferencia
     * @param x Posición X del centro
     * @param y Posición Y del centro
     * @param r radio de la circunferencia
     * */
    void drawCircle(int x, int y, float r);
    /**
     * Renderiza texto
     * @param text texto a mostrar
     * @param x posición X esquina superior izquierda
     * @param y posición Y esquina superior izquierda
     * */
    void drawText(String text, int x, int y, Font font, int tam);
    /**
     * Dibuja un círculo
     * @param x posición X del centro
     * @param y posición Y del centro
     * @param dm diámetro del círculo
     * */
    void fillCircle(int x, int y, float dm);
    /**
     * Rellena un rectángulo
     * @param x posición X esquina superior izquierda
     * @param y posición Y esquina superior izquierda
     * @param width ancho del rectángulo
     * @param height alto del rectángulo
     * */
    void fillRect(int x, int y, int width, int height);
    /**
     * Obtiene el ancho de la ventana
     * */
    int getWidth();
    /**
     * Obtiene el alto de la ventana
     * */
    int getHeight();
    /**
     * Devuelve el ancho de la ventana lógica
     * */
    public int getLogWidth();
    /**
     * Devuelve el alto de la ventana lógica
     * */
    public int getLogHeight();

    //==============METODOS-CONTROL-CANVAS=================
    /**
     * Actualiza el buffer correspondiente
     * */
    void updateGraphics();
    /**
     * Prepara el frame siguiente para que sea
     * escalado y transladado
     * */
    void prepareFrame();
    /**
     *  Traslada un objeto con una posición "pos"
     *  en función de "winSize"
     * @return Devuelve la posición trasladada
     * */
    void translate(int x, int y);
    /**
     *  Escala un objeto con un tamaño "size" en función de
     *  "winSize"
     * @return Devuelve el tamaño escalado*/
    void scale(float x, float y);
    /**
     *
     * */
    void save();
    /**
     *
     * */
    void restore();
}