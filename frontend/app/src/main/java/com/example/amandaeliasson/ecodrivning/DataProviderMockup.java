package com.example.amandaeliasson.ecodrivning;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by amandaeliasson on 2018-01-29.
 */

public class DataProviderMockup extends DataProvider {
    private Measurement latestAdd = null;
    private int counter;
    List<Measurement> measurements;


    public DataProviderMockup() {
        counter = 0;
    }
    @Override
    public List<Measurement> getData() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar c=Calendar.getInstance();
        c.set(2018+1900,3,1,12,30,6);
        List<Measurement> measurements = new LinkedList<>();
        try {

            Measurement m1 = new SpeedMeasurement(df.parse("20180301"), 55.612578, 13.000444, 1,  21, 30, "Brake");
            measurements.add(m1);
            latestAdd = m1;

            c.set(0, 0, 0);
            Measurement m2 = new SpeedMeasurement(df.parse("20180401"), 55.611881, 13.002858, 1, 21, 30, "Acceleration");
            measurements.add(m2);
            latestAdd = m2;

            Measurement m3 = new SpeedMeasurement(df.parse("20180402"), 55.612056, 13.005958, 1, 22, 60, "Acceleration");
            measurements.add(m3);
            latestAdd = m3;

            Measurement m4 = new SpeedMeasurement(df.parse("20180403"), 55.61154, 13.006076, 1, 30, 60, "Brake");
            measurements.add(m4);
            latestAdd = m4;

            Measurement m5 = new SpeedMeasurement(df.parse("20180405"), 55.611450, 13.004885, 0, 10, 2, "Acceleration");
            measurements.add(m5);
            latestAdd = m5;

            Measurement m6 = new SpeedMeasurement(df.parse("20180410"), 55.610966, 13.004960, 1, 30, 40, "Brake");
            measurements.add(m6);
            latestAdd = m6;

            Measurement m7 = new SpeedMeasurement(df.parse("20180412"), 55.611238, 13.008576, 1, 20, 30, "Acceleration");
            measurements.add(m7);
            latestAdd = m7;

            Measurement m8 = new SpeedMeasurement(df.parse("20180420"), 55.611699 , 13.008415, 1, 20, 40, "Brake");
            measurements.add(m8);
            latestAdd = m8;

            Measurement m9 = new SpeedMeasurement(df.parse("20180425"), 55.611699 , 13.008415, 0, 20, 40, "Low gear");
            measurements.add(m9);
            latestAdd = m9;

            Measurement m10 = new SpeedMeasurement(df.parse("20180428"), 55.611911, 13.010765, 0, 300, 120, "Acceleration");
            measurements.add(m10);
            latestAdd = m10;


            Measurement m11 = new SpeedMeasurement(df.parse("20180501"), 55.612396, 13.010668, 0, 120, 60, "Low gear");
            measurements.add(m11);
            latestAdd = m11;

            Measurement m12 = new SpeedMeasurement(df.parse("20180503"), 55.612535, 13.012084, 0, 120, 60, "Brake");
            measurements.add(m12);
            latestAdd = m12;

            Measurement m13 = new SpeedMeasurement(df.parse("20180510"), 55.612535, 13.012084, 1, 120, 60, "Brake");
            measurements.add(m13);
            latestAdd = m13;

            Measurement m14 = new SpeedMeasurement(df.parse("20180512"), 55.613202, 13.012471, 1, 125, 70, "Acceleration");
            measurements.add(m14);
            latestAdd = m14;

            Measurement m15 = new SpeedMeasurement(df.parse("20180518"), 55.613280, 13.014842, 1, 140, 80, "Brake");
            measurements.add(m15);
            latestAdd = m15;

            Measurement m16 = new SpeedMeasurement(df.parse("20180525"), 55.613383, 13.018575, 1, 200, 120, "Acceleration");
            measurements.add(m16);
            latestAdd = m16;


            Measurement m17 = new SpeedMeasurement(df.parse("20180528"), 55.614862, 13.020657, 1, 130, 60, "Brake");
            measurements.add(m17);
            latestAdd = m17;

            Measurement m18 = new SpeedMeasurement(df.parse("20180601"), 55.615116, 13.023049, 1, 120, 70, "Brake");
            measurements.add(m18);
            latestAdd = m18;

            Measurement m19 = new SpeedMeasurement(df.parse("20180602"), 55.614492, 13.026837, 1, 120, 70, "Brake");
            measurements.add(m19);
            latestAdd = m19;
            return measurements;
        }catch(Exception e){
         e.printStackTrace();
        }
        return measurements;
    }
    public List<Measurement> getFilteredData(Date start, Date end){
        List<Measurement> filteredData = new LinkedList<>();
        for(Measurement m : getData()){
            if(m.date.after(start) && m.date.before(end)) filteredData.add(m);
        }
        return filteredData;
    }
    public Measurement getMeasurement(){
        List<Measurement> data = getData();
        Measurement m = data.get(counter % data.size());
        counter++;
        return m;
    }
    public Measurement getLatestAdd(){
        return latestAdd;
    }
}