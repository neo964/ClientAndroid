package com.example.black.client_serverexample;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.black.client_serverexample.classifier.Recognition;
import com.example.black.client_serverexample.classifier.TensorFlowHelper;
import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class ImageClassifierActivity extends Activity {
    private static final String TAG = "ImageClassifierActivity";

    /** Image dimensions required by TF model */
    private static final int TF_INPUT_IMAGE_WIDTH = 224;
    private static final int TF_INPUT_IMAGE_HEIGHT = 224;
    /** Dimensions of model inputs. */
    private static final int DIM_BATCH_SIZE = 1;
    private static final int DIM_PIXEL_SIZE = 3;
    /** TF model asset files */
    private static final String LABELS_FILE = "labels.txt";
    private static final String MODEL_FILE = "mobilenet_quant_v1_224.tflite";
    private static final String RESULT_FILE = "Result.csv";
    private static final String ca = String.valueOf(R.string.empty_result);

    String partialResult = "Not Recognized";
    TextView textTargetUri = null;
    ImageView targetImage = null;



    private  static final int MY_PERMISSIONS_REQUEST = 100;

    private Interpreter mTensorFlowLite;
    private List<String> mLabels;

    private void initClassifier() {
        try {
            mTensorFlowLite = new Interpreter(TensorFlowHelper.loadModelFile(this, MODEL_FILE));
            mLabels = TensorFlowHelper.readLabels(this, LABELS_FILE);
        } catch (IOException e) {
            Log.w(TAG, "Unable to initialize TensorFlow Lite.", e);
        }
    }

    private void destroyClassifier() {
        mTensorFlowLite.close();
    }

    private void doRecognize(Bitmap image) {
        Log.e("PRova ", ca);

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
        formatResults(results);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //setContentView(R.layout.activity_camera);
        initClassifier();
        doRecognize(MenuActivity.staticBitmap);

        saveResultOnFile ();
        setContentView(R.layout.activity_recognition);
        textTargetUri = findViewById(R.id.textRecognition);
        textTargetUri.setText(partialResult);

        targetImage = findViewById(R.id.imageViewRecognition);
        targetImage.setImageBitmap(MenuActivity.staticBitmap);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabRecognition);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ImageClassifierActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ImageClassifierActivity.this, new String[]
                            {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                }

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null && takePictureIntent != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

       // onBackPressed();
    }

    private void saveResultOnFile() {
        if (ContextCompat.checkSelfPermission(ImageClassifierActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImageClassifierActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        }
        String path = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "Result.csv";

        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileWriter writer;
        try {
            writer = new FileWriter(path, true);
            PrintWriter printer = new PrintWriter(writer);
            String s = MenuActivity.path + ":" + partialResult + ";" + "\n";

            printer.append(s);
            printer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void formatResults(Collection<Recognition> results) {
        if (results == null || results.isEmpty()) {
           // MenuActivity.textTargetUri.setText("Not Recognized");
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
            partialResult = sb.toString();
           // MenuActivity.textTargetUri.setText(partialResult);
          //  Log.e("Result  ", MenuActivity.textTargetUri.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            destroyClassifier();
        } catch (Throwable t) {
            // close quietly
        }
    }
}
