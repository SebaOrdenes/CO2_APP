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

    private JSONObject fases;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
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
             String fileName= "Miarchivo.txt";
             File file= new File(dir,fileName);
             FileWriter fw= new FileWriter(file.getAbsoluteFile());
             BufferedWriter bw= new BufferedWriter(fw);
             int name = (int) fases.get("MEB");
             int name2 = (int) fases.get("MEApp");
             //Apps appaux= (Apps) fases.get("G1");
             //Apps appaux2= (Apps) fases.get("G2");
             //Apps appaux3= (Apps) fases.get("G3");
             //Apps appaux4= (Apps) fases.get("G4");


             //bw.write(fases.toString());
            // bw.write(fases.get("G1").toString());
            // bw.write(appaux.listaApps.toString()  +"\n" + appaux2.listaApps.toString() +"\n" + appaux3.listaApps.toString() +"\n" + appaux4.listaApps.toString());
             bw.write("\n"+this.fases.get("G1").toString()+ "\n"+this.fases.get("G2").toString()+ "\n"+this.fases.get("G3").toString()+"\n"+this.fases.get("G4").toString());
             //Log.d("a",""+ appaux.listaApps.toString()  +"\n" + appaux2.listaApps.toString() +"\n" + appaux3.listaApps.toString() +"\n" + appaux4.listaApps.toString());


             //bw.write(appaux.listaApps.toString());
             bw.close();
             Log.d("d","TXT GUARDADO");



         } catch (Exception e) {
             e.printStackTrace();
         }



       /*  try {

             File tarjeta = Environment.getExternalStorageDirectory();
             File file = new File(tarjeta.getAbsolutePath(), "nombre");
             OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
             osw.write(this.fases.toString());
             osw.flush();
             osw.close();
         } catch (IOException e) {
             e.printStackTrace();
         } */

        /*try {
            Writer output = null;
            File file = new File("storage/sdcard/" + "HOLA" + ".json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(this.fases.toString());
            output.close();
            Log.d("A","HOLA LLEGUE AQUI");
            //Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.d("A","HOLA NO SE PUDO CREAR AQUI");
            //Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }*/


       /* Writer output = null;
        String text = this.fases.toString();
        ContextWrapper contextWrapper= new ContextWrapper(context);
       // File file = new File("salidas.json");
        File path= contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
       // File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        File file= new File(path,"demo"+".json");
        try {
            path.mkdirs();
            output = new BufferedWriter(new FileWriter(file));
            Log.d("A", "printear: RESULTADO OK ");
            output.write(text);
            output.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        //File file = new File("storage/sdcard", "/" + "salidas.json");

*/



    }



}
