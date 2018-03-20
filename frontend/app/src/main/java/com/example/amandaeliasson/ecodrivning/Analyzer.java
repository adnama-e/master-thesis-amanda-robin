package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import org.apache.commons.math3.distribution.NormalDistribution;
import com.opencsv.CSVReader;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Uses a pre-trained LSTM model to predict the expected fuel consumption.
 * Input:
 *  Measured vehicle values as an array of floats.
 * Output:
 *  A classification of the current fuel consumption.
 *
 * Requires NDK. Install by:
 *  Go to tools > Android > SDK Manager
 *  Click the SDK tools tab
 *  Check the boxes for LLDB, CMake, and NDK.
 *  Click Apply
 */

public class Analyzer {
    private TensorFlowInferenceInterface tf;
    private DataHandler dataHandler;
    private static final String MODEL_FILE = "file:///android_asset/lstm-32-batch2.pb";
    private static final String INPUT_NODE = "lstm_2_input";
    private static final String[] OUTPUT_NODES = {"output_node0"};
    private static final String OUTPUT_NODE = "output_node0";
    private static final long[] INPUT_SHAPE = {1, 5, 27};
    private static final int OUTPUT_SIZE = 1;
    public static final int REGRESSION_MODE = 1, INTERVAL_MODE = 2;
    private int classifier;

    public Analyzer(AssetManager assetManager, int classifier) {
        this.classifier = classifier;
        tf = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
        dataHandler = new DataHandler(assetManager);
    }

    public float classify(float[] data, float output, int classificationMode) {
        // Calculate the expected fuel consumption.
        float expFuel = predictFuelConsumption(data);
        // Fetch the actual fuel consumption.
        float actFuel = dataHandler.getOutput();

        switch (classificationMode) {
            case INTERVAL_MODE:
                intervalClassification(expFuel, actFuel);
            case REGRESSION_MODE:
                regressionClassification(expFuel, actFuel);

        }
    }

    private float predictFuelConsumption(float[] data) {
        float[] output = new float[OUTPUT_SIZE];
        // Feed the data to the model.
        tf.feed(INPUT_NODE, data, INPUT_SHAPE);
        // Process the data.
        tf.run(OUTPUT_NODES);
        // Fetch the result.
        tf.fetch(OUTPUT_NODE, output);
        return output[0];
    }

    private float regressionClassification(float refValue, float trueValue) {
        // TODO find suitable distribution. Normal is NOT what we're looking for. 
        NormalDistribution nd = new NormalDistribution(refValue, 1);
    }

    private float intervalClassification(float refValue, float trueValue) {
        NormalDistribution nd = new NormalDistribution(refValue, 1);
    }

    private class DataHandler {
        private CSVReader inputReader, outputReader;
        private int dataIndex = 0;
        private final int NUM_DATASETS = 3;
        private float output;

        private DataHandler(AssetManager assetManager) {
            String inputCSV = "test_input" + Integer.toString(dataIndex) + ".csv";
            String outputCSV = "test_output" + Integer.toString(dataIndex) + ".csv";
            try {
                inputReader = new CSVReader(new InputStreamReader(assetManager.open(inputCSV)));
                outputReader = new CSVReader(new InputStreamReader(assetManager.open(outputCSV)));
                // We're not interested in the first row.
                inputReader.readNext();
                outputReader.readNext();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean pickDataset(int newIndex) {
            return false;
        }

        private float getOutput(){
            return output;
        }

        private float[] getInput() {
            float[] inputRow, outputRow;
            try {
                inputRow = toFloat(inputReader.readNext());
                outputRow = toFloat(outputReader.readNext());
                // The first element is the fuel consumption.
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
}
