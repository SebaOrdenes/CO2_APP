package com.tutsplus.code.android.asynctask;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.tutsplus.code.android.asynctask.Backend.Database;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

public class MainMonitorear extends AppCompatActivity  implements  View.OnClickListener{

    Button button3;
    EditText segundosIngresados;
    String nombreApp;
    String versionApp;
    Database db= new Database();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_monitorear);
        String valor = getIntent().getStringExtra("id"); // nombre de la app que se abrirá
        nombreApp= getIntent().getStringExtra("packageName");// nombre del package para abrir la app
        versionApp= getIntent().getStringExtra("version"); //version de la app que se abrirá
        button3 = (Button)findViewById(R.id.button3);
        segundosIngresados= findViewById(R.id.NumeroIngresado);
        button3.setOnClickListener(this);
        TextView textoTitulo= (TextView) findViewById(R.id.titulo);
        textoTitulo.setText("La aplicación a abrir es: "+ valor+ " en su versión "+ versionApp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openNavigator(String string){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(string);
        startActivity( launchIntent );

    }


    public String getTopActivtyFromLolipopOnwards(int num) throws PackageManager.NameNotFoundException {
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


    private Monitor getBatteryStats(){
        BatteryManager batteryManager = (BatteryManager) this.getSystemService(BATTERY_SERVICE);

        long cu =  batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
        long q = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        int level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        long energy = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER);
        long ca= batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);


        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int porcBateria = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        String technology = batteryStatus.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
        int volt = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        if (Build.VERSION.SDK_INT >= 28) {
            long time = batteryManager.computeChargeTimeRemaining();//Devuelve -1 si no se puede calcular el tiempo:
            // no hay suficientes datos actuales para tomar una decisión o la batería se está descargando actualmente.

          }

        Monitor monitoAux= new Monitor();

        monitoAux.añadir("BATTERY_PROPERTY_CURRENT_NOW",cu);
        monitoAux.añadir("BATTERY_PROPERTY_CHARGE_COUNTER",q);
        monitoAux.añadir("BATTERY_PROPERTY_CAPACITY",level);
        monitoAux.añadir("BATTERY_PROPERTY_ENERGY_COUNTER",energy);
        monitoAux.añadir("BATTERY_PROPERTY_CURRENT_AVERAGE", ca);
        monitoAux.añadir("EXTRA_LEVEL",porcBateria);
        monitoAux.añadir("EXTRA_TEMPERATURE",temp);
        monitoAux.añadir("EXTRA_TECHNOLOGY",technology);
        monitoAux.añadir("EXTRA_VOLTAGE",volt);


        return monitoAux;



    }



    void validarFases(String faseA, String faseB,String faseC, String faseD, String nombreApp,Monitor monitor) throws JSONException {

    try {
        if (monitor.getFases().get(faseA).equals(monitor.getFases().get(faseB))) {
            monitor.añadir("Val1", true);
        } else {
            monitor.añadir("Val1", false);
        }
        if (monitor.getFases().get(faseA) != monitor.getFases().get(faseC)) {
            monitor.añadir("Val2", true);
        } else {
            monitor.añadir("Val2", false);
        }
        if (monitor.getFases().get(faseC).equals(monitor.getFases().get(faseD)) && monitor.getFases().get(faseD).equals(nombreApp)) {
            monitor.añadir("Val3", true);
        } else {
            monitor.añadir("Val3", false);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private BroadcastReceiver infoBateria = new BroadcastReceiver(){

        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            try {
                String str = getTopActivtyFromLolipopOnwards(60); //cambiar ese 60 por "numero"
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //Log.d("PORCENTAJE BATERIA ", ""+ level);
            //Log.d("PORCENTAJE BATERIA ", ""+ str);

        }

    };

    public static String getCurrentTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Calendar
                .getInstance().getTime());
    }





    @SuppressLint("LongLogTag")
    private Map<String, Object> getDeviceSuperInfo() {

        Map<String, Object> data = new HashMap<>();
       String str= System.getProperty("os.version")+"(" + android.os.Build.VERSION.INCREMENTAL + ")";
       String str2= android.os.Build.MODEL            + " ("+ android.os.Build.PRODUCT + ")";

        try {
           // s += "\n OS Version: "      + System.getProperty("os.version")      + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
            data.put("OS version:",str);
           // s += "\n OS API Level: "    + android.os.Build.VERSION.SDK_INT;
            data.put("OS API Level", android.os.Build.VERSION.SDK_INT);
           // s += "\n Device: "          + android.os.Build.DEVICE;
            data.put("Device",  android.os.Build.DEVICE);
           // s += "\n Model (and Product): " + android.os.Build.MODEL            + " ("+ android.os.Build.PRODUCT + ")";
            data.put("Model (and Product)", str2);
           // s += "\n RELEASE: "         + android.os.Build.VERSION.RELEASE;
            data.put("RELEASE", android.os.Build.VERSION.RELEASE);
           // s += "\n BRAND: "           + android.os.Build.BRAND;
            data.put("BRAND", android.os.Build.BRAND);
           // s += "\n DISPLAY: "         + android.os.Build.DISPLAY;
            data.put("DISPLAY",android.os.Build.DISPLAY);
           // s += "\n CPU_ABI: "         + android.os.Build.CPU_ABI;
            data.put("CPU_ABI",android.os.Build.CPU_ABI);
           // s += "\n CPU_ABI2: "        + android.os.Build.CPU_ABI2;
            data.put("CPU_ABI2",android.os.Build.CPU_ABI2);
           // s += "\n UNKNOWN: "         + android.os.Build.UNKNOWN;
            data.put("UNKNOWN", android.os.Build.UNKNOWN);
           // s += "\n HARDWARE: "        + android.os.Build.HARDWARE;
            data.put("HARDWARE",android.os.Build.HARDWARE);
           // s += "\n Build ID: "        + android.os.Build.HARDWARE;
            data.put("Build ID",android.os.Build.HARDWARE );
           //s += "\n MANUFACTURER: "    + android.os.Build.MANUFACTURER;
            data.put("MANUFACTURER",android.os.Build.MANUFACTURER);
            //s += "\n SERIAL: "          + android.os.Build.SERIAL;
            data.put("SERIAL",android.os.Build.SERIAL);
            //s += "\n USER: "            + android.os.Build.USER;
            data.put("USER",android.os.Build.USER );
            //s += "\n HOST: "            + android.os.Build.HOST;
            data.put("HOST",android.os.Build.HOST);

        } catch (Exception e) {
            data.put("ERROR",true);
            Log.e(TAG, "Error getting Device INFO");
        }
        return data;

    }//end getDeviceSuperInfo





    private BroadcastReceiver monitorearApp(Monitor monitor){ //FUNCION QUE MONITOREA SI SE ABRE
                                                // OTRA APP MIENTRAS DURA EL MONITOREO xd y se descargue o cargue la bateria
        ArrayList listAux= new ArrayList();       //SE EXCLUYE LA APP PRINCIPAL ASYNCTASK
        Boolean bool= new Boolean(true);
         BroadcastReceiver infoBateria = new BroadcastReceiver(){

            @Override
            public void onReceive(Context ctxt, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                String str = null; //cambiar ese 60 por "numero"
                try {
                    str = getTopActivtyFromLolipopOnwards(60);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d("PORCENTAJE BATERIA ", ""+ level);
                Log.d("PORCENTAJE BATERIA ", ""+ str);
                listAux.add(str);
                if (!str.equals(nombreApp) && !str.equals("com.tutsplus.code.android.asynctask")){
                   // Log.e(TAG, "onReceive: HOLA ABRI OTRA APP QUE NO ME CORRESPONDE" );
                    monitor.añadir("OpenAnotherApp",true);
                }

                Log.d("PORCENTAJE BATERIA ", ""+ listAux.toString());
                //monitorAux.añadir("BAT",level);
            }

        };

         IntentFilter filter = new IntentFilter();
         filter.addAction(Intent.ACTION_BATTERY_CHANGED);
         filter.addAction(Intent.ACTION_VIEW);
         filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        //registerReceiver(infoBateria, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return infoBateria;
        //unregisterReceiver(infoBateria);

    }

    class Task2 extends AsyncTask<String, Void, String>{
        Monitor monitor;

      //  FirebaseFirestore db = FirebaseFirestore.getInstance();
        int segundos= Integer.parseInt(segundosIngresados.getText().toString().trim());
        @Override
        protected void onPreExecute() {
            //onBackPressed();
            Toast.makeText(getBaseContext(), "Comienzo del monitoreo", Toast.LENGTH_SHORT).show();
            this.monitor=  new Monitor(); //G1, MEB
            try {
                this.monitor.añadir("G1", getTopActivtyFromLolipopOnwards(segundos));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
                   this.monitor.añadir("MEB", getBatteryStats().getFases());
            try {
                this.monitor.añadir("G2", getTopActivtyFromLolipopOnwards(segundos));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            openNavigator(nombreApp);
            registerReceiver(monitorearApp(monitor),new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
          getDeviceSuperInfo();



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
            try {
                this.monitor.añadir("G3", getTopActivtyFromLolipopOnwards(segundos));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            // this.monitor.añadir("MEApp", getBattery());
            this.monitor.añadir("MEApp", getBatteryStats().getFases());
            try {
                this.monitor.añadir("G4", getTopActivtyFromLolipopOnwards(segundos));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            try {
                validarFases("G1","G2","G3","G4", nombreApp, monitor);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (monitor.getFases().get("OpenAnotherApp").equals(true)) {

                    Log.e(TAG, "onPostExecute: HOLA ESTE TIPO ABRIO OTRA APP MIENTRAS SE MONITOREABA" );
                }
            } catch (JSONException e) {
                this.monitor.añadir("OpenAnotherApp",false);
            }
            Map<String, Object> jsonMap = new Gson().fromJson(monitor.getFases().toString(), new TypeToken<HashMap<String, Object>>() {}.getType());
            Map<String, Object> data = new HashMap<>();
            data.put("App_Monitoreada", nombreApp);
            data.put("Segundos_Monitoreados", segundos);
            data.put("Timestamp", getCurrentTimestamp());
            data.put("Device_Info", getDeviceSuperInfo());
            data.put("Version_App", versionApp);
            data.put("Data", jsonMap);
           // db.insertInCollection(db.generateInstance(),"dataUsers",data);
            db.createUser(db.generateInstance(),"emailfalso123@gmail.com");

            //db.getUsuarioPorId(db.generateInstance(),"eoPdPAzGxu4MWwIH0UnA");
            Toast.makeText(getBaseContext(), "Monitoreo finalizado", Toast.LENGTH_SHORT).show();
           // unregisterReceiver(monitorearApp(monitor));

        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(), "Deteccion de datos cancelado", Toast.LENGTH_SHORT).show();
        }
    }


}