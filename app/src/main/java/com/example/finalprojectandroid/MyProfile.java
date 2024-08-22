package com.example.finalprojectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

import kotlinx.coroutines.scheduling.Task;

public class MyProfile extends AppCompatActivity {

    TextView profilename, profileemail;
    TextView titlename;
    Button signout, editprofile, deleteprofile, mainMenu;

    private static final int EDIT_PROFILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profilename = findViewById(R.id.profilename);
        profileemail = findViewById(R.id.profileemail);
        titlename = findViewById(R.id.titlename);
        signout = findViewById(R.id.SignOut);
        editprofile = findViewById(R.id.editprofile);
        deleteprofile = findViewById(R.id.deleteprofile);
        mainMenu = findViewById(R.id.mainMenu);

        showDataUser();

        signout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), login.class);
            startActivity(intent);
            finish();
        });

        mainMenu.setOnClickListener(view -> {
            Intent intent = new Intent(MyProfile.this, Main.class);
            startActivity(intent);
            finish();
        });

        deleteprofile.setOnClickListener(view -> {
            new AlertDialog.Builder(MyProfile.this)
                    .setTitle("Delete Profile")
                    .setMessage("Are you sure you want to delete your profile?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        String mainusername = profilename.getText().toString().trim();
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                        Query checkuserData = usersRef.orderByChild("name").equalTo(mainusername);
                        checkuserData.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        // Get user ID from snapshot
                                        String userId = userSnapshot.getKey();
                                        if (userId == null) {
                                            Toast.makeText(MyProfile.this, "User ID not found", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        // Remove user from Realtime DB
                                        userSnapshot.getRef().removeValue();

                                        // References to delete
                                        DatabaseReference[] calorieRefs = {
                                                FirebaseDatabase.getInstance().getReference("Running calories").child(userId),
                                                FirebaseDatabase.getInstance().getReference("Skipping calories").child(userId),
                                                FirebaseDatabase.getInstance().getReference("Swimming calories").child(userId),
                                                FirebaseDatabase.getInstance().getReference("Cycling calories").child(userId),
                                                FirebaseDatabase.getInstance().getReference("Exercise calories").child(userId),
                                                FirebaseDatabase.getInstance().getReference("Yoga calories").child(userId)
                                        };

                                        // Track completion of all deletions
                                        final int totalDeletions = calorieRefs.length;
                                        final AtomicInteger deletionsCompleted = new AtomicInteger(0);

                                        for (DatabaseReference ref : calorieRefs) {
                                            ref.removeValue().addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    if (deletionsCompleted.incrementAndGet() == totalDeletions) {
                                                        // All deletions completed, now delete Firebase Auth user
                                                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(authTask -> {
                                                            if (authTask.isSuccessful()) {
                                                                Intent intent = new Intent(MyProfile.this, login.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(MyProfile.this, "Failed to delete user from authentication", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                } else {
                                                    Toast.makeText(MyProfile.this, "Failed to delete some user data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    Toast.makeText(MyProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MyProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });



        editprofile.setOnClickListener(view -> {
            Intent intent = new Intent(MyProfile.this, editProfile.class);
            intent.putExtra("name", profilename.getText().toString().trim());
            intent.putExtra("email", profileemail.getText().toString().trim());
            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {

            // Update UI with new data
            String newName = data.getStringExtra("name");
            //String newEmail = data.getStringExtra("email");

            profilename.setText(newName);
           // profileemail.setText(newEmail);
        }
    }

    public void showDataUser() {
        Intent intent = getIntent();
        titlename.setText("Welcome to your Profile");
        profileemail.setText(intent.getStringExtra("email"));
        profilename.setText(intent.getStringExtra("name"));
    }

    @Override
    public void onBackPressed()
    {

    }
}
