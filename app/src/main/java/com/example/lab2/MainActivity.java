package com.example.lab2;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.lab2.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.activity_main);

        // Find the Toolbar by its ID
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        // Set a listener for navigation item clicks
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_item_home) {
                // Handle the "Home" menu item
                navigateToMainActivity();
            } else if (id == R.id.menu_item_dad_joke) {
                // Handle the "Dad Joke" menu item
                navigateToDadJokeActivity();
            } else if (id == R.id.menu_item_exit) {
                // Handle the "Exit" menu item
                finishAffinity(); // Close all activities and exit the app
            }
            drawer.closeDrawers(); // Close the navigation drawer
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_1) {
            // Handle menu item 1
            Toast.makeText(this, "You clicked on item 1", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_item_2) {
            // Handle menu item 2
            Toast.makeText(this, "You clicked on item 2", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_item_dad_joke) {
            // Handle menu item "Dad Joke"
            // Start the Dad Joke activity here
            Intent intent = new Intent(this, DadJokeActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_exit) {
            // Handle menu item "Exit"
            // Finish the current activity to shut down the app
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    // Helper method to navigate to the MainActivity
    private void navigateToMainActivity() {
        // Start the MainActivity if not already there
        if (!(this instanceof MainActivity)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    // Helper method to navigate to the DadJokeActivity
    private void navigateToDadJokeActivity() {
        Intent intent = new Intent(this, DadJokeActivity.class);
        startActivity(intent);
    }
}
