package com.isamoilovs.mygdx.game.units.tanks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.weapon.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.GameConsts;
import com.isamoilovs.mygdx.game.utils.TankOwner;

public abstract class Tank {
    float deathTimer;
    float timeToAnimate;
    Boolean isAbleToMove;
    Boolean hasBeenDestroyed;
    Animation tankDeathAnimation;
    boolean active;
    Rectangle rectangle;
    TextureRegion[][] texture;
    TextureRegion textureHp;
    Weapon weapon;
    Direction preferredDirection;
    GameScreen gameScreen;
    TankOwner ownerType;
    Vector2 position;
    Vector2 tmp;
    float speed;
    int hp;
    int hpMax;
    int currentFrame;
    float currentFrameTime;

    public Boolean isDestroyed() {
        return hasBeenDestroyed;
    }

    public Tank(GameScreen gameScreen, TextureAtlas atlas) {
        this.preferredDirection = Direction.UP;
        this.deathTimer = 0.0f;
        this.timeToAnimate = 0.3f;
        this.isAbleToMove = true;
        this.hasBeenDestroyed = false;
        this.gameScreen = gameScreen;
        this.tmp = new Vector2(0.0f, 0.0f);
        this.textureHp = atlas.findRegion("hp");
        this.tankDeathAnimation = new Animation((atlas.findRegion("explosionOfTank32")), 2, timeToAnimate);
    }

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public abstract void fire();

    public abstract void destroy();

    public void addScore(int amount){}

    public void move(Direction direction, float dt) {
        if(isAbleToMove) {
            tmp.set(position);
            tmp.add(speed * direction.getVx() * dt, speed * direction.getVy() * dt);
            if (gameScreen.getMap().isAreaClear(tmp.x, tmp.y, GameConsts.TANK_WIDTH / 2 - 2)) {
                position.set(tmp);
                currentFrameTime += dt;
                if (currentFrameTime >= GameConsts.TANK_FRAME_TIME) {
                    currentFrameTime = 0.0f;
                    currentFrame = (currentFrame > 0) ? 0 : 1;
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public TankOwner getOwnerType() {
        return ownerType;
    }

    public void renderTankExplosion(SpriteBatch batch) {
        Vector2 deathPosition = new Vector2(position);
        deathPosition.x -= GameConsts.TANK_WIDTH;
        deathPosition.y -= GameConsts.TANK_WIDTH;
        batch.draw(tankDeathAnimation.getFrame(), deathPosition.x, deathPosition.y, GameConsts.TANK_WIDTH * 2, GameConsts.TANK_WIDTH * 2);
    }

    public void updateTankExplosion(float dt) {
        System.out.println(hasBeenDestroyed + " " + deathTimer);
        if(hasBeenDestroyed) {
            deathTimer += dt;
            tankDeathAnimation.update(dt);
            if(deathTimer >= timeToAnimate) {
                hasBeenDestroyed = false;
                deathTimer = 0.0f;
                isAbleToMove = true;
            }
        }
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }


    public Direction getPreferredDirection() {
        return preferredDirection;
    }


    public Vector2 getPosition() {
        return position;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            hasBeenDestroyed = true;
            isAbleToMove = false;
            destroy();
        }
    }
}
