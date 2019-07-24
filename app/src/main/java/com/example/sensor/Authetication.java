package com.example.sensor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authetication extends AppCompatActivity {

    private Button button, button2;

    private EditText mEmailfield;
    private  EditText mPasswordfield;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authetication);

        mAuth = FirebaseAuth.getInstance();

        Typeface myTypeface = Typeface.createFromAsset(getAssets(),"fonts/trebuc.ttf");

        mEmailfield = (EditText) findViewById(R.id.emailField);
        mPasswordfield = (EditText) findViewById(R.id.passwordField);

        mEmailfield.setTypeface(myTypeface); mEmailfield.setTextColor(Color.BLACK);
        mPasswordfield.setTypeface(myTypeface); mPasswordfield.setTextColor(Color.BLACK);

        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartsignin();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartsignup();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        //Checking if the user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user)  {
        if(user != null) {
            openMainactivity();
        }
        else{
            Toast.makeText(Authetication.this,"Please Sign in", Toast.LENGTH_LONG).show();
        }
    }

    public void openMainactivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void  onStartsignup()  {

        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }

    private void  onStartsignin(){

        String email = mEmailfield.getText().toString();
        String pass = mPasswordfield.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {

            Toast.makeText(Authetication.this, "Fields are Empty", Toast.LENGTH_LONG).show();

        }else {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(! task.isSuccessful()) {
                        Toast.makeText(Authetication.this, "Sign in Problem", Toast.LENGTH_LONG).show();
                    }else {
                        openMainactivity();
                    }
                }
            });
        }

    }
}
