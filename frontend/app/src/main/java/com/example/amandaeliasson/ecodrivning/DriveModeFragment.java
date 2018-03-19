package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
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
    ImageView v;
    SeekBar sb_value;
    Analyzer analyzer;

    public DriveModeFragment() {
        dataProvider = new DataProviderMockup();
    // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        Analyzer analyzer = new Analyzer(context.getAssets());
        analyzer.realTime();

    }

//  #############################################################################



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        layout = inflater.inflate(R.layout.drivemode_fragment, container, false
//        );
        layout = inflater.inflate(R.layout.smiley, container, false
        );
//        v = layout.findViewById(R.id.warning);

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
        /*sb_value = layout.findViewById(R.id.sb_value);
        sb_value.setProgress(100);
        final ImageView im_brightness = layout.findViewById(R.id.warning);
        sb_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                im_brightness.setColorFilter(setBrightness(progress));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
        return layout;


    }

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >=    100)
        {
         int value = (int) (progress-100) * 255 / 100;
         return new PorterDuffColorFilter(Color.argb( value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);



        }
        else
        {
            int value = (int) (100-progress) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);


        }
    }


       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(
                        context, R.anim.fade_in_view);
                v.startAnimation(fadeInAnimation);
                fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                       v.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub

                    }
                });
            }

        }, 1000);*/



  /*  public void backgroundColor() {

        List<Measurement> list = dataProvider.getData();
        Measurement latestAdded = list.get(list.size()-1);

        if(latestAdded.goodValue()){
            layout.setBackgroundColor(Color.GREEN);
        }else{
            layout.setBackgroundColor(Color.RED);
        }
    }*/
    public void voiceCommand(){
        List<Measurement> list = dataProvider.getData();
        Measurement latestAdded = list.get(list.size()-1);
        if(latestAdded.typeOfMeasurment().equals("speedmeasurment") && latestAdded.goodValue() ==false);
            textToSpeech.speak("Slow down, you are driving too fast", TextToSpeech.QUEUE_FLUSH, null);
            v = layout.findViewById(R.id.happy);

    }
    public void onPause(){
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}
