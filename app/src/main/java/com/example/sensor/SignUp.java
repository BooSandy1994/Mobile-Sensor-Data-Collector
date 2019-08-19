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

public class SignUp extends AppCompatActivity {

    private Button b1;
    private EditText  mEmailField, mConfirmPass, mNewPass;
    private   EditText mName;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Typeface myTypeface = Typeface.createFromAsset(getAssets(),"fonts/trebuc.ttf");

        b1 = (Button)findViewById(R.id.button3);
        mName = (EditText) findViewById(R.id.eName);
        mEmailField = (EditText) findViewById(R.id.fieldEmail);
        mConfirmPass = (EditText) findViewById(R.id.fieldConfirm);
        mNewPass = (EditText) findViewById(R.id.fieldNew);

        mName.setTypeface(myTypeface); mName.setTextColor(Color.BLACK);
        mEmailField.setTypeface(myTypeface); mEmailField.setTextColor(Color.BLACK);
        mConfirmPass.setTypeface(myTypeface); mConfirmPass.setTextColor(Color.BLACK);
        mNewPass.setTypeface(myTypeface); mNewPass.setTextColor(Color.BLACK);


        mAuth = FirebaseAuth.getInstance();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUp();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startSignUp(){

        String name = mName.getText().toString();
        String email = mEmailField.getText().toString();
        String newPass = mNewPass.getText().toString();
        String conPass = mConfirmPass.getText().toString();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(conPass)){
            Toast.makeText(SignUp.this, "Fields Empty" , Toast.LENGTH_LONG).show();
        }
        else {

            mAuth.createUserWithEmailAndPassword(email,newPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(! task.isSuccessful()){
                        Toast.makeText(SignUp.this, "SignUp Failed", Toast.LENGTH_LONG).show();
                    }else {
                        openAuthetication();
                    }
                }
            });
        }
    }

    public void openAuthetication(){
        Intent intent = new Intent(this, Authetication.class);
        startActivity(intent);
    }


}
