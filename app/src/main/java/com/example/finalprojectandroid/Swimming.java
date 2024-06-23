package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Swimming extends AppCompatActivity {


    Button btnMainSwim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimming);

        btnMainSwim =findViewById(R.id.btnMainSwim);

        btnMainSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Swimming.this,Main.class);
                startActivity(intent);
                finish();
            }
        });

    }
}