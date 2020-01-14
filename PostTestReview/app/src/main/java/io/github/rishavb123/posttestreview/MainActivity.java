package io.github.rishavb123.posttestreview;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.textView)).setText("Hello World from java");

        ((EditText) findViewById(R.id.editText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((TextView) findViewById(R.id.textView)).setText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        ArrayList<String> list= new ArrayList<>();
//        list.add("hi");
//        list.add("hi");
//        list.add("hi");
//        list.add("hi");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
//        ArrayList<Item> list = new ArrayList<Item>();
//        list.add(new Item(1, 2));
//        list.add(new Item(1, 2));
//        list.add(new Item(1, 2));
//        list.add(new Item(1, 2));
//        list.add(new Item(1, 2));
//        list.add(new Item(1, 2));
//        list.add(new Item(1, 2));
//        CustomAdapter adapter = new CustomAdapter(list);
//        ((Spinner)findViewById(R.id.spinner)).setAdapter(adapter);

        RadioGroup rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButton2)
                    ((TextView) findViewById(R.id.textView)).setText("You clicked 2");
            }
        });

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("key", "value");
//        startActivity(intent);
//        startActivityForResult(intent, 1234);
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//            if(requestCode == NUMBER_CODE && resultCode == RESULT_OK)
//                number.setText(data.getStringExtra("key"));
//        }

    }

    class Item {
        int x1;
        int x2;
        public Item(int x1, int x2)
        {
            this.x1 = x1;
            this.x2 = x2;
        }
    }

    class CustomAdapter extends  ArrayAdapter<Item>{
        ArrayList<Item> list;
        public CustomAdapter(ArrayList<Item> l)
        {
            super(MainActivity.this, R.layout.test, l);
            list = l;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.test, parent, false);
            TextView textView = (TextView) row.findViewById(R.id.textView2);
            textView.setText(list.get(position).x1 + " " + list.get(position).x2);
            return textView;
        }
    }
}
