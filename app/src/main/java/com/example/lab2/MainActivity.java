package com.example.lab2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView characterListView;
    CharacterListAdapter characterAdapter;
    List<String> characterNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        characterListView = findViewById(R.id.characterListView);
        characterAdapter = new CharacterListAdapter(this, characterNames);
        characterListView.setAdapter(characterAdapter);

        characterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected character name
                String characterName = characterNames.get(position);

                // Get the corresponding character's details from the API response
                // You need to add logic to retrieve the height and mass here
                String height = "unknown"; // Replace with the actual height from the API
                String mass = "unknown";   // Replace with the actual mass from the API

                if (findViewById(R.id.frameLayout) == null) {
                    // Phone: Start the EmptyActivity with the selected character name
                    Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                    intent.putExtra("characterName", characterName);
                    intent.putExtra("height", height); // Pass height to the intent
                    intent.putExtra("mass", mass);     // Pass mass to the intent
                    startActivity(intent);
                } else {
                    // Tablet: Replace the DetailsFragment with the selected character name
                    DetailsFragment detailsFragment = DetailsFragment.newInstance(characterName, height, mass);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, detailsFragment)
                            .commit();
                }
            }
        });

        FetchStarWarsCharactersTask task = new FetchStarWarsCharactersTask();
        task.execute();
    }

    private class FetchStarWarsCharactersTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {
            String apiUrl = "https://swapi.dev/api/people/?format=json";

            List<String> characterNames = new ArrayList<>();

            try {
                // Perform the API request and store the response in a String
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    // Parse the JSON response to extract character names
                    JSONObject data = new JSONObject(response.toString());
                    JSONArray characters = data.getJSONArray("results");

                    for (int i = 0; i < characters.length(); i++) {
                        JSONObject character = characters.getJSONObject(i);
                        String name = character.getString("name");
                        characterNames.add(name);
                    }
                } else {
                    // Handle API request error - show a toast message
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "API request failed", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                // Handle exceptions - show a toast message
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show());
            }

            return characterNames;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                characterNames.addAll(result);
                characterAdapter.notifyDataSetChanged();
            }

            if (result != null && result.size() > 0) {
                String selectedCharacterName = result.get(0); // Assuming the first character in the list is selected

                // Find the selected character in the API response and extract height and mass
                for (String characterInfo : result) {
                    try {
                        JSONObject character = new JSONObject(characterInfo);
                        if (selectedCharacterName.equals(character.getString("name"))) {
                            String height = character.getString("height");
                            String mass = character.getString("mass");

                            // Now you have the actual "height" and "mass" values
                            // Pass them to the intent if needed
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing errors
                    }
                }
            }
        }
    }
}