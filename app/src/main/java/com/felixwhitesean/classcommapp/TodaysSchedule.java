package com.felixwhitesean.classcommapp;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TodaysSchedule extends Fragment {
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays_schedule, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String data = bundle.getString("schedules");  // Retrieve the data using the same key
            if (data != null) {
                try {
                    // Convert the JSON string back to JSONArray
                    JSONArray schedulesArray = new JSONArray(data);
                    // Dynamically create views based on the contents of the JSON array
                    createViewsBasedOnJSONArray(schedulesArray, view);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return view;
    }
    private void createViewsBasedOnJSONArray(JSONArray schedulesArray, View view) {
        LinearLayout containerLayout = view.findViewById(R.id.container);
        containerLayout.setOrientation(LinearLayout.VERTICAL);


        // Loop through the JSONArray and create views based on the details
        for (int i = 0; i < schedulesArray.length(); i++) {
            try {
                JSONObject session = schedulesArray.getJSONObject(i);
                String unit = session.getString("Unit");
                String lecturer = session.getString("Lecturer");
                LinearLayout linearLayout = getLinearLayout(unit, lecturer);
                containerLayout.addView(linearLayout);

                LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  // equivalent to android:layout_width="match_parent"
                LinearLayout.LayoutParams.WRAP_CONTENT   // equivalent to android:layout_height="wrap_content"
                );
                int marginInDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
                layoutParam.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
                linearLayout.setBackgroundResource(R.drawable.lecturer_dashboard_containers);

                // Set padding (equivalent to android:padding="10dp")
                int paddingInDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                linearLayout.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);
                linearLayout.setLayoutParams(layoutParam);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private @NonNull LinearLayout getLinearLayout(String unit, String lecturer) {
        LinearLayout linearLayout = new LinearLayout(getContext());

        TextView sessionTextView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        sessionTextView.setLayoutParams(layoutParams);
        sessionTextView.setText(unit);
        sessionTextView.setTextColor(Color.parseColor("#0964AD"));
        sessionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        sessionTextView.setTypeface(null, Typeface.BOLD);
        linearLayout.addView(sessionTextView);

        TextView lecturerTextView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  // equivalent to android:layout_width="match_parent"
                LinearLayout.LayoutParams.WRAP_CONTENT   // equivalent to android:layout_height="wrap_content"
        );
        lecturerTextView.setLayoutParams(layoutParams2);
        lecturerTextView.setTextColor(Color.parseColor("#042948"));
        lecturerTextView.setTypeface(null, Typeface.ITALIC);
        lecturerTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        lecturerTextView.setText(lecturer);

        // Add the TextView to the layout
        linearLayout.addView(lecturerTextView);
        return linearLayout;
    }
}