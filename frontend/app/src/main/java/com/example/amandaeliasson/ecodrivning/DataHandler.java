package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;


public class DataHandler extends TimerTask implements Serializable{
    private CSVReader inputReader, outputReader;
    private AssetManager assetManager;
    private float output;
    private float[] inputRow;

    public DataHandler(AssetManager assetManager, int dataIndex) {
        this.assetManager = assetManager;
        setDataset(dataIndex);
    }

    /**
     * There are currently three available test sets.
     * @param index - 0, 1, or 2.
     */
    public void setDataset(int index) {
        String inputCSV = "test_input" + Integer.toString(index) + ".csv";
        String outputCSV = "test_output" + Integer.toString(index) + ".csv";
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

    public void run() {

    }

    /**
     * @return - The fuel consumption.
     */
    public float getOutput() {
        return output;
    }

    /**
     * @return - The input data to be fed to the Analyzer.classify method.
     */
    public float[] getInput() {
        return inputRow;
    }

    /**
     * Parses the next row of the chosen dataset. Should always be called before
     * getOutput() and getInput().
     * @return - True if there was a next row, false otherwise.
     */
    public boolean nextRow() {
        try {
            inputRow = toFloat(inputReader.readNext());
            output = toFloat(outputReader.readNext())[0];
            return true;
        } catch (IOException | NullPointerException e) {
            return false;
        }
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