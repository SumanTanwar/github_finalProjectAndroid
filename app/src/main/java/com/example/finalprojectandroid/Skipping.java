package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

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

public class Skipping extends AppCompatActivity {


    TextView tvTimer;
    Button btnMainSkip, btnStart, btnPause, btnReset,
            btnSave,btnCalculate;
    EditText caloriesBurned;

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

        btnMainSkip =findViewById(R.id.btnMainSkip);

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
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caloryBurntString = caloriesBurned.getText().toString();
                double caloryBurnt = 0.0;

                try {
                    caloryBurnt = Double.parseDouble(caloryBurntString);
                } catch (NumberFormatException e) {

                    Toast.makeText(Skipping.this, "Invalid or No value...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (caloryBurnt != 0) {
                    new AlertDialog.Builder(Skipping.this)
                            .setTitle("Calories Saved")
                            .setMessage("Your number of burnt calories are saved in the database for the weekly report.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    caloriesBurned.setText("");
                                }
                            })
                            .show();
                }
            }
        });



        btnMainSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Skipping.this,Main.class);
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

    private void calculateAndDisplayCalories()
    {
        if (tvTimer.getText().toString().equals("00:00:00")) {
            // Set the caloriesBurned EditText to empty
            caloriesBurned.setText("");
            Toast.makeText(Skipping.this, "Timer is zero. No calories burned.", Toast.LENGTH_SHORT).show();
            return;
        }

        double MET = 6.0; // MET value for swimming (moderate pace)
        double weight = 70.0; // User weight in kg (you might want to get this from user input)

        // Calculate time in hours
        double timeInHours = updateTime / 3600000.0;

        // Calculate calories burned
        double caloriesBurnedValue = MET * weight * timeInHours;

        // Display the result in the EditText
        caloriesBurned.setText(String.format("%.2f", caloriesBurnedValue));

        // Reset the timer and buttons
        tvTimer.setText("00:00:00");
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
        handler.removeCallbacks(updateTimerThread);
    }

        @Override
        public void onBackPressed()
        {

        }
}