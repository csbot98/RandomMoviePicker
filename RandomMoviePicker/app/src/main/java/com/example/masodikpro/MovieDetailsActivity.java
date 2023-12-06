package com.example.masodikpro;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        recyclerView = findViewById(R.id.recyclerView);
        movies = (ArrayList<Movie>) getIntent().getSerializableExtra("MOVIES_LIST");

        // Beállítjuk a LayoutManager-t
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Létrehozunk és beállítunk egy adaptert
        MoviesPagerAdapter adapter = new MoviesPagerAdapter();
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.onNextMovie();
            }
        };

        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView);
    }
}
