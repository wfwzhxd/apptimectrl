package com.sfhunter.manager;

import android.app.admin.DeviceAdminService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class DpmService extends DeviceAdminService {

    @Override
    public void onCreate() {
        super.onCreate();
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(DpmService.class.getName(), "onReceive" + intent);
                Storage storage = Storage.getInstance(context);
                Monitor monitor = new Monitor(context);
                AppCtl appCtl = new AppCtl(context);
                for (String pkgname: storage.app_limit.keySet()) {
                    if (monitor.isAppGotLimit(pkgname)) {
                        appCtl.disableApp(pkgname);
                    }
                }
            }
        };
        registerReceiver(br, new IntentFilter(Intent.ACTION_TIME_TICK));
    }
}