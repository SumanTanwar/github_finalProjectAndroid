package com.example.finalprojectandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Exercise extends AppCompatActivity {

    CheckBox checkBoxPushUps, checkBoxSitUps, checkBoxPullUps, checkBoxPlank, checkBoxSquat;
    EditText editTextCalories;
    Button buttonCalculate, buttonMain, btnSave;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Exercise calories"); // Fixed typo

        // Initialize views
        checkBoxPushUps = findViewById(R.id.checkBoxPushUps);
        checkBoxSitUps = findViewById(R.id.checkBoxSitUps);
        checkBoxPullUps = findViewById(R.id.checkBoxPullUps);
        checkBoxPlank = findViewById(R.id.checkBoxPlank);
        checkBoxSquat = findViewById(R.id.checkBoxSquat);

        editTextCalories = findViewById(R.id.editTextCalories);

        buttonCalculate = findViewById(R.id.caloriesCalculate);
        buttonMain = findViewById(R.id.buttonMain);
        btnSave = findViewById(R.id.btnSaveCalories);

        // Set up listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCalories();
            }
        });

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Exercise.this, Main.class);
                startActivity(intent);
                finish();
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCalories();
            }
        });
    }

    private void saveCalories() {
        String caloryBurntString = editTextCalories.getText().toString();
        double caloryBurnt = 0.0;

        if (caloryBurntString.isEmpty()) {
            Toast.makeText(Exercise.this, "No calories burnt...", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            caloryBurnt = Double.parseDouble(caloryBurntString);
        } catch (NumberFormatException e) {
            Toast.makeText(Exercise.this, "Invalid value...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (caloryBurnt > 0) {
            // Get current date and time
            long currentTimeMillis = System.currentTimeMillis();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd & HH:mm", Locale.getDefault()).format(new Date(currentTimeMillis));

            // Use Firebase Authentication to get the user ID
            String userId = mAuth.getCurrentUser().getUid();

            // Create a unique ID for the entry
            String id = databaseReference.push().getKey();

            // Create a map to hold the data
            Map<String, Object> data = new HashMap<>();
            data.put("calories", caloryBurnt); // Fixed field name
            data.put("date  &  time", dateTime);    // Fixed field name

            // Save data to Firebase
            databaseReference.child(userId).child(id).setValue(data);

            new AlertDialog.Builder(Exercise.this)
                    .setTitle("Calories Saved")
                    .setMessage("Your number of burnt calories and the date and time are saved in the database for the weekly report.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editTextCalories.setText("");
                        }
                    })
                    .show();
        } else {
            Toast.makeText(Exercise.this, "No calories burnt...", Toast.LENGTH_SHORT).show();
        }

        // Reset checkboxes
        checkBoxPushUps.setChecked(false);
        checkBoxSitUps.setChecked(false);
        checkBoxPullUps.setChecked(false);
        checkBoxPlank.setChecked(false);
        checkBoxSquat.setChecked(false);
    }

    private void calculateCalories() {
        int caloriesBurned = 0;

        if (checkBoxPushUps.isChecked()) {
            caloriesBurned += 100;
        }
        if (checkBoxSitUps.isChecked()) {
            caloriesBurned += 50;
        }
        if (checkBoxPullUps.isChecked()) {
            caloriesBurned += 120;
        }
        if (checkBoxPlank.isChecked()) {
            caloriesBurned += 80;
        }
        if (checkBoxSquat.isChecked()) {
            caloriesBurned += 90;
        }

        // Display calories burned
        editTextCalories.setText(String.valueOf(caloriesBurned));

        // Reset checkboxes
        checkBoxPushUps.setChecked(false);
        checkBoxSitUps.setChecked(false);
        checkBoxPullUps.setChecked(false);
        checkBoxPlank.setChecked(false);
        checkBoxSquat.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        // Handle back press if necessary
    }
}
