package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
import android.os.Environment;
import com.opencsv.CSVReader;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;

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
    private AssetManager assetManager;

    public Analyzer(AssetManager assetManager) {
        this.assetManager = assetManager;
        tf = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
        dataHandler = new DataHandler(assetManager);
    }

    public void realTime() {
        float[] row = dataHandler.getRow();
        while (row != null) {
            System.out.println("new row");
            row = dataHandler.getRow();
            tf.feed(INPUT_NODE, row);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void postDrive() {

    }

    private class DataHandler {
        private CSVReader csvReader;
        private String[] header;

        private DataHandler(AssetManager assetManager) {
            try {
                Reader reader = new InputStreamReader(assetManager.open("scaled_kia.csv"));
                csvReader = new CSVReader(reader);
                String[] header = csvReader.readNext();
            } catch (Exception e ) {
                System.err.println(e.getStackTrace());
            }
        }

        private String[] getHeader() {
            return header;
        }

        private float[] getRow() {
            String[] inputRow;
            float[] convRow;
            try {
                inputRow = csvReader.readNext();
                convRow = new float[inputRow.length - 1];
                for (int i = 0; i < inputRow.length-1; i++) {
                    convRow[i] = Float.parseFloat(inputRow[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                convRow = null;
            }
            return convRow;
        }
    }
}
