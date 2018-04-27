package com.example.amandaeliasson.ecodrivning;

import java.util.Date;

/**
 * Created by amandaeliasson on 2018-04-26.
 */

public class EcoScoreMeasurement extends Measurement {


    public EcoScoreMeasurement(Date d, double c1, double c2, double data) {
        super(d, c1, c2, data);
    }

    @Override
    public boolean goodValue() {
        return false;
    }

    @Override
    public String typeOfMeasurment() {
        return "ecoscore";
    }
}