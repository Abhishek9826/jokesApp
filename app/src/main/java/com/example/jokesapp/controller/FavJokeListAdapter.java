package com.example.jokesapp.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jokesapp.R;
import com.example.jokesapp.modal.Joke;
import com.example.jokesapp.view.FavJokeViewHolder;

import java.util.List;

public class FavJokeListAdapter extends RecyclerView.Adapter<FavJokeViewHolder> {

    private List<Joke> mJokeList;
    private Context mContext;

    public Context getmContext() {
        return mContext;
    }

    public FavJokeListAdapter(List jokeList, Context mContext) {
        this.mJokeList = jokeList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FavJokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_joke_item,parent,false);
        return  new FavJokeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FavJokeViewHolder holder, int position) {

        String jokeText = mJokeList.get(position).getJokeText();
        holder.getTxtFavJoke().setText(jokeText);
        holder.getImgButtonShare().setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String shareBody = jokeText;
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,"Mama Joke!");
            intent.putExtra(Intent.EXTRA_TEXT,shareBody);
            v.getContext().startActivity(Intent.createChooser(intent,"Share via"));

        });

    }

    @Override
    public int getItemCount() {
        return mJokeList.size();
    }
}
