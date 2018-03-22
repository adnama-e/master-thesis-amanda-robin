package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by amandaeliasson on 2018-03-16.
 */

public class DriveModeFourth extends Fragment implements Observer {
    ImageView image1, image2, image3, image4, image5;
    int picture;
    DataProvider dataProvider;
    int visibility;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dataProvider = (DataProvider) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
        dataProvider.addObserver(this);
        visibility = 0;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.drivemodefourth, container, false);
        Button b = (Button)v.findViewById(R.id.button_change);
        image1 = (ImageView)v.findViewById(R.id.cloud1);
        image2 = (ImageView) v.findViewById(R.id.cloud2);
        image3 = (ImageView) v.findViewById(R.id.cloud3);
        image4 = (ImageView) v.findViewById(R.id.cloud4);
        image5 = (ImageView) v.findViewById(R.id.cloud5);
        image1.setVisibility(View.INVISIBLE);
        image2.setVisibility(View.INVISIBLE);
        image3.setVisibility(View.INVISIBLE);
        image4.setVisibility(View.INVISIBLE);
        image5.setVisibility(View.INVISIBLE);
        final ImageView image = (ImageView) v.findViewById(R.id.carImage1);
        picture = (R.drawable.carmoving2);
        image.setImageResource(picture);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Measurement m = dataProvider.getMeasurement();
                if(m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() ==false) {
                    if(visibility ==0){
                        image1.setVisibility(View.VISIBLE);
                        visibility = 1;
                    }else if(visibility ==1){
                        image2.setVisibility(View.VISIBLE);
                        visibility =2;
                    }else if(visibility ==2){
                        image3.setVisibility(View.VISIBLE);
                        visibility =3;
                    }else if(visibility ==3){
                        image4.setVisibility(View.VISIBLE);
                        visibility = 4;
                    }else if(visibility == 4){
                        image5.setVisibility(View.VISIBLE);
                        visibility = 5;
                    }
                }}});
        return v;
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}