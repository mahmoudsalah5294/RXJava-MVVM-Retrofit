package com.mahmoudsalah.rxwithretrofit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;

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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    MovieViewModel movieViewModel;
    SearchView searchView;
    PublishSubject<String> publishSubject = PublishSubject.create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(recyclerAdapter);
        movieViewModel = new ViewModelProvider(this,new CustomFactory(MovieRepo.getInstance()))
                .get(MovieViewModel.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                movieViewModel.subjectOnComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieViewModel.subjectOnNext(newText);
                return true;
            }
        });



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





        movieViewModel.moviesMed.observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> movies) {
                recyclerAdapter.setMovies(movies);

            }
        });

        movieViewModel.searchMutableLiveData.observe(this, new Observer<ArrayList<Movie>>() {
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