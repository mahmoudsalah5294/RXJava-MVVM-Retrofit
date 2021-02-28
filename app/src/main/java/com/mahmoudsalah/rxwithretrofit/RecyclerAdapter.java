package com.mahmoudsalah.rxwithretrofit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mahmoudsalah.rxwithretrofit.model.Movie;


import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
Context topContext;
ArrayList<Movie> data;
public static final String TAG = "here";
private ClickInterFace clickInterFace;

    public RecyclerAdapter(Context context) {
        topContext = context;

    }
    public void setOnMovieClick(ClickInterFace click){
        clickInterFace = click;
    }
    public void setMovies(ArrayList<Movie> movies){
        data = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView nameTextView,descriptionTextView;
        public ConstraintLayout constraintLayout;
        public View parent;
        private ImageButton delete;
        private ConstraintLayout background;



        public ViewHolder(@NonNull View v) {
            super(v);
            parent = v;
            constraintLayout = v.findViewById(R.id.constraintLayout);
            imageView = v.findViewById(R.id.movieImage);
            nameTextView = v.findViewById(R.id.movieTitle);
            delete = v.findViewById(R.id.deleteBtn);
            background = v.findViewById(R.id.background);

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.movie_layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        Log.i(TAG,"----OnCreateViewHolder-----");
        return vh;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameTextView.setText(data.get(position).getTitle());
        Glide.with(topContext).load(data.get(position).getImage()).into(holder.imageView);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterFace.onMovieClick(data.get(position));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterFace.onDeleteMovie(data.get(position));
            }
        });
        Log.i(TAG,"*****OnBindViewHolder*****");

        /*holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager input = (InputMethodManager) topContext.getSystemService(topContext.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
