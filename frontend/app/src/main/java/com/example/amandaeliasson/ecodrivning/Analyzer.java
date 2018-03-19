package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import android.os.Environment;
import com.opencsv.CSVReader;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

    public Analyzer(AssetManager am, String model) {
        tf = new TensorFlowInferenceInterface(am, "file:///android_asset/" + model);
        dataHandler = new DataHandler("scaled_kia.csv");
    }

    public void realTime() {
        String[] row;
        while (row = dataHandler.getRow() != null) {
            tf.feed("input:0", input, INPUT_SHAPE);

        }
    }

    public void postDrive() {

    }

    private class DataHandler {
        private CSVReader reader;

        private DataHandler(String pathToCSV) {
            try {
                File csvFile = new File(Environment.getExternalStorageDirectory() + pathToCSV);
                reader = new CSVReader(new FileReader("csvFile.getAbsolutePath"));
            } catch (Exception e ) {
                System.err.println(e.getStackTrace());
            }
        }

        private String[] getRow(String dataType) {
            String[] row;
            try {
                row = reader.readNext();
            } catch (IOException e) {
                e.printStackTrace();
                row = null;
            }
            return row;
        }
    }
}
