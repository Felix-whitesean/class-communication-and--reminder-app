package com.felixwhitesean.classcommapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

public class AlarmWorker extends Worker {
    private Context context;

    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        // Set the alarm using the code from your original implementation
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2024);       // Set the year
        calendar.set(Calendar.MONTH, Calendar.AUGUST);  // Set the month (January is 0, December is 11)
        calendar.set(Calendar.DAY_OF_MONTH, 24); // Set the day of the month
        calendar.set(Calendar.HOUR_OF_DAY, 10);  // Set the hour of the day (24-hour format)
        calendar.set(Calendar.MINUTE, 21);       // Set the minute
        calendar.set(Calendar.SECOND, 10);        // Set the second

        // Create an intent to the BroadcastReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Set the exact alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        // Indicate that the work finished successfully
        return Result.success();
    }
}
