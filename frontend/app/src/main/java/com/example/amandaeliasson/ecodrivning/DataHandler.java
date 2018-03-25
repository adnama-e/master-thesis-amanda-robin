package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DataHandler {
    private CSVReader inputReader, outputReader;
    private int dataIndex = 0;
    private final int NUM_DATASETS = 3;
    private float output;

    public DataHandler(AssetManager assetManager) {
        String inputCSV = "test_input" + Integer.toString(dataIndex) + ".csv";
        String outputCSV = "test_output" + Integer.toString(dataIndex) + ".csv";
        try {
            inputReader = new CSVReader(new InputStreamReader(assetManager.open(inputCSV)));
            outputReader = new CSVReader(new InputStreamReader(assetManager.open(outputCSV)));
            // The first row is the header and we're not interested in that.
            inputReader.readNext();
            outputReader.readNext();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean pickDataset(int newIndex) {
        return false;
    }

    public float getOutput(){
        return output;
    }

    public float[] getInput() {
        float[] inputRow, outputRow;
        try {
            inputRow = toFloat(inputReader.readNext());
            outputRow = toFloat(outputReader.readNext());
            // The first column is the fuel consumption.
            output = outputRow[0];
        } catch (Exception e) {
            e.printStackTrace();
            inputRow = null;
        }
        return inputRow;
    }

    private float[] toFloat(String[] data) {
        float[] floatData = new float[data.length];
        int i = 0;
        for (String val : data) {
            floatData[i++] = Float.parseFloat(val);
        }
        return floatData;
    }
}