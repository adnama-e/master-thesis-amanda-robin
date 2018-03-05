package com.example.amandaeliasson.ecodrivning;

import android.support.v7.app.ActionBarDrawerToggle;

import java.util.Date;

/**
 * Created by amandaeliasson on 2018-01-30.
 */

public class AccelerateMeasurement extends Measurement {
    public AccelerateMeasurement(Date d, double c1, double c2, double accelerateValue){
        super(d,c1,c2, accelerateValue);
    }
    public boolean goodValue(){
        return true;
    }
    public String typeOfMeasurment(){
        return "acceleratemeasurment";
    }
}
