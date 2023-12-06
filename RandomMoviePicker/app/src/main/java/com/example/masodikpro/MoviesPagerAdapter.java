package com.example.masodikpro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesPagerAdapter extends RecyclerView.Adapter<MoviesPagerAdapter.ViewHolder> {

    private ArrayList<Movie> movies;
    private int currentMovieIndex;

    public MoviesPagerAdapter() {
        this.movies = new ArrayList<>();
        this.currentMovieIndex = 0;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(currentMovieIndex);
        holder.textView.setText(movie.getTitle());

        // Beállítjuk a posztert az ImageView elemre
        String posterPath = movie.getPosterPath();
        if (posterPath != null && !posterPath.isEmpty()) {
            String posterUrl = "https://image.tmdb.org/t/p/original/" + posterPath;
            Picasso.get().load(posterUrl).resize(800, 1000).into(holder.imageView);
        }

        // Beállítjuk a vote_average értékét
        double voteAverage = movie.getVoteAverage();
        holder.ratingTextView.setText("Értékelés: " + voteAverage);
    }

    @Override
    public int getItemCount() {
        return movies.isEmpty() ? 0 : 1;
    }

    public void onNextMovie() {
        if (movies.isEmpty()) {
            return;
        }
        currentMovieIndex = (currentMovieIndex + 1) % movies.size();
        notifyDataSetChanged();
    }
    public void updateMovies(ArrayList<Movie> newMovies) {
        movies = newMovies;
        currentMovieIndex = 0; // Visszaállítjuk az indexet
        notifyDataSetChanged();
    }
    public void onPreviousMovie() {
        if (movies.isEmpty()) {
            return;
        }
        currentMovieIndex = (currentMovieIndex - 1 + movies.size()) % movies.size();
        notifyDataSetChanged();
    }

    public void removeAt(int adapterPosition) {
        if (adapterPosition < 0 || adapterPosition >= movies.size()) {
            return;
        }
        movies.remove(adapterPosition);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView ratingTextView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textViewMovieTitle);
            imageView = itemView.findViewById(R.id.imageViewPoster);
            ratingTextView = itemView.findViewById(R.id.textViewRating);
        }
    }
}