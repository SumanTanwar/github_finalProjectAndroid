package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Skipping extends AppCompatActivity {


    Button btnMainSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skipping);

        btnMainSkip =findViewById(R.id.btnMainSkip);


        btnMainSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Skipping.this,Main.class);
                startActivity(intent);
                finish();
            }
        });
    }
}