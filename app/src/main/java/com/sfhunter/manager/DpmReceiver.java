package com.sfhunter.manager;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.UserManager;

import androidx.annotation.NonNull;

public class DpmReceiver extends DeviceAdminReceiver {

    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        super.onEnabled(context, intent);
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName admin = new ComponentName(context.getApplicationContext(), DpmReceiver.class);
        dpm.setUninstallBlocked(admin, context.getPackageName(), true);
        dpm.addUserRestriction(admin, UserManager.DISALLOW_ADD_USER);
        dpm.addUserRestriction(admin, UserManager.DISALLOW_SAFE_BOOT);
        dpm.addUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET);
//        dpm.addUserRestriction(admin, UserManager.DISALLOW_DEBUGGING_FEATURES);
    }
}