package com.example.black.client_serverexample.Notification;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InstanceIdService extends FirebaseInstanceIdService {

    public InstanceIdService (){
        super();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.v("Token Generatad: ", token);

        sendToServer (token);
    }

    private void sendToServer (String token){
        /*try {
            URL url = new URL("https://www.whatsthatlambda.com/store");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestMethod("POST");
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes("token="+ token);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                //TODO: What I Have To do;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 */   }

}
