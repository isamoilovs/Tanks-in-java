package com.isamoilovs.mygdx.game.utils;

import com.badlogic.gdx.Input;

public class KeysControl {
    public enum Targeting {
        MOUSE, KEYBOARD;
    }
    private int up;
    private int down;
    private int right;
    private int left;
    private Targeting targeting;
    private int fire;
    private int rotateCannonLeft;
    private int rotateCannonRight;

    public KeysControl(int up, int down, int right, int left, Targeting targeting, int fire, int rotateCannonLeft, int rotateCannonRight) {
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.targeting = targeting;
        this.fire = fire;
        this.rotateCannonLeft = rotateCannonLeft;
        this.rotateCannonRight = rotateCannonRight;
    }

    public static KeysControl createStandardControl1() {
        KeysControl keysControl = new KeysControl(
                Input.Keys.UP, Input.Keys.DOWN, Input.Keys.RIGHT, Input.Keys.LEFT,
                Targeting.MOUSE, 0, 0, 0);
        return keysControl;
    }

    public static KeysControl createStandardControl2() {
        KeysControl keysControl = new KeysControl(
                Input.Keys.W, Input.Keys.S, Input.Keys.D, Input.Keys.A,
                Targeting.KEYBOARD, Input.Keys.U, Input.Keys.Y, Input.Keys.I);
        return keysControl;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public int getRight() {
        return right;
    }

    public int getLeft() {
        return left;
    }

    public Targeting getTargeting() {
        return targeting;
    }

    public int getFire() {
        return fire;
    }

    public int getRotateCannonLeft() {
        return rotateCannonLeft;
    }

    public int getRotateCannonRight() {
        return rotateCannonRight;
    }
}
