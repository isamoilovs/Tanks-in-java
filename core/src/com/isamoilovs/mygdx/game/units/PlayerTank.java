package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;

public class PlayerTank extends Tank{
    PlayerTankMovementController movementController;
    final int ORIGIN_X;
    final int ORIGIN_Y;
    final int WIDTH;
    final int HEIGHT;

    public PlayerTank(MyGdxGame game) {
        super(game);
        this.movementController = new PlayerTankMovementController(this);
        this.texture = new Texture("emptyTankAtlas.png");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(0.0f, 0.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon();
        this.rotationAngle = 0.0f;
        this.ORIGIN_X = (tankAnimation.getFrame().getRegionWidth() / 2 - CORRECTOR_OF_CENTER) * MULTIPLIER;
        this.ORIGIN_Y = (tankAnimation.getFrame().getRegionHeight()/2) * MULTIPLIER;
        this.WIDTH = tankAnimation.getFrame().getRegionWidth() * MULTIPLIER;
        this.HEIGHT = tankAnimation.getFrame().getRegionHeight()* MULTIPLIER;
    }

    public void fire() {
        float angleRad = (float)Math.toRadians(rotationAngle);
        game.getBulletEmitter().activate(
                position.x + LENGTH_OF_CANNON * (float)Math.cos(angleRad),
                position.y + LENGTH_OF_CANNON * (float)Math.sin(angleRad),
                300.0f*(float)Math.cos(angleRad),
                300.0f*(float)Math.sin(angleRad), weapon.getDamage());
    }

    public void render(SpriteBatch batch) {
        batch.draw(tankAnimation.getFrame(),
                position.x - ORIGIN_X, position.y - ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y,
                WIDTH, HEIGHT,
                1, 1, rotationAngle);

        batch.draw(new TextureRegion(weapon.getTexture()),
                position.x - ORIGIN_X, position.y - ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y,
                weapon.getTexture().getWidth() * MULTIPLIER, weapon.getTexture().getHeight() * MULTIPLIER,
                1, 1, rotationAngle);
    }

    public void update(float dt) {
        movementController.checkMovement(dt);
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire();
        }
    }
}
