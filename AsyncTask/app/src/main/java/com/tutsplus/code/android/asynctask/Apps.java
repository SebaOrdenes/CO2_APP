package com.tutsplus.code.android.asynctask;

import static android.content.Context.ACTIVITY_SERVICE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Apps {

    ArrayList listaApps = new ArrayList<>(); //private




    void obtenerAppsEnUso(Context context) {
      ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            listaApps.add(taskInfo.toString());
            //System.out.println(taskInfo.toString());
        }





      /* ActivityManager am2 = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        String packageName = am2.getRunningTasks(1).get(0).topActivity.getPackageName();
        System.out.println(packageName);*/



      /*  final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

      for (ApplicationInfo packageInfo : packages) {
            //Log.d("A", "Installed package :" + packageInfo.packageName);
            //Log.d("B", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName)); //guardar para valdiacion
            // Log.d("TAGB", "Source dir : " + packageInfo.sourceDir);
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                listaApps.add(packageInfo.packageName);
            }


        }*/



    }

    public ArrayList<String> getListaApps() {
        return listaApps;
    }

    public void setListaApps(ArrayList<String> listaApps) {
        this.listaApps = listaApps;
    }





}
