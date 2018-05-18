package com.example.black.client_serverexample.Utility;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelsView {
    private final String resource;
    AssetManager manager;


    public ModelsView (AssetManager manager){
        resource = "models/";
        this.manager = manager;
    }

    public ArrayList<String> getModels (){
        try {
            String [] listss = manager.list("models/");
            for (int i=0; i<listss.length; ++i)
            {
                Log.e("FILE:", resource +"/"+ listss[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            String list[] = manager.list(resource);
            Log.e("FILES", String.valueOf(list.length));

            if (list != null)
                for (int i=0; i<list.length; ++i)
                {
                    Log.e("FILE:", resource +"/"+ list[i]);
                }
            return  null;
        } catch (IOException e) {
            Log.v("List error:", "can't list" + resource);
        }

        /*
        File path = new File(resource);
        ArrayList<String> files = new ArrayList<>();
        File [] pathfiles = null;
        if (path.isDirectory()){
            Log.v("Directory", "\n" +
                    "            path.length();");
           // pathfiles = path.listFiles();
        } else{
            Log.v("file", "");
        }
        Log.v( "CIao: ", path.list().toString());
       /* for (int i = 0; i < pathfiles.length; i++){
            Log.v("Files: ", i + ": " + pathfiles[i].getName());
           // files.add(pathfiles[i].getName());
        }*/
/*
        Log.v("Files: ", ": ENDFILE");
        return files;*/

        return null;
    }

}
