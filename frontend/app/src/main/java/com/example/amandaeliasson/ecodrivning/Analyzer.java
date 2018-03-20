package com.example.amandaeliasson.ecodrivning;

import android.content.res.AssetManager;
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
        float[] output = new float[1];
        while (row != null) {
            System.out.println("new row");
            row = dataHandler.getRow();
            // Feed the data to the model.
            tf.feed(INPUT_NODE, row);
            // Process the data.
            tf.run(OUTPUT_NODES, true);
            // Fetch the result.
            tf.fetch(OUTPUT_NODE, output);
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
        private int dataIndex = 0;
        private final int NUM_DATASETS = 3;
        private final int TIMESTEPS = 5;

        private DataHandler(AssetManager assetManager) {
            String csvFile = "test_df" + Integer.toString(dataIndex) + ".csv";
            try {
                Reader reader = new InputStreamReader(assetManager.open(csvFile));
                csvReader = new CSVReader(reader);
                header = csvReader.readNext();
            } catch (IOException e ) {
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
                convRow = new float[inputRow.length - 1];
                for (int i = 0; i < inputRow.length - 1; i++) {
                    convRow[i] = Float.parseFloat(inputRow[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                convRow = null;
            }
            return convRow;
        }

        private float[] formatData() {
            return null;
        }
    }
}
