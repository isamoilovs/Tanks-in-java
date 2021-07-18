package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class Tank {
    protected Weapon weapon;
    protected MyGdxGame game;
    protected Texture texture;
    protected TankAnimation tankAnimation;
    protected MovementController tankMovementController;
    Vector2 position;
    float speed;
    public float rotation;
    protected static int multy = 2; //множитель размера танка (не влияет на скорость)
    protected static int correctorOfCenter = 4; //число пикселей, на которое смещен желаемый центр вращения текстуры относительно оси У

    public Tank(MyGdxGame game) {
        tankMovementController = new MovementController(this);
        this.game = game;
        this.texture = new Texture("emptyTankAtlas.png");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2();
        this.position.x = 0;
        this.position.y = 0;
        this.speed = 100;
        this.weapon = new Weapon();
        this.rotation = 0.0f;
    }
//
    public void render(SpriteBatch batch) {
        batch.draw(tankAnimation.getFrame(),
                (position.x - (tankAnimation.getFrame().getRegionWidth() / 2 - correctorOfCenter ) * multy), (position.y - tankAnimation.getFrame().getRegionHeight() / 2 * multy),
                (tankAnimation.getFrame().getRegionWidth() / 2 - correctorOfCenter) * multy, (tankAnimation.getFrame().getRegionHeight()/2) * multy,
                tankAnimation.getFrame().getRegionWidth()* multy, tankAnimation.getFrame().getRegionHeight()* multy,
                1, 1, rotation);
        batch.draw(new TextureRegion(weapon.getTexture()),
                position.x - (weapon.getTexture().getWidth()/2 - correctorOfCenter) * multy,
                (position.y - weapon.getTexture().getHeight() / 2 * multy),
                (tankAnimation.getFrame().getRegionWidth() / 2 - correctorOfCenter) * multy,
                (tankAnimation.getFrame().getRegionHeight()/2) * multy,
                weapon.getTexture().getWidth() * multy, weapon.getTexture().getHeight() * multy,
                1, 1, rotation);
    }

    public void update(float dt) {
        tankMovementController.checkMovement(dt);
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire();
        }
    }

    public void fire() {
        int lengthOfCannon = 20 * multy;
        float angleRad = (float)Math.toRadians(rotation);
        game.getBulletEmitter().activate(
                position.x + lengthOfCannon * (float)Math.cos(angleRad),
                position.y + lengthOfCannon * (float)Math.sin(angleRad),
                300.0f*(float)Math.cos(angleRad),
                300.0f*(float)Math.sin(angleRad), weapon.getDamage());
    }

    public void dispose() {
        texture.dispose();
    }

    public TankAnimation getTankAnimation() {
        return this.tankAnimation;
    }
}
