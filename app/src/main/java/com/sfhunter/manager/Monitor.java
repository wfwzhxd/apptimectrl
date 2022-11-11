package com.sfhunter.manager;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.util.Collection;
import java.util.Map;

public class Monitor {

    public static final String TAG = Monitor.class.getName();

    UsageStatsManager umgr;
    Storage storage;

    public Monitor(Context context) {
        umgr = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        storage = Storage.getInstance(context);
    }

    public boolean isAppGotLimit(String pkgname) {
        if (storage.app_limit.getOrDefault(pkgname, Integer.MAX_VALUE) < 1) {
            return true;
        }
        long endT = System.currentTimeMillis();
        Map<String, UsageStats> stats = umgr.queryAndAggregateUsageStats(endT - 1000 - storage.minutes_of_period * 60 * 1000, endT);
        Log.d(TAG, String.valueOf(stats));
        if (stats.isEmpty()) {
            // access usage permission is deny, disable all app
            return true;
        }
        if (stats.containsKey(pkgname)) {
            UsageStats ustat = stats.get(pkgname);
            long mins = ustat.getTotalTimeVisible() / 1000 / 60;
            return mins >= storage.app_limit.getOrDefault(pkgname, Integer.MAX_VALUE);
        }
        return false;
    }
}
