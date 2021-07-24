package com.isamoilovs.mygdx.game.units.tanks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.weapon.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public abstract class Tank {
    Circle circle;
    TextureRegion texture;
    TextureRegion textureHp;
    Weapon weapon;
    GameScreen gameScreen;
    Animation animation;
    TankOwner ownerType;
    float cannonRotation;
    Vector2 position;
    Vector2 tmp;
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


    public Tank(GameScreen gameScreen, TextureAtlas atlas) {
        this.gameScreen = gameScreen;
        this.tmp = new Vector2(0.0f, 0.0f);
        textureHp = atlas.findRegion("hp");
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getFrame(),
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

    public void fire() {
        if(fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0;
            float angleRad = (float)Math.toRadians(cannonRotation);
            gameScreen.getBulletEmitter().activate(this, position.x + LENGTH_OF_CANNON * (float)Math.cos(angleRad), position.y + LENGTH_OF_CANNON * (float)Math.sin(angleRad), weapon.getBulletSpeed()*(float)Math.cos(angleRad), weapon.getBulletSpeed()*(float)Math.sin(angleRad), weapon.getDamage(), weapon.getBulletLifetime());
        }
    }

    public void move(Direction direction, float dt) {
        tmp.set(position);
        tmp.add(speed * direction.getVx() * dt, speed * direction.getVy() * dt);
        if(gameScreen.getMap().isAreaClear(tmp.x, tmp.y, WIDTH / 2)) {
            rotationAngle = direction.getAngle();
            position.set(tmp);
        }
    }

    public Animation getTankAnimation() {
        return this.animation;
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

    public abstract void destroy();
    public void addScore(int amount){}

    public void rotateCannonToPoint(float pointX, float pointY,float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        cannonRotation = Utils.makeRotation(cannonRotation, angleTo, 300, dt);
        cannonRotation = Utils.checkAngleValue(cannonRotation);
    }
}
