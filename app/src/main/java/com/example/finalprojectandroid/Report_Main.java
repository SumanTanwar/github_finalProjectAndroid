package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Report_Main extends AppCompatActivity {
    private static final String TAG = "ReportMainActivity";
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();


        Log.d(TAG, "User: " + user);

        if (user == null) {

            Log.d(TAG, "User is not authenticated, navigating to login.");
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
            finish();
        }
    }
}
