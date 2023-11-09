package com.example.lab2;

import android.os.Bundle;
import android.widget.TextView;

public class DadJokeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dad_joke);

        // Find the TextView for displaying the dad joke.
        TextView dadJokeTextView = findViewById(R.id.jokeTextView);

        // Set your best dad joke text in the TextView.
        dadJokeTextView.setText("Why don't scientists trust atoms? Because they make up everything!");
    }

}
