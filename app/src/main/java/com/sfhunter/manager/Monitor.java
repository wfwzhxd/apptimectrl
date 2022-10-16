package com.sfhunter.manager;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.Map;

public class Monitor {

    UsageStatsManager umgr;
    Storage storage;

    public Monitor(Context context){
        umgr = (UsageStatsManager) context.getSystemService(context.USAGE_STATS_SERVICE);
        storage = Storage.getInstance(context);
    }

    public boolean isAppGotLimit(String pkgname){
        if (storage.app_limit.getOrDefault(pkgname, Integer.MAX_VALUE) < 1) {
            return true;
        }
        long endT = System.currentTimeMillis();
        Map<String, UsageStats> stats = umgr.queryAndAggregateUsageStats(endT-storage.minutes_of_period*60*1000, endT);
        if (stats != null && stats.containsKey(pkgname)) {
            UsageStats ustat = stats.get(pkgname);
            long mins = ustat.getTotalTimeVisible()/1000/60;
            return mins >= storage.app_limit.getOrDefault(pkgname, Integer.MAX_VALUE);
        }
        return false;
    }
}
