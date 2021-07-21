package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.GameScreen;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class PlayerTank extends Tank{
    int lives;
    int score;

    public PlayerTank(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);
        this.fireTimer = 0;
        this.ownerType = TankOwner.PLAYER;
        this.texture = atlas.findRegion("emptyTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(500.0f, 450.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 10;
        this.hp = 5;
        this.lives = 5;
        this.circle = new Circle(position.x, position.y, (float) WIDTH / 2);
    }

    public void update(float dt) {
        fireTimer += dt;
        cannonRotation = rotationAngle;
        circle.setPosition(position);
        checkMovement(dt);
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            fire();
        }
    }
    public void destroy(){
        lives--;
        hp = hpMax;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        font24.draw(batch, "Score: " + score + "\nLives: " + lives, 50, 680);
    }

    public void checkMovement(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            while (rotationAngle != Direction.LEFT.getAngle()) {
                rotationAngle = Utils.makeRotation(rotationAngle, 180, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if (position.x - (float) WIDTH / 2 < 0.0f) {
                position.x = (float) WIDTH / 2;
            }
            getTankAnimation().update(dt);
            move(Direction.LEFT, dt);

        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            while (rotationAngle != Direction.RIGHT.getAngle()) {
                rotationAngle = Utils.makeRotation(rotationAngle, 0, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if(position.x + (float) WIDTH / 2 > Gdx.graphics.getWidth()) {
                position.x = Gdx.graphics.getWidth() - (float) WIDTH / 2;
            }
            move(Direction.RIGHT, dt);
            getTankAnimation().update(dt);

        } else if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            while (rotationAngle != Direction.UP.getAngle()) {
                rotationAngle = Utils.makeRotation(rotationAngle, 90, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if(position.y + (float) (HEIGHT / 2) > Gdx.graphics.getHeight()) {
                position.y = Gdx.graphics.getHeight() - (float)(HEIGHT / 2);
            }
            move(Direction.UP, dt);
            getTankAnimation().update(dt);

        } else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            while (rotationAngle != Direction.DOWN.getAngle()) {
                rotationAngle = Utils.makeRotation(rotationAngle, -90, ROTATION_SPEED, dt);
                rotationAngle = Utils.checkAngleValue(rotationAngle);
                getTankAnimation().update(dt);
                return;
            }
            if(position.y - (float) (HEIGHT / 2) < 0) {
                position.y = (float) (HEIGHT / 2);
            }
            move(Direction.DOWN, dt);
            getTankAnimation().update(dt);
        }
    }
}
