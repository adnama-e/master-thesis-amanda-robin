package com.example.amandaeliasson.ecodrivning;

import java.util.Date;

/**
 * Created by amandaeliasson on 2018-01-30.
 */

public class BreakMeasurement extends Measurement {
    private double breakValue;

    public BreakMeasurement(Date d, double c1, double c2, double breakValue){
        super(d,c1,c2);
        this.breakValue=breakValue;

    }
    public boolean goodValue(){
        return true;
    }

}
