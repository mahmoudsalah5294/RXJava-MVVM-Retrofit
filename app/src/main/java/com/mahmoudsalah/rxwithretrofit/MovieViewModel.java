package com.mahmoudsalah.rxwithretrofit;

import android.os.Build;
import android.util.Pair;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.mahmoudsalah.rxwithretrofit.model.Movie;
import com.mahmoudsalah.rxwithretrofit.model.RepoInterface;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {
RepoInterface movieRepoInterface;
MutableLiveData<ArrayList<Movie>> moviesMed = new MediatorLiveData<>();
    MutableLiveData<Pair<String,Boolean>> goDetails = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static MovieViewModel instance = null;
private MovieViewModel(){

    moviesMed.setValue(new ArrayList<>());
    goDetails.setValue(Pair.create("",false));

}

    public void setMovieRepoInterface(RepoInterface movieRepoInterface) {
        this.movieRepoInterface = movieRepoInterface;
    }

    public static MovieViewModel getInstance(){
    if (instance==null){
        synchronized (MovieViewModel.class){
            if (instance==null){
                instance = new MovieViewModel();
            }
        }
    }
    return  instance;
}

public void getMovies(){
    movieRepoInterface.getMovies().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ArrayList<Movie>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onNext(@NonNull ArrayList<Movie> movies) {
                    moviesMed.setValue(movies);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

}
public void delete(Movie movie){
    ArrayList<Movie> value = moviesMed.getValue();
    value.remove(movie);
    moviesMed.setValue(value);;
}
public void godetailsScreen(String title){
    goDetails.setValue(Pair.create(title,true));
}
public Movie getMovieByTitle(String title){
        Movie movie = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        movie = moviesMed.getValue().stream().filter(e->e.getTitle().equals(title)).findFirst().get();
    }
    return movie;
}

    public void clearDisposable() {
        compositeDisposable.dispose();;
    }

}
