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

public class DetailsFragment extends Fragment {

    TextView activity, calories;
    Button btnMain, btnSignOut;

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

    // Method to update the TextViews with integer calories
    public void change(String activityName, int cal) {
        activity.setText(activityName);
        calories.setText("Calories: " + cal);
    }
}
