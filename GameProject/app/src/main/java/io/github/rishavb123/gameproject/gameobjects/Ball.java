package io.github.rishavb123.gameproject.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import io.github.rishavb123.gameproject.GameSurface;
import io.github.rishavb123.gameproject.R;

public class Ball extends GameObject{

    protected Bitmap bitmap;
    private static final String TAG = "Ball";

    public int dx;
    public int dy;
    protected int width;
    protected int height;

    protected int life = 700;

    public boolean speedUp;

    protected Context context;

    public Ball(int x, int y, int width, int height, int dx, int dy, Context context) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = dx;
        this.dy = dy;
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    @Override
    public void update(Canvas canvas) {

        if(x + dx - getWidth() / 2 <= 0 || x + dx + getWidth() / 2 >= GameSurface.screenWidth)
            dx *= -1;

        if(y + dy - getHeight() / 2 <= 0 || y + dy + getHeight() / 2 > GameSurface.screenHeight)
            dy *= -1;

        x += dx;
        y += dy;
        life--;
        draw(canvas);
        if(life < 0)
            GameSurface.gameObjects.remove(this);
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

}
