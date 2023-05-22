package com.elad.pomodorotimer;

import android.content.Context;
import android.icu.text.IDNA;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class StudySessionsAdapter extends ArrayAdapter <StudySession>{
    private Context context;
    private ArrayList<StudySession> arr;

    public StudySessionsAdapter(Context context, ArrayList<StudySession> arr)
    {
        super(context, R.layout.study_session_listview_layout, arr);
        this.arr = arr;
        this.context = context;
    }

    public class ViewHolder
    {
        TextView sessionGoal, sessionDuration, sessionTime;
        ImageView sessionImg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        StudySession studySession = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.study_session_listview_layout, parent, false);
            viewHolder.sessionGoal = (TextView) convertView.findViewById(R.id.session_goal_tv);
            viewHolder.sessionDuration = (TextView) convertView.findViewById(R.id.session_duration_tv);
            viewHolder.sessionTime = (TextView) convertView.findViewById(R.id.session_time_tv);
            viewHolder.sessionImg = (ImageView) convertView.findViewById(R.id.session_img);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.sessionGoal.setText(studySession.getGoal());

        int duration = Integer.parseInt(studySession.getDuration());
        int minutes = (int) (duration / 60);
        int seconds = (int) (duration % 60);
        String timeLeftFormatted = "duration: " + String.format("%02d:%02d", minutes, seconds);
        viewHolder.sessionDuration.setText(timeLeftFormatted);

        long yourMilliseconds = Long.parseLong(studySession.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        Date resultDate = new Date(yourMilliseconds);
        String formattedTime = sdf.format(resultDate);
        viewHolder.sessionTime.setText("time: " + formattedTime);

        Picasso.get()
                .load(studySession.getImage())
                .into(viewHolder.sessionImg);

        return convertView;
    }
}
