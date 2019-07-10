package com.example.sensor;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import static com.example.sensor.foreNotification.CHANNEL_ID;

public class myAlarmService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

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

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Intent notificationIntent = new Intent(this,uiService.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
//                notificationIntent,0);
//
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Sensor Monitor Service")
//                .setContentText("Sensors are being monitored")
//                .setSmallIcon(R.drawable.ic_android)
//                .setContentIntent(pendingIntent)
//                .build();
//        startForeground(1,notification);

        Intent intent1 = new Intent(this,myReceiver.class);
        PendingIntent pendingIntent1 = (PendingIntent) PendingIntent.getBroadcast(getApplicationContext(),1, intent1,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
                                      + 1000, 150000, pendingIntent1);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
