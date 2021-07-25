package com.isamoilovs.mygdx.game.utils;

public enum Direction {
    UP(0, 1, 90.0f, 0), DOWN(0, -1, 270.0f, 4), LEFT(-1, 0, 180.0f, 2), RIGHT(1, 0, 0.0f, 6);

    int vx;
    int vy;

    public int getIndex() {
        return index;
    }

    int index;
    float angle;

    public int getVx() {
        return vx;
    }

    public int getVy() {
        return vy;
    }
    Direction(int vx, int vy, float angle, int index) {
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.index = index;
    }

    public float getAngle() {
        return angle;
    }




}
