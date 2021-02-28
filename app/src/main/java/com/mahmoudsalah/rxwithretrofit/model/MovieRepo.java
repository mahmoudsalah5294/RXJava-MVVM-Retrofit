package com.mahmoudsalah.rxwithretrofit.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepo implements RepoInterface {

    MutableLiveData<ArrayList<Movie>> mutableLiveData = new MutableLiveData<>();
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    public static final String URL = "https://api.androidhive.info/";
    private static MovieRepo instance = null;
    Observable<ArrayList<Movie>> moviesObservable;

    private MovieRepo(){

        retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        retrofitInterface = retrofit.create(RetrofitInterface.class);

        moviesObservable = retrofitInterface.getMovies();
        mutableLiveData.setValue(new ArrayList<>());
    }
    public static RepoInterface getInstance(){
        if (instance==null){
            synchronized (MovieRepo.class){
                if (instance==null){
                    instance = new MovieRepo();
                }
            }
        }
        return instance;

    }
    public Observable<ArrayList<Movie>> getMovies(){

        return retrofitInterface.getMovies() ;
    }

    public Observable<ArrayList<Movie>> getSearchedMovies(){

        return retrofitInterface.getMovies() ;
    }

    public void deleteMovie(Movie movie){

    }

    public Movie getMovieByTitle(String title){
//        for (Movie movie: mutableLiveData.getValue()){
//            if (movie != null)
//                if (movie.getTitle().equals(title))
//                    return movie;
//        }
        return null;
    }
    public void getCompositeDisposable(){

    }
}
