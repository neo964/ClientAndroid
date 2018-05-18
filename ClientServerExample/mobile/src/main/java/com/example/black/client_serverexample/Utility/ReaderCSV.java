package com.example.black.client_serverexample.Utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReaderCSV {
    String path;

    public ReaderCSV (String path){
        this.path = path;
    }

    public String getText (){
        StringBuilder text = new StringBuilder();
        try  {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String s;
            while((s = bufferedReader.readLine()) != null) {
                text.append(s);
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

}
