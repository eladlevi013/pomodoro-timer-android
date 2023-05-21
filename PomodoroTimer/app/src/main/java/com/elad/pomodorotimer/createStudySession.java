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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class createStudySession extends AppCompatActivity {
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
        Intent intent = getIntent();
        study_session_time_tv.setText(intent.getStringExtra("time"));
        study_session_duration_tv.setText(intent.getStringExtra("duration"));

        Button add_study_btn = findViewById(R.id.add_btn);
        add_study_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewStudySession();
            }
        });
    }

//    private void editStudySessionById(String pid) {
//        DocumentReference document = database.collection("studySessions").document(pid);
//        document
//                .update("studySessionTime", "5900")
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(getApplicationContext(), "studySession updated", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
//
//    private void deleteStudySessionById(String pid) {
//        database.collection("studySessions").document(pid)
//                .delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(getApplicationContext(), "studySession deleted", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
//                    }
//                });
//    }

    private void createNewStudySession() {
        EditText goal_et = findViewById(R.id.set_goal_et);
        EditText img_et = findViewById(R.id.set_image_et);

        String goal = goal_et.getText().toString();
        String img = img_et.getText().toString();
        String time = study_session_time_tv.getText().toString();
        String duration = study_session_duration_tv.getText().toString();

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
    }
}