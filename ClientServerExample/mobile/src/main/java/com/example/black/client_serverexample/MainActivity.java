package com.example.black.client_serverexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity{

    TextView response;
    Button buttonConnect;
    ImageButton buttonMenu;

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

        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonMenu = (ImageButton) findViewById(R.id.menuButton);
        response = (TextView) findViewById(R.id.responseTextView);
        client = new Client(response);

        final String url = "https://futurestud.io/images/futurestudio-logo-transparent.png";

        buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                client.downloadFile(url);
               /* Client myClient = new Client(editTextAddress.getText()
                        .toString(), Integer.parseInt(editTextPort
                        .getText().toString()), response);
                myClient.execute();*/
            }
        });
    }

    public void OpenMenu (View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
