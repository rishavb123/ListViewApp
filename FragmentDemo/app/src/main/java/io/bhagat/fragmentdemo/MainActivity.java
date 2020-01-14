package io.bhagat.fragmentdemo;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TopFragment.ReceiveString{

    TextView textView;
    Button button;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.id_main_button);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.id_top_frame, new TopFragment());
        fragmentTransaction.add(R.id.id_bot_frame, new BottomFragment());

        fragmentTransaction.commit();
    }


    @Override
    public void receive(String str) {
        textView.setText(str + " -interface used-");
    }
}
