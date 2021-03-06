package com.mahmoudsalah.rxwithretrofit.model;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import io.reactivex.Observable;

public interface RepoInterface {

    Observable<ArrayList<Movie>> getMovies();
    Observable<ArrayList<Movie>> getSearchedMovies();
    Movie getMovieByTitle(String title);
    void deleteMovie(Movie movie);
    void getCompositeDisposable();
}
