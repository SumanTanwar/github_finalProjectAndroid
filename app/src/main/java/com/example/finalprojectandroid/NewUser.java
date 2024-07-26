package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class NewUser extends AppCompatActivity {

    EditText nameEdit,emailEdit,passEdit,confirmPassEdit;
    Button signUpButton,registerButton;
    TextView txtSignIn;
    FirebaseAuth mAuth;
    RadioGroup radioGroup;
//    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        nameEdit = findViewById(R.id.name);
        emailEdit = findViewById(R.id.emailCreateUser);
        passEdit = findViewById(R.id.passCreateUser);
        confirmPassEdit = findViewById(R.id.confirmPassCreateUser);
        registerButton = findViewById(R.id.newUserRegister);
     //   signUpButton = findViewById(R.id.newUserSignUp);
        txtSignIn = findViewById(R.id.txtNewUserSignIn);
        radioGroup = findViewById(R.id.rdgrp);
//        progressBar =findViewById(R.id.progressBar);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressBar.setVisibility(View.VISIBLE);
                String name,email,password,confirmPass;
                name = String.valueOf(nameEdit.getText());
                email = String.valueOf(emailEdit.getText());
                password = String.valueOf(passEdit.getText());
                confirmPass = String.valueOf(confirmPassEdit.getText());


                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(NewUser.this,"Enter name",Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == -1) {
                    Toast.makeText(NewUser.this, "Please select a gender", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(NewUser.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(NewUser.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPass))
                {
                    Toast.makeText(NewUser.this,"Confirm password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPass)) {
                    Toast.makeText(NewUser.this,"Passwords do not match",Toast.LENGTH_SHORT).show();

                    confirmPassEdit.requestFocus();
                    return;
                }






                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful())
                                {

                                    Toast.makeText(NewUser.this, "Account Created ",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(NewUser.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                }

                                Intent intent = new Intent(NewUser.this,login.class);
                                startActivity(intent);
                                finish();
                            }
                        });


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