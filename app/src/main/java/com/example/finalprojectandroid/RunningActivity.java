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

public class RunningActivity extends AppCompatActivity {

    private Button btnMainRun, btnCalculate, btnSave;
    private TextView tvTimer;
    private Button btnStart, btnPause, btnReset;
    private EditText caloriesBurned;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long timeInMillis = 0L;
    private long timeSwapBuff = 0L;
    private long updateTime = 0L;

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
        setContentView(R.layout.activity_running);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Running calories");

        // Initialize Views
        btnMainRun = findViewById(R.id.btnMainRun);
        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        caloriesBurned = findViewById(R.id.caloriesBurned);
        btnSave = findViewById(R.id.btnSaveCalories);
        btnCalculate = findViewById(R.id.btnCalculateRun);

        // Start button listener
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

        // Pause button listener
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMillis;
                handler.removeCallbacks(updateTimerThread);
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
            }
        });

        // Reset button listener
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        // Save button listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCalorieData();
            }
        });

        // Calculate button listener
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayCalories();
            }
        });

        // Main button listener
        btnMainRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunningActivity.this, Main.class);
                startActivity(intent);
                finish();
            }
        });

        // Initially disable Pause and Reset buttons
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
    }

    private void resetTimer() {
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


    private void calculateAndDisplayCalories() {
        if (tvTimer.getText().toString().equals("00:00:00")) {
            caloriesBurned.setText("");
            Toast.makeText(RunningActivity.this, "Timer is zero. No calories burned.", Toast.LENGTH_SHORT).show();
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


    private void saveCalorieData() {
        String calorieBurntString = caloriesBurned.getText().toString();
        double calorieBurnt = 0.0;

        if (calorieBurntString.isEmpty()) {
            Toast.makeText(RunningActivity.this, "Please enter a value...", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            calorieBurnt = Double.parseDouble(calorieBurntString);
        } catch (NumberFormatException e) {
            Toast.makeText(RunningActivity.this, "Invalid value...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (calorieBurnt > 0) {
            String dateTime = new java.text.SimpleDateFormat("yyyy-MM-dd  &  HH:mm", java.util.Locale.getDefault()).format(new java.util.Date());

            String userId = mAuth.getCurrentUser().getUid();
            String id = databaseReference.push().getKey();

            Map<String, Object> data = new HashMap<>();
            data.put("calories", calorieBurnt);
            data.put("date  &  time", dateTime);

            databaseReference.child(userId).child(id).setValue(data);

            new AlertDialog.Builder(RunningActivity.this)
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
            Toast.makeText(RunningActivity.this, "Calories burned must be greater than zero.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
