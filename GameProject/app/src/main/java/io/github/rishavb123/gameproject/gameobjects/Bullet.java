package io.github.rishavb123.gameproject.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import io.github.rishavb123.gameproject.GameSurface;
import io.github.rishavb123.gameproject.R;

public class Bullet extends Ball {

    public Bullet(int x, int y, int width, int height, int dx, int dy, Context context) {
        super(x, y, width, height, dx, dy, context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tennis);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        life = 300;
    }

    @Override
    public void collide(GameObject gameObject) {
        super.collide(gameObject);
        if(gameObject instanceof Ball && !(gameObject instanceof Player) && !(gameObject instanceof Bullet))
            GameSurface.gameObjects.remove(gameObject);
    }
}
