package com.example.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.resetEmailEditText);
        btnResetPassword = findViewById(R.id.resetPassword);
        mAuth = FirebaseAuth.getInstance();

        btnBack = findViewById(R.id.back);


        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }

                resetPassword(email);
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(forgotPassword.this,login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(forgotPassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(forgotPassword.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}