package com.supinfo.supsms.util;

import com.supinfo.supsms.application.SupApplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

public class Util {

	public static Context getAppContext(){
		return SupApplication.getInstance().getApplicationContext();
	}
	
	public static boolean isOnline() {
		final ConnectivityManager cm = (ConnectivityManager) Util.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null) {
			return cm.getActiveNetworkInfo().isConnectedOrConnecting();
		} else {
			return false;
		}
	}

    public static String getAppVersionName(){
        try {
            return getAppContext().getPackageManager().getPackageInfo(getAppContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getAppVersionCode(){
        try {
            return getAppContext().getPackageManager().getPackageInfo(getAppContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
