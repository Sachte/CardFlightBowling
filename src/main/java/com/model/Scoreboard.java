package com.model;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard {
    List<ScoreBoardFrame> scores;

    public Scoreboard(){
        scores = new ArrayList<ScoreBoardFrame>();
    }

    public void addFrame(int frameNum, int frameScoreVal, String moves){
        ScoreBoardFrame frame = new ScoreBoardFrame();
        frame.setFrameNumber(frameNum);
        frame.setFrameScore(frameScoreVal);
        frame.setMoves(moves);
        scores.add(frame);
    }
    public void addFrame(int frameNum, String moves){
        ScoreBoardFrame frame = new ScoreBoardFrame();
        frame.setFrameNumber(frameNum);
        frame.setMoves(moves);
        frame.setFrameScore(-1);
        scores.add(frame);
    }

    public List<ScoreBoardFrame> getScores() {
        return scores;
    }

    public void setScores(List<ScoreBoardFrame> scores) {
        this.scores = scores;
    }

    public class ScoreBoardFrame{
        int frameNumber;
        int frameScore;
        String moves;
        public int getFrameNumber() {
            return frameNumber;
        }

        public String getMoves() {
            return moves;
        }

        public void setMoves(String moves) {
            this.moves = moves;
        }

        public void setFrameNumber(int frameNumber) {
            this.frameNumber = frameNumber;
        }

        public int getFrameScore() {
            return frameScore;
        }

        public void setFrameScore(int frameScore) {
            this.frameScore = frameScore;
        }
    }
}
