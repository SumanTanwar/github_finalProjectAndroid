package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.HashMap;

public class Skipping extends AppCompatActivity {

    TextView tvTimer;
    Button btnMainSkip, btnStart, btnPause, btnReset, btnSave, btnCalculate;
    EditText caloriesBurned;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    Handler handler = new Handler();
    long startTime = 0L;
    long timeInMillis = 0L;
    long timeSwapBuff = 0L;
    long updateTime = 0L;

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMillis = System.currentTimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMillis;
            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            int hrs = mins / 60;
            secs = secs % 60;
            tvTimer.setText(String.format("%02d:%02d:%02d", hrs, mins, secs));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skipping);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Skipping calories");

        btnMainSkip = findViewById(R.id.btnMainSkip);
        tvTimer = findViewById(R.id.timerSkip);
        btnStart = findViewById(R.id.btnStartSkip);
        btnPause = findViewById(R.id.btnPauseSkip);
        btnReset = findViewById(R.id.btnResetSkip);
        btnSave = findViewById(R.id.btnSaveCalories);
        btnCalculate = findViewById(R.id.btnCalculateSkip);
        caloriesBurned = findViewById(R.id.caloriesBurned);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnReset.setEnabled(true);
                caloriesBurned.setText("");
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMillis;
                handler.removeCallbacks(updateTimerThread);
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeInMillis = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                tvTimer.setText("00:00:00");
                handler.removeCallbacks(updateTimerThread);
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnReset.setEnabled(false);
                caloriesBurned.setText("");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caloryBurntString = caloriesBurned.getText().toString();
                double caloryBurnt = 0.0;

                if (caloryBurntString.isEmpty()) {
                    Toast.makeText(Skipping.this, "Please enter a value...", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    caloryBurnt = Double.parseDouble(caloryBurntString);
                } catch (NumberFormatException e) {
                    Toast.makeText(Skipping.this, "Invalid value...", Toast.LENGTH_SHORT).show();
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
                    data.put("calories", caloryBurnt);
                    data.put("date  &  time", dateTime);

                    // Save data to Firebase
                    databaseReference.child(userId).child(id).setValue(data);

                    new AlertDialog.Builder(Skipping.this)
                            .setTitle("Calories Saved")
                            .setMessage("Your number of burnt calories and the date are saved in the database for the weekly report.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    caloriesBurned.setText("");
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(Skipping.this, "Calories burned must be greater than zero.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMainSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Skipping.this, Main.class);
                startActivity(intent);
                finish();
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayCalories();
            }
        });
    }

    private void calculateAndDisplayCalories() {
        if (tvTimer.getText().toString().equals("00:00:00")) {
            caloriesBurned.setText("");
            Toast.makeText(Skipping.this, "Timer is zero. No calories burned.", Toast.LENGTH_SHORT).show();
            return;
        }

        double MET = 12.3; // MET value for skipping
        double weight = 70.0; // User weight in kg

        // Calculate time in hours
        double timeInHours = updateTime / 3600000.0;

        // Calculate calories burned
        double caloriesBurnedValue = MET * weight * timeInHours;

        // Display the result in the EditText
        caloriesBurned.setText(String.format("%.2f", caloriesBurnedValue));
        tvTimer.setText("00:00:00");
        handler.removeCallbacks(updateTimerThread);
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        // Add any functionality for when the back button is pressed, if needed.
    }
}
