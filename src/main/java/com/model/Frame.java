package com.model;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    List<Roll> rolls;

    public Frame(){
        rolls = new ArrayList<>();
    }

    public Frame(Frame other){
        rolls = new ArrayList<>();
        for(int i=0;i<other.getRolls().size();i++){
            rolls.add(new Roll(other.getRolls().get(i)));
        }
    }
    public List<Roll> getRolls() {
        return rolls;
    }

    public void setRolls(List<Roll> rolls) {
        this.rolls = rolls;
    }
}
