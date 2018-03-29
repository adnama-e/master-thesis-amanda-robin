package com.example.amandaeliasson.ecodrivning;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Observable;

/**
 * Created by amandaeliasson on 2018-03-28.
 */

public class State extends Observable implements Serializable {
    private Calendar startDate, endDate;

    public State(){
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
        setChanged();
        notifyObservers();
    }

    public void setStartDate(int year, int month, int date) {
        this.startDate.set(year, month, date);
        setChanged();
        notifyObservers();
    }
}
