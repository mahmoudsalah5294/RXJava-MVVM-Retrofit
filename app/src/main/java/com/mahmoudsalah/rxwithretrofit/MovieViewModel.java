package com.mahmoudsalah.rxwithretrofit;

import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.mahmoudsalah.rxwithretrofit.model.Movie;
import com.mahmoudsalah.rxwithretrofit.model.RepoInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MovieViewModel extends ViewModel {
RepoInterface movieRepoInterface;
MutableLiveData<ArrayList<Movie>> moviesMed = new MediatorLiveData<>();
MutableLiveData<ArrayList<Movie>> searchMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Pair<String,Boolean>> goDetails = new MutableLiveData<>();
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static MovieViewModel instance = null;
    PublishSubject<String> publishSubject = PublishSubject.create();
private MovieViewModel(){

    moviesMed.setValue(new ArrayList<>());
    goDetails.setValue(Pair.create("",false));

    publishSubject.debounce(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(new Predicate<String>() {
                @Override
                public boolean test(@NonNull String s) throws Exception {
                    if (!s.isEmpty()){
                        return true;
                    }else {
                        moviesMed.setValue(moviesMed.getValue());
                        return false;
                    }
                }
            }).distinctUntilChanged().switchMap(new Function<String, ObservableSource<List<Movie>>>() {
        @Override
        public ObservableSource<List<Movie>> apply(@NonNull String s) throws Exception {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return Observable.just(moviesMed.getValue().stream()
                        .filter(e->e.getTitle().toLowerCase().startsWith(s.toLowerCase())).collect(Collectors.toList()));

            }
            return Observable.just(new ArrayList<Movie>());
        }
    }).subscribe(new io.reactivex.Observer<List<Movie>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            compositeDisposable.add(d);
        }

        @Override
        public void onNext(@NonNull List<Movie> movies) {
            ArrayList<Movie> m = new ArrayList<>();
            m.addAll(movies);
            searchMutableLiveData.setValue(m);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.i("mahmoud",e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    });

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
    moviesMed.setValue(value);
}
public void getSearchedMovies(){
    movieRepoInterface.getSearchedMovies().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ArrayList<Movie>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull ArrayList<Movie> movies) {
                    searchMutableLiveData.setValue(movies);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

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

    public void subjectOnNext(String s){
        publishSubject.onNext(s);

    }
    public void subjectOnComplete(){
        publishSubject.onComplete();

    }

}
