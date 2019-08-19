package com.example.sensor;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class AppLocationManager implements LocationListener {

    private LocationManager locationManager;
    private String longitude;
    private String latitude;
    private Criteria criteria;
    private String provider;
    private double lati, longi;

    public AppLocationManager(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria,true);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,0,this);
        setMostRecentLocation(locationManager.getLastKnownLocation(provider));

    }

    private void setMostRecentLocation(Location lastKnownLocation) {

    }

    public double getLatdouble() {
        return  lati;
    }

    public double getLongidouble() {
        return longi;
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) location.getLatitude();
        double lon = (double) location.getLongitude();

        lati = lat;
        longi = lon;

        latitude = String.valueOf(lat);
        longitude = String.valueOf(lon);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
