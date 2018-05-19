package com.example.black.client_serverexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.black.client_serverexample.Utility.ReaderCSV;
import com.example.black.client_serverexample.classifier.SaveFileToDisk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MenuActivity extends Activity {

    /*static TextView textTargetUri = null;
    ImageView targetImage = null;*/
    final int REQUEST_IMAGE_CAPTURE = 1;
    final int REQUEST_GALLERY_CAPTURE = 2;
    static Bitmap staticBitmap = null;
    static String path = null;
    Button galleryButton;
    Button updateButton;
    Button csvButton;
    private  static final int MY_PERMISSIONS_REQUEST = 100;
    AssetManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getAssets();
        setMenu();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //setContentView(R.layout.activity_recognition);

      /*  textTargetUri = findViewById(R.id.textRecognition);
        targetImage = findViewById(R.id.imageViewRecognition);*/


        Log.v("Request Code", " : " + requestCode);
        Log.v("Result Code", " : " + resultCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            SaveFileToDisk saveFileToDisk = new SaveFileToDisk();
            saveFileToDisk.saveImage(staticBitmap);
            staticBitmap = (Bitmap) extras.get("data");
            path = new String(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + staticBitmap.toString() + ".png");
        }else if (requestCode == REQUEST_GALLERY_CAPTURE && resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            try {
                staticBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                path = targetUri.getPath();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(MenuActivity.this, ImageClassifierActivity.class);
        startActivity(intent);
    }

    private void setMenu (){
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        toolbar.setTitle("Home");

        galleryButton = (Button) findViewById(R.id.GalleryButton);
        updateButton = (Button) findViewById(R.id.UpdateButton);
        csvButton = (Button) findViewById(R.id.CSVButton);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY_CAPTURE);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
               // goToModels();
               // setContentView(R.layout.activity_main);
            }
        });

        csvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ReaderCSVActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MenuActivity.this, new String[]
                            {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                }

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

    }

    public ArrayList<String> getModels () {
        try {
            String[] listss = manager.list("models");
            for (int i = 0; i < listss.length; ++i) {
                Log.e("FILE:", "models" + "/" + listss[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
