package com.example.amandaeliasson.ecodrivning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.anastr.speedviewlib.SpeedView;

import java.util.List;

/**
 * Created by amandaeliasson on 2018-02-23.
 */

public class AverageFragment extends NamedFragment {
    private SpeedView speedView;
    private DataProviderMockup dataprovider;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_average, container, false);
        speedView = v.findViewById(R.id.speedview1);
        dataprovider = new DataProviderMockup();
        setData();
        return v;

    }

    private void setData() {
        List<Measurement> data = dataprovider.getData();
        for(Measurement m: data){
            if(m.typeOfMeasurment().equals("speedmeasurment")){
                speedView.speedPercentTo((int)m.getData());
            }
        }
    }

    public String getName() {
        return "Average view";
    }
}
