package com.sfhunter.manager;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Storage {
    private static Storage mInstance;

    public long last_enable_ts;
    public int minutes_of_period;
    public Map<String, Integer> app_limit = new LinkedHashMap<>();


    private final static String KEY_LAST_ENABLE_TS = "last_enable_ts";
    private final static String KEY_MINUTES_OF_PERIOD = "minutes_of_period";
    private final static String KEY_APP_LIMIT = "app_limit";

    private Context context;

    private Storage(Context context){
        this.context = context.getApplicationContext();
        SharedPreferences sp = this.context.getSharedPreferences(Storage.class.getName(), Context.MODE_PRIVATE);
        last_enable_ts = sp.getLong(KEY_LAST_ENABLE_TS, 0);
        minutes_of_period = sp.getInt(KEY_MINUTES_OF_PERIOD, 24*60);
        String app_limit_str = sp.getString(KEY_APP_LIMIT, "");
        try {
            JSONObject jsobj = new JSONObject(app_limit_str);
            Iterator<String> keys = jsobj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                app_limit.put(key, jsobj.getInt(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Storage getInstance(Context context){
        if (Storage.mInstance == null){
            Storage.mInstance = new Storage(context);
        }
        return Storage.mInstance;
    }

    public synchronized void save() {
        SharedPreferences sp = this.context.getSharedPreferences(Storage.class.getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(KEY_LAST_ENABLE_TS, last_enable_ts);
        editor.putInt(KEY_MINUTES_OF_PERIOD, minutes_of_period);
        String app_limit_str = new JSONObject(app_limit).toString();
        editor.putString(KEY_APP_LIMIT, app_limit_str);
        editor.commit();
    }

    @Override
    public String toString() {
        return "Storage{" +
                "last_enable_ts=" + last_enable_ts +
                ", minutes_of_period=" + minutes_of_period +
                ", app_limit=" + app_limit +
                '}';
    }
}
