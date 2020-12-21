package com.model;

public class Roll {
    private int score;
    public Roll(){

    }
    public Roll(Roll other){
        score=other.getScore();
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
