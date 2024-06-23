package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Exercise extends AppCompatActivity {

    private CheckBox checkBoxPushUps, checkBoxSitUps, checkBoxPullUps, checkBoxPlank, checkBoxSquat;
    private EditText editTextCalories;
    private Button buttonCalculate, buttonMain,btnExcerciseReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);


        checkBoxPushUps = findViewById(R.id.checkBoxPushUps);
        checkBoxSitUps = findViewById(R.id.checkBoxSitUps);
        checkBoxPullUps = findViewById(R.id.checkBoxPullUps);
        checkBoxPlank = findViewById(R.id.checkBoxPlank);
        checkBoxSquat = findViewById(R.id.checkBoxSquat);
        editTextCalories = findViewById(R.id.editTextCalories);
        buttonCalculate = findViewById(R.id.caloriesCalculate);
        buttonMain = findViewById(R.id.buttonMain);
        btnExcerciseReset = findViewById(R.id.buttonResetExcercise);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
        btnExcerciseReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxPushUps.setChecked(false);
                checkBoxSitUps.setChecked(false);
                checkBoxPullUps.setChecked(false);
                checkBoxPlank.setChecked(false);
                checkBoxSquat.setChecked(false);
                editTextCalories.setText("");
            }
        });


        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Exercise.this,Main.class);
                    startActivity(intent);
                    finish();
                }
        });
    }

    private void calculateCalories() {
        int caloriesBurned = 0;

        if (checkBoxPushUps.isChecked()) {
            caloriesBurned += 100; // Example value for Push Ups
        }
        if (checkBoxSitUps.isChecked()) {
            caloriesBurned += 50; // Example value for Sit Ups
        }
        if (checkBoxPullUps.isChecked()) {
            caloriesBurned += 120; // Example value for Pull Ups
        }
        if (checkBoxPlank.isChecked()) {
            caloriesBurned += 80; // Example value for Plank
        }
        if (checkBoxSquat.isChecked()) {
            caloriesBurned += 90; // Example value for Squat
        }

        editTextCalories.setText(String.valueOf(caloriesBurned));

    }
}