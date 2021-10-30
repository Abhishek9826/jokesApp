package com.example.jokesapp.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.jokesapp.R;
import com.example.jokesapp.modal.Joke;

public  class CardDataAdapter extends ArrayAdapter<String> {

    private  Context mContext;
    private boolean clicked = true;
    private JokeLikeListener mJokeLikeListener;
    private Joke mJoke;
    private SharedPreferences mSharedPreferences;

    public CardDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mJokeLikeListener = (JokeLikeListener)context;
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        //supply the layout for your card
        TextView v = contentView.findViewById(R.id.content);
        v.setText(getItem(position));

        ImageButton likeButton = contentView.findViewById(R.id.likeButton);

        if(mSharedPreferences.contains(getItem(position))){
            likeButton.setImageResource(R.drawable.like_filled);
            clicked= false;
        }else {
            clicked = true;
        }

        likeButton.setOnClickListener(v1 -> {

            if (clicked){
                likeButton.setImageResource(R.drawable.like_filled);

                clicked = false;
                YoYo.with(Techniques.Tada)
                        .duration(1000)
                        .playOn(likeButton);
                mJoke = new Joke(getItem(position),true);
                mJokeLikeListener . jokeIsLiked(mJoke);
            }else {
                likeButton.setImageResource(R.drawable.like_empty);
                clicked = true;
                mJoke = new Joke(getItem(position),false);
                mJokeLikeListener . jokeIsLiked(mJoke);
            }
        });

        ImageButton imageButton = contentView.findViewById(R.id.shareButton);
         imageButton.setOnClickListener(v12 -> {
             Intent intent = new Intent(Intent.ACTION_SEND);
             String shareBody = v.getText().toString();
             intent.setType("text/plain");
             intent.putExtra(Intent.EXTRA_SUBJECT,"Mama Joke!");
             intent.putExtra(Intent.EXTRA_TEXT,shareBody);
             v.getContext().startActivity(Intent.createChooser(intent,"Share via"));


         });

        return contentView;
    }

}