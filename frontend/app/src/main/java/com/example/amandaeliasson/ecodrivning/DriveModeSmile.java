package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amandaeliasson on 2018-03-12.
 */

public class DriveModeSmile extends Fragment implements Observer {
    SmileView image;
    RelativeLayout layout;
    Button b;
    DataProvider dataProvider;
    int picture;
    Timer time;
    //private ImageSwitcher imageSwitcher;
    @SuppressLint("ResourceAsColor")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dataProvider = (DataProvider) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
        dataProvider.addObserver(this);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drivemodesmile, container, false);
        image = v.findViewById(R.id.smileyId);
        picture = R.drawable.smiley_happy2;
        image.setImageResource(picture);
        time = new Timer();
        b = v.findViewById(R.id.dataButton);
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
                                    image.change();
                                }
                            });

                        }else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image.changeBack();
                                }
                            });
                        }
                    }
                }, 0, 1000);;

            }
            });
        return v;
    }

    @Override
    public void onPause() {
        time.cancel();
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
