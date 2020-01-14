package io.github.rishavb123.gameproject;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private GameSurface gameSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameSurface.pause();
        GameSurface.hit.pause();
        GameSurface.backgroundMusic.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameSurface.resume();
    }

}