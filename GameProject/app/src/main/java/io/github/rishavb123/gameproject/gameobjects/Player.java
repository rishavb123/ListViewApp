package io.github.rishavb123.gameproject.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.SensorEvent;

import io.github.rishavb123.gameproject.GameSurface;
import io.github.rishavb123.gameproject.R;

public class Player extends Ball {

    public Player(int x, int y, int width, int height, Context context) {
        super(x, y, width, height, 0, 0, context);
    }

    @Override
    public void sensorEvent(SensorEvent event) {
        dx += event.values[1];
        dy += event.values[0];
    }

    @Override
    public void collide(GameObject gameObject) {
        if(!(gameObject instanceof Bullet)) {
            if (GameSurface.score >= 500)
                GameSurface.score -= 500;
            else
                GameSurface.score = 0;
            if (!GameSurface.hit.isPlaying())
                GameSurface.hit.start();
            else {
                GameSurface.hit.seekTo(0);
            }
            GameSurface.gameObjects.remove(gameObject);
            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.broken), width, height, false);
            GameSurface.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ball), width, height, false);
                }
            }, 300);
        }
    }

    @Override
    public void update(Canvas canvas) {
        if(x + dx - getWidth() / 2 <= 0 || x + dx + getWidth() / 2 >= GameSurface.screenWidth)
            dx *= -1;

        if(y + dy - getHeight() / 2 <= 0 || y + dy + getHeight() / 2 > GameSurface.screenHeight)
            dy *= -1;

        x += dx;
        y += dy;
        draw(canvas);
    }
}
