package com.example.lab2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        if (savedInstanceState == null) {
            String characterName = getIntent().getStringExtra("characterName");
            String height = getIntent().getStringExtra("height");
            String mass = getIntent().getStringExtra("mass");

            DetailsFragment detailsFragment = DetailsFragment.newInstance(characterName, height, mass);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, detailsFragment)
                    .commit();
        }
    }
}
