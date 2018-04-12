package com.example.amandaeliasson.ecodrivning;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.VideoView;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amandaeliasson on 2018-03-12.
 */

public class DriveModeThird extends Fragment implements Observer {
    CloudView image1, image2, image3, image4, image5;
    int picture;
    DataProvider dataProvider;
    int visibility;
    VideoView video;
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
        final View v = inflater.inflate(R.layout.drivemodethird, container, false);

        Button databutton =v.findViewById(R.id.dataButton2);
        image1 =  v.findViewById(R.id.cloud1m);
        image2 =  v.findViewById(R.id.cloud2m);
        image2.setFlowerToWaitFor(image1);
        image3 = v.findViewById(R.id.cloud3m);
        image3.setFlowerToWaitFor(image2);
        image4 =  v.findViewById(R.id.cloud4m);
        image4.setFlowerToWaitFor(image3);
        image5 =  v.findViewById(R.id.cloud5m);
        image5.setFlowerToWaitFor(image4);

        video = v.findViewById(R.id.video);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        String uriPath = "android.resource://com.example.amandaeliasson.ecodrivning/" + R.raw.carmoving9;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
        time = new Timer();
        databutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Measurement m = dataProvider.getMeasurement();
                        if (m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() == false) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image5.grow();
                                    image4.grow();
                                    image3.grow();
                                    image2.grow();
                                    image1.grow();
                                }
                            });
                        }else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    image1.unGrow();
                                    image2.unGrow();
                                    image3.unGrow();
                                    image4.unGrow();
                                    image5.unGrow();
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
