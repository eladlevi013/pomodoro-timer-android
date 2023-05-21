package com.elad.pomodorotimer;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ListView studySessionsListView = rootView.findViewById(R.id.studySessionsListView);

        ArrayList<StudySession> arr = new ArrayList<>();
        arr.add(new StudySession("Making Android App", "12:56", "123123", "https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png"));
        arr.add(new StudySession("Making React App", "15:56", "123123", "https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png"));
        arr.add(new StudySession("Making Flutter App", "21:56", "123123", "https://media.sproutsocial.com/uploads/2017/02/10x-featured-social-media-image-size.png"));

        StudySessionsAdapter studySessionsAdapter = new StudySessionsAdapter(getContext(), arr);
        studySessionsListView.setAdapter(studySessionsAdapter);

        studySessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), StudySessionPage.class);
                intent.putExtra("goal", arr.get(i).getGoal());
                intent.putExtra("duration", arr.get(i).getDuration());
                intent.putExtra("time", arr.get(i).getTime());
                intent.putExtra("img", arr.get(i).getImage());
                startActivity(intent);
            }
        });

        return rootView;
    }
}
