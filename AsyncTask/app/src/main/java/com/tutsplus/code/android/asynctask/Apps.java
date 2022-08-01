package com.tutsplus.code.android.asynctask;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Apps {

    ArrayList<String> listaApps = new ArrayList<>();
    HashSet<String> listaApps2;


    void obtenerAppsEnUso(Context context) {
        final PackageManager pm = context.getPackageManager();



        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            //Log.d("A", "Installed package :" + packageInfo.packageName);
            //Log.d("B", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName)); //guardar para valdiacion
            // Log.d("TAGB", "Source dir : " + packageInfo.sourceDir);
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                listaApps.add(packageInfo.packageName);
            }


        }



    }



}
