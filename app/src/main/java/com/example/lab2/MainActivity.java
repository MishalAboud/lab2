package com.example.lab2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private ImageView catImageView;
    private ProgressBar progressBar;
    private Bitmap currentCatImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catImageView = findViewById(R.id.catImageView);
        progressBar = findViewById(R.id.progressBar);

        catImageView.setImageResource(R.drawable.banner_image);

    }


    public class CatImages extends AsyncTask<String, Integer, String> {
        private Bitmap currentCatImage;

        @Override
        protected String doInBackground(String... strings) {
            while (true) {
                String imageUrl = fetchCatImageUrl();
                if (imageUrl != null) {
                    String imageId = extractImageIdFromUrl(imageUrl);
                    if (imageId != null && doesImageExistLocally(imageId)) {
                        // The image exists locally; load it
                        currentCatImage = loadLocalCatImage(imageId);
                    } else {
                        // The image does not exist locally; download and save it
                        currentCatImage = downloadCatImage(imageUrl);
                        if (imageId != null && currentCatImage != null) {
                            saveLocalCatImage(imageId, currentCatImage);
                        }
                    }
                    publishProgress(0); // Show progress bar
                    publishProgress(100); // Hide progress bar and update image
                }

                for (int i = 0; i < 100; i++) {
                    try {
                        publishProgress(i);
                        Thread.sleep(30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressBar.setProgress(progress);

            if (progress == 100) {
                catImageView.setImageBitmap(currentCatImage);
            }
        }

        private String fetchCatImageUrl() {
            try {
                URL url = new URL("https://cataas.com/cat?json=true");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000); // Set a timeout (adjust as needed)
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                    if (scanner.hasNext()) {
                        String json = scanner.next();
                        JSONObject jsonObject = new JSONObject(json);
                        String imageUrl = jsonObject.optString("url", null);
                        if (imageUrl != null) {
                            return "https://cataas.com/cat" + imageUrl; // Add the base URL
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CatApp", "Error in fetchCatImageUrl (IO): " + e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("CatApp", "Error in fetchCatImageUrl (JSON parsing): " + e.getMessage());
            }


            return null;
        }


        private String extractImageIdFromUrl(String imageUrl) {
            try {
                URI uri = new URI(imageUrl);
                String path = uri.getPath();
                String[] pathSegments = path.split("/");
                return pathSegments[pathSegments.length - 1];
            } catch (URISyntaxException e) {
                e.printStackTrace();
                Log.e("CatApp", "Error in extractImageIdFromUrl: " + e.getMessage());
            }

            return null;
        }


        private boolean doesImageExistLocally(String imageId) {
            try {
                openFileInput("cat_image_" + imageId + ".png");
                return true;
            } catch (FileNotFoundException e) {
                return false;
            }
        }


        private Bitmap loadLocalCatImage(String imageId) {
            try {
                FileInputStream inputStream = openFileInput("cat_image_" + imageId + ".png");
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("CatApp", "Error in loadLocalCatImage: " + e.getMessage());
            }

            return null;
        }


        private Bitmap downloadCatImage(String imageUrl) {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5000); // Set a timeout (adjust as needed)
                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    Bitmap catImage = BitmapFactory.decodeStream(inputStream);
                    return catImage;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("CatApp", "Error in downloadCatImage: " + e.getMessage());
            }

            return null;
        }


        private void saveLocalCatImage(String imageId, Bitmap catImage) {
            try {
                // Define a filename for the image using the imageId
                String fileName = "cat_image_" + imageId + ".png";
                FileOutputStream outputStream = openFileOutput(fileName, MODE_PRIVATE);
                catImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("CatApp", "Error in saveLocalCatImage: " + e.getMessage());
            }
        }

    }
}