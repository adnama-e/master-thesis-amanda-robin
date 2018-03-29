package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import java.util.Timer;
import java.util.TimerTask;


public class DriveModeSmile extends Fragment implements Observer {
    ImageView image;
    RelativeLayout layout;
    Button b;
    int picture;
    DataHandler dataHandler;
    Analyzer analyzer;

    @SuppressLint("ResourceAsColor")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssetManager am = getContext().getAssets();
        dataHandler = new DataHandler(am, 1);
        analyzer = new Analyzer(am);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.drivemodesmile, container, false);
        image = v.findViewById(R.id.smileyId);
        picture = R.drawable.emoji1;
        image.setImageResource(picture);
        layout = v.findViewById(R.id.background);
        layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.greenS));
        b = v.findViewById(R.id.dataButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!dataHandler.nextRow()) {
                            this.cancel();
                        }
                        float[] input = dataHandler.getInput();
                        float output = dataHandler.getOutput();
                        double cls = analyzer.classify(input, output);
                        int numClasses = 5;
                        for (int i = numClasses; i > 0; i--) {
                            if cl
                        }
                    }
                }, 0, 500);

                if (picture == (R.drawable.emoji1)) {
                    picture = R.drawable.emoji2;
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.LimeS));
                } else if (picture == (R.drawable.emoji2)) {
                    picture = R.drawable.emoji3;
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellowS));
                } else if (picture == (R.drawable.emoji3)) {
                    picture = R.drawable.emoji4;
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));

                } else if (picture == (R.drawable.emoji4)) {
                    picture = R.drawable.emoji5;
                    layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));

                }
                image.setImageResource(picture);
            }
        });
        return v;
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
