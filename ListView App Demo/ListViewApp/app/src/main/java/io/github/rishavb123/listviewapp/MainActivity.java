package io.github.rishavb123.listviewapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ArrayList<AppItem> arrayList;
    private ImageView selectedImage;
    private TextView selectedText;
    private EditText editText;
    private AppItem selectedItem;
    private TextToSpeech tts;

    private CustomAdapter customAdapter;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ListView listView = findViewById(R.id.listView);
        selectedImage = findViewById(R.id.icon);
        selectedText = findViewById(R.id.description);
        AppItem selectedItem = (savedInstanceState != null)? (AppItem) savedInstanceState.getSerializable("selected") : null;

        tts = new TextToSpeech(this, null);

        tts.setLanguage(Locale.US);

        int[] images = {
                R.drawable.aim,
                R.drawable.brown,
                R.drawable.chrome,
                R.drawable.facebook,
                R.drawable.messenger,
                R.drawable.pintrest,
                R.drawable.purple,
                R.drawable.skype,
                R.drawable.target,
                R.drawable.weather
        };

        String[] names = {
                "Aim",
                "Brown",
                "Chrome",
                "Facebook",
                "Messenger",
                "Pintrest",
                "Purple Wallpapers",
                "Skype",
                "Target",
                "Weather"
        };

        String[] packageNames = {
                "medindia4u.aim.app.main",
                "com.MedievalRim.Brown",
                "com.android.chrome",
                "com.facebook.katana",
                "com.facebook.orca",
                "com.pinterest",
                "com.andromo.dev230101.app316721",
                "com.skype.raider",
                "com.target.ui",
                "com.live.wea.widget.channel"
        };

        String[] categories = {
                "Aiming",
                "Color",
                "Web",
                "Social",
                "Social",
                "Pining",
                "Color",
                "Social",
                "Aiming",
                "Tracking"
        };

        double[] prices = {
                99.99,
                100,
                100.01,
                0,
                6.66,
                12.80,
                0,
                20,
                80.80,
                1
        };

        String[] features = {
                "This is an aiming app",
                "The color brown",
                "The best browser",
                "Social Media that I do not have",
                "Made it for a group chat",
                "Pine interest",
                "The purple color, but on wallpapers. It's amazing!",
                "Skip hype",
                "The target for the aiming app",
                "App that stores your location"
        };



        if(savedInstanceState == null)
        {
            arrayList = createAppItems(images, names, categories, features, prices, packageNames);
            arrayList.add(0, new AppItem("DOWNLOADABLE"));
            arrayList.add(new AppItem("DOWNLOADED"));
            arrayList.addAll(getAppItems());
        }
        else
            arrayList = (ArrayList<AppItem>) savedInstanceState.getSerializable("list");


        customAdapter = new CustomAdapter(this, R.layout.custom_layout, arrayList);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideSoftKeyboard();
                select(i);
                tts.speak((String) (((TextView) view.findViewById(R.id.textView))).getText(), TextToSpeech.QUEUE_ADD, null);
            }
        });

        if(selectedItem != null)
            select((arrayList.indexOf(selectedItem) >= 0)? arrayList.indexOf(selectedItem): -1);

        editText = findViewById(R.id.search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    hideSoftKeyboard();
                    return true;
                }

                return false;
            }
        });

        hideSoftKeyboard();
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
            }
        });

    }

    public void hideSoftKeyboard(){
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void select(int i)
    {
        if(i<0) return;
        selectedItem = customAdapter.getItem(i);
        if(selectedItem.isDownloaded())
            selectedImage.setImageDrawable(selectedItem.getImage());
        else
            selectedImage.setImageResource(selectedItem.getImageResource());
        String description;
        if(0 == selectedItem.getPrice())
            description = selectedItem.getAppName()+":\n\nPrice: $0.00 (FREE)";
        else
            description = selectedItem.getAppName()+":\n\nPrice: $"+(int)selectedItem.getPrice()+"." +Integer.toString((int)(selectedItem.getPrice()*100)).substring(Integer.toString((int)(selectedItem.getPrice()*100)).length() - 2);

        description += "\n\nCategory: "+selectedItem.getCategory();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            description += "\n\nDescription: "+selectedItem.getFeatures();

        selectedText.setText(description);
    }

    public ArrayList<AppItem> createAppItems(int[] images, String[] names, String[] categories, String[] features, double[] prices, String[] packageNames)
    {
        ArrayList<AppItem> list = new ArrayList<>();

        for(int i = 0; i < images.length; i++)
        {
            list.add(new AppItem(images[i], names[i], categories[i], features[i], prices[i], packageNames[i]));
        }
        return list;
    }

    public ArrayList<AppItem> getAppItems()
    {
        ArrayList<AppItem> list = new ArrayList<>();

        int flags = PackageManager.GET_META_DATA |
                PackageManager.GET_SHARED_LIBRARY_FILES |
                PackageManager.GET_UNINSTALLED_PACKAGES;

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(flags);

        for (ApplicationInfo applicationInfo : apps) {
            String name = (String) pm.getApplicationLabel(applicationInfo);
            String packageName = applicationInfo.processName;
            String feature = (String) applicationInfo.loadDescription(pm);
            String category = (String) applicationInfo.loadDescription(pm);
            Drawable icon = null;
            try {
                icon = getPackageManager().getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            list.add(new AppItem(icon, name, category, feature, packageName));
        }
        return list;
    }


    public int getOrientation()
    {
        return getResources().getConfiguration().orientation;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putSerializable("selected", selectedItem);
        outState.putSerializable("list", arrayList);
    }

}
