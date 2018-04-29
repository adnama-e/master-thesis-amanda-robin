package com.example.amandaeliasson.ecodrivning;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by amandaeliasson on 2018-02-23.
 */

public class ProgressFragment extends NamedFragment implements Observer{
    private GraphView graph3;
    private State state;
    TextView improvText;
    private DynamoDBMapper dynamoDBMapper;
    private List<Pair<Date, Double>> dataPoints;
    public ProgressFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = (State) getArguments().getSerializable(MainActivity.ARGS_STATE);
        state.addObserver(this);
        dynamoDBMapper = MainActivity.dynamoDBMapper;
//        new AsyncTask<Void,Void,OverviewDO>(){
//
//            @Override
//            protected OverviewDO doInBackground(Void... voids) {
//                return null;
//            }
//        }.execute();
        dataPoints = new LinkedList<>();
        dataPoints.add(getDataPoint("20180401", 0.8));
        dataPoints.add(getDataPoint("20180404", 0.7));
        dataPoints.add(getDataPoint("20180410", 0.9));
        dataPoints.add(getDataPoint("20180501", 0.95));
        dataPoints.add(getDataPoint("20180520", 0.8));

    }
    private Pair<Date, Double> getDataPoint(String date, double val){
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            return new Pair<>(df.parse(date), val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        graph3 = v.findViewById(R.id.graph_acc);
        improvText = v.findViewById(R.id.imtext);
        graph3.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        getImprovmentScore();


        createGraph();
        return v;
    }
    public void createGraph(){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph3.addSeries(series);
    }

    public String getName() {
        return "Progress";
    }

    @Override
    public void update(Observable observable, Object o) {
        Date startDate = state.getStartDate().getTime();
        Date endDate = state.getEndDate().getTime();
        List<Pair<Date, Double>> filtered = new LinkedList<>();
        for(Pair<Date, Double> p: dataPoints){
            if(p.first.after(startDate) && p.first.before(endDate)){
                filtered.add(p);
            }
        }
        DataPoint[] points = new DataPoint[filtered.size()];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for(int i=0;i<points.length;i++){
            if(filtered.get(i).second < min) min = filtered.get(i).second;
            if(filtered.get(i).second > max) max = filtered.get(i).second;
            DataPoint dp = new DataPoint(filtered.get(i).first, filtered.get(i).second);
            points[i]=dp;
        }
        graph3.removeAllSeries();
        graph3.addSeries(new LineGraphSeries<DataPoint>(points));
        if(filtered.size()>1) improvText.setText(String.format("%3.0f %%",((filtered.get(filtered.size()-1).second / filtered.get(0).second) -1)*100));


    }
    public void getImprovmentScore(){
        improvText.setText(Integer.toString(50) + "%");
    }
}
