package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUser extends AppCompatActivity {

    EditText nameEdit, emailEdit, passEdit, confirmPassEdit;
    Button registerButton;
    TextView txtLogin,txtPolicy;
    FirebaseAuth mAuth;
    RadioGroup radioGroup;
    CheckBox checkboxPolicy;
    DatabaseReference mDatabase;

     boolean isPasswordVisible = false;
     boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        nameEdit = findViewById(R.id.name);
        radioGroup = findViewById(R.id.rdgrp);
        emailEdit = findViewById(R.id.emailCreateUser);
        passEdit = findViewById(R.id.passCreateUser);
        confirmPassEdit = findViewById(R.id.confirmPassCreateUser);
        checkboxPolicy = findViewById(R.id.checkPolicy);
        registerButton = findViewById(R.id.newUserRegister);
<<<<<<< HEAD
     //   signUpButton = findViewById(R.id.newUserSignUp);
        txtSignIn = findViewById(R.id.txtNewUserSignIn);
        radioGroup = findViewById(R.id.rdgrp);
//        progressBar =findViewById(R.id.progressBar);
=======
        txtLogin = findViewById(R.id.txtNewUserLogin);
        txtPolicy = findViewById(R.id.privacyPolicy);
>>>>>>> realTimeDatabase


        // Set on click listener for the eye icon

        passEdit.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passEdit.getRight() - passEdit.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(passEdit);
                    return true;
                }
            }
            return false;
        });

        confirmPassEdit.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (confirmPassEdit.getRight() - confirmPassEdit.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(confirmPassEdit);
                    return true;
                }
            }
            return false;
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString().trim();
                String email = emailEdit.getText().toString().trim();
                String password = passEdit.getText().toString().trim();
                String confirmPass = confirmPassEdit.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(NewUser.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(NewUser.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(NewUser.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(NewUser.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPass)) {
                    Toast.makeText(NewUser.this, "Confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPass)) {
                    Toast.makeText(NewUser.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    confirmPassEdit.requestFocus();
                    return;
                }
                if (!checkboxPolicy.isChecked()) {
                    Toast.makeText(NewUser.this, "Please accept the policy", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        writeNewUser(user.getUid(), name, email);
                                    }
                                    Toast.makeText(NewUser.this, "Account Created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(NewUser.this, Main.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(NewUser.this, "Authentication failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUser.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        txtPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUser.this, conditions.class);
                startActivity(intent);
                finish();
            }
        });


    }


    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
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

    public static class User {
        public String name;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
