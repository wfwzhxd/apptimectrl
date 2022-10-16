package com.sfhunter.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.UserManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String PKG_NAME = "com.miHoYo.Yuanshen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Storage storage = Storage.getInstance(MainActivity.this);
        AppCtl appCtl = new AppCtl(MainActivity.this);
        EditText vpkgName = findViewById(R.id.packageName);
        EditText vMinutes = findViewById(R.id.allowMinutes);
        Button btn_add = findViewById(R.id.btn_add);
        Button btn_enable = findViewById(R.id.btn_enable);
        TextView tv = findViewById(R.id.textView);
        tv.setText(storage.toString());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkgname = String.valueOf(vpkgName.getText());
                if (String.valueOf(vMinutes.getText()).length() < 3) {
                    return;
                }
                int minutes = Integer.valueOf(String.valueOf(vMinutes.getText()));
                Toast.makeText(MainActivity.this, pkgname + String.valueOf(minutes), Toast.LENGTH_SHORT).show();
                try {
                    getPackageManager().getPackageInfo(pkgname, 0);
                    // installed
                    if (storage.app_limit.keySet().contains(pkgname) && minutes>=storage.app_limit.get(pkgname)){
                        Toast.makeText(MainActivity.this, "Minutes too Long", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    storage.app_limit.put(pkgname, minutes);
                    storage.save();

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Not Install", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        btn_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long act_mins = (System.currentTimeMillis()-storage.last_enable_ts)/1000/60;
                if (act_mins > storage.minutes_of_period) {
                    for (String pkgname:storage.app_limit.keySet()){
                        appCtl.enableApp(pkgname);
                    }
                    storage.last_enable_ts = System.currentTimeMillis();
                    storage.save();
                } else {
                    Toast.makeText(MainActivity.this, "Sleep Time not enough", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_enable.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int minutes = Integer.valueOf("0" + String.valueOf(vMinutes.getText()));
                if (minutes < 60 || minutes > 6*60) {
                    return true;
                }
                storage.minutes_of_period = minutes;
                storage.save();
                return true;
            }
        });

    }
}