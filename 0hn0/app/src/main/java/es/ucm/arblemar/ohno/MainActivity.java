package es.ucm.arblemar.ohno;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import es.ucm.arblemar.androidengine.AndroidEngine;
import es.ucm.arblemar.gamelogic.states.LoadState;

public class MainActivity extends AppCompatActivity {

    private AndroidEngine engine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        engine = new AndroidEngine(this,400,600);
        LoadState assets = new LoadState(engine);
        if(!engine.init(assets,"oh no",400,600)){
            System.err.println("Error al inicializar el engine");
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        engine.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        engine.onPause();
    }
}