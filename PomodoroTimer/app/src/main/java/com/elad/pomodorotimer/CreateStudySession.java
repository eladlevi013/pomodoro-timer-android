package com.elad.pomodorotimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CreateStudySession extends AppCompatActivity {
    //FIREBASE-FIRESTORE
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    public static final String KEY_STUDYSESSION_GOAL = "studySessionGoal";
    public static final String KEY_STUDYSESSION_DURATION = "studySessionDuration";
    public static final String KEY_STUDYSESSION_TIME = "studySessionTime";
    public static final String KEY_STUDYSESSION_IMAGE = "studySessionImage";
    TextView study_session_time_tv, study_session_duration_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study_session);

        study_session_time_tv = findViewById(R.id.session_time_tv);
        study_session_duration_tv = findViewById(R.id.session_duration_tv);

        // gets the data from the sender
        Intent intent = getIntent();

        int duration = Integer.parseInt(intent.getStringExtra("duration"));
        int minutes = (int) (duration / 60);
        int seconds = (int) (duration % 60);
        String timeLeftFormatted = "duration: " + String.format("%02d:%02d", minutes, seconds);
        study_session_duration_tv.setText(timeLeftFormatted);

        long yourMilliseconds = Long.parseLong(intent.getStringExtra("time"));
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Date resultDate = new Date(yourMilliseconds);
        String formattedTime = sdf.format(resultDate);
        study_session_time_tv.setText("time: " + formattedTime);

        Button add_study_btn = findViewById(R.id.add_btn);
        add_study_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                createNewStudySession();
            }
        });
    }

    private void createNewStudySession() {
        EditText goal_et = findViewById(R.id.set_goal_et);
        EditText img_et = findViewById(R.id.set_image_et);
        String goal = goal_et.getText().toString();
        String img = img_et.getText().toString();
        String time = getIntent().getStringExtra("time");
        String duration = getIntent().getStringExtra("duration");

        // data validation
        if(img.isEmpty())
        {
            img = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQpKjTt-J8EHJOCD5DPMy7pl2u57HDoaOjNA5n3vj78&s";
        }

        //MAPPING
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_STUDYSESSION_GOAL , goal);
        data.put(KEY_STUDYSESSION_DURATION , duration);
        data.put(KEY_STUDYSESSION_TIME , time);
        data.put(KEY_STUDYSESSION_IMAGE , img);

        database
                .collection("studySessions")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "studySession created", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                });

        // Moving back to the timer page
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}