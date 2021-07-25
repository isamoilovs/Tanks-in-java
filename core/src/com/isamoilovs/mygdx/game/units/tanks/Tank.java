package com.isamoilovs.mygdx.game.units.tanks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.weapon.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public abstract class Tank {

    boolean active;
    Circle circle;
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

    final int TEXTURE_RESOLUTION = 16;
    final int MULTIPLIER = 4; //множитель размера танка (не влияет на скорость)
    final int ORIGIN_X = (TEXTURE_RESOLUTION / 2) * MULTIPLIER;
    final int ORIGIN_Y = (TEXTURE_RESOLUTION / 2) * MULTIPLIER;
    final int WIDTH = TEXTURE_RESOLUTION * MULTIPLIER;
    final int HEIGHT = TEXTURE_RESOLUTION * MULTIPLIER;
    final float FRAME_TIME = 0.2f;

    public Tank(GameScreen gameScreen, TextureAtlas atlas) {
        this.gameScreen = gameScreen;
        this.tmp = new Vector2(0.0f, 0.0f);
        textureHp = atlas.findRegion("hp");
    }

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public abstract void fire();

    public void move(Direction direction, float dt) {
        tmp.set(position);
        tmp.add(speed * direction.getVx() * dt, speed * direction.getVy() * dt);
        if (gameScreen.getMap().isAreaClear(tmp.x, tmp.y, WIDTH / 2)) {
            position.set(tmp);
            currentFrameTime += dt;
            if(currentFrameTime >= FRAME_TIME) {
                currentFrameTime = 0.0f;
                currentFrame = (currentFrame > 0) ? 0 : 1;
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public TankOwner getOwnerType() {
        return ownerType;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }

    public void animate(float dt) {

    }

    public abstract void destroy();

    public void addScore(int amount) {
    }
}
