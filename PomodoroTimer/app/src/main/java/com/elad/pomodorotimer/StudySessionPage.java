package com.elad.pomodorotimer;

import static java.lang.System.currentTimeMillis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StudySessionPage extends AppCompatActivity {
    EditText sessionGoal_et, sessionImage_et;
    TextView sessionGoal, sessionDuration, sessionTime;
    ImageView sessionImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session_page);

        // binds the elements to the variables
        sessionGoal_et = findViewById(R.id.set_goal_et);
        sessionImage_et = findViewById(R.id.set_image_et);
        sessionGoal = (TextView) findViewById(R.id.session_goal_tv);
        sessionDuration = (TextView) findViewById(R.id.session_duration_tv);
        sessionTime = (TextView) findViewById(R.id.session_time_tv);
        sessionImg = (ImageView) findViewById(R.id.session_img);
        Button update_btn = findViewById(R.id.update_btn);
        Button delete_btn = findViewById(R.id.delete_btn);

        // sets the values
        Intent intent = getIntent();
        sessionGoal.setText(intent.getStringExtra("goal"));

        int duration = Integer.parseInt(intent.getStringExtra("duration"));
        int minutes = (int) (duration / 60);
        int seconds = (int) (duration % 60);
        String timeLeftFormatted = "duration: " + String.format("%02d:%02d", minutes, seconds);
        sessionDuration.setText(timeLeftFormatted);

        long yourMilliseconds = Long.parseLong(intent.getStringExtra("time"));
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Date resultDate = new Date(yourMilliseconds);
        String formattedTime = sdf.format(resultDate);
        sessionTime.setText("time: " + formattedTime);

        Picasso.get()
                .load(intent.getStringExtra("img"))
                .into(sessionImg);

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editStudySessionById(intent.getStringExtra("pid"));
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteStudySessionById(intent.getStringExtra("pid"));
            }
        });
    }

    private void editStudySessionById(String pid) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference document = database.collection("studySessions").document(pid);

        if(!sessionGoal_et.getText().toString().isEmpty())
            document
                    .update("studySessionGoal", sessionGoal_et.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "studySession goal updated", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                        }
                    });

        if(!sessionImage_et.getText().toString().isEmpty())
            document
                    .update("studySessionImage", sessionImage_et.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "studySession image updated", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                        }
                    });
    }

    private void deleteStudySessionById(String pid) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("studySessions").document(pid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "studySession deleted", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                });
    }
}