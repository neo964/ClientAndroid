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

    static TextView textTargetUri = null;
    ImageView targetImage = null;
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_camera) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
        } else if (id == R.id.nav_gallery) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_GALLERY_CAPTURE);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            intent = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            /*InstanceIdService idService = new InstanceIdService();
            idService.onTokenRefresh();*/

       /* } else if (id == R.id.nav_send) {
            FirebaseApp.initializeApp(this);
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.v("Token: ",token );
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        setContentView(R.layout.activity_recognition);

        textTargetUri = (TextView) findViewById(R.id.textView2);
        targetImage = (ImageView) findViewById(R.id.imageView2);


        Log.v("Request Code", " : " + requestCode);
        Log.v("Result Code", " : " + resultCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            SaveFileToDisk saveFileToDisk = new SaveFileToDisk();
            saveFileToDisk.saveImage(staticBitmap);
            staticBitmap = (Bitmap) extras.get("data");
            targetImage.setImageBitmap(staticBitmap);
            path = new String(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + staticBitmap.toString() + ".png");
        }else if (requestCode == REQUEST_GALLERY_CAPTURE && resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            try {
                staticBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(staticBitmap);
                path = targetUri.getPath();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(MenuActivity.this, ImageClassifierActivity.class);
        startActivity(intent);
    }

    public void returnToMainPage (View view){
        setMenu();
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
                goToModels();
               // setContentView(R.layout.activity_main);
            }
        });

        csvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCSV();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

      /*  NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    public void goToCSV (){
        Intent intent = new Intent(this, ReaderCSV.class);
        startActivity(intent);

    }

    public void goToModels (){
        ArrayList <String> files = getModels();

        setContentView(R.layout.activity_models);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmodel);
      //  setSupportActionBar(toolbar);

        toolbar.setTitle("Models Available");
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.content_models, files);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(arrayAdapter);
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
