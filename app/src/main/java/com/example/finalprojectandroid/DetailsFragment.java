package com.example.finalprojectandroid;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import java.util.concurrent.atomic.AtomicInteger;

public class DetailsFragment extends Fragment {

    private TextView activity, calories, message, linkDeleteAllCalories;
    private Button btnMain, btnSignOut;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    // Required empty public constructor
    public DetailsFragment() {
        // The public DetailsFragment() constructor is necessary for proper fragment instantiation
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_info, container, false);

        // Initialize UI elements
        activity = view.findViewById(R.id.activity);
        calories = view.findViewById(R.id.calories);
        message = view.findViewById(R.id.message);
        btnMain = view.findViewById(R.id.buttonGoToMain);
        btnSignOut = view.findViewById(R.id.btnSignOut);
        linkDeleteAllCalories = view.findViewById(R.id.linkDeleteAllCalories);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetch data
        fetchCaloriesData();

        // Set up button listeners
        btnMain.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Main.class);
            startActivity(intent);
        });

        btnSignOut.setOnClickListener(view1 -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), login.class);
            startActivity(intent);
        });

        linkDeleteAllCalories.setOnClickListener(view1 -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete All Calories")
                    .setMessage("Are you sure you want to delete all calorie data?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteAllCalories())
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        });

        return view;
    }

    private void fetchCaloriesData() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = databaseReference.child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalCalories = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Object caloriesValue = dataSnapshot.child("calories").getValue();
                    if (caloriesValue instanceof Double) {
                        totalCalories += ((Double) caloriesValue).intValue();
                    } else if (caloriesValue instanceof Long) {
                        totalCalories += ((Long) caloriesValue).intValue();
                    }
                }
                // Update UI with the fetched data
                change("Calories Burnt", totalCalories);

                // Show message if total calories exceed 200
                showCongratulationsMessageIfNeeded(totalCalories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllCalories() {
        String userId = mAuth.getCurrentUser().getUid();
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
            ref.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        if (deletionsCompleted.incrementAndGet() == totalDeletions) {
                            // All deletions completed
                            change("Calories Burnt", 0); // Update UI
                            message.setVisibility(View.GONE); // Hide the message
                            Toast.makeText(getActivity(), "All calories data has been deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to delete some calories data", Toast.LENGTH_SHORT).show());
        }
    }

    public void change(String activityName, int caloriesBurned) {
        // Update the TextViews with the activity name and calories burned
        if (activity != null && calories != null) {
            activity.setText(activityName);
            this.calories.setText(String.valueOf(caloriesBurned));
        }
    }

    public void showCongratulationsMessageIfNeeded(int totalCalories) {
        if (totalCalories > 200) {
            message.setVisibility(View.VISIBLE);
            message.setText("!Doing great!");

            // Create a Handler to post a delayed task
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> message.setVisibility(View.GONE), 3000); // 3000 milliseconds = 3 seconds
        } else {
            message.setVisibility(View.GONE);
        }
    }
}
