package com.elad.pomodorotimer;

import android.os.Parcel;
import android.os.Parcelable;

public class StudySession  {
    private String goal;
    private String duration;
    private String time;
    private String image;

    public StudySession(String goal, String duration, String time, String image) {
        this.goal = goal;
        this.duration = duration;
        this.time = time;
        this.image = image;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String toString() {
        return "StudySession{" +
                "goal='" + goal + '\'' +
                ", duration='" + duration + '\'' +
                ", time='" + time + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
