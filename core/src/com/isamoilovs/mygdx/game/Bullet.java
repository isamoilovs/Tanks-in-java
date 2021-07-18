package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public int getDamage() {
        return damage;
    }

    private int damage;

    public Vector2 getPosition() {
        return position;
    }

    Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.position.x = 0.0f;
        this.position.y = 0.0f;
        this.velocity.x = 0.0f;
        this.velocity.y = 0.0f;
        this.damage = 0;
        disActivate();
    }

    public void activate(float x, float y, float vx, float vy, int damage) {
        active = true;
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.damage = damage;
    }

    public void disActivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if(position.x < 0.0f || position.x > 1280.0f || position.y < 0.0f || position.y > 720)
            disActivate();
    }
}
