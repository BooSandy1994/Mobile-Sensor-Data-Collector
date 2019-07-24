package com.example.sensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.os.Build.VERSION_CODES.M;

public class welcome_activity extends AppCompatActivity {

    public static final int ASK_MULTIPLE_PERMISSION_CODE = 911;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activity);
    }

    public void gotoSignin(View v) {

        if(Build.VERSION.SDK_INT >= M ) {

            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                 checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&&
                  checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                  checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                Intent intent = new Intent(this, Authetication.class);
                startActivity(intent);

            }else {
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "Location and Storage Services are needed for the app", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[] {
                                                 Manifest.permission.ACCESS_FINE_LOCATION,
                                                 Manifest.permission.READ_EXTERNAL_STORAGE,
                                                 Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                 Manifest.permission.ACCESS_COARSE_LOCATION},
                                                 ASK_MULTIPLE_PERMISSION_CODE);


            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == ASK_MULTIPLE_PERMISSION_CODE) {

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, Authetication.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Permissions were not granted", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }
}
