package com.example.black.client_serverexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.black.client_serverexample.Utility.ReaderCSV;

public class ReaderCSVActivity extends Activity {

    AssetManager manager;
    private  static final int MY_PERMISSIONS_REQUEST = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getAssets();
        setMenu ();
    }

     void setMenu (){
        setContentView(R.layout.activity_models);

        ReaderCSV readerCSV = new ReaderCSV(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "Result.csv");

        setContentView(R.layout.activity_csv);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarcsv);
        //setSupportActionBar(toolbar);

       // toolbar.setTitle("CSV");
        TextView textView = (TextView) findViewById(R.id.textViewCSV);
        textView.setText(readerCSV.getText());

         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCSV);
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (ContextCompat.checkSelfPermission(ReaderCSVActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                     ActivityCompat.requestPermissions(ReaderCSVActivity.this, new String[]
                             {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                 }

                 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 if (takePictureIntent.resolveActivity(getPackageManager()) != null && takePictureIntent != null) {
                     startActivityForResult(takePictureIntent, 1);
                 }
             }
         });
    }
}
