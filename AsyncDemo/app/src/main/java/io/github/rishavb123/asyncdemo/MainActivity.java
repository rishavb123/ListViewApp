package io.github.rishavb123.asyncdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        (new AsyncThread()).execute();
        for(int i = 0; i< 1000; i++)
            Log.d("TAG-Main", ""+i);
    }

    public class AsyncThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0; i< 1000; i++)
                Log.d("TAG-Thread", ""+i);
            return null;
        }
    }

}
