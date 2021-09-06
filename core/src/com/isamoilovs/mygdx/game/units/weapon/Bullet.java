package com.isamoilovs.mygdx.game.units.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.units.tanks.Animation;
import com.isamoilovs.mygdx.game.units.tanks.Tank;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.GameConsts;

public class Bullet {
    private Vector2 position;
    private Rectangle bulletRectangle;
    private Vector2 velocity;
    private boolean active;
    private Tank owner;
    private float currentTime;
    private float maxTime;

    public void setBulletDirection(Direction bulletDirection) {
        this.bulletDirection = bulletDirection;
    }

    public Direction getBulletDirection() {
        return bulletDirection;
    }

    private Direction bulletDirection;
    float deathTimer;
    float timeToAnimate;
    Boolean isAbleToMove;
    Boolean hasBeenDestroyed;
    Animation bulletDeathAnimation;

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

    public boolean isDestroyed() {
        return hasBeenDestroyed;
    }

    public Bullet(TextureAtlas atlas) {
        this.bulletDirection = Direction.UP;
        hasBeenDestroyed = false;
        deathTimer = 0.0f;
        timeToAnimate = 0.5f;
        this.bulletDeathAnimation = new Animation(new TextureRegion(atlas.findRegion("explosionOfbullet16")), 3, timeToAnimate);
        this.position = new Vector2();
        this.bulletRectangle = new Rectangle();
        this.bulletRectangle.setPosition(position.x - GameConsts.BULLET_WIDTH / 2, position.y - GameConsts.BULLET_WIDTH/2);
        this.bulletRectangle.setWidth(GameConsts.BULLET_WIDTH);
        this.bulletRectangle.setHeight(GameConsts.BULLET_WIDTH);
        this.velocity = new Vector2();
        this.position.x = 240.0f;
        this.position.y = 60.0f;
        this.velocity.x = 0.0f;
        this.velocity.y = 0.0f;
        this.damage = 0;
        disActivate();
    }

    public void renderBulletExplosion(SpriteBatch batch) {
        batch.draw(bulletDeathAnimation.getFrame(),
                position.x - GameConsts.TANK_WIDTH/2,
                position.y - GameConsts.TANK_HEIGHT/2,
                GameConsts.TANK_WIDTH, GameConsts.TANK_HEIGHT);
    }

    public void updateBulletExplosion(float dt) {
        if(hasBeenDestroyed) {
            deathTimer += dt;
            bulletDeathAnimation.update(dt);
            if(deathTimer >= timeToAnimate) {
                hasBeenDestroyed = false;
                deathTimer = 0.0f;
                isAbleToMove = true;
            }
        }
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, int damage, float maxTime) {
        this.bulletDirection = owner.getPreferredDirection();
        this.owner = owner;
        this.active = true;
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
        hasBeenDestroyed = true;
    }

    public boolean isActive() {
        return active;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        this.bulletRectangle.setPosition(position.x - GameConsts.BULLET_WIDTH / 2, position.y - GameConsts.BULLET_WIDTH/2);
//
//        currentTime += dt;
//        if(currentTime >= maxTime) {
//            disActivate();
//        }

        if(position.x <= GameConsts.MAP_DEFAULT_DX || position.x >= Gdx.graphics.getWidth() - GameConsts.MAP_DEFAULT_DX || position.y <= GameConsts.MAP_DEFAULT_DY || position.y >= Gdx.graphics.getHeight() - GameConsts.MAP_DEFAULT_DY)
            disActivate();
    }

    public Rectangle getBulletRectangle() {
        return bulletRectangle;
    }
}
