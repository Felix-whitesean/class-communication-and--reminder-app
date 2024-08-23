package com.felixwhitesean.classcommapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.SmsManager;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsWorker extends Worker {

    private DatabaseReference databaseReference;

    public NotificationsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public Result doWork() {
        // Check network status
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Fetch recipient phone number and timetable from Firebase
            fetchRecipientPhoneNumberAndSendNotification();
        } else {
            // Handle offline case
            boolean smsSent = sendSmsNotification();
            if (!smsSent) {
                // Retry the work in case of SMS failure
                return Result.retry();
            }
        }

        return Result.success();
    }

    private void fetchRecipientPhoneNumberAndSendNotification() {
        // Fetch recipient phone number
        databaseReference.child("recipients").child("user_id").child("phoneNumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneNumber = dataSnapshot.getValue(String.class);

                if (phoneNumber != null) {
                    // Fetch timetable and send notification
                    fetchTimetableAndSendNotification(phoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                // Log the error or retry
            }
        });
    }

    private void fetchTimetableAndSendNotification(String phoneNumber) {
        // Fetch the timetable from Firebase
        databaseReference.child("timetable").child("class_schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Process timetable data to find the next class
                String nextClassMessage = "Your next class is at [time]"; // Replace with actual message

                boolean notificationSent = sendSmsNotification(phoneNumber, nextClassMessage);
                if (!notificationSent) {
                    // Retry if SMS failed
                    // You might want to handle retries differently
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                // Log the error or retry
            }
        });
    }

    private boolean sendSmsNotification(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            return true; // SMS sent successfully
        } catch (Exception e) {
            e.printStackTrace();
            return false; // SMS failed to send
        }
    }

    private boolean sendSmsNotification() {
        // Handle offline case with default message or similar
        return false;
    }
}
