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
    private int fire;

    public KeysControl(int up, int down, int right, int left, int fire) {
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.fire = fire;
    }

    public static KeysControl createStandardControl1() {
        KeysControl keysControl = new KeysControl(
                Input.Keys.W, Input.Keys.S, Input.Keys.D, Input.Keys.A, Input.Keys.SPACE);
        return keysControl;
    }

    public static KeysControl createStandardControl2() {
        KeysControl keysControl = new KeysControl(
                Input.Keys.UP, Input.Keys.DOWN, Input.Keys.RIGHT, Input.Keys.LEFT, Input.Keys.NUMPAD_0);
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

    public int getFire() {
        return fire;
    }
}
