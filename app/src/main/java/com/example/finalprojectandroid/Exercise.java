package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Exercise extends AppCompatActivity {

     CheckBox checkBoxPushUps, checkBoxSitUps, checkBoxPullUps, checkBoxPlank, checkBoxSquat;
     EditText editTextCalories;
     Button buttonCalculate, buttonMain,btnSave;

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
        btnSave = findViewById(R.id.btnSaveCalories);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caloryBurntString = editTextCalories.getText().toString();  // Updated variable
                double caloryBurnt = 0.0;

                try {
                    caloryBurnt = Double.parseDouble(caloryBurntString);
                } catch (NumberFormatException e) {
                    Toast.makeText(Exercise.this, "Invalid or No value...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (caloryBurnt != 0) {
                    new AlertDialog.Builder(Exercise.this)
                            .setTitle("Calories Saved")
                            .setMessage("Your number of burnt calories are saved in the database for the weekly report.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editTextCalories.setText("");  // Updated variable
                                }
                            })
                            .show();
                }

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

        checkBoxPushUps.setChecked(false);
        checkBoxSitUps.setChecked(false);
        checkBoxPullUps.setChecked(false);
        checkBoxPlank.setChecked(false);
        checkBoxSquat.setChecked(false);

        editTextCalories.setText(String.valueOf(caloriesBurned));

    }

    @Override
    public void onBackPressed() {
        // Prevent back button functionality
    }

}