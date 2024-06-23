package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    Button btnRunning,btnSkipping, btnSwimming,
            btnCycling,btnExcercise,btnyoga,btnWeeklyReport;
    TextView txtSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRunning =findViewById(R.id.running);
        btnSkipping =findViewById(R.id.skipping);
        btnSwimming =findViewById(R.id.swimming);
        btnCycling =findViewById(R.id.cycling);
        btnExcercise =findViewById(R.id.excercise);
        btnyoga =findViewById(R.id.yoga);
        btnWeeklyReport =findViewById(R.id.weeklyreport);
        txtSignOut =findViewById(R.id.signout);


        btnRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,RunningActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnSkipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,Skipping.class);
                startActivity(intent);
                finish();
            }
        });
        btnSwimming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,Swimming.class);
                startActivity(intent);
                finish();
            }
        });

        btnCycling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,Cycling.class);
                startActivity(intent);
                finish();
            }
        });
        btnExcercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,Exercise.class);
                startActivity(intent);
                finish();
            }
        });
        btnyoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,Yoga.class);
                startActivity(intent);
                finish();
            }
        });
        btnWeeklyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,Report.class);
                startActivity(intent);
                finish();
            }
        });
        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this,login.class);
                startActivity(intent);
                finish();
            }
        });


    }
}