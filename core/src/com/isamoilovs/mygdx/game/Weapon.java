package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Weapon {

    private float firePeriod;
    private int damage;
    private Texture texture;

    public Texture getTexture() {
        return texture;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }


    public Weapon() {
        this.texture = new Texture("cannon.png");
        this.firePeriod = 0.4f;
        this.damage = 1;
    }
}
