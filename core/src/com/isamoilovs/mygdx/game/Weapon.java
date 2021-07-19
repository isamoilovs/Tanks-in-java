package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {

    private float firePeriod;
    private int damage;
    private TextureRegion texture;

    public TextureRegion getTexture() {
        return texture;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }


    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("cannon");
        this.firePeriod = 0.4f;
        this.damage = 1;
    }
}
