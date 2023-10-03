package com.example.lab2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        Button thankYouButton = findViewById(R.id.thankYouButton);
        Button dontCallMeButton = findViewById(R.id.dontCallMeButton);

        Intent intent = getIntent();
        if (intent != null) {
            String userName = intent.getStringExtra("userName");
            if (userName != null) {
                String welcomeMessage = getString(R.string.welcome_message, userName);
                welcomeTextView.setText(welcomeMessage);
            }
        }

        thankYouButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1); // User is happy
                finish();
            }
        });

        dontCallMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0); // User wants to change their name
                finish();
            }
        });
    }
}
