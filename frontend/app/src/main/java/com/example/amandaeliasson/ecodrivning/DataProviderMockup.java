package com.example.amandaeliasson.ecodrivning;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by amandaeliasson on 2018-01-29.
 */

public class DataProviderMockup extends DataProvider {
    private Measurement latestAdd = null;
    private int counter;


    public DataProviderMockup() {
        counter = 0;
    }
    @Override
    public List<Measurement> getData() {
        List<Measurement> measurements = new LinkedList<>();
        Measurement m1 = new SpeedMeasurement(new Date(), 55.60457822286884, 13.001237567514181, 60);
        measurements.add(m1);
        latestAdd = m1;

        Measurement m2 = new SpeedMeasurement(new Date(), 55.60329635732146, 13.001317111775279, 60);
        measurements.add(m2);
        latestAdd = m2;

        Measurement m3 = new SpeedMeasurement(new Date(), 55.60299634018647, 12.998828021809459, 60);
        measurements.add(m3);
        latestAdd = m3;

        Measurement m4 = new SpeedMeasurement(new Date(), 55.60367819399276, 12.99830767326057, 60);
        measurements.add(m4);
        latestAdd = m4;

        Measurement m5 = new SpeedMeasurement(new Date(), 55.603563037292865, 12.996188728138804, 60);
        measurements.add(m5);
        latestAdd = m5;

        Measurement m6 = new SpeedMeasurement(new Date(), 55.60439033975849, 12.995669301599264, 60);
        measurements.add(m6);
        latestAdd = m6;


        /*Measurement m7 = new BrakeMeasurement(new Date(), 55.60439033975849, 12.995669301599264, 60);
        measurements.add(m7);
        latestAdd = m7;*/
        return measurements;
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