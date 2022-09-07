package com.tutsplus.code.android.asynctask;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.simple.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Monitor {

    public JSONObject fases;
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
     public Monitor(){  //constructor
          this.fases= new JSONObject();

     }

     public void añadir(String key, Object value) {
         try {
             this.fases.put(key, value);
         } catch (JSONException e) {
             e.printStackTrace();
         }
     }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public void printear(){
         try {
             File path= Environment.getExternalStorageDirectory();
             File dir= new File(path,"/Documents/");
             dir.mkdirs();
             String fileName= "ARCHIVO.txt";
             File file= new File(dir,fileName);
             FileWriter fw= new FileWriter(file.getAbsoluteFile());
             BufferedWriter bw= new BufferedWriter(fw);
             int name = (int) fases.get("MEB");
             int name2 = (int) fases.get("MEApp");
             bw.write(this.fases.get("G1").toString()+ "\n"+this.fases.get("G2").toString()+ "\n"+this.fases.get("G3").toString()+"\n"+this.fases.get("G4").toString());
             /*try {
                 bw.write(this.fases.get("OpenAnotherApp").toString());
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (JSONException e) {
                 e.printStackTrace();
             }*/

             bw.write("\n"+ this.fases);
             //bw.write("\n"+ this.fases.get("Val1").toString()+ "\n"+ this.fases.get("Val2").toString()+"\n"+ this.fases.get("Val3").toString());
             bw.close();
             Log.d("d","TXT GUARDADO");

         } catch (Exception e) {
             e.printStackTrace();
         }
       }

    public JSONObject getFases() {
        return fases;
    }

    public void setFases(JSONObject fases) {
        this.fases = fases;
    }



}
