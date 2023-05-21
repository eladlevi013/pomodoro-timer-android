package com.elad.pomodorotimer;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    //FIREBASE-FIRESTORE
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    public static final String KEY_STUDYSESSION_GOAL = "studySessionGoal";
    public static final String KEY_STUDYSESSION_DURATION = "studySessionDuration";
    public static final long KEY_STUDYSESSION_TIME = "studySessionTime";
    public static final String KEY_STUDYSESSION_IMAGE = "studySessionImage";

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

    private void editStudySessionById(String pid) {
        DocumentReference document = database.collection("studySessions").document(pid);
        document
                .update("studySessionTime", "5900")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "studySession updated", Toast.LENGTH_LONG).show();
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
        database.collection("studySessions").document(pid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "studySession deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getStudySessionById(String pid) {
        DocumentReference document = database
                .collection("studySessions")
                .document(pid);
        document
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                System.out.println(documentSnapshot.getId() + " ---> " + documentSnapshot.getData());
                            } else {
                                Toast.makeText(getApplicationContext(), "No doc for you", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getStudySessionByValue(String goal) {
        database.collection("studySessions")
                .whereEqualTo("studySessionGoal", goal)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                System.out.println(document.getId() + " ---> " + document.getData());
                            }
                        }   else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createNewStudySession() {
        //GET PARAMETERS
        String ssGoal = studySessionGoal.getText().toString().trim();
        String ssDuration = studySessionDuration.getText().toString().trim();
        long ssTime = studySessionTime.getText().toString().trim();//CHECK DATA
        String ssImage = "https://www.apple.com/newsroom/images/product/iphone/geo/Apple-iPhone-14-Pro-iPhone-14-Pro-Max-deep-purple-220907-geo_inline.jpg.large.jpg";

        //MAPPING
        Map<String, Object> data = new HashMap<>();
        data.put(KEY_STUDYSESSION_GOAL , ssGoal);
        data.put(KEY_STUDYSESSION_DURATION , ssDuration);
        data.put(KEY_STUDYSESSION_TIME , ssTime);
        data.put(KEY_STUDYSESSION_IMAGE , ssImage);

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

    private void getAllStudySessions()
    {
        database
                .collection("studySessions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        studySessions = new ArrayList<StudySession>();

                        if(task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                StudySession ss = document.toObject(StudySession.class);
                                studySessions.add(ss);
                            }
                            /*
                            CHANGE FROM Products
                            Intent intent = new Intent(getApplicationContext(), Products.class);
                            intent.putParcelableArrayListExtra("list", studySessions);
                            startActivity(intent);

                             */
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}