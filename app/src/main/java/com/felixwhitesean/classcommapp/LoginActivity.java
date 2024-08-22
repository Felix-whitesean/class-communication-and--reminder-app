package com.felixwhitesean.classcommapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private TextView Ultimate, signUpRedirect;
    private Button Login ;
    private EditText email,Password;

    public static final String PREFS_NAME = "UserLoginPrefs";
    public static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String CURRENT_USER_ID = "CurrentUserId";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),GeneralDashboard.class);
            startActivity(intent);
            finish(); // Close LoginActivity after redirecting
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth= FirebaseAuth.getInstance();
        Login= findViewById(R.id.loginbtn);
        email= findViewById(R.id.Email);
        Password= findViewById(R.id.password);
        signUpRedirect = findViewById(R.id.signup_here_redirect);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email,password;
                Email= email.getText().toString();
                password= Password.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(LoginActivity.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(Email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Save login state
                                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if(currentUser != null) {
                                        String userUID = currentUser.getUid();
                                        Toast.makeText(LoginActivity.this, userUID, Toast.LENGTH_SHORT).show();
                                        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putBoolean(IS_LOGGED_IN, true);
                                        editor.putString(CURRENT_USER_ID, userUID);
                                        editor.apply();
                                    }
                                    Toast.makeText(LoginActivity.this, "Login Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), GeneralDashboard.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
        signUpRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}