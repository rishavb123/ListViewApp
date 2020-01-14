package io.bhagat.audioproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Client client;
    private final int SAMPLE_RATE = 44100; // The sampling rate
    private volatile boolean mShouldContinue; // Indicates if recording / playback should stop

    private static final String TAG = "MainActivityTag";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},
                    1234);
        }
        else {
            main();
        }


    }

    public void main() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                client = new Client("192.168.1.33", 8000);
                client.start();
            }
        });

        findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mShouldContinue) {
                    send("Starting Recording . . . ");
                    mShouldContinue = true;
                    recordAudio();
                }
                else {
                    mShouldContinue = false;
                }

            }
        });
    }

    public void recordAudio() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

                // buffer size in bytes
                int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
                    bufferSize = SAMPLE_RATE * 2;
                }

                short[] audioBuffer = new short[bufferSize / 2];

                AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize);

                if (record.getState() != AudioRecord.STATE_INITIALIZED) {
                    send("Audio Record can't initialize!");
                    return;
                }
                record.startRecording();

                send("Start recording loop");

                long shortsRead = 0;
                while (mShouldContinue) {
                    int numberOfShort = record.read(audioBuffer, 0, audioBuffer.length);
                    shortsRead += numberOfShort;
                    send(audioBuffer);
                }

                record.stop();
                record.release();

                send(String.format("Recording stopped. Samples read: %d", shortsRead));
            }
        }).start();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.close();
    }

    public void send(final Object obj)
    {
        runAsync(new Runnable() {
            @Override
            public void run() {
                client.send(obj);
            }
        });
    }

    public void runAsync(Runnable runnable)
    {
        new RunAsync().execute(runnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1234)
        {
            if(grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                main();
            else
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},
                        1234);
        }

    }

    class RunAsync extends AsyncTask<Runnable, Void, Void> {

        @Override
        protected Void doInBackground(Runnable... runnables) {

            runnables[0].run();

            return null;
        }
    }

}
