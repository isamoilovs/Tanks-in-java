package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Utils;

public class PlayerTankWithMovableTurret extends PlayerTank implements IRotateCannon {

    private float cannonRotation;
    private float fireTimer;

    public PlayerTankWithMovableTurret(MyGdxGame game) {
        super(game);
        this.texture = new Texture("emptyTankAtlas.png");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
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
                weapon.getTexture().getWidth() * MULTIPLIER, weapon.getTexture().getHeight() * MULTIPLIER,
                1, 1, cannonRotation);
    }

    public void update(float dt) {
        movementController.checkMovement(dt);
        if(Gdx.input.isTouched()) {
            fire(dt);
        }
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        this.rotateCannonToPoint(mx, my, dt);
    }

    public void rotateCannonToPoint(float pointX, float pointY,float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        cannonRotation = Utils.makeRotation(cannonRotation, angleTo, 180, dt);
        cannonRotation = Utils.checkAngleValue(cannonRotation);
    }

    public void fire() {}
    public void fire(float dt) {
        fireTimer += dt;
        if(fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0;
            float angleRad = (float)Math.toRadians(cannonRotation);
            int lengthOfCannon = 20 * MULTIPLIER;
            game.getBulletEmitter().activate(position.x + lengthOfCannon * (float)Math.cos(angleRad), position.y + lengthOfCannon * (float)Math.sin(angleRad), 300.0f*(float)Math.cos(angleRad), 300.0f*(float)Math.sin(angleRad), weapon.getDamage());
        }
    }
}
