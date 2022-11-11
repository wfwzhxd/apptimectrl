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
        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(new ScreenStatReceiver(), screenFilter);
    }
}

class TickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TickReceiver.class.getName(), "onReceive" + intent);
        Storage storage = Storage.getInstance(context);
        Monitor monitor = new Monitor(context);
        AppCtl appCtl = new AppCtl(context);
        for (String pkgname: storage.app_limit.keySet()) {
            if (monitor.isAppGotLimit(pkgname)) {
                appCtl.disableApp(pkgname);
            }
        }
    }
}

class ScreenStatReceiver extends BroadcastReceiver {

    TickReceiver tr = new TickReceiver();
    boolean trregisted = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(ScreenStatReceiver.class.getName(), "onReceive" + intent);
        final String action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action) && !trregisted) {
            trregisted = true;
            context.registerReceiver(tr, new IntentFilter(Intent.ACTION_TIME_TICK));
        }
        if (Intent.ACTION_SCREEN_OFF.equals(action) && trregisted) {
            trregisted = false;
            context.unregisterReceiver(tr);
        }
    }
}