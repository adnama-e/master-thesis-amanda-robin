package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by amandaeliasson on 2018-03-12.
 */

public class DriveModeSmile extends Fragment implements Observer {
    ImageView image;
    RelativeLayout layout;
    Button b;
    DataProvider dataProvider;
    int picture;
    @SuppressLint("ResourceAsColor")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dataProvider = (DataProvider) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
        dataProvider.addObserver(this);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.drivemodesmile, container, false);
        image = (ImageView)v.findViewById(R.id.smileyId);
        picture = R.drawable.emoji1;
        image.setImageResource(picture);
        layout  = (RelativeLayout)v.findViewById(R.id.background);
        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greenS));
        b = (Button)v.findViewById(R.id.dataButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Measurement m = dataProvider.getMeasurement();
                if(m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() ==false){
                    if(picture == (R.drawable.emoji1)){
                        picture = R.drawable.emoji2;
                        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.LimeS));
                    }
                    else if (picture == (R.drawable.emoji2)){
                        picture = R.drawable.emoji3;
                        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellowS));
                    }
                    else if(picture == (R.drawable.emoji3)){
                        picture = R.drawable.emoji4;
                        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));

                    }else if(picture == (R.drawable.emoji4)){
                        picture = R.drawable.emoji5;
                        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));

                    }
                    image.setImageResource(picture);
                }else{}
            }
            });
        return v;
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
