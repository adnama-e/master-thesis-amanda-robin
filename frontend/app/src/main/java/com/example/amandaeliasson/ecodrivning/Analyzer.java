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
    private static final String MODEL_FILE = "file:///android_asset/lstm-32-batch2.pb";
    private static final String INPUT_NODE = "lstm_2_input";
    private static final String[] OUTPUT_NODES = {"output_node0"};
    private static final String OUTPUT_NODE = "output_node0";
    private static final long[] INPUT_SIZE = {1, 5, 27};
    private static final int OUTPUT_SIZE = 1;
    private static AssetManager assetManager;

    public Analyzer(AssetManager am) {
        tf = new TensorFlowInferenceInterface(am, MODEL_FILE);
        dataHandler = new DataHandler("scaled_kia.csv");
    }

    public void realTime() {
        String[] row = dataHandler.getRow();
        while (row != null) {
            System.out.println(row.toString());
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

        private String[] getRow() {
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
