package com.felixwhitesean.classcommapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;


public class TodaysSchedule extends Fragment {
    FirebaseFirestore db;
    String userUID;
    SharedPreferences sharedPref;
    private static final String PREFS_NAME = "UserLoginPrefs";
    private static final String CURRENT_USER_ID = "CurrentUserId";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_todays_schedule, container, false);
    }
}