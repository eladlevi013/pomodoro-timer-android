package com.elad.pomodorotimer;

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

public class StudySessionPage extends AppCompatActivity {
    public class ViewHolder
    {
        TextView sessionGoal, sessionDuration, sessionTime;
        ImageView sessionImg;
    }
    EditText sessionGoal_et, sessionImage_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session_page);

        // binds editText
        sessionGoal_et = findViewById(R.id.set_goal_et);
        sessionImage_et = findViewById(R.id.set_image_et);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.sessionGoal = (TextView) findViewById(R.id.session_goal_tv);
        viewHolder.sessionDuration = (TextView) findViewById(R.id.session_duration_tv);
        viewHolder.sessionTime = (TextView) findViewById(R.id.session_time_tv);
        viewHolder.sessionImg = (ImageView) findViewById(R.id.session_img);

        // sets the values
        Intent intent = getIntent();
        viewHolder.sessionGoal.setText(intent.getStringExtra("goal"));
        viewHolder.sessionDuration.setText(intent.getStringExtra("duration"));
        viewHolder.sessionTime.setText(intent.getStringExtra("time"));
        Picasso.get()
                .load(intent.getStringExtra("img"))
                .into(viewHolder.sessionImg);

        Button update_btn = findViewById(R.id.update_btn);
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editStudySessionById(intent.getStringExtra("pid"));
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
}