package com.example.finalprojectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editProfile extends AppCompatActivity {

    EditText nameEdit, passwordEdit, oldPassword;
    Button saveButton, btnMainMenu;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameEdit = findViewById(R.id.editname);
        passwordEdit = findViewById(R.id.editpassword);
        oldPassword = findViewById(R.id.oldpassword);
        saveButton = findViewById(R.id.save);
        btnMainMenu = findViewById(R.id.mainMenu);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        nameEdit.setText(name); // Set current name in EditText

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String currentPassword = oldPassword.getText().toString().trim();
                    String newPassword = passwordEdit.getText().toString().trim();

                    // Validate current password
                    if (TextUtils.isEmpty(currentPassword)) {
                        oldPassword.setError("Enter your current password");
                        return;
                    }

                    AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

                    // Reauthenticate user with current credentials
                    currentUser.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Password authentication successful, update profile
                                        updateProfile(currentUser, newPassword);
                                    } else {
                                        // Password authentication failed
                                        Toast.makeText(editProfile.this, "Incorrect current password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(editProfile.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(editProfile.this, Main.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateProfile(FirebaseUser user, String newPassword) {
        String newName = nameEdit.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            mDatabase.child("users").child(user.getUid()).child("name").setValue(newName);

                            if (!TextUtils.isEmpty(newPassword)) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> passwordTask) {
                                                if (passwordTask.isSuccessful()) {
                                                    Toast.makeText(editProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(editProfile.this, "Failed to update password: " + passwordTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }

                            // Return to MyProfile with updated data
                            Intent intent = new Intent();
                            intent.putExtra("name", newName); // Pass updated name back
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(editProfile.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // Handle back press if needed
    }
}
