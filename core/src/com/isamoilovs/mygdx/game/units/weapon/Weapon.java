package com.isamoilovs.mygdx.game.units.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {

    private float firePeriod;
    private int damage;
    private TextureRegion texture;
    private float bulletSpeed;

    public float getBulletLifetime() {
        return bulletLifetime;
    }

    private float bulletLifetime;

    public float getRadius() {
        return radius;
    }

    private float radius;



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


    public Weapon(TextureAtlas atlas, float firePeriod, float bulletSpeed) {
        this.texture = atlas.findRegion("cannon");
        this.firePeriod = firePeriod;
        this.damage = 1;
        this.bulletSpeed = 1000.0f;
        this.radius = 500.0f;
        this.bulletLifetime = radius / bulletSpeed;
    }
}
