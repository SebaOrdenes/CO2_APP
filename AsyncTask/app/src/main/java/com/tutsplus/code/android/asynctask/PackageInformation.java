package com.tutsplus.code.android.asynctask;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PackageInformation {
    private Context mContext;

    public PackageInformation(Context context) {
        mContext = context;
    }

    class InfoObject {
        public String appname = "";
        public String pname = "";
        public String versionName = "";
        public int versionCode = 0;
        public Drawable icon;


        public void InfoObjectAggregatePrint() { //not used yet
            Log.v(appname, appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
        }

    }

    private ArrayList< InfoObject > getPackages() {
        ArrayList < InfoObject > apps = getInstalledApps(false);
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
           // Log.e(TAG, "getPackages: "+apps.get(i) );
            apps.get(i);
        }
        return apps;
    }




    public ArrayList < InfoObject > getInstalledApps(boolean getSysPackages) {

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);



        ArrayList < InfoObject > res = new ArrayList < InfoObject > ();
        List<PackageInfo> packs = mContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            InfoObject newInfo = new InfoObject();
            newInfo.appname = p.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(mContext.getPackageManager());
            res.add(newInfo);



        }
        return res;
    }
}
