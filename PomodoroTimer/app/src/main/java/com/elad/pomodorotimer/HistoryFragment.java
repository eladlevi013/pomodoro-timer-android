package com.elad.pomodorotimer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<StudySession> arr;

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

        // getting data from firebase
        database.collection("studySessions").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arr = new ArrayList<>(); // Initialize the list here

                // for each document in the collection
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // get the data from the document
                    String goal = documentSnapshot.getString("studySessionGoal");
                    String duration = documentSnapshot.getString("studySessionDuration");
                    String time = documentSnapshot.getString("studySessionTime");
                    String image = documentSnapshot.getString("studySessionImage");

                    // create a new StudySession object
                    StudySession studySession = new StudySession(goal, duration, time, image);

                    // add the StudySession object to the array
                    arr.add(studySession);
                }

                ListView studySessionsListView = rootView.findViewById(R.id.studySessionsListView);

                // create a new adapter
                StudySessionsAdapter adapter = new StudySessionsAdapter(getContext(), arr);

                // set the adapter to the list view
                studySessionsListView.setAdapter(adapter);

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
            }
        });

        return rootView;
    }
}
