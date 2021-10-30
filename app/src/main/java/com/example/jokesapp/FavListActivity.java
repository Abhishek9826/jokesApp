package com.example.jokesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.jokesapp.fragment.FavJokeFragment;

public class FavListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_list);
        FavJokeFragment   mFavJokeFragment = FavJokeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fav_joke_container,mFavJokeFragment).commit();

    }
}