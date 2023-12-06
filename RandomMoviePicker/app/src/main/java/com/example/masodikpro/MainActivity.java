package com.example.masodikpro;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private NumberPicker yearPicker;
    private RatingBar ratingBar;
    private Button searchButton;
    private Spinner genreSpinner;
    private ArrayList<String> genreNames = new ArrayList<>();
    private Map<String, Integer> genreMap = new HashMap<>();
    private RecyclerView recyclerViewMovies;
    private MoviesPagerAdapter adapter; // A MovieDetailsActivity-ből áthozott adapter
    private CardView cardViewMoviesList;
    private ArrayList<Movie> movies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        genreSpinner = findViewById(R.id.spinner_genre);
        yearPicker = findViewById(R.id.yearPicker);
        ratingBar = findViewById(R.id.ratingBar);
        searchButton = findViewById(R.id.submitButton);

        processGenres("{\"genres\":[{\"id\":28,\"name\":\"Akció\"},{\"id\":12,\"name\":\"Kaland\"},{\"id\":16,\"name\":\"Animáció\"},{\"id\":35,\"name\":\"Vígjáték\"},{\"id\":80,\"name\":\"Bűnügyi\"},{\"id\":99,\"name\":\"Dokumentumfilm\"},{\"id\":18,\"name\":\"Dráma\"},{\"id\":10751,\"name\":\"Családi\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":36,\"name\":\"Történelmi\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Zene\"},{\"id\":9648,\"name\":\"Misztikus\"},{\"id\":10749,\"name\":\"Romantikus\"},{\"id\":878,\"name\":\"Sci-Fi\"},{\"id\":10770,\"name\":\"TV film\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"Háborús\"},{\"id\":37,\"name\":\"Western\"}]}");

        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genreNames);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);

        yearPicker.setMinValue(1990);
        yearPicker.setMaxValue(2023);

        ratingBar.setNumStars(10);
        ratingBar.setStepSize(0.5f);

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        cardViewMoviesList = findViewById(R.id.cardViewMoviesList);

        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));

        // Módosított sor: most már nincs szükség argumentumra
        adapter = new MoviesPagerAdapter();

        recyclerViewMovies.setAdapter(adapter);

        // Swipe funkció hozzáadása
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeAt(viewHolder.getAdapterPosition());
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerViewMovies);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMovies(); // Ezt a metódust kell módosítani, hogy frissítse az adaptert a lekért filmekkel
                cardViewMoviesList.setVisibility(View.VISIBLE);
            }
        });
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardViewMoviesList.setVisibility(View.GONE);
            }
        });
    }

    private void processGenres(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray genresArray = jsonObject.getJSONArray("genres");
            for (int i = 0; i < genresArray.length(); i++) {
                JSONObject genreObject = genresArray.getJSONObject(i);
                int id = genreObject.getInt("id");
                String name = genreObject.getString("name");
                genreMap.put(name, id);
                genreNames.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchMovies() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String selectedGenreName = genreSpinner.getSelectedItem().toString();
                    Integer selectedGenreId = genreMap.get(selectedGenreName);
                    if (selectedGenreId == null) {
                        return;
                    }

                    int selectedYear = yearPicker.getValue();
                    final float selectedRating = ratingBar.getRating();

                    String response = ApiManager.performApiCall(selectedYear, selectedGenreId.toString(), selectedRating);

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    ArrayList<Movie> movies = new ArrayList<>();

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject movieObject = resultsArray.getJSONObject(i);
                        float movieRating = (float) movieObject.getDouble("vote_average");
                        if (movieRating >= selectedRating) {
                            String title = movieObject.getString("title");
                            String posterPath = movieObject.getString("poster_path");
                            Double voteAverage = movieObject.getDouble("vote_average");
                            movies.add(new Movie(title, posterPath, voteAverage));
                        }
                    }

                    runOnUiThread(() -> {
                        if (!movies.isEmpty()) {
                            adapter.updateMovies(movies); // Frissítsd az adaptert az új filmekkel
                            cardViewMoviesList.setVisibility(View.VISIBLE); // Tedd láthatóvá a CardView-t
                        } else {
                            Toast.makeText(MainActivity.this, "Nem található film a megadott szűrőkkel.", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Hiba történt az adatok betöltésekor.", Toast.LENGTH_LONG).show());
                }
            }
        }).start();
    }
}
