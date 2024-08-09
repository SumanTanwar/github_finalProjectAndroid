package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Yoga extends AppCompatActivity {

     CheckBox checkBoxMeditation, checkBoxSuryaNamaskar, checkBoxTreePose, checkBoxCobraPose, checkBoxShukhasana, checkBoxShabasana;
     EditText editTextCalories;
     Button buttonCalculate, buttonMain, btnSave;

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
                    Toast.makeText(Yoga.this, "Invalid or No value...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (caloryBurnt != 0) {
                    new AlertDialog.Builder(Yoga.this)
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

                checkBoxMeditation.setChecked(false);
                checkBoxSuryaNamaskar.setChecked(false);
                checkBoxTreePose.setChecked(false);
                checkBoxCobraPose.setChecked(false);
                checkBoxShukhasana.setChecked(false);
                checkBoxShabasana.setChecked(false);
            }
        });



        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Yoga.this, Main.class);
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


        checkBoxMeditation.setChecked(false);
        checkBoxSuryaNamaskar.setChecked(false);
        checkBoxTreePose.setChecked(false);
        checkBoxCobraPose.setChecked(false);
        checkBoxShukhasana.setChecked(false);
        checkBoxShabasana.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        //  you are an idiot
    }

}
