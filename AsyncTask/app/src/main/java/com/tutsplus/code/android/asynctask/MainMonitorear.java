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
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainMonitorear extends AppCompatActivity  implements  View.OnClickListener{

    Button button3;
    EditText segundosIngresados;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_monitorear);
        button3 = (Button)findViewById(R.id.button3);
        segundosIngresados= findViewById(R.id.NumeroIngresado);
        button3.setOnClickListener(this);


    }

   private void esperarSegundos(int segundosDeEspera){
        segundosDeEspera= segundosDeEspera*1000;
        try{
            Thread.sleep(segundosDeEspera); //
        }catch (InterruptedException e){}
    }

    private void openNavigator(){
        String url = "http://www.google.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }


    public String getTopActivtyFromLolipopOnwards(int num) {
        int numaux= num*1000;
        String topPackageName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - numaux * 10, time);
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
        return "null";
        //return topPackageName;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button3:
                segundosIngresados.setError(null);
                String posibleNumero = segundosIngresados.getText().toString();
                if ("".equals(posibleNumero)) {
                    // Primer error
                    segundosIngresados.setError("Introduce un número");
                    // Le damos focus
                    segundosIngresados.requestFocus();
                    // Y terminamos la ejecución
                    return;
                }
                int segundos= Integer.parseInt(segundosIngresados.getText().toString().trim());

                if (segundos> 60 || segundos <=0 ){
                    segundosIngresados.setError("Introduce un número mayo a 0 y menor a 60");
                    // Le damos focus
                    segundosIngresados.requestFocus();
                    // Y terminamos la ejecución
                    return;
                }else {

                    new Task2().execute();
                }


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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    class Task2 extends AsyncTask<String, Void, String>{
        Monitor monitor;
        int segundos= Integer.parseInt(segundosIngresados.getText().toString().trim());
        @Override
        protected void onPreExecute() {
            //onBackPressed();
            Toast.makeText(getBaseContext(), "Comienzo del monitoreo", Toast.LENGTH_SHORT).show();
            this.monitor=  new Monitor(); //G1, MEB
            this.monitor.añadir("G1", getTopActivtyFromLolipopOnwards(segundos));
            this.monitor.añadir("MEB", getBattery());
            this.monitor.añadir("G2", getTopActivtyFromLolipopOnwards(segundos));
            openNavigator();

        }
        @Override
        protected String doInBackground(String... strings) {
           try {
                Thread.sleep(segundos*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "strings[0]";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            this.monitor.añadir("G3", getTopActivtyFromLolipopOnwards(segundos));
            this.monitor.añadir("MEApp", getBattery());
            this.monitor.añadir("G4", getTopActivtyFromLolipopOnwards(segundos));
            this.monitor.printear();
            Toast.makeText(getBaseContext(), "Monitoreo finalizado", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(), "Deteccion de datos cancelado", Toast.LENGTH_SHORT).show();
        }
    }





    private class MiAsyncTask extends AsyncTask<Void,Integer,Boolean>{
        Monitor monitor;
        int segundos= Integer.parseInt(segundosIngresados.getText().toString().trim());
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Toast.makeText(getBaseContext(), "Comienzo del monitoreo", Toast.LENGTH_SHORT).show();
            this.monitor=  new Monitor(); //G1, MEB
            //this.monitor.añadir("G1", generateApps());
            this.monitor.añadir("G1", getTopActivtyFromLolipopOnwards(segundos));
            this.monitor.añadir("MEB", getBattery());
        }



        @Override
        protected Boolean doInBackground(Void... params) {

            this.monitor.añadir("G2", getTopActivtyFromLolipopOnwards(segundos));
            openNavigator();
           publishProgress(); //llama a onprogressUpdate
           esperarSegundos(segundos);

            // SystemClock.sleep(40000);
            this.monitor.añadir("G3", getTopActivtyFromLolipopOnwards(segundos));
            this.monitor.añadir("MEApp", getBattery());
            //UnSegundo(segundosIngresados); //con esto se setea un tiempo antes de que se termine el onprogress thread

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            //this.monitor.añadir("G2", generateApps());
            //this.monitor.añadir("G2", getTopActivtyFromLolipopOnwards());
            //openNavigator();
           // generateApps();//G2
           // supportFinishAfterTransition();

        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            //super.onPostExecute(aVoid);
            if(resultado){
               // this.monitor.añadir("G3", generateApps());
                //this.monitor.añadir("G3", getTopActivtyFromLolipopOnwards());
                //this.monitor.añadir("MEApp", getBattery());
                Toast.makeText(getBaseContext(), "Datos energeticos monitoreados", Toast.LENGTH_SHORT).show();
                //this.monitor.añadir("G4", generateApps());
                this.monitor.añadir("G4", getTopActivtyFromLolipopOnwards(segundos));
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