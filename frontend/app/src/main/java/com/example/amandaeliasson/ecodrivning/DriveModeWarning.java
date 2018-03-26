package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by amandaeliasson on 2018-03-12.
 */

public class DriveModeWarning extends Fragment implements Observer {
    View layout;
//    DataProvider dataProvider;
    DataHandler dataHandler;
    TextToSpeech textToSpeech;
    Context context;
    ImageView image;
    Button dataButton;
    int trans;
    Analyzer analyzer;

    public DriveModeWarning() {
        trans = 0;
        analyzer = new Analyzer(getContext().getAssets(), Analyzer.REGRESSION_MODE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dataHandler = (DataHandler) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
        dataHandler.addObserver(this);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.drivemodewarning, container, false);
        // layout = inflater.inflate(R.layout.smiley, container, false
        // );
        image = layout.findViewById(R.id.warning);
        image.setVisibility(View.INVISIBLE);
        //dataProvider = new DataProviderMockup();
        dataButton = layout.findViewById(R.id.dataB);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setVisibility(View.VISIBLE);
                Measurement m = dataProvider.getMeasurement();
                if (m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() == false) {
                    if (trans != 255) {
                        trans = trans + 20;
                    }
                } else {
                    if (trans != 0) {
                        trans = trans - 20;
                    }
                }
                image.setImageAlpha(trans);
            }
        });


        context = container.getContext();
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    // voiceCommand();
                }
            }
        });
        return layout;


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setProgress() {
        List<Measurement> list = dataProvider.getData(); // TODO replace with analyzer.classify()
        Measurement latestAdded = list.get(list.size() - 1);
        if (latestAdded.typeOfMeasurment().equals("speedmeasurment") && latestAdded.goodValue() == false)
            ;
        image.setImageAlpha((int) (100 * (255 / 100)));

    }

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >= 100) {
            int value = (int) (progress - 100) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);
        } else {
            int value = (int) (100 - progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public void voiceCommand() {
        List<Measurement> list = dataProvider.getData();
        Measurement latestAdded = list.get(list.size() - 1);
        if (latestAdded.typeOfMeasurment().equals("speedmeasurment") && latestAdded.goodValue() == false)
            ;
        textToSpeech.speak("Slow down, you are driving too fast", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object o) {
        dataProvider.getData();
        image.setImageAlpha(100);
    }
}