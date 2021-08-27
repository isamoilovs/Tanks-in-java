package com.isamoilovs.mygdx.game.units.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.units.tanks.Tank;
import com.isamoilovs.mygdx.game.utils.GameConsts;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private Tank owner;
    private float currentTime;
    private float maxTime;
    public Tank getOwner() {
        return owner;
    }

    public int getDamage() {
        return damage;
    }

    private int damage;

    public Vector2 getPosition() {
        return position;
    }

    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.position.x = 240.0f;
        this.position.y = 60.0f;
        this.velocity.x = 0.0f;
        this.velocity.y = 0.0f;
        this.damage = 0;
        disActivate();
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, int damage, float maxTime) {
        this.owner = owner;
        active = true;
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.damage = damage;
        this.maxTime = maxTime;
        this.currentTime = 0.0f;
    }

    public void disActivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);

        currentTime += dt;
        if(currentTime >= maxTime) {
            disActivate();
        }

        if(position.x <= GameConsts.MAP_DEFAULT_DX || position.x >= Gdx.graphics.getWidth() - GameConsts.MAP_DEFAULT_DX || position.y <= GameConsts.MAP_DEFAULT_DY || position.y >= Gdx.graphics.getHeight() - GameConsts.MAP_DEFAULT_DY)
            disActivate();
    }
}
