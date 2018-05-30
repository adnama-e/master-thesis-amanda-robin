package com.example.amandaeliasson.ecodrivning;

import android.os.AsyncTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by amandaeliasson on 2018-04-26.
 */

public class AWSDataProvider extends DataProvider {
    private List<Measurement> measurements;
    private int counter;
    public AWSDataProvider(){
        counter=0;
        measurements = new ArrayList<>();
        new AsyncTask<Void, Void, DriveScoresDO>() {
            @Override
            protected DriveScoresDO doInBackground(Void... voids) {
                return MainActivity.dynamoDBMapper.load(DriveScoresDO.class,"devdata", "1");
            }

            @Override
            protected void onPostExecute(DriveScoresDO o) {
                DateFormat df = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss");
                List<String> dateTimes = o.getDatetime();
                List<String> scores = o.getScore();

                for(int i=0; i<dateTimes.size();i++){
                    try {
                        measurements.add(new EcoScoreMeasurement(df.parse(dateTimes.get(i)),0,0, Double.parseDouble(scores.get(i)),0,0, null));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }
    @Override
    public List<Measurement> getData() {
        return measurements;
    }

    @Override
    public Measurement getMeasurement() {
        if(measurements.isEmpty()) return new Measurement(new Date(), 0,0,0, 0, 0, null) {
            @Override
            public boolean goodValue() {
                return false;
            }

            @Override
            public String typeOfMeasurment() {
                return "nullmeasurement";
            }
        };
        Measurement m = measurements.get(counter);
        counter = (counter + 1) % measurements.size();
        return m;
    }
}
