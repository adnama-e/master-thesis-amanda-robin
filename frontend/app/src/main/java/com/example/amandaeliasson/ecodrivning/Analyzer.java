package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

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
    private static final String MODEL_FILE = "file:///android_asset/lstm-32-batch2.pb";
    private static final String INPUT_NODE = "lstm_2_input";
    private static final String[] OUTPUT_NODES = {"output_node0"};
    private static final String OUTPUT_NODE = "output_node0";
    private static final long[] INPUT_SHAPE = {1, 5, 27};
    private static final int OUTPUT_SIZE = 1;
    public static final int REGRESSION_MODE = 1, INTERVAL_MODE = 2;
    private static final int NUM_CLASSES = 4;
    private int classificationMode;

    public Analyzer(AssetManager assetManager, int classificationMode) {
        this.classificationMode = classificationMode;
        tf = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
    }

    public float classify(float[] data, float actualFuelConsumption) {
        // Calculate the expected fuel consumption.
        float expFuel = predictFuelConsumption(data);
        float diff = expFuel - actualFuelConsumption;
        float cls = -1;
        switch (classificationMode) {
            case INTERVAL_MODE:
                cls = intervalClassification(sigmoid(diff));
                break;
            case REGRESSION_MODE:
                cls = regressionClassification(sigmoid(diff));
                break;
        }
        return cls;
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

    private float regressionClassification(float activationValue) {
        return 1 - activationValue;
    }

    private float intervalClassification(float activationValue) {
        float limit = 1;
        for (int i = NUM_CLASSES; i > 0; i--) {
            if (activationValue < limit / i) {
                return (float) i;
            }
        }
        return -1;
    }

    /**
     * A sigmoid activation function.
     * @param x - the difference between the expected and the actual value.
     * @return - A float in the range (0, 1). A lower value means bad driving.
     */
    private float sigmoid(float x) {
        return (float) (1/(1+5*Math.exp(x)));
    }

}
