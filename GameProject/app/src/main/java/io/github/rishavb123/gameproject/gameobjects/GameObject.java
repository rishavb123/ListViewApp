package io.github.rishavb123.gameproject.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.SensorEvent;
import android.graphics.Rect;

public abstract class GameObject {

    protected int x;
    protected int y;

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(getBitmap(), getDrawX(), getDrawY(), null);
    }

    public void update(Canvas canvas)
    {
        draw(canvas);
    }

    public abstract Bitmap getBitmap();

    public abstract int getWidth();
    public abstract int getHeight();

    public void sensorEvent(SensorEvent event) {}

    private int getDrawX() {
        return x - getWidth()/2;
    }

    private int getDrawY() {
        return y - getHeight() / 2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getRect() {
        return new Rect(x - getWidth() / 2, y - getHeight() / 2, x + getWidth() / 2, y + getHeight() / 2);
    }

    public void collide(GameObject gameObject) {}

}
