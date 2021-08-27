package com.isamoilovs.mygdx.game.utils;

public class Player {
    public Integer score;
    public String name;
    public Player() {
        name = "...";
        score = null;
    }
    public void changeTo(Player p) {
        this.score = p.getScore();
        this.name = p.getName();
    }

    public Integer getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }
}
