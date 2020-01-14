package io.github.rishavb123.broadcastdemo;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    BroadcastReceiver battery;
    BroadcastReceiver connectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.id_text);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(battery);
    }

    @Override
    protected void onResume() {
        super.onResume();
        battery = new BatteryMonitor();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(battery, intentFilter);

        connectivity = new ConnectivityMonitor();
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivity, intentFilter1);
    }

    public class BatteryMonitor extends  BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Charging State Changed", Toast.LENGTH_SHORT).show();
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status)
            {
                case -1:
                    textView.setText("ERROR");
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    textView.setText("FULL");
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    textView.setText("CHARGING");
                    break;
            }
        }
    }

    public class ConnectivityMonitor extends  BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "CONNECTIVITY CHANGED", Toast.LENGTH_SHORT);
            int status = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK, -1);
            switch (status)
            {
                case -1:
                    textView.setText("ERROR");
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    textView.setText("FULL");
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    textView.setText("CHARGING");
                    break;
            }
        }
    }
}
