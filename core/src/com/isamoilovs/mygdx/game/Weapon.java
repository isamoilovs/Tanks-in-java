package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {

    private float firePeriod;
    private int damage;
    private TextureRegion texture;
    private float bulletSpeed;

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setFirePeriod(float firePeriod) {
        this.firePeriod = firePeriod;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getDamage() {
        return damage;
    }


    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("cannon");
        this.firePeriod = 0.5f;
        this.damage = 1;
        this.bulletSpeed = 500.0f;
    }
}
