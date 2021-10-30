package com.example.jokesapp.modal;

public class Joke {

    private String jokeText;
    private boolean jokeIsLiked;

    public Joke(String jokeText, boolean jokeIsLiked) {
        this.jokeText = jokeText;
        this.jokeIsLiked = jokeIsLiked;
    }

    public String getJokeText() {
        return jokeText;
    }

    public void setJokeText(String jokeText) {
        this.jokeText = jokeText;
    }

    public boolean isJokeIsLiked() {
        return jokeIsLiked;
    }

    public void setJokeIsLiked(boolean jokeIsLiked) {
        this.jokeIsLiked = jokeIsLiked;
    }
}
