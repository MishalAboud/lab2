package com.example.lab2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText;
    private SharedPreferences sharedPreferences;

    private static final int NAME_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String savedName = sharedPreferences.getString("userName", "");

        if (!savedName.isEmpty()) {
            nameEditText.setText(savedName);
        }

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = nameEditText.getText().toString();
                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                intent.putExtra("userName", userName);
                startActivityForResult(intent, NAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        String userName = nameEditText.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NAME_ACTIVITY_REQUEST_CODE) {
            if (resultCode == 0) {
                // User wants to change their name
                // Handle accordingly
            } else if (resultCode == 1) {
                // User is happy
                finish(); // Close the app
            }
        }
    }
}
