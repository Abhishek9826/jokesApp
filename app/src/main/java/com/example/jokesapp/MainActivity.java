package com.example.jokesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.example.jokesapp.controller.*;
import com.example.jokesapp.modal.Joke;
import com.example.jokesapp.modal.JokeManager;
import com.wenchao.cardstack.CardStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import java.util.*;

public class MainActivity extends AppCompatActivity implements CardStack.CardEventListener,JokeLikeListener {

    CardStack mCardStack;
    CardDataAdapter mCardAdapter;
    private  List<Joke> allJoke = new ArrayList<>();


    private JokeManager mJokeManager;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private  ShakeDetector  mShakeDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCardStack = findViewById(R.id.container);

        mJokeManager = new JokeManager(this);

        mCardStack.setContentResource(R.layout.jokes_card);
        mCardStack.setStackMargin(20);

        mCardAdapter = new CardDataAdapter(this,0);


        mSensorManager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
mShakeDetector = new ShakeDetector();
mShakeDetector.setOnShakeListener(count -> {
    handleShakeEvent();
});


        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
                        try {
                            JSONObject rootObject = new JSONObject(loadJSONFromAsset());
                            JSONArray stupidJokes = rootObject.getJSONArray("stupid");
                            addjokeToArrayList(stupidJokes,allJoke);

                            JSONArray tallJokes = rootObject.getJSONArray("tall");
                            addjokeToArrayList(tallJokes,allJoke);


                            JSONArray fatJokes = rootObject.getJSONArray("fat");
                            addjokeToArrayList(fatJokes,allJoke);


                            JSONArray hairyJokes = rootObject.getJSONArray("hairy");
                            addjokeToArrayList(hairyJokes,allJoke);

                            JSONArray oldJokes = rootObject.getJSONArray("old");
                            addjokeToArrayList(oldJokes,allJoke);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        for(Joke joke : allJoke){
                            mCardAdapter.add(joke.getJokeText());
                        }
                        mCardStack.setAdapter(mCardAdapter);
                    }
                }).create().start();


mCardStack.setListener(this);



    }

    private void handleShakeEvent() {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work

                       Collections.shuffle(allJoke);
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {

                        mCardAdapter.clear();
                        mCardAdapter = new CardDataAdapter(MainActivity.this,0);
                        for(Joke joke : allJoke){
                          mCardAdapter.add(joke.getJokeText());

                        }
                        mCardStack.setAdapter(mCardAdapter);
                    }
                }).create().start();

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("jokes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private  void addjokeToArrayList(JSONArray jsonArray, List<Joke>arrayList) throws JSONException {
        if(jsonArray != null){
            for(int i =0; i<jsonArray.length(); i++){
                arrayList.add(new Joke(jsonArray.getString(i),false));
            }
            }
    }

    @Override
    public boolean swipeEnd(int section, float distance) {
        return (distance>300)? true: false;

    }

    @Override
    public boolean swipeStart(int section, float distance) {
        return true;
    }

    @Override
    public boolean swipeContinue(int section, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void discarded(int mIndex, int direction) {

    }

    @Override
    public void topCardTapped() {

    }

    @Override
    public void jokeIsLiked(Joke joke) {

        if(joke.isJokeIsLiked()){
            mJokeManager.saveJoke(joke);
        }else {
            mJokeManager.deleteJoke(joke);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        startActivity(new Intent(MainActivity.this,FavListActivity.class));

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector,mAccelerometer,SensorManager.SENSOR_DELAY_UI);

    }

    @Override
    protected void onPause() {
        super.onPause();
mSensorManager.unregisterListener(mShakeDetector);

    }
}