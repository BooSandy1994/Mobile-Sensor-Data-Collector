package com.example.sensor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings.Secure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opencsv.CSVWriter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;

import static android.app.PendingIntent.getActivity;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.os.Build.VERSION_CODES.M;


public class MainActivity<data> extends AppCompatActivity implements SensorEventListener, ValueEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor accelerometer, mGyro, mMagno, mLight, mPressure, mTemp, mHumi, mProxi, mGrav, mPedo, mheart;

    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference mRootref = firebaseDatabase.getReference();

    private FirebaseAuth mAuth;

    public static DatabaseReference mUser;
    public static DatabaseReference mSub;
    public static DatabaseReference mTime;
    public static DatabaseReference mDate;

    public static DatabaseReference mXa;
    public static DatabaseReference mYa;
    public static DatabaseReference mZa;

    public static DatabaseReference mXg;
    public static DatabaseReference mYg;
    public static DatabaseReference mZg;

    public static DatabaseReference mXm;
    public static DatabaseReference mYm;
    public static DatabaseReference mZm;

    public static DatabaseReference mLongitude;
    public static DatabaseReference mLatitude;
    public static DatabaseReference mLightref;
    public static DatabaseReference mPressureref;
    public static DatabaseReference mTemperatureref;
    public static DatabaseReference mHumidityref;
    public static DatabaseReference mProximityref;
    public static DatabaseReference mPedometerref;
    public static DatabaseReference mHeartref;
    public static DatabaseReference mGravref;

    private Calendar c;
    private String sdcard, cdate, fileName, filePath;
    private File fname;
    private CSVWriter writer;
    private Handler fileHandler = new Handler();

    public static AppLocationManager appLocationManager;
    public static String xA, yA, zA, xG, yG, zG, xM, yM, zM, sLight, sPressure, sPedo, sHumi, sTemp, sHeart, sGrav, sProxi;
    public static String[] data = null;
    public String aId;

    public static TextView xVal, yVal, zVal, xGVal, yGVal, zGVal, xMVal, yMVal, zMVal, light, pressure, temp, humi, proxi, grav, pedo, heart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        aId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        mUser = mRootref.child(aId);

        appLocationManager = new AppLocationManager(MainActivity.this);

        xVal = (TextView) findViewById(R.id.xVal);
        yVal = (TextView) findViewById(R.id.yVal);
        zVal = (TextView) findViewById(R.id.zVal);

        xGVal = (TextView) findViewById(R.id.xGVal);
        yGVal = (TextView) findViewById(R.id.yGVal);
        zGVal = (TextView) findViewById(R.id.zGVal);

        xMVal = (TextView) findViewById(R.id.xMVal);
        yMVal = (TextView) findViewById(R.id.yMVal);
        zMVal = (TextView) findViewById(R.id.zMVal);

        light = (TextView) findViewById(R.id.light);
        pressure = (TextView) findViewById(R.id.pressure);
        humi = (TextView) findViewById(R.id.humi);
        temp = (TextView) findViewById(R.id.temp);
        proxi = (TextView) findViewById(R.id.proxi);
        grav = (TextView) findViewById(R.id.grav);
        pedo = (TextView) findViewById(R.id.pedo);
        heart = (TextView) findViewById(R.id.heart);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/trebuc.ttf");
        pedo.setTypeface(myTypeface);
        pedo.setTextColor(Color.BLACK);


        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered acclerometer listener");
        } else {
            xVal.setText("123abcd");
            yVal.setText("123abcd");
            zVal.setText("123abcd");
        }

        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(MainActivity.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyro listener");
        } else {
            xGVal.setText("123abcd");
            yGVal.setText("123abcd");
            zGVal.setText("123abcd");
        }

        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagno != null) {
            sensorManager.registerListener(MainActivity.this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Magno listener");
        } else {
            xMVal.setText("123abcd");
            yMVal.setText("123abcd");
            zMVal.setText("123abcd");
        }

        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(MainActivity.this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Light listener");
        } else {
            light.setText("123abcd");
        }

        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(MainActivity.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Pressure listener");
        } else {
            pressure.setText("123abcd");
        }

        mTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemp != null) {
            sensorManager.registerListener(MainActivity.this, mTemp, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Temperature listener");
        } else {
            temp.setText("123abcd");
        }

        mHumi = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumi != null) {
            sensorManager.registerListener(MainActivity.this, mHumi, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Humidity listener");
        } else {
            humi.setText("123abcd");
        }

        mProxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mProxi != null) {
            sensorManager.registerListener(MainActivity.this, mProxi, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Proximity listener");
        } else {
            humi.setText("123abcd");
        }

        mGrav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (mGrav != null) {
            sensorManager.registerListener(MainActivity.this, mGrav, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Proximity listener");
        } else {
            grav.setText("123abcd");
        }

        mPedo = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mPedo != null) {
            sensorManager.registerListener(MainActivity.this, mPedo, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Pedometer listener");
        } else {
            pedo.setText("123abcd");
        }

        mheart = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (mheart != null) {
            sensorManager.registerListener(MainActivity.this, mheart, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Device temperature listener");
        } else {
            heart.setText("123abcd");
        }


    }

    @SuppressLint("HardwareIds")
    public static void upload(Context context) {

        String thisTime, thisDate;

        String longitude,latitude;



        longitude = appLocationManager.getLongitude();
        latitude = appLocationManager.getLatitude();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        thisTime = sdf.format(new Date());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        thisDate = df.format(new Date());

        Log.d(TAG, "timeNow  :" + thisTime + thisDate);


        mSub = mUser.child("Sensor data");
        mDate = mSub.child(thisDate);
        mTime = mDate.child(thisTime);

        mLongitude = mTime.child("Longitude");
        mLatitude = mTime.child("Latitude");

        mXa = mTime.child("xAVal");
        mYa = mTime.child("yAVal");
        mZa = mTime.child("zAVal");

        mXg = mTime.child("xGVal");
        mYg = mTime.child("yGVal");
        mZg = mTime.child("zGVal");

        mXm = mTime.child("xMVal");
        mYm = mTime.child("yMVal");
        mZm = mTime.child("zMVal");

        mLightref = mTime.child("Light");
        mPressureref = mTime.child("Pressure");
        mTemperatureref = mTime.child("Temperature");
        mHumidityref = mTime.child("Humidity");
        mProximityref = mTime.child("Proximity");
        mPedometerref = mTime.child("Pedometer");
        mHeartref = mTime.child("Heart");
        mGravref = mTime.child("Gravity");

        mLongitude.setValue(longitude);
        mLatitude.setValue(latitude);

        xA = xVal.getText().toString() ; mXa.setValue(xA);
        yA = yVal.getText().toString(); mYa.setValue(yA);
        zA = zVal.getText().toString(); mZa.setValue(zA);

        xG = xGVal.getText().toString(); mXg.setValue(xG);
        yG = yGVal.getText().toString(); mYg.setValue(yG);
        zG = zGVal.getText().toString(); mZg.setValue(zG);

        xM = xMVal.getText().toString(); mXm.setValue(xM);
        yM = yMVal.getText().toString(); mYm.setValue(yM);
        zM = zMVal.getText().toString(); mZm.setValue(zM);

        sLight = light.getText().toString(); mLightref.setValue(sLight);
        sPressure = pressure.getText().toString(); mPressureref.setValue(sPressure);
        sTemp = temp.getText().toString(); mTemperatureref.setValue(sTemp);
        sHumi = humi.getText().toString(); mHumidityref.setValue(sHumi);
        sPedo = pedo.getText().toString(); mPedometerref.setValue(sPedo);
        sProxi = proxi.getText().toString(); mProximityref.setValue(sProxi);
        sGrav = grav.getText().toString(); mGravref.setValue(sGrav);
        sHeart = heart.getText().toString(); mHeartref.setValue(sHeart);
    }

    public void goNext(View view) {

        Intent intent = new Intent(this, uiService.class);
        startActivity(intent);
    }


    public void signOutUser(View view){

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, Authetication.class);
        startActivity(intent);

    }

    public void resetPedo(View view){
       pedo.setText("0");
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor s = event.sensor;
        try {
            if(s.getType() == Sensor.TYPE_ACCELEROMETER) {


                Log.d(TAG, "onSensorChanged: X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

                xVal.setText(""+event.values[0]);
                yVal.setText("" + event.values[1]);
                zVal.setText("" + event.values[2]);

            }else if(s.getType() == Sensor.TYPE_GYROSCOPE) {
                xGVal.setText("" + event.values[0]);
                yGVal.setText("" + event.values[1]);
                zGVal.setText("" + event.values[2]);
            }else if(s.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                xMVal.setText("" + event.values[0]);
                yMVal.setText("" + event.values[1]);
                zMVal.setText("" + event.values[2]);
            }else if(s.getType() == Sensor.TYPE_LIGHT) {
                light.setText("" + event.values[0]);
            }else if(s.getType() == Sensor.TYPE_PRESSURE) {
                pressure.setText("" + event.values[0]);
            }else if(s.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                temp.setText("" + event.values[0]);
            }else if(s.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                humi.setText("" + event.values[0]);
            }else if(s.getType() == Sensor.TYPE_PROXIMITY) {
                proxi.setText("" + event.values[0]);
            }else if(s.getType() == Sensor.TYPE_GRAVITY) {
                grav.setText("" + event.values[0]);
            }else if(s.getType() == Sensor.TYPE_STEP_COUNTER) {
                pedo.setText("" + event.values[0]);
            }else if(s.getType() == Sensor.TYPE_HEART_RATE) {
                heart.setText("" + event.values[0]);
            }
        } catch (Exception e) {
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }



}
