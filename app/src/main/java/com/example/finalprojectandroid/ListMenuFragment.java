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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ListMenuFragment extends ListFragment {

    String[] activityNames = new String[]{"Running", "Skipping", "Swimming", "Cycling", "Exercise", "Yoga", "Total"};
    int[] calories = new int[activityNames.length]; // Array for storing calories

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listitem_info, container, false);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Yoga calories");

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
            DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment2);
            if (detailsFragment != null) {
                detailsFragment.change(activityNames[position], calories[position]);
            }
            getListView().setItemChecked(position, true);
        }
    }

    private void fetchCaloriesData() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Reset calorie counts
                for (int i = 0; i < calories.length; i++) {
                    calories[i] = 0;
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    if (data != null) {
                        // Handle different data types
                        Object caloriesObject = data.get("calories");
                        if (caloriesObject instanceof Long) {
                            // If calories are stored as Long
                            calories[5] += ((Long) caloriesObject).intValue();
                            calories[6] += ((Long) caloriesObject).intValue();
                        } else if (caloriesObject instanceof Double) {
                            // If calories are stored as Double
                            calories[5] += ((Double) caloriesObject).intValue();
                            calories[6] += ((Double) caloriesObject).intValue();
                        }
                    }
                }
                // Notify adapter about the changes
                ((ArrayAdapter<?>) getListAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                System.err.println("Error fetching data: " + error.getMessage());
            }
        });
    }
}
