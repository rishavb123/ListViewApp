package io.bhagat.fragmentdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BottomFragment extends Fragment {

    TextView textTitle;
    TextView textInfo;

    Button button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_bottom, null);

        textInfo = fragmentView.findViewById(R.id.id_text_info_bottom);
        textTitle = fragmentView.findViewById(R.id.id_text_title_bottom);

        button = fragmentView.findViewById(R.id.id_button_bottom);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInfo.setText("Button Clicked");
                ((TextView)getActivity().findViewById(R.id.id_main_text)).setText("Button Clicked");
            }
        });

        return fragmentView;
    }
}
