package com.example.amandaeliasson.ecodrivning;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by amandaeliasson on 2018-02-23.
 */

public class ProgressFragment extends NamedFragment implements Observer{
    private GraphView graph3;
    private State state;
    TextView improvText;
    public ProgressFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = (State) getArguments().getSerializable(MainActivity.ARGS_STATE);
        state.addObserver(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        graph3 = v.findViewById(R.id.graph_acc);
        improvText = v.findViewById(R.id.imtext);
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

    }
    public void getImprovmentScore(){
        improvText.setText(Integer.toString(50) + "%");
    }
}
