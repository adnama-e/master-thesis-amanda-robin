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
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by amandaeliasson on 2018-03-12.
 */

public class DriveModeThird extends Fragment implements Observer {
    ImageView image1, image2, image3, image4, image5;
    int picture;
    DataProvider dataProvider;
    int visibility;
    VideoView video;

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
        Button button = (Button) v.findViewById(R.id.videoButton);
        Button databutton = (Button) v.findViewById(R.id.dataButton2);
        image1 = (ImageView) v.findViewById(R.id.cloud1m);
        image2 = (ImageView) v.findViewById(R.id.cloud2m);
        image3 = (ImageView) v.findViewById(R.id.cloud3m);
        image4 = (ImageView) v.findViewById(R.id.cloud4m);
        image5 = (ImageView) v.findViewById(R.id.cloud5m);
        image1.setVisibility(View.INVISIBLE);
        image2.setVisibility(View.INVISIBLE);
        image3.setVisibility(View.INVISIBLE);
        image4.setVisibility(View.INVISIBLE);
        image5.setVisibility(View.INVISIBLE);

        video = (VideoView) v.findViewById(R.id.video);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
       /* video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                video.start();
            }
        });*/
        String uriPath = "android.resource://com.example.amandaeliasson.ecodrivning/" + R.raw.newcar2;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();

       /* button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoView video = (VideoView) v.findViewById(R.id.video);
                String uriPath = "android.resource://com.example.amandaeliasson.ecodrivning/"+R.raw.carmovie;
                Uri uri = Uri.parse(uriPath);
                video.setVideoURI(uri);
                video.requestFocus();
                    video.start();
            }
        });*/
        databutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Measurement m = dataProvider.getMeasurement();
                if (m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() == false) {
                    if (visibility == 0) {
                        image1.setVisibility(View.VISIBLE);
                        visibility = 1;
                    } else if (visibility == 1) {
                        image2.setVisibility(View.VISIBLE);
                        visibility = 2;
                    } else if (visibility == 2) {
                        image3.setVisibility(View.VISIBLE);
                        visibility = 3;
                    } else if (visibility == 3) {
                        image4.setVisibility(View.VISIBLE);
                        visibility = 4;
                    } else if (visibility == 4) {
                        image5.setVisibility(View.VISIBLE);
                        visibility = 5;
                    }
                }
            }
        });

        return v;
    }


    @Override
    public void update(Observable observable, Object o) {

    }
}
