package com.felixwhitesean.classcommapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {
//    public void onStart(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.user_info);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.landing_page);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Button getStartedButton = findViewById(R.id.rectangle_28);
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("acknowledgment","Getting started button clicked");
                // Navigate to authentication_page_activity
                Intent intent = new Intent(MainActivity.this, userInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
