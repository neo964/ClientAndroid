package com.example.black.client_serverexample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.black.client_serverexample.classifier.Recognition;
import com.example.black.client_serverexample.classifier.SaveFileToDisk;
import com.example.black.client_serverexample.classifier.TensorFlowHelper;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PhotoClassifier {
    private static final String TAG = "ImageClassifierActivity";
    private  static final int MY_PERMISSIONS_REQUEST = 100;

    /** Camera image capture size */
    private static final int PREVIEW_IMAGE_WIDTH = 640;
    private static final int PREVIEW_IMAGE_HEIGHT = 480;
    /** Image dimensions required by TF model */
    private static final int TF_INPUT_IMAGE_WIDTH = 224;
    private static final int TF_INPUT_IMAGE_HEIGHT = 224;
    /** Dimensions of model inputs. */
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    /** TF model asset files */
    private static final String LABELS_FILE = "labels.txt";
    private static final String MODEL_FILE = "mobilenet_quant_v1_224.tflite";

    // private ButtonInputDriver mButtonDriver;
    private boolean mProcessing;

    private ImageView mImage;
    private TextView mResultText;
    private StringBuilder galleryString;

    // TODO: ADD ARTIFICIAL INTELLIGENCE
    private Interpreter mTensorFlowLite;
    private List<String> mLabels;
    /**
     * Initialize the classifier that will be used to process images.
     */
    private void initClassifier() {
        // TODO: ADD ARTIFICIAL INTELLIGENCE
        try {
            mTensorFlowLite = new Interpreter(TensorFlowHelper.loadModelFile(null, MODEL_FILE));
            mLabels = TensorFlowHelper.readLabels(null, LABELS_FILE);
        } catch (IOException e) {
            Log.w(TAG, "Unable to initialize TensorFlow Lite.", e);
        }
    }

    /**
     * Clean up the resources used by the classifier.
     */
    private void destroyClassifier() {
        // TODO: ADD ARTIFICIAL INTELLIGENCE
        mTensorFlowLite.close();
    }

    /**
     * Process an image and identify what is in it. When done, the method
     * {@link #onPhotoRecognitionReady(Collection)} must be called with the results of
     * the image recognition process.
     *
     * @param image Bitmap containing the image to be classified. The image can be
     *              of any size, but preprocessing might occur to resize it to the
     *              format expected by the classification process, which can be time
     *              and power consuming.
     */
    private void doRecognize(Bitmap image) {
        // TODO: ADD ARTIFICIAL INTELLIGENCE
        // Allocate space for the inference results
        byte[][] confidencePerLabel = new byte[1][mLabels.size()];
        // Allocate buffer for image pixels.
        int[] intValues = new int[TF_INPUT_IMAGE_WIDTH * TF_INPUT_IMAGE_HEIGHT];
        ByteBuffer imgData = ByteBuffer.allocateDirect(
                DIM_BATCH_SIZE * TF_INPUT_IMAGE_WIDTH * TF_INPUT_IMAGE_HEIGHT * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());

        // Read image data into buffer formatted for the TensorFlow model
        TensorFlowHelper.convertBitmapToByteBuffer(image, intValues, imgData);

        // Run inference on the network with the image bytes in imgData as input,
        // storing results on the confidencePerLabel array.
        mTensorFlowLite.run(imgData, confidencePerLabel);

        // Get the results with the highest confidence and map them to their labels
        Collection<Recognition> results = TensorFlowHelper.getBestResults(confidencePerLabel, mLabels);
        // Report the results with the highest confidence
        onPhotoRecognitionReady(results);
    }

    /**
     * Load the image that will be used in the classification process.
     * When done, the method {@link #onPhotoReady(Bitmap)} must be called with the image.
     */

    // --------------------------------------------------------------------------------------
    // NOTE: The normal codelab flow won't require you to change anything below this line,
    // although you are encouraged to read and understand it.

    PhotoClassifier (){
        initClassifier();
    }

    /**
     * Register a GPIO button that, when clicked, will generate the {@link KeyEvent#KEYCODE_ENTER}
     * key, to be handled by {@link #onKeyUp(int, KeyEvent)} just like any regular keyboard
     * event.
     *
     * If there's no button connected to the board, the doRecognize can still be triggered by
     * sending key events using a USB keyboard or `adb shell input keyevent 66`.
     */
    /**
     * Image capture process complete
     */

    public String onPhotoReady(Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
        doRecognize(bitmap);
        return galleryString.toString();
    }

    /**
     * Image classification process complete
     */
    private void onPhotoRecognitionReady(Collection<Recognition> results) {
        updateStatus(formatResults(results));
        mProcessing = false;
    }

    /**
     * Format results list for display
     */
    private String formatResults(Collection<Recognition> results) {
        if (results == null || results.isEmpty()) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator<Recognition> it = results.iterator();
            int counter = 0;
            while (it.hasNext()) {
                Recognition r = it.next();
                sb.append(r.getTitle());
                counter++;
                if (counter < results.size() - 1) {
                    sb.append(", ");
                } else if (counter == results.size() - 1) {
                    sb.append(" or ");
                }
            }
            galleryString = sb;
            return sb.toString();
        }
    }

    /**
     * Report updates to the display and log output
     */
    private void updateStatus(String status) {
        Log.d(TAG, status);
        mResultText.setText(status);
    }
}
