package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.utils.TankOwner;

public abstract class Tank {
    Circle circle;
    TextureRegion texture;
    TextureRegion textureHp;
    Weapon weapon;
    MyGdxGame game;
    TankAnimation tankAnimation;

    public TankOwner getOwnerType() {
        return ownerType;
    }

    TankOwner ownerType;
    float cannonRotation;

    public Vector2 getPosition() {
        return position;
    }

    Vector2 position;
    float speed;
    float rotationAngle;
    float fireTimer;
    int hp;
    int hpMax;
    final int TEXTURE_RESOLUTION = 32;
    final int MULTIPLIER = 2; //множитель размера танка (не влияет на скорость)
    final int LENGTH_OF_CANNON = 20 * MULTIPLIER; //рассчет длины пушки для отрисовки выстрела из дула пушки
    final int CORRECTOR_OF_CENTER = 20 - 16; //число пикселей, на которое смещен желаемый центр вращения текстуры относительно оси У
    final int ORIGIN_X = (TEXTURE_RESOLUTION / 2 - CORRECTOR_OF_CENTER) * MULTIPLIER;
    final int ORIGIN_Y = (TEXTURE_RESOLUTION / 2) * MULTIPLIER;
    final int WIDTH = TEXTURE_RESOLUTION * MULTIPLIER;
    final int HEIGHT = TEXTURE_RESOLUTION * MULTIPLIER;
    final int ROTATION_SPEED = 180;
    final float BULLET_SPEED = 1200.0f;


    public Tank(MyGdxGame game, TextureAtlas atlas) {
        this.game = game;
        textureHp = atlas.findRegion("hp");
    }

    public void render(SpriteBatch batch) {
        batch.draw(tankAnimation.getFrame(),
                position.x - ORIGIN_X,
                position.y - ORIGIN_Y,
                ORIGIN_X,
                ORIGIN_Y,
                WIDTH,
                HEIGHT,
                1, 1, rotationAngle);

        batch.draw(weapon.getTexture(),
                position.x - ORIGIN_X, position.y - ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y,
                weapon.getTexture().getRegionWidth() * MULTIPLIER, weapon.getTexture().getRegionHeight() * MULTIPLIER,
                1, 1, cannonRotation);

        if(hp < hpMax) {
            batch.setColor(0, 0, 0, 0.8f);
            batch.draw(textureHp, position.x - WIDTH / (2 * MULTIPLIER) - 2, position.y + HEIGHT / 2 - 2, 36, 12);
            batch.setColor(0, 1, 0, 0.5f);
            batch.draw(textureHp, position.x - WIDTH / (2 * MULTIPLIER), position.y + HEIGHT / 2, (float) hp / hpMax * textureHp.getRegionWidth(), textureHp.getRegionHeight());
            batch.setColor(1,1,1,1);
        }
    }

    public abstract void update(float dt);

    public void fire(float dt) {
        if(fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0;
            float angleRad = (float)Math.toRadians(cannonRotation);
            game.getBulletEmitter().activate(this, position.x + LENGTH_OF_CANNON * (float)Math.cos(angleRad), position.y + LENGTH_OF_CANNON * (float)Math.sin(angleRad), weapon.getBulletSpeed()*(float)Math.cos(angleRad), weapon.getBulletSpeed()*(float)Math.sin(angleRad), weapon.getDamage());
        }
    }

    public TankAnimation getTankAnimation() {
        return this.tankAnimation;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }
    public abstract void checkMovement(float dt);
    public abstract void destroy();
}
