package com.example.sensor;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import static android.content.ContentValues.TAG;
import static com.example.sensor.foreNotification.CHANNEL_ID;
import static com.example.sensor.welcome_activity.ASK_MULTIPLE_PERMISSION_CODE;

public class myReceiver extends BroadcastReceiver {

    private Context context;
    private double longitude, latitude, altitude, speed;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;



        if(context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&&
                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){



        }


        requestLocationUpdates();



        Log.d(TAG, "location: " + longitude + "  " +  latitude +  "  " + altitude + "  " + speed);
        // MainActivity.upload(longitude,latitude,altitude,speed);
    }

    public void requestLocationUpdates() {

        fusedLocationProviderClient = new FusedLocationProviderClient(context);
        locationRequest = new LocationRequest();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(2000);
        locationRequest.setInterval(4000);

//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//                latitude = locationResult.getLastLocation().getLatitude();
//                longitude = locationResult.getLastLocation().getLongitude();
//                altitude = locationResult.getLastLocation().getAltitude();
//                speed = locationResult.getLastLocation().getSpeed();
//
//            }
//        }, Looper.getMainLooper());

    }





}
