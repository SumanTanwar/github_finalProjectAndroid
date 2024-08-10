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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.HashMap;


public class Swimming extends AppCompatActivity {

    TextView tvTimer;
    Button btnMainSwim, btnStart, btnPause, btnReset, btnSave, btnCalculate;
    EditText caloriesCycle;

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
        setContentView(R.layout.activity_swimming);



        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Swimming calories");

        btnMainSwim = findViewById(R.id.btnMainSwim);
        tvTimer = findViewById(R.id.timerSwim);
        btnStart = findViewById(R.id.btnStartSwim);
        btnPause = findViewById(R.id.btnPauseSwim);
        btnReset = findViewById(R.id.btnResetSwim);
        btnSave = findViewById(R.id.btnSaveCalories);
        btnCalculate = findViewById(R.id.btnCalculateSwim);
        caloriesCycle = findViewById(R.id.caloriesSwim);  // Updated ID

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnReset.setEnabled(true);
                caloriesCycle.setText("");
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
                String caloryBurntString = caloriesCycle.getText().toString();
                double caloryBurnt = 0.0;

                if (caloryBurntString.isEmpty()) {
                    Toast.makeText(Swimming.this, "No calories burnt...", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    caloryBurnt = Double.parseDouble(caloryBurntString);
                } catch (NumberFormatException e) {
                    Toast.makeText(Swimming.this, "Invalid value...", Toast.LENGTH_SHORT).show();
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

                    new AlertDialog.Builder(Swimming.this)
                            .setTitle("Calories Saved")
                            .setMessage("Your number of burnt calories and the date are saved in the database for the weekly report.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    caloriesCycle.setText("");
                                }
                            })
                            .show();
                }
            }
        });


        btnMainSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Swimming.this, Main.class);
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
        // Check if the timer is "00:00:00"
        if (tvTimer.getText().toString().equals("00:00:00")) {
            // Set the caloriesBurned EditText to empty
            caloriesCycle.setText("");
            Toast.makeText(Swimming.this, "Timer is zero. No calories burned.", Toast.LENGTH_SHORT).show();
            return;
        }

        double MET = 6.0; // MET value for swimming (moderate pace)
        double weight = 70.0; // User weight in kg (you might want to get this from user input)

        // Calculate time in hours
        double timeInHours = updateTime / 3600000.0;

        // Calculate calories burned
        double caloriesBurnedValue = MET * weight * timeInHours;

        // Display the result in the EditText
        caloriesCycle.setText(String.format("%.2f", caloriesBurnedValue));

        // Reset the timer and buttons
        tvTimer.setText("00:00:00");
        btnStart.setEnabled(true);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
        handler.removeCallbacks(updateTimerThread);
    }


    @Override
    public void onBackPressed() {
        // Prevent back button functionality
    }
}
