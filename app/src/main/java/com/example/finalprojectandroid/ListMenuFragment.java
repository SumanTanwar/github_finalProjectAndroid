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

public class ListMenuFragment extends ListFragment {

    String[] activityNames = new String[]{"Running", "Skipping", "Swimming", "Cycling", "Exercise", "Yoga", "Total", "Comments"};
    int[] calories = new int[]{300, 200, 250, 400, 350, 150, 1000, 0}; // Updated to int array

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listitem_info, container, false);
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

        // Ensure position is within bounds for both arrays
        if (position >= 0 && position < activityNames.length && position < calories.length) {
            DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().findFragmentById(R.id.fragment2);
            if (detailsFragment != null) {
                detailsFragment.change(activityNames[position], calories[position]);
            }
            getListView().setItemChecked(position, true);
        } else {
            System.err.println("Error: Invalid list item position " + position);
        }
    }
}
