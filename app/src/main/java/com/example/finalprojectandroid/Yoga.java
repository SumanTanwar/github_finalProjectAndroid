package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class Yoga extends AppCompatActivity {

    private CheckBox checkBoxMeditation, checkBoxSuryaNamaskar, checkBoxTreePose, checkBoxCobraPose, checkBoxShukhasana, checkBoxShabasana;
    private EditText editTextCalories;
    private Button buttonCalculate, buttonMain,btnReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

        checkBoxMeditation = findViewById(R.id.checkBoxMeditation);
        checkBoxSuryaNamaskar = findViewById(R.id.checkBoxSuryaNamaskar);
        checkBoxTreePose = findViewById(R.id.checkBoxTreePose);
        checkBoxCobraPose = findViewById(R.id.checkBoxCobraPose);
        checkBoxShukhasana = findViewById(R.id.checkBoxShukhasana);
        checkBoxShabasana = findViewById(R.id.checkBoxShabasana);
        editTextCalories = findViewById(R.id.editTextCaloriesYoga);
        buttonCalculate = findViewById(R.id.caloriesCalculateYoga);
        buttonMain = findViewById(R.id.buttonMainYoga);
        btnReset  = findViewById(R.id.buttonResetYoga);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxMeditation.setChecked(false);
                checkBoxSuryaNamaskar.setChecked(false);
                checkBoxTreePose.setChecked(false);
                checkBoxCobraPose.setChecked(false);
                checkBoxShukhasana.setChecked(false);
                checkBoxShabasana.setChecked(false);
                editTextCalories.setText("");
            }
        });

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Yoga.this,Main.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void calculateCalories() {
        int caloriesBurned = 0;

        if (checkBoxMeditation.isChecked()) {
            caloriesBurned += 20; // Example value for Meditation
        }
        if (checkBoxSuryaNamaskar.isChecked()) {
            caloriesBurned += 50; // Example value for Surya Namaskar
        }
        if (checkBoxTreePose.isChecked()) {
            caloriesBurned += 30; // Example value for Tree Pose
        }
        if (checkBoxCobraPose.isChecked()) {
            caloriesBurned += 25; // Example value for Cobra Pose
        }
        if (checkBoxShukhasana.isChecked()) {
            caloriesBurned += 15; // Example value for Shukhasana
        }
        if (checkBoxShabasana.isChecked()) {
            caloriesBurned += 10; // Example value for Shabasana
        }

        editTextCalories.setText(String.valueOf(caloriesBurned));

    }
}