package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity {

    Button btnRunning, btnSkipping, btnSwimming, btnCycling, btnExcercise, btnYoga, btnWeeklyReport;
    FirebaseAuth auth;
    TextView textView, txtSignOut;
    FirebaseUser user;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        txtSignOut = findViewById(R.id.signout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        } else {
            // Fetch the user's name from the database
            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        textView.setText("Welcome  " + name);
                    } else {
                        textView.setText("User");
                    }
                }

                @Override
                public void onCancelled( DatabaseError databaseError) {
                    Toast.makeText(Main.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnRunning = findViewById(R.id.running);
        btnSkipping = findViewById(R.id.skipping);
        btnSwimming = findViewById(R.id.swimming);
        btnCycling = findViewById(R.id.cycling);
        btnExcercise = findViewById(R.id.excercise);
        btnYoga = findViewById(R.id.yoga);
        btnWeeklyReport = findViewById(R.id.weeklyreport);

        btnRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, RunningActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSkipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Skipping.class);
                startActivity(intent);
                finish();
            }
        });
        btnSwimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Swimming.class);
                startActivity(intent);
                finish();
            }
        });

        btnCycling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Cycling.class);
                startActivity(intent);
                finish();
            }
        });
        btnExcercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Exercise.class);
                startActivity(intent);
                finish();
            }
        });
        btnYoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Yoga.class);
                startActivity(intent);
                finish();
            }
        });
        btnWeeklyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Report.class);
                startActivity(intent);
                finish();
            }
        });
        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
