package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amandaeliasson on 2018-03-19.
 */

public class DriveModeFlower extends Fragment implements Observer {
    FlowerView image1, image2, image3;
    Button button;
    RelativeLayout layout;
    DataProvider dataProvider;
    Timer time;

    int picture, picture2, picture3;

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
        View v = inflater.inflate(R.layout.drivemodeflower, container, false);
        button = (Button) v.findViewById(R.id.flowerdata);
        image1 = (FlowerView) v.findViewById(R.id.flower1);
        image1.setVisibility(View.INVISIBLE);
        image2 = (FlowerView) v.findViewById(R.id.flower2);
        image2.setVisibility(View.INVISIBLE);
        image2.setFlowerToWaitFor(image1);
        image3 = (FlowerView) v.findViewById(R.id.flower3);
        image3.setVisibility(View.INVISIBLE);
        image3.setFlowerToWaitFor(image2);
        time = new Timer();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Measurement m = dataProvider.getMeasurement();

                        if (m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() == true) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image1.grow();
                                    image2.grow();
                                    image3.grow();
                                }
                            });
                        }
                    }
                }, 0, 1000);

            }
        });
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
