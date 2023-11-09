package com.example.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private String[] navigationItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawer_layout);
        ListView drawerList = findViewById(R.id.drawerList);

        navigationItems = getResources().getStringArray(R.array.navigation_items);

        // Initialize the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, navigationItems));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Set the Toolbar as the support ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        switch (position) {
            case 0: // Home
                if (!this.getClass().equals(BaseActivity.class)) {
                    startActivity(new Intent(this, BaseActivity.class));
                    finish();
                }
                break;
            case 1: // Dad Joke
                if (!this.getClass().equals(DadJokeActivity.class)) {
                    startActivity(new Intent(this, DadJokeActivity.class));
                    finish();
                }
                break;
            case 2: // Exit
                finishAffinity();
                break;
        }
        drawerLayout.closeDrawers();
    }

}
