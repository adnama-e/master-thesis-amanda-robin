package com.example.amandaeliasson.ecodrivning;

import android.content.Context;
import android.content.res.AssetManager;
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

import com.google.gson.internal.bind.DateTypeAdapter;

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
    ImageView image;
    SeekBar sb_value;
    Analyzer analyzer;

    public DriveModeFragment() {
        dataProvider = new DataProviderMockup();
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        connectToOBD();
    }
//    public void connectToOBD() {
//        if (OBDAdress == null) {
//            enableBluetooth();
//            linkOBD();
//        }
//    }
//
//    public void enableBluetooth() {
//        if (!BA.isEnabled()) {
//            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(turnOn, ENABLE_BLUETOOTH);
//            System.out.println("Turned on!");
//        } else {
//            System.out.println("Already on!");
//        }
//    }
//
//    public void makeVisible() {
//        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        startActivityForResult(getVisible, MAKE_VISIBLE);
//    }
//
//    public void linkOBD() {
//        // Get paired devices.
//        ArrayList<String> deviceNames = new ArrayList<String>();
//        final ArrayList<String> deviceAddresses = new ArrayList<String>();
//        pairedDevices = BA.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                deviceNames.add(device.getName());
//                deviceAddresses.add(device.getAddress());
//            }
//        } else {
//            System.out.println("No paired devices");
//            Toast.makeText(getActivity(), "Please pair tablet with OBD device",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//    }

    public void onStart() {
        super.onStart();
        AssetManager am = context.getAssets();
        DataHandler dh = new DataHandler(am);
        Analyzer analyzer = new Analyzer(am, Analyzer.INTERVAL_MODE);
        float[] input;
        long startTime, endTime;
        while ((input = dh.getInput()) != null) {
            startTime = System.currentTimeMillis();
            float cls = analyzer.classify(input, dh.getOutput());
            endTime = System.currentTimeMillis();
            System.out.println("Prediction: " + cls);
            System.out.println("Execution time: " + (endTime - startTime));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.drivemode_fragment, container, false);
        // layout = inflater.inflate(R.layout.smiley, container, false
        // );

        context = container.getContext();
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                    //voiceCommand();
                }
            }
        });
        //sb_value = layout.findViewById(R.id.sb_value);
        //sb_value.setProgress(100);
        image = layout.findViewById(R.id.warning);
        //int alpha = sb_value.getProgress();
        //image.setAlpha(alpha);
        //sb_value.setOnSeekBarChangeListener(onSeekBarChangeListener);
        setAlpha();
        return layout;
    }

    public void setAlpha() {
        List<Measurement> list = dataProvider.getData();
        Measurement latestAdded = list.get(list.size() - 1);
        if (latestAdded.typeOfMeasurment().equals("speedmeasurment") && latestAdded.goodValue() == false)
            ;
        image.setImageAlpha(1 * (255 / 100));
    }
   /* SeekBar.OnSeekBarChangeListener onSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    int alpha = sb_value.getProgress();
                    image.setAlpha(alpha);   //deprecated
                    //image.setImageAlpha(alpha); //for API Level 16+
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };*/
        /*sb_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //im_brightness.setColorFilter(setBrightness(progress));
                seekBar.setProgress(30);
                image.setImageAlpha((int)(progress * (255/100)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

    public static PorterDuffColorFilter setBrightness(int progress) {
        if (progress >= 100) {
            int value = (int) (progress - 100) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);


        } else {
            int value = (int) (100 - progress) * 255 / 100;
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
    public void voiceCommand() {
        List<Measurement> list = dataProvider.getData();
        Measurement latestAdded = list.get(list.size() - 1);
        if (latestAdded.typeOfMeasurment().equals("speedmeasurment") && latestAdded.goodValue() == false)
            ;
        textToSpeech.speak("Slow down, you are driving too fast", TextToSpeech.QUEUE_FLUSH, null);
        //v = layout.findViewById(R.id.happy);

    }

    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

}
