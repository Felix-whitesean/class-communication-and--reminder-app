package com.felixwhitesean.classcommapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UserDetailsWorker extends Worker {
    private FirebaseFirestore firestore;
    private SharedPreferences  sp, sharedPreferences;
    String timetableName;

    public UserDetailsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        firestore = FirebaseFirestore.getInstance();
        sp = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("sessionVariables", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Retrieve the user ID from the input data
        String userId = getInputData().getString("userId");

        if (userId == null) {
            return Result.failure();
        }
        final boolean[] isSuccessful = {false};

        firestore.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Gson gson = new Gson();
                            JsonObject jsonObject = new JsonObject();
                            //Fetching user details
                            String course = document.getString("course");
                            String department = document.getString("department");
                            jsonObject.addProperty("username", document.getString("username"));
                            jsonObject.addProperty("email", document.getString("email"));
                            jsonObject.addProperty("category", document.getString("user_category"));
                            jsonObject.addProperty("course", course);
                            jsonObject.addProperty("department", department);
                            jsonObject.addProperty("phone", document.getString("phone_number"));

                            String json = gson.toJson(jsonObject);

//                             = sp.getString(course, null);
                            timetableName = sp.getString("timetable", null);
//                            Data data = new Data.Builder().putString("timetable", timetableName).build();
//                            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(TodaysScheduleWorker.class, 15, TimeUnit.MINUTES).setInputData(data).build();
//                            UUID workRequestId = periodicWorkRequest.getId();

                            Data data = new Data.Builder().putString("timetable", timetableName).build();
                            OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(TodaysScheduleWorker.class).setInputData(data).build();
                            UUID workRequestId = oneTimeWorkRequest.getId();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("workRequestId", workRequestId.toString());
                            editor.apply();
                            WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);
                                    // Store the JSON in SharedPreferences

                            SharedPreferences.Editor edt = sp.edit();
                            edt.putString("user-details", json);
                            edt.apply();

                            isSuccessful[0] = true;
                        } else {
                            // Handle document not found
                            System.out.println("User not found");
                        }
                    } else {
                        // Handle Firestore errors
                        System.out.println("Error fetching user details");
                    }
                    // Return Result based on success or failure
                });
        // Return Result based on asynchronous operation outcome
        return isSuccessful[0] ? Result.success() : Result.failure();
    }
    public void scheduleOneTimeFetch() {
        Data data = new Data.Builder().putString("timetable", timetableName).build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(TodaysScheduleWorker.class).setInputData(data).build();

        UUID workRequestId = oneTimeWorkRequest.getId();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("workRequestId", workRequestId.toString());
        editor.apply();

        // Enqueue the work
        WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);

        // Listen for the completion and schedule the next one
        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(workRequestId)
                .observe((LifecycleOwner) this, workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        // Once this work finishes, schedule the next one after 1 minute
                        scheduleOneTimeFetch();  // Recursive call to create and enqueue the next request
                    }
                });
    }
}
