package com.example.amandaeliasson.ecodrivning;

import java.util.Date;

/**
 * Created by amandaeliasson on 2018-01-30.
 */

public class BrakeMeasurement extends Measurement {
    public BrakeMeasurement(Date d, double c1, double c2, double breakValue){
        super(d,c1,c2, breakValue);

    }
    public boolean goodValue(){
        return true;
    }
    public String typeOfMeasurment(){
        return "breakMeasurment";
    }

}
