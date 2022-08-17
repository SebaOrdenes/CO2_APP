package com.tutsplus.code.android.asynctask;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainMonitorear extends AppCompatActivity  implements  View.OnClickListener{

    Button button3;
    ProgressBar progressBar;
    long hour_in_mil = 1000*60*60; // In Milliseconds
    long end_time = System.currentTimeMillis();
    long start_time = end_time - hour_in_mil;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_monitorear);
        //Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        //startActivity(permissionIntent);
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


    public String getTopActivtyFromLolipopOnwards() {
        String topPackageName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap< Long, UsageStats > mySortedMap = new TreeMap< Long, UsageStats >();
                for (UsageStats usageStats: stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    return topPackageName;
                    //Log.e("TopPackage Name", topPackageName);
                    //Log.d(TAG, "getTopActivtyFromLolipopOnwards: HOLA PASE POR AQUI");
                }
            }
        }
        return null;
        //return topPackageName;
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
            this.monitor=  new Monitor(); //G1, MEB
            //this.monitor.añadir("G1", generateApps());
            this.monitor.añadir("G1", getTopActivtyFromLolipopOnwards());
            this.monitor.añadir("MEB", getBattery());
        }



        @Override
        protected Boolean doInBackground(Void... params) {

                publishProgress(); //llama a onprogressUpdate
                UnSegundo(); //con esto se setea un tiempo antes de que se termine el onprogress thread

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //this.monitor.añadir("G2", generateApps());
            this.monitor.añadir("G2", getTopActivtyFromLolipopOnwards());
            openNavigator();
           // generateApps();//G2

           // supportFinishAfterTransition();

        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            //super.onPostExecute(aVoid);
            if(resultado){
               // this.monitor.añadir("G3", generateApps());
                this.monitor.añadir("G3", getTopActivtyFromLolipopOnwards());
                this.monitor.añadir("MEApp", getBattery());
                Toast.makeText(getBaseContext(), "Datos energeticos monitoreados", Toast.LENGTH_SHORT).show();
                //this.monitor.añadir("G4", generateApps());
                this.monitor.añadir("G4", getTopActivtyFromLolipopOnwards());
                this.monitor.printear();



            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getBaseContext(), "Deteccion de datos cancelado", Toast.LENGTH_SHORT).show();


        }


    }




}