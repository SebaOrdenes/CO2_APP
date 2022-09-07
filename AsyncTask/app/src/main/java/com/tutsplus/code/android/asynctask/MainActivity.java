package com.tutsplus.code.android.asynctask;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    int REQUEST_CODE=200;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verificarpermisos();

        /*PackageInformation androidPackagesInfo = new PackageInformation(this);
        ArrayList<PackageInformation.InfoObject> appsData = androidPackagesInfo.getInstalledApps(true);

        for (PackageInformation.InfoObject info: appsData) {
            System.out.println(info.appname);
            System.out.println(info.versionName);

            //Toast.makeText(MainActivity.this, info.appname, Toast.LENGTH_SHORT).show();
            //Drawable somedrawable = info.icon;

        }*/


    }


    public void next(View view){
        Intent next = new Intent(this, ListaAplicaciones.class);
        startActivity(next);

}


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarpermisos() {
        int PermisosAlmacenamiento= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        //startActivity(permissionIntent);

        if (PermisosAlmacenamiento== PackageManager.PERMISSION_GRANTED && verifyAccessEnables(this)){
            Toast.makeText(this,"Permisos otorgados", Toast.LENGTH_SHORT).show();

        }
        else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            startActivity(permissionIntent);


        }

    }

    private Boolean verifyAccessEnables(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}