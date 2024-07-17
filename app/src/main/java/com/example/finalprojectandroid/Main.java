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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Main extends AppCompatActivity {

    Button btnRunning, btnSkipping, btnSwimming,
            btnCycling, btnExercise, btnYoga,
            btnWeeklyReport;
    FirebaseAuth auth;
    TextView textView, txtSignOut, txtMyProfile;
    FirebaseUser user;
    DatabaseReference mDatabase; // Declare DatabaseReference at class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize mDatabase here
        user = auth.getCurrentUser();

        txtMyProfile = findViewById(R.id.my_profile);
        textView = findViewById(R.id.user_details);
        btnWeeklyReport = findViewById(R.id.weeklyreport);
        txtSignOut = findViewById(R.id.signout);

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
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);
                        String password = dataSnapshot.child("password").getValue(String.class);

                        textView.setText("Welcome " + name);

                        txtMyProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Main.this, MyProfile.class);
                                intent.putExtra("name", name);
                                intent.putExtra("email", email);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        textView.setText("User");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Main.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Set onClick listener for btnWeeklyReport
        btnWeeklyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Report_Main.class);
                startActivity(intent);
            }
        });

        // Set onClick listener for txtSignOut
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
        // Handle back press if needed
    }
}
