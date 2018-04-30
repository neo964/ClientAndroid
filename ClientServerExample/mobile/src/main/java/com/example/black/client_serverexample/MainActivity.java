package com.example.black.client_serverexample;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity{

    TextView response;
    Button buttonConnect;

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
/*
    private void downloadFile(String url) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://futurestud.io/");

        Retrofit retrofit = builder.build();

        // TODO get client

        FileDownloadClient fileDownloadClient = retrofit.create(FileDownloadClient.class);
        fileDownloadClient.downloadFile();

        Call<ResponseBody> call = fileDownloadClient.downloadFileStream(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {

                new AsyncTask<Void, Void, Void>(){
                    @Override
                    protected Void doInBackground (Void... voids){
                        boolean success = writeResponseBodyToDisk(response.body());

                        return null;
                    }
                }.execute();
               // Toast.makeText(MainActivity.this, "Download was Successful :)" + success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "NO :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk (ResponseBody body){
        try {
            //TODO Here to change the directory
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "futureStudio.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte [] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true){
                    int read = inputStream.read(fileReader);

                    if (read == -1){
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    //response.append("Download Future Studio: " + "File Donwload:" + fileSizeDownloaded + " of: " + fileSize + "\n");
                    Log.d("Download Future Studio","file donwload" + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return  true;
            } catch (IOException e){
                return  false;
            }finally {
                if (inputStream != null){
                    inputStream.close();
                }
                if (outputStream != null){
                    outputStream.close();
                }
            }
            }catch (IOException e){
            return  false;
        }

    }*/
}
