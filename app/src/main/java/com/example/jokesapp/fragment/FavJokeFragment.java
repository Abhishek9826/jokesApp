package com.example.jokesapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jokesapp.R;
import com.example.jokesapp.controller.FavJokeListAdapter;
import com.example.jokesapp.modal.Joke;
import com.example.jokesapp.modal.JokeManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FavJokeFragment extends Fragment {

    RecyclerView mRecyclerView;
    FavJokeListAdapter mFavJokeListAdapter;
    JokeManager mJokeManager;
    private List<Joke> mJokeList = new ArrayList<>();

    private Joke deleteJoke;

    public FavJokeFragment() {

    }

    public static FavJokeFragment newInstance() {
        FavJokeFragment fragment = new FavJokeFragment();

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mJokeManager = new JokeManager(getContext());
        mJokeList.clear();
        if(mJokeManager.retrieveJokes().size()>0){
            for(Joke joke : mJokeManager.retrieveJokes()){
                mJokeList.add(joke);
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fav_joke,container,false);
        if(view!=null){
            mRecyclerView = view.findViewById(R.id.rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mFavJokeListAdapter = new FavJokeListAdapter(mJokeList,getContext());
            mRecyclerView.setAdapter(mFavJokeListAdapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);

        }
        return  view;

    }

    ItemTouchHelper.SimpleCallback mSimpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    case  ItemTouchHelper.RIGHT:

                        deleteJoke = mJokeList.get(position);
                        mJokeManager.deleteJoke(mJokeList.get(position));
                        mJokeList.remove(position);
                        mFavJokeListAdapter.notifyItemRemoved(position);
                        mFavJokeListAdapter.notifyDataSetChanged();

                        Snackbar.make(mRecyclerView,"backUp", Snackbar.LENGTH_LONG )
                                .setAction("Undo", v -> {

                                    mJokeList.add(position,deleteJoke);
                                    mJokeManager.saveJoke(deleteJoke);
                                    mFavJokeListAdapter.notifyItemInserted(position);
                                }).show();
break;

            }
        }
    };

}