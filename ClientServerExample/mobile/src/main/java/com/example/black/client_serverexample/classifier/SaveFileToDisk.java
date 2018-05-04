package com.example.black.client_serverexample.classifier;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class SaveFileToDisk {

    public boolean writeResponseBodyToDisk (ResponseBody body){
        try {
            //TODO Here to change the directory
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "futureStudio.png");

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
                    //textResponse.append ("Download Future Studio: " + "File Donwload:" + fileSizeDownloaded + " of: " + fileSize + "\n");
                    Log.d("Download Future Studio","file donwload" + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return  true;
            } catch (IOException e){
                e.printStackTrace();
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
    }

    public boolean saveImage (Bitmap bitmap) {
        try {
            //TODO Here to change the directory
            File capture = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "capture.png");

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(capture);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
