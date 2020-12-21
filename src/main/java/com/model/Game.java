package com.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Game {

    @Id private String id;

    private String gameName;

    private List<Frame> frames;

    private GameStatus gameStatus;

    public Game(){
        frames = new ArrayList<Frame>();
        gameStatus=GameStatus.ACTIVE;
    }

    public Game(Game other){
        this.id=gameName=other.gameName;
        this.gameStatus=other.gameStatus;
        this.frames=new ArrayList<Frame>();
        for(int i=0;i<other.getFrames().size();i++){
            this.frames.add(new Frame(other.getFrames().get(i)));
        }
    }
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
