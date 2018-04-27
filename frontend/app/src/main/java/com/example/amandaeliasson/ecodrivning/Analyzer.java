package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import android.util.ArraySet;
import android.util.Log;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Uses a pre-trained LSTM model to predict the expected fuel consumption.
 * Input:
 * Measured vehicle values as an array of floats.
 * Output:
 * A classification of the current fuel consumption.
 *
 * Requires NDK. Install by:
 * Go to tools > Android > SDK Manager
 * Click the SDK tools tab
 * Check the boxes for LLDB, CMake, and NDK.
 * Click Apply
 */

public class Analyzer {
    private TensorFlowInferenceInterface tf;
    private static final String MODEL_FILE = "file:///android_asset/lstm-32-batch2.pb";
    private static final String INPUT_NODE = "lstm_2_input";
    private static final String[] OUTPUT_NODES = {"output_node0"};
    private static final String OUTPUT_NODE = "output_node0";
    private static final long[] INPUT_SHAPE = {1, 5, 27};
    private static final int OUTPUT_SIZE = 1;
    private DynamoDBMapper dynamoDBMapper;
    private List<String> timeForSession, scoreForSession;
    private int numLogs;
    private DateFormat dateFormatter;


    public Analyzer(AssetManager assetManager, DynamoDBMapper dynamoDBMapper) {
        tf = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
        this.dynamoDBMapper = dynamoDBMapper;
        dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
    }

    /**
     * Will classify the current fuel consumption on a scale from -1 to 1.
     *
     * @param data                  - Input data to be fed to the LSTM model for prediction.
     * @param actualFuelConsumption - The actual fuel consumption.
     * @return A double between -1 and 1 where -1 is the worst and 1 is the best.
     */
    public double classify(float[] data, float actualFuelConsumption) {
        float expFuel = predictFuelConsumption(data);
        double score = 1 - densityRatio(expFuel, actualFuelConsumption);
        if (actualFuelConsumption > expFuel) {
            score *= -1;
        }
        appendScore(score);
        return score;
    }

    public void initSession() {
        Log.i("SESS_INIT", "Analyzing session initialized.");
        scoreForSession = new ArrayList<>();
        timeForSession = new ArrayList<>();
        numLogs = 0;
    }

    public void endAndUploadSession() {
        final DriveScoresDO item = new DriveScoresDO();
        item.setUserId("MyID");
        item.setDatetime(timeForSession);
        item.setDriveId("Robin");
        item.setScore(scoreForSession);

        new Thread(new Runnable() {
            @Override
            public void run() {
                dynamoDBMapper.save(item);
            }
        }).start();
        Log.i("SESS_END", "Analyzing session terminated");
        Log.i("UPLOAD", String.format("%d scores recorded and uploaded", numLogs));
    }

    /*
    ############# PRIVATE METHODS #############
     */

    private void appendScore(double score) {
        timeForSession.add(dateFormatter.format(new Date()));
        System.out.println(dateFormatter.format(new Date()));
        scoreForSession.add(Double.toString(score));
        numLogs += 1;
    }

    private float predictFuelConsumption(float[] data) {
        float[] output = new float[OUTPUT_SIZE];
        // Feed the data to the model.
        tf.feed(INPUT_NODE, data, INPUT_SHAPE);
        // Process the data.
        tf.run(OUTPUT_NODES);
        // Fetch the measurements.
        tf.fetch(OUTPUT_NODE, output);
        return output[0];
    }

    private double densityRatio(float refValue, float actValue) {
        NormalDistribution nd = new NormalDistribution(refValue, 0.069652);
        double upper = nd.density(refValue);
        double lower = nd.density(actValue);
        return lower / upper;
    }
}