package com.felixwhitesean.classcommapp;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkManager;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class UpcomingNotifications extends Fragment {
    TextView tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_upcoming_notifications, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("Notifications");  // Retrieve the data using the same key
            if (data != null) {
                try {
                    JSONArray notificationsArray = new JSONArray(data);
                    // Dynamically create views based on the contents of the JSON array
                    createViewsBasedOnJSONArray(notificationsArray, view);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getContext(), "No bundle found", Toast.LENGTH_SHORT).show();
        }
//        Toast.makeText(getContext(), "Finished", Toast.LENGTH_SHORT).show();
        return view;
    }
    private void createViewsBasedOnJSONArray(JSONArray notificationsArray, View view) {
        LinearLayout containerLayout = view.findViewById(R.id.container);
        containerLayout.setOrientation(LinearLayout.VERTICAL);


        // Loop through the JSONArray and create views based on the details
        for (int i = 0; i < notificationsArray.length(); i++) {
            try {
                JSONObject session = notificationsArray.getJSONObject(i);
                String period = session.getString("Period");
                boolean alarmStatus = session.getBoolean("Alarm");
                String unit = session.getString("Unit");
                String lecturer = session.getString("Lecturer");

                LinearLayout linearLayout = getLinearLayout(unit, lecturer, period, alarmStatus);
                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,  // equivalent to android:layout_width="match_parent"
                        LinearLayout.LayoutParams.WRAP_CONTENT   // equivalent to android:layout_height="wrap_content"
                );
                int marginInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                layoutParam.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);

                // Set padding (equivalent to android:padding="10dp")
                int paddingInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                linearLayout.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);
                linearLayout.setLayoutParams(layoutParam);

                if(alarmStatus){
                    linearLayout.setBackgroundResource(R.drawable.rectangle_19_ek1_shape);
                }
                else{
                    linearLayout.setBackgroundResource(R.drawable.rectangle_19_shape);
                }
                containerLayout.addView(linearLayout);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private @NonNull LinearLayout getLinearLayout(String unit, String lecturer, String period, Boolean alarmStatus) {
        LinearLayout linearLayout = new LinearLayout(getContext());

        LinearLayout lecturerLayout = getLecturerLinearLayout(lecturer);
        LinearLayout unitDetails = getUnitDetailsLayout(alarmStatus, unit, period);
        RelativeLayout alarmIndicator = setAlarmIndicator(alarmStatus);

        // Add the TextView to the layout
        linearLayout.addView(lecturerLayout);
        linearLayout.addView(unitDetails);
        linearLayout.addView(alarmIndicator);
        return linearLayout;
    }
    private @NonNull LinearLayout getLecturerLinearLayout(String lecturer) {
        LinearLayout lecturerLayout = new LinearLayout(getContext());
        lecturerLayout.setOrientation(LinearLayout.VERTICAL);

        //Ellipse params
        View ellipse = new View(getContext());
        ellipse.setBackgroundResource(R.drawable.ellipse_25_shape);
        int widthInDp = 100;
        int heightInDp = 100;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthInDp, heightInDp);
        params.setMargins(0, 0, 0, 0);
        ellipse.setLayoutParams(params);

        //TextView params
        TextView textView = new TextView(getContext()); // Replace 'context' with your actual context
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12); // Set text size to 12sp
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.rectangle_3_color));
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,  // Width: wrap_content
                dpToPx(34, getContext())                 // Height: 34dp converted to pixels
        );
        params.topMargin = dpToPx(8, getContext());
        params.bottomMargin = dpToPx(10, getContext());

        // Apply LayoutParams to the TextView
        textView.setLayoutParams(params2);
        textView.setText(lecturer);

        // Helper function to convert dp to px
        lecturerLayout.addView(ellipse);
        lecturerLayout.addView(textView);
        return lecturerLayout;
    }
    public int dpToPx(int dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    public @NonNull LinearLayout getUnitDetailsLayout(boolean alarmStatus, String unit, String period){
        LinearLayout unitDetailsLinearLayout = new LinearLayout(getContext());
        unitDetailsLinearLayout.setOrientation(LinearLayout.VERTICAL);

        //Universal variables
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 0, 0);

        //Unit name
        TextView unitName = new TextView(getContext());
        unitName.setLayoutParams(params);
        unitName.setTextColor(Color.parseColor("#0964AD"));
        unitName.setText(unit);

        //Duration
        TextView time_period = new TextView(getContext());
        time_period.setLayoutParams(params);
        time_period.setTextColor(Color.parseColor("#0964AD"));
        time_period.setText(period);

        unitDetailsLinearLayout.addView(unitName);
        unitDetailsLinearLayout.addView(time_period);
        //Alarm time
        if(alarmStatus) {
            TextView alarm_duration = new TextView(getContext());
            alarm_duration.setText(period);
            String time = period.substring(0, 4);

            TextView timeTv = new TextView(getContext());
            timeTv.setLayoutParams(params);
            SimpleDateFormat inputFormat = new SimpleDateFormat("HHmm");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

            try {
                // Parse the time and convert it to the desired format
                Date classTime = inputFormat.parse(time);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(classTime);
                calendar.add(Calendar.HOUR_OF_DAY, -1);  // Subtract 1 hour

                // Get the new formatted time
                String formattedTime = outputFormat.format(calendar.getTime());
                timeTv.setText(formattedTime);

                Data data = new Data.Builder().putString("nextAlarm", formattedTime).build();
                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class).setInputData(data).setExpedited(
                        OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).build();
                UUID workRequestId = oneTimeWorkRequest.getId();
                WorkManager.getInstance(getContext()).enqueue(oneTimeWorkRequest);

                WorkManager.getInstance(getContext())
                        .getWorkInfoByIdLiveData(workRequestId)
                        .observe(getViewLifecycleOwner(), workInfo -> {
                            if (workInfo != null && workInfo.getState().isFinished()) {
                                Toast.makeText(getContext(), "Alarm work setting finished", Toast.LENGTH_SHORT).show();
                            }
                        });

                timeTv.setTextColor(Color.parseColor("#0964AD"));
                unitDetailsLinearLayout.addView(timeTv);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return unitDetailsLinearLayout;
    }
    public @NonNull RelativeLayout setAlarmIndicator(boolean alarmStatus){
        RelativeLayout alarmIndicatorView = new RelativeLayout(getContext());
        ImageView alarmIndicator = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, // Width is wrap_content
                RelativeLayout.LayoutParams.WRAP_CONTENT                  // Height in dp (34dp converted to pixels)
        );
//        params.leftMargin = dpToPx(18, getContext());   // Top margin 8dp
        params.rightMargin = dpToPx(15, getContext());
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        alarmIndicator.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, // Width is wrap_content
                ViewGroup.LayoutParams.MATCH_PARENT                  // Height in dp (34dp converted to pixels)
        );

        alarmIndicatorView.setLayoutParams(params1);
        if(alarmStatus){
            alarmIndicator.setImageResource(R.drawable.icon_alarm_check);
        }
        else{
            alarmIndicator.setImageResource(R.drawable.icon_alarm_check_red);
        }
        alarmIndicatorView.addView(alarmIndicator);

        return alarmIndicatorView;
    }
}