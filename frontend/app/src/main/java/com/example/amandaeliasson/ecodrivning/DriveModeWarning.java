package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.content.res.AssetManager;
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

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;


public class DriveModeWarning extends Fragment implements Observer {
    View layout;

    DataProvider dataProvider;

    TextToSpeech textToSpeech;
    Context context;
    WarningView image;
    Button dataButton;
    int alpha;
    DataHandler dataHandler;
    Analyzer analyzer;
    Timer time;

    public DriveModeWarning() {
        alpha = 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AssetManager am = getContext().getAssets();
        dataHandler = new DataHandler(am, 1);
        analyzer = new Analyzer(am);
        Bundle args = getArguments();
        dataProvider = (DataProvider) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
        dataProvider.addObserver(this);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.drivemodewarning, container, false);
        image = layout.findViewById(R.id.warning);
        image.setVisibility(View.INVISIBLE);
        dataButton = layout.findViewById(R.id.dataB);
        time = new Timer();
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void run() {
                                Measurement m = dataProvider.getMeasurement();
                                    if(m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() ==false) {
                                            image.increaseAlpha();
                                        }else{
                                            image.reduceAlpha();

                                    }
                                    System.out.println(image.alpha());
                                    //image.setImageAlpha(alpha);
                                    image.setVisibility(View.VISIBLE);
                                    image.setAlpha();
                                    }});

                    }
                }, 0, 500);
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
        textToSpeech.speak("Slow down, you are driving too fast", TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onPause() {
        time.cancel();
        /*if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }*/
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object o) {
    }
}