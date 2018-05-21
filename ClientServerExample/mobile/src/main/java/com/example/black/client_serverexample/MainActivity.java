package com.example.black.client_serverexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.black.client_serverexample.Utility.Client;

public class MainActivity extends Activity{

    TextView response;
    Button buttonConnect;
    //ImageButton buttonMenu;
    NavigationView navigationView;

    Client client;

    private  static final int MY_PERMISSIONS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.MANAGE_DOCUMENTS}, MY_PERMISSIONS_REQUEST);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST);
        }

        buttonConnect = (Button) findViewById(R.id.connectButton);
       // buttonMenu = (ImageButton) findViewById(R.id.menuButton);
        response = (TextView) findViewById(R.id.InfoText);
        client = new Client(response);

        final String url = "https://futurestud.io/images/futurestudio-logo-transparent.png";

        buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                client.downloadFile(url);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST);
                }

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null && takePictureIntent != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });

    }

    public void OpenMenu (View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
