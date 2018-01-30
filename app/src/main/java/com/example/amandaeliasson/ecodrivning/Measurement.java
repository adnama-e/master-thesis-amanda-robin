package com.example.amandaeliasson.ecodrivning;

import java.util.Date;

/**
 * Created by amandaeliasson on 2018-01-29.
 */

public abstract class Measurement {
    protected Date date;
    protected double coordinate1;
    protected double coordinate2;

    public Measurement(Date d, double c1, double c2){
        date=d;
        coordinate1=c1;
        coordinate2=c2;
    }
    public double getCoordinate1(){
        return coordinate1;
    }
    public double getCoordinate2(){
        return coordinate2;
    }
    public Date getDate(){
        return date;
    }
    public abstract boolean goodValue();

}
