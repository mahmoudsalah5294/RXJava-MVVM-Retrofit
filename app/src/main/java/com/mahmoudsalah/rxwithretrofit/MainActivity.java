package com.mahmoudsalah.rxwithretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.mahmoudsalah.rxwithretrofit.model.Movie;
import com.mahmoudsalah.rxwithretrofit.model.MovieRepo;
import com.mahmoudsalah.rxwithretrofit.model.RepoInterface;
import com.mahmoudsalah.rxwithretrofit.view.MovieDetailsView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    MovieViewModel movieViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.setOnMovieClick(new ClickInterFace() {
            @Override
            public void onMovieClick(Movie movie) {
                movieViewModel.godetailsScreen(movie.getTitle());
            }

            @Override
            public void onDeleteMovie(Movie movie) {
                movieViewModel.delete(movie);
            }
        });




        movieViewModel = new ViewModelProvider(this,new CustomFactory(MovieRepo.getInstance()))
                .get(MovieViewModel.class);
        movieViewModel.moviesMed.observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                recyclerAdapter.setMovies(movies);
            }
        });




        movieViewModel.goDetails.observe(this, new Observer<Pair<String, Boolean>>() {
            @Override
            public void onChanged(Pair<String, Boolean> stringBooleanPair) {
                if (stringBooleanPair.second)
                    startActivity(new Intent(MainActivity.this, MovieDetailsView.class).putExtra("title",stringBooleanPair.first));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieViewModel.clearDisposable();
    }
}