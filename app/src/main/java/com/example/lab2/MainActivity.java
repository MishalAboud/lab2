package com.example.lab2;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint);

        // Row 4: EditText
        EditText editText = findViewById(R.id.editText);

        // Row 3: Button and CheckBox
        Button pressButton = findViewById(R.id.press_button);
        CheckBox checkBox = findViewById(R.id.checkBox);

        // Row 1: TextView and ImageButton
        TextView textView = findViewById(R.id.textView);
        ImageButton imageButton = findViewById(R.id.imageButton);

        // Set OnClickListener for the "Press Me" button
        pressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = editText.getText().toString();
                textView.setText(newText);

                // Create a translated Toast
                String toastMessage = getResources().getString(R.string.toast_message);
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnCheckedChangeListener for the CheckBox
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String message = "The checkbox is now " + (isChecked ? "on" : "off");
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Undo action, set the checkbox back to its original state
                                checkBox.setChecked(!isChecked);
                            }
                        });
                snackbar.show();
            }
        });
    }
}
