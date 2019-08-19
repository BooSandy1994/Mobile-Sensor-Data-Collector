package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class uiService extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_ui_service);
    }

    public void startService(View v){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent serviceIntent = new Intent(this, myService.class/*myAlarmService.class*/);

            startService(serviceIntent);
        }else {
            Intent intent = new Intent(this,Authetication.class);
            startActivity(intent);
        }


    }


    public void stopService(View v) {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent serviceIntent = new Intent(this, myService.class/*myAlarmService.class*/);

            stopService(serviceIntent);
        }else {
            Intent intent = new Intent(this,Authetication.class);
            startActivity(intent);
        }


    }

    public void gotoSurvey(View v) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent surveyIntent = new Intent(this, Survey.class);
            startActivity(surveyIntent);
        }else {
            Intent intent = new Intent(this,Authetication.class);
            startActivity(intent);
        }
    }


}

