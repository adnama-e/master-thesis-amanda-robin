package com.example.amandaeliasson.ecodrivning;

import java.util.Date;

/**
 * Created by amandaeliasson on 2018-01-29.
 */

public class SpeedMeasurement extends Measurement {
    private double speed;
    public SpeedMeasurement(Date d, double c1, double c2, double s) {
        super(d, c1, c2);
        speed= s;
    }
    public double getSpeed(){

        return speed;
    }
    public boolean goodValue(){
        if(getSpeed() > 70){
            return true;
        }else{
            return false;
        }
    }
    public String typeOfMeasurment(){

        return "speedmeasurment";
    }

}
