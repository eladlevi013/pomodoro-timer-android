package com.elad.pomodorotimer;

import static java.lang.System.currentTimeMillis;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class TimerFragment extends Fragment {
    TextView timer_tv;
    Button start_timer_btn;
    CountDownTimer countDownTimer;
    boolean isTimerRunning = false;
    long timeRemainingInMillis;

    public TimerFragment() {
        // Required empty public constructor
    }

    public static TimerFragment newInstance() {
        return new TimerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        timer_tv = view.findViewById(R.id.timer_text);
        start_timer_btn = view.findViewById(R.id.timer_toggle);

        start_timer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        return view;
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(25 * 60 * 1000, 1000) { // 25 minutes (adjust as needed)
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                // Timer finished
                timer_tv.setText("Timer Finished");
                isTimerRunning = false;
                start_timer_btn.setText("Start");
            }
        }.start();

        isTimerRunning = true;
        start_timer_btn.setText("Stop");
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        Intent intent = new Intent(getActivity(), createStudySession.class);
        intent.putExtra("duration", timeRemainingInMillis + "");
        intent.putExtra("time", currentTimeMillis() + "");
        startActivity(intent);

        isTimerRunning = false;
        start_timer_btn.setText("Start");
        timeRemainingInMillis = 0;
        updateTimerText();
    }



    private void updateTimerText() {
        int minutes = (int) (timeRemainingInMillis / 1000) / 60;
        int seconds = (int) (timeRemainingInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timer_tv.setText(timeLeftFormatted);
    }
}