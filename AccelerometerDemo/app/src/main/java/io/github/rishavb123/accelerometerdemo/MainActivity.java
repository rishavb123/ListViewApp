package io.github.rishavb123.accelerometerdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textViewX;
    TextView textViewY;
    TextView textViewZ;

    private int brightness;
    private ContentResolver contentResolver;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewX = findViewById(R.id.textViewX);
        textViewY = findViewById(R.id.textViewY);
        textViewZ = findViewById(R.id.textViewZ);

        window = getWindow();
        contentResolver = getContentResolver();

        System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        try {
            brightness = System.getInt(contentResolver, System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if(Settings.System.canWrite(this))
        {
            Toast.makeText(this, "Write allowed ", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Write not allowed", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                textViewX.setText(Float.toString(values[0]));
                textViewY.setText(Float.toString(values[1]));
                textViewZ.setText(Float.toString(values[2]));

                float y = values[1];

                float c = 10;
                float change = (int) y*c;
                Log.d("TESTTEST", ""+(brightness - change));

                if(brightness - change*c > 0 && brightness - change*c < 255) {
                    brightness -= change;
                    System.putInt(contentResolver, System.SCREEN_BRIGHTNESS, brightness);
//                    WindowManager.LayoutParams layoutParams = window.getAttributes();
//                    layoutParams.screenBrightness = brightness / 255f;
//                    window.setAttributes(layoutParams);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
}
