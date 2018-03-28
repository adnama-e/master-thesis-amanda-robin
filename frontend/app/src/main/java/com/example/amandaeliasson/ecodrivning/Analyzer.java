package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import org.apache.commons.math3.distribution.NormalDistribution;
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

    public double classify(float[] data, float actualFuelConsumption) {
        // Calculate the expected fuel consumption.
        float expFuel = predictFuelConsumption(data);
        double cls = -1;
        switch (classificationMode) {
            case INTERVAL_MODE:
                cls = intervalClassification(expFuel, actualFuelConsumption);
                break;
            case REGRESSION_MODE:
                cls = regressionClassification(expFuel, actualFuelConsumption);
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

    private double regressionClassification(float refValue, float actValue) {
        if (actValue < refValue) {
            return 0;
        }
        return 1 - usingDensity(refValue, actValue);
    }

    private float intervalClassification(float refValue, float actValue) {
        double densityRatio = usingDensity(refValue, actValue);
        return 0;
    }

    private double usingProbability(float refValue, float actValue) {
        NormalDistribution nd = new NormalDistribution(refValue, 0.069652);
        if (refValue < actValue) {
            return nd.probability(refValue, actValue);
        } else {
            return nd.probability(actValue, refValue);
        }
    }

    private double usingDensity(float refValue, float actValue) {
        NormalDistribution nd = new NormalDistribution(refValue, 0.069652);
        double upper = nd.density(refValue);
        double lower = nd.density(actValue);
        return lower / upper;
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
