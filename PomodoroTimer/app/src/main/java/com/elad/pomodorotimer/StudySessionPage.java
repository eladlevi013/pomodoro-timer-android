package com.elad.pomodorotimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class StudySessionPage extends AppCompatActivity {
    public class ViewHolder
    {
        TextView sessionGoal, sessionDuration, sessionTime;
        ImageView sessionImg;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session_page);

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
                
            }
        });
    }
}