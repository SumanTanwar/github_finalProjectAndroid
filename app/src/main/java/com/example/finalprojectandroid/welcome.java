package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcome extends AppCompatActivity {

    private static final int splashMeOut = 3000;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in, go to main activity
                    Intent intent = new Intent(welcome.this, Main.class);
                    startActivity(intent);
                } else {
                    // No user is signed in, go to login activity
                    Intent intent = new Intent(welcome.this, login.class);
                    startActivity(intent);
                }
                finish();
            }
        }, splashMeOut);
    }
}
