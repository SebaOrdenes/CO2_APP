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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaAplicaciones extends AppCompatActivity {

    ListView listView;
    TextView text;
    String[] listaAux;
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
                Log.i("Click", "click en el elemento " + position + " de mi ListView correspondiente a "+ listaAux[position] );
                next(o.toString(), listaAux[position]);


                //Intent launchIntent = getPackageManager().getLaunchIntentForPackage(listaAux[position]);
                //startActivity( launchIntent );

            }
        });


    }
    public void next(String x, String y){
        Intent next = new Intent(this, MainMonitorear.class);
        next.putExtra("id", x);
        next.putExtra("packageName",y);

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
        for (ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                // get package
                Resources res = getPackageManager().getResourcesForApplication(ri.activityInfo.applicationInfo);

                // if activity label res is found
                if (ri.activityInfo.labelRes != 0) {
                    name = res.getString(ri.activityInfo.labelRes);
                } else {
                    name = ri.activityInfo.applicationInfo.loadLabel(
                            getPackageManager()).toString();
                }
                apps[i] = name;
                appsWithPackageName[i]=ri.activityInfo.applicationInfo.packageName;
               // appsWithPackageName[i]= ri.activityInfo.applicationInfo.packageName;
                Log.d(TAG, ""+ ri.activityInfo.applicationInfo.packageName);
                //agregarElementos("");
                i++;
            }
        }

        Log.d(TAG, "getallapps: "+ appsWithPackageName[0]);
        this.listaAux=appsWithPackageName;
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

    public void getallapps3(View view) {
        // get list of all the apps installed
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        String[] apps = new String[packList.size()];
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            apps[i] = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
        }
        // set all the apps name in list view
        listView.setAdapter(new ArrayAdapter<String>(ListaAplicaciones.this, android.R.layout.simple_list_item_1, apps));
        // write total count of apps available.
        text.setText(packList.size() + " Apps are installed");
    }






    public void getallapps2(View view) {
        // get list of all the apps installed
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        final PackageManager pm = getPackageManager();
        String[] apps = new String[packList.size()];
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            apps[i] = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
           // Log.d("TAG", "getallapps2: "+ packInfo.packageName);
            Log.d("TAG", "---------->: "+ packInfo.packageName);

        }
        // set all the apps name in list view
        listView.setAdapter(new ArrayAdapter<String>(ListaAplicaciones.this, android.R.layout.simple_list_item_1, apps));
        // write total count of apps available.
        text.setText(packList.size() + " Apps are installed");

        //startActivity(pm.getLaunchIntentForPackage("com.android.providers.calendar"));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }





}