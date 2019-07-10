package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;

import static com.example.sensor.MainActivity.upload;

public class uiService extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_service);
    }

    public void startService(View v){


        Intent serviceIntent = new Intent(this, myAlarmService.class);

        startService(serviceIntent);

    }


    public void stopService(View v) {

        Intent serviceIntent = new Intent(this, myAlarmService.class);

        stopService(serviceIntent);


    }





}

