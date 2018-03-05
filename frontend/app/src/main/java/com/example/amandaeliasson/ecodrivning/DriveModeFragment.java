package com.example.amandaeliasson.ecodrivning;

import android.app.AlertDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.service.voice.VoiceInteractionService;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

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
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    private String OBDAdress;
    private static final int ENABLE_BLUETOOTH = 1;
    private static final int MAKE_VISIBLE = 2;
    private static final String NAME = "OBD_CONNECTION";
    private static final String OBD_TAG = "OBD";

    public DriveModeFragment() {
        dataProvider = new DataProviderMockup();
    // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BA = BluetoothAdapter.getDefaultAdapter();
    }

    public void onStart() {
        super.onStart();
        connectToOBD();
    }

    public void connectToOBD() {
        if (OBDAdress == null) {
            enableBluetooth();
            linkOBD();
        }
    }

    public void enableBluetooth() {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, ENABLE_BLUETOOTH);
            System.out.println("Turned on!");
        } else {
            System.out.println("Already on!");
        }
    }

    public void makeVisible() {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, MAKE_VISIBLE);
    }

    public void linkOBD() {
        // Get paired devices.
        ArrayList<String> deviceNames = new ArrayList<String>();
        final ArrayList<String> deviceAddresses = new ArrayList<String>();
        pairedDevices = BA.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceNames.add(device.getName());
                deviceAddresses.add(device.getAddress());
            }
        } else {
            System.out.println("No paired devices");
            Toast.makeText(getActivity(), "Please pair tablet with OBD device",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Display paired devices.
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getContext());
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(),
                android.R.layout.select_dialog_singlechoice,
                deviceNames.toArray(new String[deviceNames.size()]));
        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                OBDAdress = deviceAddresses.get(position);
                BluetoothThread btThread = new BluetoothThread(OBDAdress);
                btThread.run();
            }
        });
        alertDialog.setTitle("Choose OBD device");
        alertDialog.show();
    }

    private class BluetoothThread extends Thread {
        private BluetoothSocket socket;
        private UUID uuid;
        private BluetoothDevice obd;

        public BluetoothThread(String address) {
            // Randomly generated using a generator on the internet.
            uuid = UUID.fromString("d34abf40-b5b0-4aca-8f86-d7597b50ac1c");
            obd = BA.getRemoteDevice(address);
        }

        public void run() {
            try {
                socket = obd.createInsecureRfcommSocketToServiceRecord(uuid);
                System.out.println("Connected");
                socket.connect();
            } catch (IOException e) {
                Log.e(OBD_TAG, "Unable to connect to OBD", e);
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(OBD_TAG, "Could not close connected socket", e);
            }
        }
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
