package com.mahmoudsalah.rxwithretrofit.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mahmoudsalah.rxwithretrofit.MovieViewModel;
import com.mahmoudsalah.rxwithretrofit.R;
import com.mahmoudsalah.rxwithretrofit.model.Movie;

public class MovieDetailsView extends AppCompatActivity {
    TextView titleTxt, detailsTxt;
    ImageView imageView;
    MovieViewModel movieViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        titleTxt = findViewById(R.id.titleTxt);
        detailsTxt = findViewById(R.id.detailsTxt);
        imageView = findViewById(R.id.imageView);

        movieViewModel = MovieViewModel.getInstance();

        String title = getIntent().getStringExtra("title");
        Movie movie = movieViewModel.getMovieByTitle(title);
        if (movie == null){
            Toast.makeText(this, "nullllllll", Toast.LENGTH_SHORT).show();
        }else {
            titleTxt.setText(movie.getTitle());
            detailsTxt.setText(movie.getRating());
            Glide.with(this).load(movie.getImage()).into(imageView);
        }
    }




}