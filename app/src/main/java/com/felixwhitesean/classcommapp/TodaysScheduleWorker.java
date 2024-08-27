package com.felixwhitesean.classcommapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TodaysScheduleWorker extends Worker {
    private final FirebaseFirestore firestore;
    private final SharedPreferences sp;
    String timetableName;

    public TodaysScheduleWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        firestore = FirebaseFirestore.getInstance();
        sp = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Initialize variables
        timetableName = getInputData().getString("timetable");
        if (timetableName == null) {
            return Result.failure();
        }
        else{
//            timetableName = timetableName.toUpperCase();
            final boolean[] isSuccessful = {false};
            List<String> trueSessions = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(1);

            firestore.collection("timetable").document(timetableName).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                boolean session1 = document.getBoolean("session1");
                                boolean session2 = document.getBoolean("session2");
                                boolean session3 = document.getBoolean("session3");
                                boolean session4 = document.getBoolean("session4");
                                boolean session5 = document.getBoolean("session5");

                                boolean[] sessions = {session1, session2, session3, session4, session5};
                                String[] sessionNames = {"session1", "session2", "session3", "session4", "session5"};

                                for (int i = 0; i < sessions.length; i++) {
                                    if (sessions[i]) {
                                        trueSessions.add(sessionNames[i]); // Add the session name to the list if true
                                    }
                                }
                                // Now trueSessions contains all the true sessions
                                if (!trueSessions.isEmpty()) {
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("TodaySessions", trueSessions.toString());
                                    editor.apply();
                                    Log.d("Result", trueSessions.toString());
                                }else{
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("TodaySessions", "No session today");
                                    editor.apply();
                                    Log.d("Result","No session today");
                                }

                                isSuccessful[0] = true;
                            } else {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("TodaySessions", String.valueOf(document));
                                editor.apply();
                                Log.d("Error","Document not found");
                                // Handle document not found
                                isSuccessful[0] = false;
                            }
                        } else {
                            // Handle Firestore errors
                            isSuccessful[0] = false;
                            Log.d("Error","Error fetching user details");
                        }
                        latch.countDown();
                        // Return Result based on success or failure
                    });
            try {
                latch.await();  // Wait for Firestore task to complete
            } catch (InterruptedException e) {
                Log.e("Firestore", "Error waiting for Firestore task", e);
                return Result.failure();
            }
            return isSuccessful[0] ? Result.success(): Result.failure();
        }
    }
}
