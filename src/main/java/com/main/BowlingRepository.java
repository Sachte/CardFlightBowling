package com.main;

import com.model.Game;

import java.util.HashMap;

public class BowlingRepository {
    HashMap<String, Game> map;
    public BowlingRepository(){
        map = new HashMap<>();
    }
    public Game findByGameName(String gameName){
        if(map.containsKey(gameName)){
            return map.get(gameName);
        } else{
            return null;
        }
    }
    public void save(Game game){
        map.put(game.getGameName(), game);
    }
}
