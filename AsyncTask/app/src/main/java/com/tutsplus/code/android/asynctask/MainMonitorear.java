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
                MainMonitorear.EjemploAsyncTask ejemploAsyncTask = new MainMonitorear.EjemploAsyncTask();
                ejemploAsyncTask.execute();

                break;
            default:
                break;
        }
    }

    public void generateApps(){
        Context context = getApplicationContext();
        Apps apps = new Apps();
        apps.obtenerAppsEnUso(context);
        Log.d("TAG", "generateApps: "+ apps.listaApps);

        //String json = new Gson().toJson(apps); //se genera el json a partir del objeto app
        //System.out.println(json);
    }

    private BroadcastReceiver infoBateria = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Log.d("PORCENTAJE BATERIA ", ""+ level);

        }
    };



    private class EjemploAsyncTask extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getBaseContext(), "Comienzo del monitoreo", Toast.LENGTH_SHORT).show();
            generateApps();
            registerReceiver(infoBateria, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        }

        @Override
        protected Boolean doInBackground(Void... params) {

                publishProgress(); //llama a onprogressUpdate
                generateApps();
                UnSegundo(); //con esto se setea un tiempo antes de que se termine el onprogress thread

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            openNavigator();
            generateApps();

        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            //super.onPostExecute(aVoid);
            if(resultado){
                Toast.makeText(getBaseContext(), "Datos energeticos monitoreados", Toast.LENGTH_SHORT).show();
                generateApps();
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getBaseContext(), "Tarea Larga Ha sido cancelada", Toast.LENGTH_SHORT).show();
            //setContentView(R.layout.activity_main);

        }


    }







}