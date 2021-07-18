package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TankAndCannon extends Tank {

    private float cannonRotation;
    private float fireTimer;

    public TankAndCannon(MyGdxGame game) {
        super(game);
        this.texture = new Texture("emptyTankAtlas.png");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(tankAnimation.getFrame(),
                (position.x - (tankAnimation.getFrame().getRegionWidth() / 2 - correctorOfCenter) * multy),
                (position.y - tankAnimation.getFrame().getRegionHeight() / 2 * multy),
                (tankAnimation.getFrame().getRegionWidth() / 2 - correctorOfCenter) * multy,
                (tankAnimation.getFrame().getRegionHeight()/2) * multy,
                tankAnimation.getFrame().getRegionWidth()* multy,
                tankAnimation.getFrame().getRegionHeight()* multy,
                1, 1, rotation);
        batch.draw(new TextureRegion(weapon.getTexture()),
                position.x - (weapon.getTexture().getWidth()/2 - correctorOfCenter) * multy,
                (position.y - weapon.getTexture().getHeight() / 2 * multy),
                (tankAnimation.getFrame().getRegionWidth() / 2 - correctorOfCenter) * multy,
                (tankAnimation.getFrame().getRegionHeight()/2) * multy,
                weapon.getTexture().getWidth() * multy, weapon.getTexture().getHeight() * multy,
                1, 1, cannonRotation);
    }

    public void update(float dt) {
        tankMovementController.checkMovement(dt);
        if(Gdx.input.isTouched()) {
            fire(dt);
        }
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        float angleTo = Utils.getAngle(position.x, position.y, mx, my);
        cannonRotation = Utils.makeRotation(cannonRotation, angleTo, 180, dt);
        cannonRotation = Utils.checkAngleValue(cannonRotation);
    }

    public void fire(float dt) {
        fireTimer += dt;
        if(fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0;
            float angleRad = (float)Math.toRadians(cannonRotation);
            int lengthOfCannon = 20 * multy;
            game.getBulletEmitter().activate(position.x + lengthOfCannon * (float)Math.cos(angleRad), position.y + lengthOfCannon * (float)Math.sin(angleRad), 300.0f*(float)Math.cos(angleRad), 300.0f*(float)Math.sin(angleRad), weapon.getDamage());
        }
    }
}
