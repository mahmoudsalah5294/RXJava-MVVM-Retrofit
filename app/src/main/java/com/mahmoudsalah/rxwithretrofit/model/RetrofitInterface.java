package com.mahmoudsalah.rxwithretrofit.model;

import java.util.ArrayList;


import io.reactivex.Observable;
import retrofit2.http.GET;

public interface RetrofitInterface {

    @GET("json/movies.json")
    Observable<ArrayList<Movie>> getMovies();
}
