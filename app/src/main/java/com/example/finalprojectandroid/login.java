package com.example.finalprojectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    EditText emailEdit, passEdit;
    TextView txtNewUser, txtForgotPassword;
    Button logRegbtn;
    FirebaseAuth mAuth;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.EditLogin);
        passEdit = findViewById(R.id.EditPass);
        logRegbtn = findViewById(R.id.loginRegister);
        txtNewUser = findViewById(R.id.txtNewUserLink);
        txtForgotPassword = findViewById(R.id.forgotPassword);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        passEdit.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passEdit.getRight() - passEdit.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(passEdit);
                    return true;
                }
            }
            return false;
        });

        if (currentUser != null) {
            // User is signed in, navigate to the main activity
            Intent intent = new Intent(login.this, Main.class);
            startActivity(intent);
            finish();
            return;
        }

        logRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(emailEdit.getText());
                String password = String.valueOf(passEdit.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, Main.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(login.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                                    passEdit.setText("");
                                }
                            }
                        });
            }
        });

        txtNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, NewUser.class);
                startActivity(intent);
                finish();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, forgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void togglePasswordVisibility(EditText editText) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_open, 0);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_closed, 0);
        }
        editText.setSelection(editText.getText().length());
    }

    @Override
    public void onBackPressed() {
        // Disable back button
    }
}
