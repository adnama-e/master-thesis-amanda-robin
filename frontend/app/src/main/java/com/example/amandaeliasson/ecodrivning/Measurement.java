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
    protected int distance;
    protected int duration;
    protected String markerReason;
    public Measurement(Date d, double c1, double c2, double data, int distance, int duration, String markerReason){
        date=d;
        coordinate1=c1;
        coordinate2=c2;
        this.data = data;
        this.duration = duration;
        this.distance = distance;
        this.markerReason = markerReason;
    }
    public double getCoordinate1(){

        return coordinate1;
    }
    public String getMarkerReason(){
        return markerReason;
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
    public int getDistance(){
        return distance;
    }
    public int getDuration(){
        return duration;
    }
    public abstract boolean goodValue();
    public abstract String typeOfMeasurment();

}
