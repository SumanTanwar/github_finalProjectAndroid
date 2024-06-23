package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class welcome extends AppCompatActivity {

    private  static  final  int splashMeOut = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(welcome.this,login.class);
                startActivity(intent);
                finish();
            }
        },splashMeOut);
    }
}