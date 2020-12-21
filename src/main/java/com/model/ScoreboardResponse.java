package com.model;

public class ScoreboardResponse extends Response{
    private int currentFrame;
    private Scoreboard scoreboard;
    private int rollsRemaining;
    private boolean gameDone;
    public int getCurrentFrame() {
        return currentFrame;
    }

    public boolean isGameDone() {
        return gameDone;
    }

    public void setGameDone(boolean gameDone) {
        this.gameDone = gameDone;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public int getRollsRemaining() {
        return rollsRemaining;
    }

    public void setRollsRemaining(int rollsRemaining) {
        this.rollsRemaining = rollsRemaining;
    }
}
