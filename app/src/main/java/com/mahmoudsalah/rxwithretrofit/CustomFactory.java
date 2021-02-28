package com.mahmoudsalah.rxwithretrofit;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mahmoudsalah.rxwithretrofit.model.RepoInterface;


public class CustomFactory implements ViewModelProvider.Factory {
    private RepoInterface repo;

    public CustomFactory(RepoInterface repo) {
       MovieViewModel movieViewModel = MovieViewModel.getInstance();
        this.repo = repo;
        movieViewModel.setMovieRepoInterface(repo);
        movieViewModel.getMovies();
        movieViewModel.getSearchedMovies();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) MovieViewModel.getInstance();
    }
}
