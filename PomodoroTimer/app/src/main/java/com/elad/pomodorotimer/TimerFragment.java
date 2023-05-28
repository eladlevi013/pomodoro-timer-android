package com.elad.pomodorotimer;

import static android.content.Context.ACTIVITY_SERVICE;
import static java.lang.System.currentTimeMillis;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TimerFragment extends Fragment {
    TextView timer_tv;
    Button start_timer_btn;
    CountDownTimer countDownTimer;
    boolean isTimerRunning = false;
    long timeRemainingInSeconds;
    private static final int NOTIFICATION_ID = 123;
    private static final int TWENTY_FIVE_MILLISECONDS = 25 * 60 * 1000;
    private static final String PACKAGE_NAME = "com.elad.pomodorotimer";
    private static final String CHANNEL_ID = "timer_channel";

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
        countDownTimer = new CountDownTimer(TWENTY_FIVE_MILLISECONDS, 1000) { // Adjust the duration as needed (in seconds)
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingInSeconds = millisUntilFinished / 1000; // Convert milliseconds to seconds
                updateTimerText();
            }

            @Override
            public void onFinish() {
                ActivityManager am = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
                ActivityManager.RunningTaskInfo task = tasks.get(0); // current task
                ComponentName rootActivity = task.baseActivity;
                String currentPackageName = rootActivity.getPackageName();

                if (!currentPackageName.equals(PACKAGE_NAME)) {
                    showNotification();
                } else {
                    // When timer is finished, create a new study session
                    Intent intent = new Intent(getActivity(), CreateStudySession.class);
                    intent.putExtra("duration", String.valueOf(TWENTY_FIVE_MILLISECONDS));
                    intent.putExtra("time", currentTimeMillis() + "");
                    startActivity(intent);

                    timer_tv.setText("Timer Finished");
                    isTimerRunning = false;
                    start_timer_btn.setText("Start");
                    timeRemainingInSeconds = 0;
                    updateTimerText();
                }
            }
        }.start();
        isTimerRunning = true;
        start_timer_btn.setText("Stop");
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        long elapsedTimeInSeconds = TWENTY_FIVE_MILLISECONDS/1000 - timeRemainingInSeconds;
        Intent intent = new Intent(getActivity(), CreateStudySession.class);
        intent.putExtra("duration", String.valueOf(elapsedTimeInSeconds));
        intent.putExtra("time", String.valueOf(currentTimeMillis()));
        startActivity(intent);

        isTimerRunning = false;
        start_timer_btn.setText("Start");
        timeRemainingInSeconds = 0;
        updateTimerText();
    }


    private void updateTimerText() {
        int minutes = (int) (timeRemainingInSeconds / 60);
        int seconds = (int) (timeRemainingInSeconds % 60);
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timer_tv.setText(timeLeftFormatted);
    }

    private void showNotification() {
        Context context = requireContext();

        // Create the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Timer Channel";
            String channelDescription = "Channel for timer notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Create the intent for the activity to be launched when the notification is clicked
        Intent intent = new Intent(context, CreateStudySession.class);  // Replace `YourActivity` with the desired activity class
        intent.putExtra("duration", String.valueOf(TWENTY_FIVE_MILLISECONDS));
        intent.putExtra("time", String.valueOf(currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        timer_tv.setText("Timer Finished");
        isTimerRunning = false;
        start_timer_btn.setText("Start");
        timeRemainingInSeconds = 0;
        updateTimerText();

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_timer_24)
                .setContentTitle("Timer Complete")
                .setContentText("Your timer has finished.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)  // Set the PendingIntent
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
