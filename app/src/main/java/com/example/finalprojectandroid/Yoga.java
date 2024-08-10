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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.HashMap;

public class Yoga extends AppCompatActivity {

    CheckBox checkBoxMeditation, checkBoxSuryaNamaskar, checkBoxTreePose, checkBoxCobraPose, checkBoxShukhasana, checkBoxShabasana;
    EditText editTextCalories;
    Button buttonCalculate, buttonMain, btnSave;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoga);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Yoga calories");

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
                String caloryBurntString = editTextCalories.getText().toString();
                double caloryBurnt = 0.0;

                if (caloryBurntString.isEmpty()) {
                    Toast.makeText(Yoga.this, "No calories burnt...", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    caloryBurnt = Double.parseDouble(caloryBurntString);
                } catch (NumberFormatException e) {
                    Toast.makeText(Yoga.this, "Invalid value...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (caloryBurnt > 0) {
                    // Get current date and time
                    long currentTimeMillis = System.currentTimeMillis();
                    String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd  &  HH:mm", java.util.Locale.getDefault()).format(new java.util.Date(currentTimeMillis));

                    // Use Firebase Authentication to get the user ID
                    String userId = mAuth.getCurrentUser().getUid();

                    // Create a unique ID for the entry
                    String id = databaseReference.push().getKey();

                    // Create a map to hold the data
                    Map<String, Object> data = new HashMap<>();
                    data.put("calories ", caloryBurnt);
                    data.put("date & Time ", dateTime);

                    // Save data to Firebase
                    databaseReference.child(userId).child(id).setValue(data);

                    new AlertDialog.Builder(Yoga.this)
                            .setTitle("Calories Saved")
                            .setMessage("Your number of burnt calories and the date and time are saved in the database for the weekly report.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editTextCalories.setText("");
                                }
                            })
                            .show();
                }
                else{
                    Toast.makeText(Yoga.this, "No calories burnt...", Toast.LENGTH_SHORT).show();
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
            caloriesBurned += 2; // Example value for Meditation
        }
        if (checkBoxSuryaNamaskar.isChecked()) {
            caloriesBurned += 20; // Example value for Surya Namaskar
        }
        if (checkBoxTreePose.isChecked()) {
            caloriesBurned += 10; // Example value for Tree Pose
        }
        if (checkBoxCobraPose.isChecked()) {
            caloriesBurned += 15; // Example value for Cobra Pose
        }
        if (checkBoxShukhasana.isChecked()) {
            caloriesBurned += 5; // Example value for Shukhasana
        }
        if (checkBoxShabasana.isChecked()) {
            caloriesBurned += 1; // Example value for Shabasana
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
