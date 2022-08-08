package com.tutsplus.code.android.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MainMonitorear extends AppCompatActivity  implements  View.OnClickListener{

    Button button3;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_monitorear);
        button3 = (Button)findViewById(R.id.button3);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        button3.setOnClickListener(this);
    }

   private void UnSegundo(){
        try{
            Thread.sleep(10000); //diez segundos
        }catch (InterruptedException e){}
    }

    private void openNavigator(){
        String url = "http://www.google.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button3:
                MainMonitorear.MiAsyncTask ejemploAsyncTask = new MainMonitorear.MiAsyncTask();
                ejemploAsyncTask.execute();

                break;
            default:
                break;
        }
    }

    public Apps generateApps() {
        Context context = getApplicationContext();
        Apps apps = new Apps();
        apps.obtenerAppsEnUso(context);
        return apps;
    }
    private int getBattery(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return level;

    }



    private class MiAsyncTask extends AsyncTask<Void,Integer,Boolean>{



        Monitor monitor;
        int num;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getBaseContext(), "Comienzo del monitoreo", Toast.LENGTH_SHORT).show();
            this.monitor=  new Monitor(generateApps(), getBattery()); //G1, MEB
        }

        // {"G1": {}.... "G2":{}.  }

        @Override
        protected Boolean doInBackground(Void... params) {

                publishProgress(); //llama a onprogressUpdate
                UnSegundo(); //con esto se setea un tiempo antes de que se termine el onprogress thread

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            monitor.anadirApps(generateApps()); //G2
           // generateApps();//G2
            openNavigator();
              // generateApps(); //G3
            //medir energia ME APP


        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            //super.onPostExecute(aVoid);
            if(resultado){
                monitor.anadirApps(generateApps() ); //G3
                monitor.anadirPorcentajeBateria(getBattery()); //MEapp
                Toast.makeText(getBaseContext(), "Datos energeticos monitoreados", Toast.LENGTH_SHORT).show();
                monitor.anadirApps(generateApps()); //G4
                String json = new Gson().toJson(monitor); //se genera el json
                json.toString();
                int length = json.length();

                for(int i=0; i<length; i+=1024)
                {
                    if(i+1024<length)
                        Log.d("JSON OUTPUT", json.substring(i, i+1024));
                    else
                        Log.d("JSON OUTPUT", json.substring(i, length));
                }

            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getBaseContext(), "Deteccion de datos cancelado", Toast.LENGTH_SHORT).show();


        }


    }
}