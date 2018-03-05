package com.example.amandaeliasson.ecodrivning;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import com.github.anastr.speedviewlib.SpeedView;

import com.github.anastr.speedviewlib.components.Indicators.NeedleIndicator;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;


public class FirstHighScoreButton extends NamedFragment {
    private SpeedView speedView;
    private DataProviderMockup dataprovider;



    public FirstHighScoreButton() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_first_high_score_button, container, false);

      /*  speedView = v.findViewById(R.id.speedview1);
        dataprovider = new DataProviderMockup();
        setData();*/
        //PieChart pc = (PieChart) v.findViewById(R.id.progress_pichart);


        return v;
    /*}
    public void setData(){
        List<Measurement> data = dataprovider.getData();
        for(Measurement m: data){
            if(m.typeOfMeasurment().equals("speedmeasurment")){
                speedView.speedPercentTo((int)m.getData());
            }
        }*/


    }

    @Override
    public String getName() {
        return "Old average view";
    }
    // Allocate paint outside onDraw to avoid unnecessary object creation
}

