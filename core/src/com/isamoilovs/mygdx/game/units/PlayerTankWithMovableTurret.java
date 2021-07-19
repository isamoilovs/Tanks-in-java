package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.utils.Utils;

public class PlayerTankWithMovableTurret extends PlayerTank implements IRotateCannon {

    private float cannonRotation;

    public PlayerTankWithMovableTurret(MyGdxGame game, TextureAtlas atlas) {
        super(game, atlas);
        this.texture = atlas.findRegion("emptyTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.circle = new Circle(position.x, position.y, WIDTH / 2);
        this.position = new Vector2(500.0f, 500.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 5;
        this.hp = hpMax;
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

        batch.draw(new TextureRegion(weapon.getTexture()),
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

    public void update(float dt) {
        fireTimer += dt;
        movementController.checkMovement(dt);
        if(Gdx.input.isTouched()) {
            fire(dt);
        }
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        this.rotateCannonToPoint(mx, my, dt);
        circle.setPosition(position);
    }

    public void rotateCannonToPoint(float pointX, float pointY,float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        cannonRotation = Utils.makeRotation(cannonRotation, angleTo, 180, dt);
        cannonRotation = Utils.checkAngleValue(cannonRotation);
    }

    public void fire() {}
    public void fire(float dt) {
        if(fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0;
            float angleRad = (float)Math.toRadians(cannonRotation);
            game.getBulletEmitter().activate(position.x + LENGTH_OF_CANNON * (float)Math.cos(angleRad), position.y + LENGTH_OF_CANNON * (float)Math.sin(angleRad), 300.0f*(float)Math.cos(angleRad), 300.0f*(float)Math.sin(angleRad), weapon.getDamage());
        }
    }
}
