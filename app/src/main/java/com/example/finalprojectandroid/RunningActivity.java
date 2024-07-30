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

public class RunningActivity extends AppCompatActivity {

    Button btnMainRun, btnCalulate,btnSave;
    TextView tvTimer;
    Button btnStart, btnPause, btnReset;
    EditText caloriedBurned;

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
        setContentView(R.layout.activity_running);

        btnMainRun = findViewById(R.id.btnMainRun);
        caloriedBurned = findViewById(R.id.caloriesBurned);
        btnCalulate = findViewById(R.id.btnCalculateRun);
        btnSave = findViewById(R.id.btnSaveCalories);

        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnReset.setEnabled(true);
                caloriedBurned.setText("");
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
                caloriedBurned.setText("");

            }
        });

        btnPause.setEnabled(false);
        btnReset.setEnabled(false);

        btnMainRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RunningActivity.this, Main.class);
                startActivity(intent);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caloryBurntString = caloriedBurned.getText().toString();
                double caloryBurnt = 0.0;

                try {
                    caloryBurnt = Double.parseDouble(caloryBurntString);
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid number
                    Toast.makeText(RunningActivity.this, "Invalid or No value...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (caloryBurnt != 0) {
                    new AlertDialog.Builder(RunningActivity.this)
                            .setTitle("Calories Saved")
                            .setMessage("Your number of burnt calories are saved in the database for the weekly report.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    caloriedBurned.setText("");
                                }
                            })
                            .show();
                }
            }
        });



        btnCalulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayCalories();

            }
        });
    }

    private void calculateAndDisplayCalories() {
        double MET = 8.0; // MET value for running (moderate pace)
        double weight = 70.0; // User weight in kg (you might want to get this from user input)

        tvTimer.setText("00:00:00");
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
        handler.removeCallbacks(updateTimerThread);

        // Calculate time in hours
        double timeInHours = updateTime / 3600000.0;

        // Calculate calories burned
        double caloriesBurned = MET * weight * timeInHours;

        // Display the result in the EditText
        caloriedBurned.setText(String.format("%.2f", caloriesBurned));


    }
    @Override
    public void onBackPressed()
    {

    }
}
