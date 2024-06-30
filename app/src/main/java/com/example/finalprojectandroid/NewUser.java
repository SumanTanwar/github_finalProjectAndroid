package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class NewUser extends AppCompatActivity {

    EditText emailEdit,passEdit,confirmPassEdit;
    Button submitButton,registerButton;
    TextView txtSignIn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),Main.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mAuth = FirebaseAuth.getInstance();
        emailEdit = findViewById(R.id.emailCreateUser);
        passEdit = findViewById(R.id.passCreateUser);
        confirmPassEdit = findViewById(R.id.confirmPassCreateUser);
        registerButton = findViewById(R.id.newUserRegister);
        submitButton = findViewById(R.id.newUserSignUp);
        txtSignIn = findViewById(R.id.txtNewUserSignIn);
        progressBar =findViewById(R.id.progressBar);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email = String.valueOf(emailEdit.getText());
                password = String.valueOf(passEdit.getText());

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

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
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
                            }
                        });


            }
        });



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