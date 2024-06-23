package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Cycling extends AppCompatActivity {

    Button btnMainCycle;

    private TextView tvTimer;
    private Button btnStart, btnPause, btnReset;

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
        setContentView(R.layout.activity_cycling);

        btnMainCycle =findViewById(R.id.btnMainCycle);

        tvTimer = findViewById(R.id.timerCycle);
        btnStart = findViewById(R.id.btnStartCycle);
        btnPause = findViewById(R.id.btnPauseCycle);
        btnReset = findViewById(R.id.btnResetCycle);

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

        btnMainCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cycling.this,Main.class);
                startActivity(intent);
                finish();
            }
        });


    }
}