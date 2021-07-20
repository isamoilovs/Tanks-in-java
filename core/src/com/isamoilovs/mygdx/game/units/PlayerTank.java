package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class PlayerTank extends Tank{
    int lives;

    public PlayerTank(MyGdxGame game, TextureAtlas atlas) {
        super(game, atlas);
        this.fireTimer = 0;
        this.ownerType = TankOwner.PLAYER;
        this.texture = atlas.findRegion("emptyTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(0.0f, 0.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 10;
        this.hp = 5;
        this.lives = 5;
        this.circle = new Circle(position.x, position.y, WIDTH / 2);
    }

    public void update(float dt) {
        fireTimer += dt;
        cannonRotation = rotationAngle;
        checkMovement(dt);
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire(dt);
        }
        circle.setPosition(position);
    }
    public void destroy(){
        lives--;
        hp = hpMax;
    }

    public void checkMovement(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            while (rotationAngle != 180) {
                rotationAngle = Utils.makeRotation(rotationAngle, 180, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if(position.x < 0) {
                position.x = 0;
                return;
            } else {
                position.x -= speed * dt;
                getTankAnimation().update(dt);
            }


        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            while (rotationAngle != 0) {
                rotationAngle = Utils.makeRotation(rotationAngle, 0, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if(position.x > Gdx.graphics.getWidth()) {
                position.x = Gdx.graphics.getWidth();
                return;
            } else {
                position.x += speed * dt;
                getTankAnimation().update(dt);
            }

        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            while (rotationAngle != 90) {
                rotationAngle = Utils.makeRotation(rotationAngle, 90, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if(position.y > Gdx.graphics.getHeight()) {
                position.y = Gdx.graphics.getHeight();
                return;
            } else {
                getTankAnimation().update(dt);
                position.y += speed * dt;
            }

        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            while (rotationAngle != -90) {
                rotationAngle = Utils.makeRotation(rotationAngle, -90, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if(position.y < 0) {
                position.y = 0;
                return;
            } else {
                getTankAnimation().update(dt);
                position.y -= speed * dt;
            }
        }
    }
}
