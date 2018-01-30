package com.example.amandaeliasson.ecodrivning;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by amandaeliasson on 2018-01-29.
 */

public class DataProviderMockup implements DataProvider {
    public DataProviderMockup() {
    }

    @Override
    public List<Measurement> getData() {
        List<Measurement> measurements = new LinkedList<>();
        measurements.add(new SpeedMeasurement(new Date(), 55.586090, 12.994164, 80));
        measurements.add(new SpeedMeasurement(new Date(), 55.612808, 13.003448, 60));
        measurements.add(new SpeedMeasurement(new Date(), 55.612810, 13.013448, 60));
        measurements.add(new SpeedMeasurement(new Date(), 55.596090, 12.995164, 80));
        measurements.add(new SpeedMeasurement(new Date(), 55.622808, 13.004448, 60));
        return measurements;
    }
}