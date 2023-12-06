package com.example.masodikpro;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class ApiManager {
    private static final String TAG = "ApiManager";
    private static final String API_KEY = "6b29614e300d19b809e5cd1c8d231145";
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    public static String performApiCall(int selectedYear, String selectedGenre, float selectedRating) {
        String apiUrl = buildApiUrl(selectedYear, selectedGenre, selectedRating);
        return getApiResponse(apiUrl);
    }

    private static String buildApiUrl(int selectedYear, String selectedGenre, float selectedRating) {
        StringBuilder apiUrlBuilder = new StringBuilder(BASE_URL);
        apiUrlBuilder.append("?api_key=").append(API_KEY);
        apiUrlBuilder.append("&primary_release_year=").append(selectedYear);
        apiUrlBuilder.append("&with_genres=").append(selectedGenre);
        apiUrlBuilder.append("&vote_average.gte=").append(selectedRating);
        return apiUrlBuilder.toString();
    }

    private static String getApiResponse(String apiUrl) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();
        } catch (Exception e) {
            Log.e(TAG, "Error during API call", e);
        }
        return response.toString();
    }
}