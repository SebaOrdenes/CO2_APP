package com.tutsplus.code.android.asynctask;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaAplicaciones extends AppCompatActivity {

    ListView listView;
    TextView text;
   ArrayList listaAux;
    ArrayList listAuxVersion;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_aplicaciones);

        // initialise layout
        listView = findViewById(R.id.listview);
        text = findViewById(R.id.totalapp);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                // Realiza lo que deseas, al recibir clic en el elemento de tu listView determinado por su posicion.
                Log.i("Click", "click en el elemento " + position + " de mi ListView correspondiente a "+ listaAux.get(position) );
                next(o.toString(), (String) listaAux.get(position), (String) listAuxVersion.get(position));


                //Intent launchIntent = getPackageManager().getLaunchIntentForPackage(listaAux[position]);
                //startActivity( launchIntent );

            }
        });


    }
    public void next(String x, String y, String z){
        Intent next = new Intent(this, MainMonitorear.class);
        next.putExtra("id", x);
        next.putExtra("packageName",y);
        next.putExtra("version",z);

        startActivity(next);


    }

void agregarElementos(String item){
    List<String> Players_list = new ArrayList<String>(Arrays.asList(item));
    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Players_list);
    listView.setAdapter(arrayAdapter);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = (String) parent.getItemAtPosition(position);
            Log.d(TAG, "onItemClick: item presionado "+ selectedItem);

        }
    });


}



    public void getallapps(View view) throws PackageManager.NameNotFoundException {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // get list of all the apps installed
        List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
        List<String> componentList = new ArrayList<String>();
        String name = null;
        int i = 0;
        // get size of ril and create a list
        String[] apps = new String[ril.size()];
        String[] appsWithPackageName = new String[ril.size()];
        String[] appsVers = new String[ril.size()];
        Map<String, Object> data = new HashMap<>();

        //List<ApplicationInfo> appsAux = getPackageManager().getInstalledApplications(0);
        Log.e(TAG, "RIL SIZE: "+ ril.toString() );
        PackageInformation androidPackagesInfo = new PackageInformation(this);
        ArrayList<PackageInformation.InfoObject> appsData = androidPackagesInfo.getInstalledApps(true);
        for (PackageInformation.InfoObject info: appsData) {
           // System.out.println(info.pname);

            for (ResolveInfo ri: ril){

                if (ri.activityInfo.applicationInfo.packageName.equals(info.pname)){
                    Log.e(TAG, "HOLA HAY UNA APP IGUAL ");

                }
            }
            i++;
        }
        for (ResolveInfo ri : ril) {
            Log.e(TAG, "getallapps: "+ ri.activityInfo.applicationInfo );
            if (ri.activityInfo != null) {
                // get package
                Resources res = getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);
                // Log.e(TAG, "getallapps: "+ri.activityInfo.icon );

                // if activity label res is found
                if (ri.activityInfo.labelRes != 0) {
                    name = res.getString(ri.activityInfo.labelRes);

                } else {
                    name = ri.activityInfo.applicationInfo.loadLabel(
                            getPackageManager()).toString();
                }
                // Log.e(TAG, "getallapps: "+ ri.activityInfo.applicationInfo.);
                //   <-- apps[i] = name;
                //<--   appsWithPackageName[i]=ri.activityInfo.applicationInfo.packageName;
                // appsWithPackageName[i]= ri.activityInfo.applicationInfo.packageName;
                //Log.e(TAG, "getallapps: "+pInfo.versionName );
                // Log.d(TAG, ""+ ri.activityInfo.applicationInfo.packageName);
                //agregarElementos("");
                i++;
            }
        }


        Log.d(TAG, "getallapps: "+ appsWithPackageName[0]);
        //this.listaAux=appsWithPackageName;
        // set all the apps name in list view
        try {
            listView.setAdapter(new ArrayAdapter<String>(ListaAplicaciones.this, android.R.layout.simple_list_item_1, apps));
        } catch (Exception e) {
            e.printStackTrace();
        }
       // listView.setAdapter(new ArrayAdapter<String>(ListaAplicaciones.this, android.R.layout.simple_list_item_1, apps));
         // write total count of apps available.
        text.setText(ril.size() + " Apps are installed");

    }





    public void getallapps2(View view) {
        // get list of all the apps installed
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        List<ApplicationInfo> appsAux = getPackageManager().getInstalledApplications(0);
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null); //new
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);//new
        List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0); //new
        final PackageManager pm = getPackageManager();
        String[] apps = new String[ril.size()];
        String[] appsPkg = new String[packList.size()];
        String[] appsVers = new String[packList.size()];
        ArrayList appsAux2= new ArrayList();
        ArrayList appsPkgAux= new ArrayList();
        ArrayList appsVersAux= new ArrayList();


        for (ResolveInfo resolveInfo: ril){
           // Log.e(TAG, "RESOLVEINFO->: "+ resolveInfo.activityInfo.packageName);

            for (int i = 0; i < packList.size(); i++) {

                PackageInfo packInfo = packList.get(i);
                if (resolveInfo.activityInfo.packageName.equals(packList.get(i).packageName)){
                  //  Log.e(TAG, "HOLA ENCONTRE UN PAQUETE IGUAL Y SOY  "+ resolveInfo.activityInfo.packageName );
                    appsPkg[i]=packInfo.packageName;
                    appsVers[i]= packInfo.versionName;
                    appsAux2.add(packInfo.applicationInfo.loadLabel(this.getPackageManager()).toString());
                    appsPkgAux.add(packInfo.packageName);
                    appsVersAux.add(packInfo.versionName);

                }


            }


        }
        for (int i = 0; i < appsAux2.size(); i++) {

            apps[i] = appsAux2.get(i).toString();
        }

        this.listaAux=appsPkgAux;
        this.listAuxVersion=appsVersAux;

        // set all the apps name in list view
        listView.setAdapter(new ArrayAdapter<String>(ListaAplicaciones.this, android.R.layout.simple_list_item_1, apps));
        // write total count of apps available.
        text.setText(ril.size() + " Apps are installed");

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    public void getallapps3(View view) {
        // get list of all the apps installed
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        String[] apps = new String[packList.size()];
        String[] appsPackageName = new String[packList.size()];
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            apps[i] = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();


        }
        // set all the apps name in list view
        listView.setAdapter(new ArrayAdapter<String>(ListaAplicaciones.this, android.R.layout.simple_list_item_1, apps));
        // write total count of apps available.
        text.setText(packList.size() + " Apps are installed");
    }





}