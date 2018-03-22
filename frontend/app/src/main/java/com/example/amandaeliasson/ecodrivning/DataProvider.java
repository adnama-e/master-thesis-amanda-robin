package com.example.amandaeliasson.ecodrivning;

import com.google.android.gms.common.data.DataBufferObserver;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

/**
 * Created by amandaeliasson on 2018-01-29.
 */

public abstract class DataProvider extends Observable implements Serializable{
    public abstract List<Measurement> getData();
    public abstract Measurement getMeasurement();
}
