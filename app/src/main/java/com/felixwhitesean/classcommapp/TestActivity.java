package com.felixwhitesean.classcommapp;//package com.felixwhitesean.classcommapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private FirebaseFirestore db;
    private TextView textView;
    private Button writeButton;
    private Button readButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        textView = findViewById(R.id.textView);
        writeButton = findViewById(R.id.writeButton);
        readButton = findViewById(R.id.readButton);

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToFirestore();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromFirestore();
            }
        });
    }

    private void writeToFirestore() {
        Map<String, Object> testData = new HashMap<>();
        testData.put("testField", "Hello, Firestore!");

        db.collection("testCollection").document("testDocument")
                .set(testData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    Toast.makeText(TestActivity.this, "Write successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    Toast.makeText(TestActivity.this, "Write failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void readFromFirestore() {
        db.collection("testCollection").document("testDocument")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                textView.setText(document.getString("testField"));
                                Toast.makeText(TestActivity.this, "Read successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "No such document");
                                textView.setText("No such document");
                                Toast.makeText(TestActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            textView.setText("Read failed");
                            Toast.makeText(TestActivity.this, "Read failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
