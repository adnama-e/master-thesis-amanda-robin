package com.example.amandaeliasson.ecodrivning;

import android.annotation.TargetApi;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SecondHighScoreButton extends Fragment {
    private List<ProgressBar> progressBars;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private DataProviderMockup dataprovider;
    public SecondHighScoreButton() {
        progressBars = new ArrayList<>();
        dataprovider = new DataProviderMockup();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_second_high_score_button, container, false);
        progressBars.add((ProgressBar) v.findViewById(R.id.progress_H_1));
        progressBars.add((ProgressBar) v.findViewById(R.id.progress_H_2));
        progressBars.add((ProgressBar) v.findViewById(R.id.progress_H_3));
        t1 = (TextView) v.findViewById(R.id.textdatabraking);
        t2 = (TextView) v.findViewById(R.id.textdatalegal);
        t3 = (TextView) v.findViewById(R.id.textdataAcc);
        setData();
        setColor();
        setDataText();
        return v;

    }
    public void setData() {
        List<Measurement> measurements = dataprovider.getData();
        //List<Double> speed = new ArrayList<>();
        Map<Date, Double> speed = new HashMap<>();
        for (Measurement m : measurements) {
            if (m.typeOfMeasurment().equals("speedmeasurment")) {
                double i = m.getData();
                speed.put(m.getDate(), i);
            }
        }
        Set<Date> dates = speed.keySet();
        for (Date d : dates) {
            if(d.getDay() ==3){
                double data = speed.get(d);
                progressBars.get(1).setProgress((int) data);
            }
        }

//        progressBars.get(0).setProgress(15);
        progressBars.get(0).setProgress(70);
        progressBars.get(2).setProgress(95);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setColor(){
       for(ProgressBar p: progressBars){
            if(p.getProgress() > 50){
                p.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.green1)));
            }else{
                p.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        }

        }
    }
    public void setDataText(){
        t1.setText(Integer.toString(progressBars.get(0).getProgress()) + "%");
        t2.setText(Integer.toString(progressBars.get(1).getProgress()) + "%");
        t3.setText(Integer.toString(progressBars.get(2).getProgress()) + "%");
    }

}
