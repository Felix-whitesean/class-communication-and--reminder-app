package com.felixwhitesean.classcommapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class TodaysSchedule extends Fragment {
    TextView course;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays_schedule, container, false);
        course = view.findViewById(R.id.course_name);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("schedules");  // Retrieve the data using the same key

            Toast.makeText(getActivity(), "Received data: " + data, Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}