package com.sfhunter.manager;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

public class AppCtl {

    private static final String TAG = AppCtl.class.getName();

    private DevicePolicyManager dpm;
    private ComponentName admin;

    public AppCtl(Context context) {
        dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        this.admin = new ComponentName(context.getApplicationContext(), DpmReceiver.class);;
    }

    public void enableApp(@NonNull String pkgname) {
        dpm.setPackagesSuspended(admin, new String[]{pkgname}, false);
        Log.d(TAG, "enableApp: " + pkgname);
    }

    public void disableApp(@NonNull String pkgname) {
        dpm.setPackagesSuspended(admin, new String[]{pkgname}, true);
        Log.d(TAG, "disableApp: " + pkgname);
    }
}
