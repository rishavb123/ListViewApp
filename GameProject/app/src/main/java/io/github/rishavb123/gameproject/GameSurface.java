package io.github.rishavb123.gameproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

import io.github.rishavb123.gameproject.gameobjects.Ball;
import io.github.rishavb123.gameproject.gameobjects.Bullet;
import io.github.rishavb123.gameproject.gameobjects.GameObject;
import io.github.rishavb123.gameproject.gameobjects.Player;

public class GameSurface extends SurfaceView implements Runnable {

    private Context context;

    Thread gameThread;
    SurfaceHolder holder;
    public static Handler handler;

    volatile boolean running = false;
    volatile boolean speedUp = false;

    public static MediaPlayer backgroundMusic;
    public static MediaPlayer hit;

    Paint paintProperty;

    public static volatile int score = 0;

    public static int screenWidth;
    public static int screenHeight;

    int[] colors;
    int colorState;

    public static ArrayList<GameObject> gameObjects;

    public GameSurface(final Context context) {
        super(context);

        this.context = context;

        backgroundMusic = MediaPlayer.create(context, R.raw.background);
        hit = MediaPlayer.create(context, R.raw.hit);

        backgroundMusic.setVolume(0.3f, 0.3f);
        hit.setVolume(3f,3f);
        backgroundMusic.start();
        backgroundMusic.setLooping(true);

        colors = new int[]{0, 0, 0};

        holder=getHolder();
        handler = new Handler();

        Display screenDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point sizeOfScreen = new Point();
        screenDisplay.getSize(sizeOfScreen);
        screenWidth=sizeOfScreen.x;
        screenHeight=sizeOfScreen.y;

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                for(GameObject gameObject: gameObjects)
                    gameObject.sensorEvent(event);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        paintProperty= new Paint();
        paintProperty.setTextSize(30);
        paintProperty.setColor(Color.BLUE);

        gameObjects = new ArrayList<>();
        gameObjects.add(new Player(screenWidth / 2, screenHeight / 2  , 100, 100, context ));

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!speedUp) {
                    for (int i = 0; i < gameObjects.size(); i++) {
                        GameObject gameObject = gameObjects.get(i);
                        if (gameObject instanceof Ball && !(gameObject instanceof Player)) {
                            Ball ball = (Ball) gameObject;
                            ball.setDx(ball.getDx() * 2);
                            ball.setDy(ball.getDy() * 2);
                            ball.speedUp = true;
                        }
                    }
                    speedUp = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < gameObjects.size(); i++) {
                                GameObject gameObject = gameObjects.get(i);
                                if (gameObject instanceof Ball && !(gameObject instanceof Player) && ((Ball) gameObject).speedUp) {
                                    Ball ball = (Ball) gameObject;
                                    ball.setDx(ball.getDx() / 2);
                                    ball.setDy(ball.getDy() / 2);
                                    ball.speedUp = false;
                                }
                            }
                            speedUp = false;
                            score *= 1.1;
                        }
                    }, 1000);
                }
                else {
                    gameObjects.add(new Bullet(gameObjects.get(0).getX(), gameObjects.get(0).getY(), 100, 100, 3 * ((Player)gameObjects.get(0)).dx, 3 * ((Player)gameObjects.get(0)).dy, context));
                }
                return false;
            }
        });

    }

    @Override
    public void run() {
        long startTime = new Date().getTime();
        while (new Date().getTime() - startTime < 60000){
            if (!holder.getSurface().isValid())
                continue;
            Canvas canvas = holder.lockCanvas();
            if(!speedUp) {
                switch (colorState) {
                    case 0:
                        colors[0]++;
                        if (colors[0] == 255)
                            colorState++;
                        break;
                    case 1:
                        colors[1]++;
                        colors[0]--;
                        if (colors[1] == 255)
                            colorState++;
                        break;
                    case 2:
                        colors[2]++;
                        if (colors[2] == 255)
                            colorState++;
                        break;
                    case 3:
                        colors[0]++;
                        if (colors[0] == 255)
                            colorState++;
                        break;
                    case 4:
                        colors[0]--;
                        colors[1]--;
                        colors[2]--;
                        if (colors[0] == 0)
                            colorState = 0;
                        break;
                }
                canvas.drawRGB(colors[0],colors[1],colors[2]);
            }
            else
                canvas.drawRGB((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));

            if(Math.random() < score/100000.0 + 0.005) {
                int rand = (int)(4*Math.random());
                switch (rand)
                {
                    case 0:
                        gameObjects.add(new Ball(100, 100, 50, 50, (int)(Math.random() * 10) - 5, (int) (Math.random() * 10) - 5, context));
                    case 1:
                        gameObjects.add(new Ball(screenWidth - 100, 100, 50, 50, (int)(Math.random() * 10) - 5, (int) (Math.random() * 10) - 5, context));
                    case 2:
                        gameObjects.add(new Ball(screenWidth - 100, screenWidth - 100, 50, 50, (int)(Math.random() * 10) - 5, (int) (Math.random() * 10) - 5, context));
                    case 3:
                        gameObjects.add(new Ball(100, screenHeight - 100, 50, 50, (int)(Math.random() * 10) - 5, (int) (Math.random() * 10) - 5, context));
                }
            }
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject gameObject = gameObjects.get(i);
                gameObject.update(canvas);
                for(int j = 0; j < gameObjects.size(); j++)
                    if(gameObject.getRect().intersect(gameObjects.get(j).getRect()) && gameObject != gameObjects.get(j))
                        gameObject.collide(gameObjects.get(j));
            }
            canvas.drawText("Score: "+score, 100, screenHeight - 100, paintProperty);
            canvas.drawText("Time Left: "+(60 - (int)((new Date().getTime() - startTime) / 1000)), 100, 100, paintProperty);

            score ++;
            holder.unlockCanvasAndPost(canvas);
        }
        Canvas canvas= holder.lockCanvas();
        paintProperty.setTextSize(100);
        canvas.drawRGB(colors[0],colors[1],colors[2]);
        canvas.drawText("Score: "+score, 200, 400, paintProperty);
        holder.unlockCanvasAndPost(canvas);
    }

    public void resume(){
        running=true;
        gameThread=new Thread(this);
        gameThread.start();
    }

    public void pause() {
        running = false;
        while (true) {
            try{
            gameThread.join();
            } catch (InterruptedException e) {
            }
        }
    }
}
