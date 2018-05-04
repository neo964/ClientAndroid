package com.example.black.client_serverexample;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.black.client_serverexample.classifier.SaveFileToDisk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Client {

   // String dstAddress;
   // int dstPort;
    String response = "";
    TextView textResponse;
    SaveFileToDisk saveFileToDisk;

    Client(TextView textResponse) {
        //dstAddress = addr;
       // dstPort = port;
        this.textResponse = textResponse;
        saveFileToDisk = new SaveFileToDisk();
    }

    void downloadFile(String url) {
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
                        boolean success = saveFileToDisk.writeResponseBodyToDisk(response.body());

                        return null;
                    }
                }.execute();
                // Toast.makeText(MainActivity.this, "Download was Successful :)" + success, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              //  Toast.makeText(MainActivity., "NO :(", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
