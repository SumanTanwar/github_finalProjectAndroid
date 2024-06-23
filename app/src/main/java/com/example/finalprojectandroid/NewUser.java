package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewUser extends AppCompatActivity {

    Button submitButton;
    TextView txtSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        submitButton = findViewById(R.id.newUserSignUp);
        txtSignIn = findViewById(R.id.txtNewUserSignIn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewUser.this,login.class);
                startActivity(intent);
                finish();
            }
        });

        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUser.this,Main.class);
                startActivity(intent);
                finish();
            }
        });

    }
}