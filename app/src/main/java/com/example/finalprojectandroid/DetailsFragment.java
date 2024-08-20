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

    TextView activity, calories;
    Button btnMain, btnSignOut;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    // Required empty public constructor
    public DetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details_info, container, false);

        // Initialize views
        activity = view.findViewById(R.id.activity);
        calories = view.findViewById(R.id.calories);
        btnMain = view.findViewById(R.id.buttonGoToMain);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Yoga calories"); // Ensure this path is correct

        // Example date/time for testing; update this as needed
        String selectedDateTime = "2024-08-14 15:30";
        fetchCaloriesData(selectedDateTime);

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
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchCaloriesData(String dateFilter) {
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child(userId).orderByChild("dateTime").equalTo(dateFilter)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                                if (data != null) {
                                    String activityName = "Yoga"; // Hardcoded, adjust if necessary
                                    Object caloriesValue = data.get("calories");
                                    int caloriesBurned = 0;

                                    if (caloriesValue instanceof Double) {
                                        caloriesBurned = ((Double) caloriesValue).intValue();
                                    } else if (caloriesValue instanceof Long) {
                                        caloriesBurned = ((Long) caloriesValue).intValue();
                                    } else {
                                        // Log unexpected data type
                                        System.err.println("Unexpected data type for calories: " + caloriesValue.getClass().getName());
                                        continue;
                                    }

                                    change(activityName, caloriesBurned);
                                }
                            }
                        } else {
                            // Log if no data is found
                            System.out.println("No data found for the selected date/time");
                            change("No Data", 0); // Optional: Handle no data case
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Log error
                        System.err.println("Error fetching data: " + error.getMessage());
                    }
                });
    }

    public void change(String activityName, int caloriesBurned) {
        // Update the TextViews with the activity name and calories burned
        if (activity != null && calories != null) {
            activity.setText(activityName);
            calories.setText(String.valueOf(caloriesBurned));
        }
    }
}
