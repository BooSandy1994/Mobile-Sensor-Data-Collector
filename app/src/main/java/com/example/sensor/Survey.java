package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Survey extends AppCompatActivity {

    private static final String TAG = "SurveyClass" ;
    private EditText msAnswer, mbAnswer;

    private TextView firstQuestion, secondQuestion;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootref = firebaseDatabase.getReference();

    private DatabaseReference mUser;
    private DatabaseReference mSub;
    private DatabaseReference mDate;
    private DatabaseReference mTime;
    private DatabaseReference mAns1;
    private DatabaseReference mAns2;

    private FirebaseAuth mAuth;

    private  String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Typeface mTypeface = Typeface.createFromAsset(getAssets(),"fonts/trebuc.ttf");

        mAuth = FirebaseAuth.getInstance();

        idUser = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mUser = mRootref.child(idUser);


        msAnswer = (EditText) findViewById(R.id.fAns);
        mbAnswer = (EditText) findViewById(R.id.sAns);

        firstQuestion = (TextView) findViewById(R.id.fQues);
        secondQuestion = (TextView) findViewById(R.id.sQues);

        firstQuestion.setTypeface(mTypeface);firstQuestion.setTextColor(Color.BLACK);

        secondQuestion.setTypeface(mTypeface);secondQuestion.setTextColor(Color.BLACK);

        msAnswer.setTypeface(mTypeface);
        mbAnswer.setTypeface(mTypeface);

        String timeStampNow, timeStampBefore;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeStampNow = sdf.format(new Date());

        Date date = null;
        try {
            date = sdf.parse(timeStampNow);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -20);
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        timeStampBefore = sdf1.format(calendar.getTime());

        Log.d(TAG, "times " + timeStampNow + "and " + timeStampBefore);

        String finalQuestion1 = "Where was your phone between " + timeStampBefore + " and "
                                 + timeStampNow + " today ?";
        String finalQuestion2 = "What were you doing between " + timeStampBefore + " and " + timeStampNow
                                 + " today ?";
        firstQuestion.setText(finalQuestion1);
        secondQuestion.setText(finalQuestion2);
    }

    private void preUploadSurvey() {
        String sAns, bAns, timeNow, dateNow;
        sAns = msAnswer.getText().toString();
        bAns = mbAnswer.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeNow = sdf.format(new Date());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        dateNow = df.format(new Date());

        mSub = mUser.child("Survey");
        mDate = mSub.child(dateNow);
        mTime = mDate.child(timeNow);

        mAns1 = mTime.child("Answer1:");
        mAns2 = mTime.child("Answer2:");

        mAns1.setValue(sAns);
        mAns2.setValue(bAns);

    }

    public void uploadSurvey(View v) {

        preUploadSurvey();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
