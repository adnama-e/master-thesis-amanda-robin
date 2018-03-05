package com.example.amandaeliasson.ecodrivning;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by amandaeliasson on 2018-02-23.
 */

public class ProgressFragment extends NamedFragment {
    private GraphView graph, graph2, graph3;

    public ProgressFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        graph = (GraphView) v.findViewById(R.id.graph_speed);
        graph2 = (GraphView) v.findViewById(R.id.graph_brake);
        graph3 = (GraphView) v.findViewById(R.id.graph_acc);
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
        graph.addSeries(series);
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 6),
                new DataPoint(2, 7),
                new DataPoint(3, 3),
                new DataPoint(4, 6)
        });
        graph2.addSeries(series2);
    }

    public String getName() {
        return "Progress view";
    }
}