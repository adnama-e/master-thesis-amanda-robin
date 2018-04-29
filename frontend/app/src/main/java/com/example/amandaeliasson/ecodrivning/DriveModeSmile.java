package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;
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
    Timer timer;
    int counter;
    TextView scoreText;
    DataHandler dataHandler;
    Analyzer analyzer;
    private boolean started;
    private String userId, driveId;
    private State state;

    @SuppressLint("ResourceAsColor")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dataProvider = (DataProvider) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
        dataProvider.addObserver(this);
        AssetManager am = getContext().getAssets();
        dataHandler = new DataHandler(am, 1, -1);
        analyzer = new Analyzer(am, MainActivity.dynamoDBMapper);
        userId = "Amanda";
        driveId = "testDrive";
        state = (State) args.getSerializable(MainActivity.ARGS_STATE);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drivemodesmile, container, false);
        image = v.findViewById(R.id.smileyId);
        picture = R.drawable.smiley_happy2;
        image.setImageResource(picture);
        scoreText = v.findViewById(R.id.todaysScore);
        counter =0;
        scoreText.setText(Integer.toString(counter));
        timer = new Timer();
        b = v.findViewById(R.id.dataButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!started) {
                    analyzer.initSession();
                    started = true;
                    timer.schedule(new TimerTask() {
                        double alpha;

                        @Override
                        public void run() {
                            alpha = runWithCSV();
                            if (alpha == -1) {
                                timer.cancel();
                            }
                            //if ((int) alpha == 0) alpha = Math.abs(alpha);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (alpha < -0.001) {
                                        image.change();
                                    } else if (alpha > -0.001) {
                                        image.changeBack();
                                    }
                                    scoreText.setText(String.format(Locale.ENGLISH, "%2.1f %%", alpha * 100));
                                }
                            });


                        }
                    }, 0, 1000);
//                time.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        final Measurement m = dataProvider.getMeasurement();
//                        if(m.typeOfMeasurment().equals("ecoscore") && m.goodValue() ==false) {
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    image.change();
//                                    counter--;
//                                    //scoreText.setText(Integer.toString(counter));
//                                    scoreText.setText(Double.toString(m.getData()));
//                                }
//                            });
//
//                        }else{
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    image.changeBack();
//                                    counter++;
//                                    //scoreText.setText(Integer.toString(counter));
//                                    scoreText.setText(Double.toString(m.getData()));
//                                }
//                            });
//                        }
//                    }
//                }, 0, 1000);
                }else{
                    started = false;
                    timer.cancel();
                    analyzer.endAndUploadSession(userId,driveId);
                    Bundle args = new Bundle();
                    args.putString(FinishFragment.ARG_USER_ID, userId);
                    args.putString(FinishFragment.ARG_DRIVE_ID, driveId);
                    args.putSerializable(MainActivity.ARGS_STATE, state);
                    Fragment fragment = new FinishFragment();
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.content2, fragment).commit();

                }

            }
            });
        return v;
    }

    @Override
    public void onPause() {
        timer.cancel();
        super.onPause();
    }

    @Override
    public void update(Observable observable, Object o) {

    }
    private double runWithCSV() {
        if (!dataHandler.nextRow()) {
            analyzer.endAndUploadSession(userId,driveId);
            return -1;
        }
        float[] input = dataHandler.getInput();
        float output = dataHandler.getOutput();
        double cls = analyzer.classify(input, output);
//        int alpha = 0;
//        if (cls < 0) {
//            alpha = (int) (cls * -1 * 255);
//        }
        return cls;
    }
}
