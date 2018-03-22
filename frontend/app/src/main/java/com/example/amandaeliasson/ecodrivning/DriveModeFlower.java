package com.example.amandaeliasson.ecodrivning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by amandaeliasson on 2018-03-19.
 */

public class DriveModeFlower extends Fragment implements Observer {
    ImageView image;
    Button button;
    RelativeLayout layout;
    DataProvider dataProvider;
    int picture;
    @SuppressLint("ResourceAsColor")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dataProvider = (DataProvider) args.getSerializable(MainActivity.ARGS_DATA_PROVIDER);
        dataProvider.addObserver(this);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.drivemodeflower, container, false);
        button = (Button) v.findViewById(R.id.flowerdata);
        image = (ImageView)v.findViewById(R.id.flower1);
        picture = R.drawable.growing1;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Measurement m = dataProvider.getMeasurement();

                //Denna ska ändras sedan, då blomman ska växa när man kör bra!
                if(m.typeOfMeasurment().equals("speedmeasurment") && m.goodValue() ==false) {
                    if(picture == R.drawable.growing1){
                        picture = R.drawable.growing2;
                    }else if (picture == R.drawable.growing2){
                        picture = R.drawable.growing3;
                    }else if(picture == R.drawable.growing3){
                        picture = R.drawable.growing4;
                    }else if(picture == R.drawable.growing4){
                        picture = R.drawable.growing5;
                    }else if(picture == R.drawable.growing5){
                        picture = R.drawable.growing6;
                    }else if(picture == R.drawable.growing6){
                        picture = R.drawable.growing7;
                    }
                    image.setImageResource(picture);
                }}});
        return v;

    }
    @Override
    public void update(Observable observable, Object o) {

    }
}
