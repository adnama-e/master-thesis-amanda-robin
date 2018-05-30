package com.example.amandaeliasson.ecodrivning;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.anastr.speedviewlib.SpeedView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by amandaeliasson on 2018-02-23.
 */

public class AverageFragment extends NamedFragment implements Observer{
    private SpeedView speedView;
    private DataProviderMockup dataprovider;
    private TextView text1, text2;
    private State state;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        state = (State) args.getSerializable(MainActivity.ARGS_STATE);
        state.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_average, container, false);
        speedView = v.findViewById(R.id.accView);
        dataprovider = new DataProviderMockup();
        text1 =  v.findViewById(R.id.totalTime);
        text2 =  v.findViewById(R.id.totalDistance);
        setData();
        return v;
    }

    private void setData() {
        List<Measurement> data = dataprovider.getFilteredData(state.getStartDate().getTime(), state.getEndDate().getTime());
        float sum = 0;
        int duration =0;
        int distance = 0;
        for (Measurement m : data) {
            sum +=m.getData();
            duration += m.getDuration();
            distance += m.getDistance();
        }
        if(data.isEmpty()){
            speedView.speedTo(0);
        }else{
            float averageEcoScore = (sum/data.size())*100;

            speedView.speedTo(averageEcoScore);
        }
        int hours=duration/60,minutes=duration%60;
        text2.setText(Integer.toString(distance) + " " + "kilometers");
        if(hours>0){
            text1.setText(String.format("%d hours, %d minutes", hours, minutes));
        }else{
            text1.setText(String.format("%d minutes", minutes));
        }
    }

    public String getName() {
        return "Score";
    }

    @Override
    public void update(Observable observable, Object o) {
        setData();
    }
}
