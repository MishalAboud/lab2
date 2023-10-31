package com.example.lab2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {
    public static DetailsFragment newInstance(String characterName, String height, String mass) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString("characterName", characterName);
        args.putString("height", height);
        args.putString("mass", mass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Find the TextViews in the layout
        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView heightTextView = view.findViewById(R.id.heightTextView);
        TextView massTextView = view.findViewById(R.id.massTextView);

        // Get the character details from the arguments
        Bundle args = getArguments();
        if (args != null) {
            String characterName = args.getString("characterName");
            String height = args.getString("height");
            String mass = args.getString("mass");

            // Set the character details in the TextViews
            nameTextView.setText(characterName);
            heightTextView.setText("Name: " + characterName);
            heightTextView.setText("Height: " + height);
            massTextView.setText("Mass: " + mass);

            // Set values for other character details TextViews here
        }

        return view;
    }
}
