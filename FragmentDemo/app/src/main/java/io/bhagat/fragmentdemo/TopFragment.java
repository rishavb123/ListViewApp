package io.bhagat.fragmentdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotations.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TopFragment extends Fragment{

    TextView textTitle;
    TextView textInfo;

    Button button;

    ReceiveString receiveString;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_top, null);

        textInfo = fragmentView.findViewById(R.id.id_text_info_bottom);
        textTitle = fragmentView.findViewById(R.id.id_text_title_bottom);

        button = fragmentView.findViewById(R.id.id_button_top);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInfo.setText("Button Clicked");
//                ((TextView)getActivity().findViewById(R.id.id_main_text)).setText("Button Clicked");
                receiveString.receive("Clicked");
            }
        });

        return fragmentView;
    }

    public interface ReceiveString {
        public void receive(String str);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        receiveString = (ReceiveString) context;
    }
}
