package com.example.finalprojectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
            String mainusername = profilename.getText().toString().trim();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

            Query checkuserData = reference.orderByChild("name").equalTo(mainusername);
            checkuserData.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            // Remove user from Realtime Database
                            userSnapshot.getRef().removeValue();
                        }

                        // Remove user from Firebase Authentication
                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MyProfile.this, login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MyProfile.this, "Failed to delete user from authentication", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(MyProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MyProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        editprofile.setOnClickListener(view -> {
            Intent intent = new Intent(MyProfile.this, editProfile.class);
            intent.putExtra("name", profilename.getText().toString().trim()); // Pass current name
            intent.putExtra("email", profileemail.getText().toString().trim()); // Pass current email
            startActivityForResult(intent, EDIT_PROFILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == RESULT_OK) {
            // Update UI with new data
            String newName = data.getStringExtra("name");
            String newEmail = data.getStringExtra("email");

            profilename.setText(newName);
            profileemail.setText(newEmail);
        }
    }

    public void showDataUser() {
        Intent intent = getIntent();
        titlename.setText("Welcome to your Profile");
        profileemail.setText(intent.getStringExtra("email"));
        profilename.setText(intent.getStringExtra("name"));
    }

    @Override
    public void onBackPressed() {
        // Handle back press if needed
    }
}
