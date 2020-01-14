package io.github.rishavb123.musicdemo;

import android.media.MediaPlayer;
import android.media.TimedText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button pause;
    Button stop;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pause = findViewById(R.id.pause_btn);
        stop = findViewById(R.id.stop_btn);

        mediaPlayer = MediaPlayer.create(this, R.raw.audio);
        ((SeekBar) findViewById(R.id.seekBar)).setMax(mediaPlayer.getDuration());


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                else
                    mediaPlayer.start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
//                    mediaPlayer.stop();
//                    try {
//                        mediaPlayer.prepare();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
                else
                    mediaPlayer.start();

            }
        });

        new Thread() {
            @Override
            public void run() {
                super.run();
                while(true) {

                    findViewById(R.id.textView).post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.textView)).setText("" + mediaPlayer.getCurrentPosition()/1000.0);
                        }
                    });

                    findViewById(R.id.seekBar).post(new Runnable() {
                        @Override
                        public void run() {
                            ((SeekBar) findViewById(R.id.seekBar)).setProgress(mediaPlayer.getCurrentPosition());
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
