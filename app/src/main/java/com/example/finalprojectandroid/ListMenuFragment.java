package com.example.finalprojectandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ListMenuFragment extends ListFragment {

    private String[] activityNames = new String[]{"Running", "Skipping", "Swimming", "Cycling", "Exercise", "Yoga", "Total"};
    private int[] calories = new int[activityNames.length]; // Array for storing calories

    private FirebaseAuth mAuth;
    private DatabaseReference runningDatabaseReference;
    private DatabaseReference skippingDatabaseReference;
    private DatabaseReference swimmingDatabaseReference;
    private DatabaseReference cyclingDatabaseReference;
    private DatabaseReference exerciseDatabaseReference;
    private DatabaseReference yogaDatabaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listitem_info, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        runningDatabaseReference = FirebaseDatabase.getInstance().getReference("Running calories");
        skippingDatabaseReference = FirebaseDatabase.getInstance().getReference("Skipping calories");
        swimmingDatabaseReference = FirebaseDatabase.getInstance().getReference("Swimming calories");
        cyclingDatabaseReference = FirebaseDatabase.getInstance().getReference("Cycling calories");
        exerciseDatabaseReference = FirebaseDatabase.getInstance().getReference("Exercise calories");
        yogaDatabaseReference = FirebaseDatabase.getInstance().getReference("Yoga calories");

        // Fetch and update data
        fetchCaloriesData();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, activityNames);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (position >= 0 && position < activityNames.length) {
            if (position == 6) { // Total item clicked
                DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment2);
                if (detailsFragment != null) {
                    detailsFragment.change("Total Calories", calories[position]);
                    // Trigger message display
                    detailsFragment.showCongratulationsMessageIfNeeded(calories[position]);
                }
            } else {
                DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment2);
                if (detailsFragment != null) {
                    detailsFragment.change(activityNames[position], calories[position]);
                }
            }
            getListView().setItemChecked(position, true);
        }
    }

    private void fetchCaloriesData() {
        String userId = mAuth.getCurrentUser().getUid();

        fetchCaloriesFromDatabase(runningDatabaseReference, 0);
        fetchCaloriesFromDatabase(skippingDatabaseReference, 1);
        fetchCaloriesFromDatabase(swimmingDatabaseReference, 2);
        fetchCaloriesFromDatabase(cyclingDatabaseReference, 3);
        fetchCaloriesFromDatabase(exerciseDatabaseReference, 4);
        fetchCaloriesFromDatabase(yogaDatabaseReference, 5);
    }

    private void fetchCaloriesFromDatabase(DatabaseReference databaseRef, final int index) {
        databaseRef.child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int totalCalories = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                            if (data != null) {
                                Object caloriesObject = data.get("calories");
                                if (caloriesObject instanceof Long) {
                                    totalCalories += ((Long) caloriesObject).intValue();
                                } else if (caloriesObject instanceof Double) {
                                    totalCalories += ((Double) caloriesObject).intValue();
                                }
                            }
                        }
                        calories[index] = totalCalories;
                        calories[6] = calories[0] + calories[1] + calories[2] + calories[3] + calories[4] + calories[5]; // Update total calories index
                        ((ArrayAdapter<?>) getListAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here, if necessary
                    }
                });
    }
}
