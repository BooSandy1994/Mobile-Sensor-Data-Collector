package com.example.sensor;

import com.opencsv.CSVWriter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;


public class MainActivity<data> extends AppCompatActivity implements SensorEventListener, ValueEventListener {

    private static final String TAG = "MainActivity";
    private SensorManager sensorManager;
    private Sensor accelerometer, mGyro, mMagno, mLight, mPressure, mTemp, mHumi, mProxi, mGrav, mPedo, mheart, mGeo;

    private float[] accelerometerReading = new float[3];
    private float[] magnetometerReading = new float[3];

    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

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

   // public static DatabaseReference mHVelocity;
    public static DatabaseReference mLongitude;
    public static DatabaseReference mLatitude;
    public static DatabaseReference mAzimuthref;
    public static DatabaseReference mPitchref;
    public static DatabaseReference mRollref;
    public static DatabaseReference mLightref;
    public static DatabaseReference mPressureref;
    public static DatabaseReference mTemperatureref;
    public static DatabaseReference mHumidityref;
    public static DatabaseReference mProximityref;
    public static DatabaseReference mPedometerref;
    public static DatabaseReference mHeartref;
    public static DatabaseReference mxGravref;
    public static DatabaseReference myGravref;
    public static DatabaseReference mzGravref;


    public static AppLocationManager appLocationManager;
    public String aId;

    public static String xVal, yVal, zVal, xGVal, yGVal, zGVal, xMVal, yMVal, zMVal, light, pressure, temp, humi, proxi, xGrav,
            yGrav, zGrav, heart, azimuthGeo, pitchGeo, rollGeo;
    public static TextView  pedo;
    private Display mdisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();




        aId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        mUser = mRootref.child(aId);
        appLocationManager = new AppLocationManager(MainActivity.this);

        pedo = findViewById(R.id.pedo);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/trebuc.ttf");
        pedo.setTypeface(myTypeface);
        pedo.setTextColor(Color.BLACK);


        Log.d(TAG, "onCreate: Initializing Sensor Services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mdisplay = wm.getDefaultDisplay();



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();


        mGeo = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        if(mGeo != null) {
            sensorManager.registerListener(MainActivity.this, mGeo, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Geomagnetic sensor active");
        }else {
            azimuthGeo = "NA";
            pitchGeo = "NA";
            rollGeo = "NA";
        }

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (accelerometer != null) {
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered accelerometer listener");
        } else {
            xVal = "NA";
            yVal = "NA";
            zVal = "NA";
        }

        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (mGyro != null) {
            sensorManager.registerListener(MainActivity.this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyro listener");
        } else {
            xGVal = "NA";
            yGVal = "NA";
            zGVal = "NA";
        }

        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagno != null) {
            sensorManager.registerListener(MainActivity.this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Magno listener");
        } else {
            xMVal = "NA";
            yMVal = "NA";
            zMVal = "NA";
        }

        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLight != null) {
            sensorManager.registerListener(MainActivity.this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Light listener");
        } else {
            light = "NA";
        }

        mPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (mPressure != null) {
            sensorManager.registerListener(MainActivity.this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Pressure listener");
        } else {
            pressure = "NA";
        }

        mTemp = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (mTemp != null) {
            sensorManager.registerListener(MainActivity.this, mTemp, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Temperature listener");
        } else {
            temp = "NA";
        }

        mHumi = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (mHumi != null) {
            sensorManager.registerListener(MainActivity.this, mHumi, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Humidity listener");
        } else {
            humi = "NA";
        }

        mProxi = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (mProxi != null) {
            sensorManager.registerListener(MainActivity.this, mProxi, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Proximity listener");
        } else {
            proxi = "NA";
        }

        mGrav = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (mGrav != null) {
            sensorManager.registerListener(MainActivity.this, mGrav, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Proximity listener");
        } else {
            xGrav = "NA";
            yGrav = "NA";
            zGrav = "NA";
        }

        mPedo = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (mPedo != null) {
            sensorManager.registerListener(MainActivity.this, mPedo, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Pedometer listener");
        } else {
            pedo.setText("NA");
        }

        mheart = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (mheart != null) {
            sensorManager.registerListener(MainActivity.this, mheart, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Device temperature listener");
        } else {
            heart = "NA";
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void update(){

        String longitude, latitude;


        longitude = appLocationManager.getLongitude();
        latitude = appLocationManager.getLatitude();


        String thisDate;
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        thisDate = df.format(new Date());

        String thisTime;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        thisTime = sdf.format(new Date());

        String basedir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "Analysis-"+thisDate+".csv";
        String filePath = basedir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer = null;

        //file writing begins
        if(f.exists() && !f.isDirectory()){
            try{
                FileWriter mWriter = new FileWriter(filePath, true);
                writer = new CSVWriter(mWriter);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try {
                writer = new CSVWriter(new FileWriter(filePath));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        List<?> l = new ArrayList<>();


        //float[] f1 = new float[] {};

        String[] data = {thisTime,longitude,latitude,xVal,yVal,zVal,xGVal,yGVal,
                            zGVal,xMVal,yMVal,zMVal,azimuthGeo,pitchGeo,rollGeo,
                            light,pressure,temp,humi,pedo.getText().toString(),proxi,xGrav,yGrav,zGrav,heart};

        writer.writeNext(data);
        try{
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void upload(Context context) {

        String thisTime, thisDate;

        String longitude, latitude, sPedo;


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

       // mHVelocity = mTime.child("Haversine Velocity");
        mAzimuthref = mTime.child("Device Direction");
        mPitchref = mTime.child("Top - Bottom tilt");
        mRollref = mTime.child("Left - Right tilt");
        mLightref = mTime.child("Light");
        mPressureref = mTime.child("Pressure");
        mTemperatureref = mTime.child("Temperature");
        mHumidityref = mTime.child("Humidity");
        mProximityref = mTime.child("Proximity");
        mPedometerref = mTime.child("Pedometer");
        mHeartref = mTime.child("Heart");
        mxGravref = mTime.child("xGrav");
        myGravref = mTime.child("yGrav");
        mzGravref = mTime.child("zGrav");


        mLongitude.setValue(longitude);
        mLatitude.setValue(latitude);
       // mHVelocity.setValue(velocityHaversine);

        mXa.setValue(xVal);
        mYa.setValue(yVal);
        mZa.setValue(zVal);
        mXg.setValue(xGVal);
        mYg.setValue(yGVal);
        mZg.setValue(zGVal);
        mXm.setValue(xMVal);
        mYm.setValue(yMVal);
        mZm.setValue(zMVal);
        mAzimuthref.setValue(azimuthGeo);
        mPitchref.setValue(pitchGeo);
        mRollref.setValue(rollGeo);
        mLightref.setValue(light);
        mPressureref.setValue(pressure);
        mTemperatureref.setValue(temp);
        mHumidityref.setValue(humi);
        sPedo = pedo.getText().toString();
        mPedometerref.setValue(sPedo);
        mProximityref.setValue(proxi);
        mxGravref.setValue(xGrav);
        myGravref.setValue(yGrav);
        mzGravref.setValue(zGrav);
        mHeartref.setValue(heart);
    }

    public void goNext(View view) {

        Intent intent = new Intent(this, uiService.class);
        startActivity(intent);
    }

    public void signOutUser(View view) {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, Authetication.class);
        startActivity(intent);

    }

    public void resetPedo(View view) {
        pedo.setText("0");
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor s = event.sensor;
        try {
            if (s.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                Log.d(TAG, "onSensorChanged: X: " + event.values[0] + "Y: " + event.values[1] + "Z: " + event.values[2]);

                xVal = "" + event.values[0];
                yVal = "" + event.values[1];
                zVal = "" + event.values[2];

                System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.length );


            } else if (s.getType() == Sensor.TYPE_GYROSCOPE) {
                xGVal= "" + event.values[0];
                yGVal= "" + event.values[1];
                zGVal= "" + event.values[2];
            } else if (s.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                xMVal= "" + event.values[0];
                yMVal= "" + event.values[1];
                zMVal= "" + event.values[2];

                System.arraycopy(event.values, 0, magnetometerReading, 0 , magnetometerReading.length);

            } else if (s.getType() == Sensor.TYPE_LIGHT) {
                light= "" + event.values[0];
            } else if (s.getType() == Sensor.TYPE_PRESSURE) {
                pressure= "" + event.values[0];
            } else if (s.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                temp= "" + event.values[0];
            } else if (s.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                humi= "" + event.values[0];
            } else if (s.getType() == Sensor.TYPE_PROXIMITY) {
                proxi= "" + event.values[0];
            } else if (s.getType() == Sensor.TYPE_GRAVITY) {
                xGrav= "" + event.values[0];
                yGrav= "" + event.values[1];
                zGrav= "" + event.values[2];
            } else if (s.getType() == Sensor.TYPE_STEP_COUNTER) {
                pedo.setText("" + event.values[0]);
            } else if (s.getType() == Sensor.TYPE_HEART_RATE) {
                heart= "" + event.values[0];
            }
        } catch (Exception e) {
        }


        boolean rotationOk;
        rotationOk = SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);

        float[] rotationMatrixAdjusted = new float[9];

        switch (mdisplay.getRotation()) {
            case Surface.ROTATION_0 :
                rotationMatrixAdjusted = rotationMatrix.clone();
                break;

            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, rotationMatrixAdjusted);
                break;

            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_MINUS_X,SensorManager.AXIS_MINUS_Y, rotationMatrixAdjusted);
                break;

            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X,rotationMatrixAdjusted);
                break;
        }


        if(rotationOk)
            SensorManager.getOrientation(rotationMatrixAdjusted,orientationAngles);

        azimuthGeo = "" + orientationAngles[0];
        pitchGeo   = "" + orientationAngles[1];
        rollGeo    = "" + orientationAngles[2];

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





}