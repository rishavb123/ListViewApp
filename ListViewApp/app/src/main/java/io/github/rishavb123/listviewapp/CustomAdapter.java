package io.github.rishavb123.listviewapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<AppItem> {

    private ArrayList<AppItem> originalList;
    private Context context;
    private ArrayList<AppItem> list;
    private int resource;



    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AppItem> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        list = objects;
        originalList = objects;

    }

    @Override
    public boolean isEnabled(int position)
    {
        return !getItem(position).isSeperator();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public AppItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<AppItem> filtered = new ArrayList<>();

                if(constraint == null || constraint.length() == 0)
                {
                    results.count = originalList.size();
                    results.values = originalList;
                }
                else {
                    constraint = constraint.toString().toLowerCase();
                    for(AppItem a: originalList)
                    {
                        String data = a.getAppName()+" "+a.getCategory()+" "+a.getFeatures();
                        if(data.toLowerCase().contains(constraint) || a.isSeperator())
                        {
                            filtered.add(a);
                        }
                    }
                    results.count = filtered.size();
                    results.values = filtered;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<AppItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(resource, null);
        ImageView imageView = adapterLayout.findViewById(R.id.imageView);
        TextView textView = adapterLayout.findViewById(R.id.textView);
        Button button = adapterLayout.findViewById(R.id.button);
        Button toPlayStore = adapterLayout.findViewById(R.id.play_btn);
        if(!list.get(position).isSeperator()) {

            if (list.get(position).isDownloaded())
                imageView.setImageDrawable(list.get(position).getImage());
            else
                imageView.setImageResource(list.get(position).getImageResource());
            textView.setText(list.get(position).getAppName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getContext()).hideSoftKeyboard();
                    originalList.remove(list.remove(position));
                    notifyDataSetChanged();
                }
            });

            toPlayStore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+list.get(position).getPackageName()));
                    intent.setPackage("com.android.vending");
                    getContext().startActivity(intent);
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(list.get(position).getPackageName());
                        getContext().startActivity(intent);
                    } catch (NullPointerException e)
                    {
                        Toast toast = Toast.makeText(getContext(), "Could Not Open App", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 100);
                        toast.show();
                    }
                }
            });
        }
        else {
            textView.setText(list.get(position).getAppName());
            textView.setTextColor(getContext().getResources().getColor(R.color.white));
            adapterLayout.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            ((ViewManager) adapterLayout).removeView(imageView);
            ((ViewManager) adapterLayout).removeView(button);
            ((ViewManager) adapterLayout).removeView(toPlayStore);
        }

        return adapterLayout;
    }
}
