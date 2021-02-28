package com.mahmoudsalah.rxwithretrofit;


import com.mahmoudsalah.rxwithretrofit.model.Movie;

public interface ClickInterFace {

    void onMovieClick(Movie movie);
    void onDeleteMovie(Movie movie);
}
