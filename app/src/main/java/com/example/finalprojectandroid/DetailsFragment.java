package com.example.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DetailsFragment extends Fragment {

    private TextView activity, calories, message;
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
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.details_info, container, false);

        // Initialize UI elements
        activity = view.findViewById(R.id.activity);
        calories = view.findViewById(R.id.calories);
        message = view.findViewById(R.id.message);
        btnMain = view.findViewById(R.id.buttonGoToMain);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Fetch data
        fetchCaloriesData();

        // Set up button listeners
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Main.class);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
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
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    if (data != null) {
                        Object caloriesValue = data.get("calories");
                        if (caloriesValue instanceof Double) {
                            totalCalories += ((Double) caloriesValue).intValue();
                        } else if (caloriesValue instanceof Long) {
                            totalCalories += ((Long) caloriesValue).intValue();
                        }
                    }
                }
                // Update UI with the fetched data
                change("Calories Burnt", totalCalories);

                // Show message if total calories exceed 200
                showCongratulationsMessageIfNeeded(totalCalories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here, if necessary
            }
        });
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
            message.setText("Congratulations! Doing great!");
        } else {
            message.setVisibility(View.GONE);
        }
    }
}
