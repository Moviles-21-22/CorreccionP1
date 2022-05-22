package es.ucm.arblemar.engine;

import java.util.List;

/**
 * Interfaz del input. Contiene un enmuerado con 3 estados para procesar cuando se pulsa la pantalla,
 * cuando se levanta el input de la pantalla y cuando se arrastra el input por la pantalla.
 */
public interface Input {
    enum TouchEvent {
        touchDown(0),
        touchUp(1),
        touchDrag(2);

        private int x, y;
        private final int value;

        public void setX(int _x){
            x = _x;
        }
        public void setY(int _y){
            y = _y;
        }

        public int getValue() {
            return value;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }

        TouchEvent(int i) {
            this.value = i;
        }
    }

    List<TouchEvent> GetTouchEvents();
}