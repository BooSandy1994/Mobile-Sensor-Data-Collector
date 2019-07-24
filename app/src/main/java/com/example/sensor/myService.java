package com.example.sensor;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import static com.example.sensor.MainActivity.upload;
import static com.example.sensor.foreNotification.CHANNEL_ID;

public class myService extends Service {

    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(this,uiService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sensor Monitor Service")
                .setContentText("Sensors are being monitored")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

        mToastRunnable.run();

        return START_STICKY;
    }

    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this,60000);
            MainActivity.upload(myService.this);
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mToastRunnable);
    }


}
