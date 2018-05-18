package com.example.black.client_serverexample;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.black.client_serverexample.Utility.ReaderCSV;

public class ReaderCSVActivity extends Activity {

    AssetManager manager;

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
    }
}
