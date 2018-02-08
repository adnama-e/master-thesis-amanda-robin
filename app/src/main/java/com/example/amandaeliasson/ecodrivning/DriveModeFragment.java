package com.example.amandaeliasson.ecodrivning;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

/**
 * Created by amandaeliasson on 2018-01-31.
 */

public class DriveModeFragment extends Fragment {
    View layout;
    DataProviderMockup dataProvider;
    TextToSpeech textToSpeech;
    Context context;

    public DriveModeFragment(){
        dataProvider = new DataProviderMockup();
    // Required empty public constructor
}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.drivemode_fragment, container, false
        );
        context = container.getContext();
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    voiceCommand();
                }
            }
        });
        this.backgroundColor();

        return layout;
    }
    public void backgroundColor() {
        List<Measurement> list = dataProvider.getData();
        Measurement latestAdded = list.get(list.size()-1);

        if(latestAdded.goodValue()){
            layout.setBackgroundColor(Color.GREEN);

        }else{
            layout.setBackgroundColor(Color.RED);
        }
    }
    public void voiceCommand(){
        List<Measurement> list = dataProvider.getData();
        Measurement latestAdded = list.get(list.size()-1);
        if(latestAdded.typeOfMeasurment().equals("speedmeasurment") && latestAdded.goodValue() ==false);
            textToSpeech.speak("Slow down, you are driving too fast", TextToSpeech.QUEUE_FLUSH, null);
    }
    public void onPause(){
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }
}
