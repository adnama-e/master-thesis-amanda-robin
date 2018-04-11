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

import java.sql.Time;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amandaeliasson on 2018-03-16.
 */

public class DriveModeFourth extends Fragment implements Observer {
    FourthView image1, image2, image3, image4, image5;
    int picture;
    DataProvider dataProvider;
    int visibility;
    Timer time;

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
        image1 = (FourthView)v.findViewById(R.id.cloud1);
        image2 = (FourthView) v.findViewById(R.id.cloud2);
        image3 = (FourthView) v.findViewById(R.id.cloud3);
        image4 = (FourthView) v.findViewById(R.id.cloud4);
        image5 = (FourthView) v.findViewById(R.id.cloud5);
        image1.setVisibility(View.INVISIBLE);

        image2.setVisibility(View.INVISIBLE);
        image2.setFlowerToWaitFor(image1);
        image3.setVisibility(View.INVISIBLE);
        image3.setFlowerToWaitFor(image2);
        image4.setVisibility(View.INVISIBLE);
        image4.setFlowerToWaitFor(image3);
        image5.setVisibility(View.INVISIBLE);
        image5.setFlowerToWaitFor(image4);
        final ImageView image = (ImageView) v.findViewById(R.id.carImage1);
        picture = (R.drawable.carmoving2);
        image.setImageResource(picture);
        time = new Timer();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Measurement m = dataProvider.getMeasurement();
                        if(m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() ==false) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image1.grow();
                                    image2.grow();
                                    image3.grow();
                                    image4.grow();
                                    image5.grow();
                                }
                            });
                        }
                    }
                },0,1000);

               }});
        return v;
    }
    public void onPause() {
        time.cancel();
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}