package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import android.util.Log;

import com.opencsv.CSVReader;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Uses a pre-trained LSTM model to predict the expected fuel consumption.
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
    private static final int CLASSIFY_MODE = 1, REGRESSION_MODE = 2;
    private AssetManager assetManager;
    private Classifier classifier;
    private int currentMode;

    public Analyzer(AssetManager assetManager, Classifier classifier) {
        this.assetManager = assetManager;
        this.classifier = classifier;
        currentMode = CLASSIFY_MODE;
        tf = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
        dataHandler = new DataHandler(assetManager);
    }

    public float predictFuelConsumption(float[] data) {
        float[] output = new float[OUTPUT_SIZE];
        // Feed the data to the model.
        tf.feed(INPUT_NODE, data, INPUT_SHAPE);
        // Process the data.
        tf.run(OUTPUT_NODES);
        // Fetch the result.
        tf.fetch(OUTPUT_NODE, output);
        return output[0];
    }

    /**
     * Sets the classifying mode.
     * Available modes:
     *  Classes - Outputs a class.
     *  Regression - Outputs a value between 0 and 1.
     * @param clsMode The mode to use.
     */
    public void setClassifyingMode(int clsMode) {
        currentMode = clsMode;
    }

    public String classify(float refValue, float trueValue) {
        if (currentMode == CLASSIFY_MODE) {

        } else if (currentMode == REGRESSION_MODE) {

        } else {
            Log.e("ERROR", "Chosen mode (" + currentMode + ") isn't available");
            return null;
        }
    }

    public void postDrive() {

    }

    private class DataHandler {
        private CSVReader csvReader;
        private String[] header;
        private int dataIndex = 0;
        private final int NUM_DATASETS = 3;

        private DataHandler(AssetManager assetManager) {
            String csvFile = "test_input" + Integer.toString(dataIndex) + ".csv";
            try {
                Reader reader = new InputStreamReader(assetManager.open(csvFile));
                csvReader = new CSVReader(reader);
                header = csvReader.readNext();
            } catch (IOException e) {
                System.err.println(e.getStackTrace());
            }
        }

        private String[] getHeader() {
            return header;
        }

        private boolean pickDataset(int newIndex) {
            return false;
        }

        private float[] getRow() {
            String[] inputRow;
            float[] convRow;
            try {
                inputRow = csvReader.readNext();
                convRow = toFloat(inputRow);
            } catch (Exception e) {
                e.printStackTrace();
                convRow = null;
            }
            return convRow;
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
