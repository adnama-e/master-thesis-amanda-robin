package com.example.amandaeliasson.ecodrivning;

import java.util.Date;

/**
 * Created by amandaeliasson on 2018-01-29.
 */

public abstract class Measurement {
    protected Date date;
    protected double coordinate1;
    protected double coordinate2;
    protected double data;
    public Measurement(Date d, double c1, double c2, double data){
        date=d;
        coordinate1=c1;
        coordinate2=c2;
        this.data = data;
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
    public double getData(){
        return data;

    }
    public abstract boolean goodValue();
    public abstract String typeOfMeasurment();

}
