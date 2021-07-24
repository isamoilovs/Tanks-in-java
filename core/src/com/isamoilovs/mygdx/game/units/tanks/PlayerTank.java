package com.isamoilovs.mygdx.game.units.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.map.emitters.PerksEmitter;
import com.isamoilovs.mygdx.game.units.weapon.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.KeysControl;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class PlayerTank extends Tank{
    KeysControl keysControl;
    int index;
    int lives;
    int score;
    float shieldTime;
    float currentShieldTimer;
    StringBuilder tmpString;
    private boolean ableToBeDamaged;

    public PlayerTank(int index, KeysControl keysControl, GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);
        this.currentShieldTimer = 0.0f;
        this.keysControl = keysControl;
        this.fireTimer = 0;
        this.index = index;
        this.ownerType = TankOwner.PLAYER;
        this.texture = atlas.findRegion("emptyTankAtlas");
        this.animation = new Animation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(500.0f, 450.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 10;
        this.hp = 5;
        this.lives = 5;
        this.circle = new Circle(position.x, position.y, (float) WIDTH / 2);
        this.tmpString = new StringBuilder();
        this.ableToBeDamaged = true;
        float cordX, cordY;
        do {
            cordX = MathUtils.random(0, Gdx.graphics.getWidth());
            cordY = MathUtils.random(0, Gdx.graphics.getHeight());
        } while (!gameScreen.getMap().isAreaClear(cordX, cordY, 32));
        this.position = new Vector2(cordX, cordY);
    }

    public void update(float dt) {
        fireTimer += dt;
        circle.setPosition(position);
        checkMovement(dt);

        if(!ableToBeDamaged) {
            currentShieldTimer +=  dt;
        }
        if(currentShieldTimer >= PerksEmitter.PerkType.SHIELD.getActionTime()) {
            removeShield();
            currentShieldTimer = 0;
        }

        if (keysControl.getTargeting() == KeysControl.Targeting.MOUSE) {
            rotateCannonToPoint(gameScreen.getMousePosition().x, gameScreen.getMousePosition().y, dt);
            if(Gdx.input.isTouched()) {
                fire();
            }
        } else {
            if(Gdx.input.isKeyPressed(keysControl.getRotateCannonLeft())) {
                cannonRotation = Utils.makeRotation(cannonRotation, cannonRotation + 30.0f, 270.0f, dt);
                cannonRotation = Utils.checkAngleValue(cannonRotation);
            }

            if(Gdx.input.isKeyPressed(keysControl.getRotateCannonRight())) {
                cannonRotation = Utils.makeRotation(cannonRotation, cannonRotation - 30.0f, 270.0f, dt);
                cannonRotation = Utils.checkAngleValue(cannonRotation);
            }

            if(Gdx.input.isKeyPressed(keysControl.getFire())) {
                fire();
            }
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
        tmpString.setLength(0);
        tmpString.append("Player: ").append(index);
        tmpString.append("\nScore: ").append(score);
        tmpString.append("\nLives: ").append(lives);
        font24.draw(batch, tmpString, 50 + (index - 1) * 200, 680);
    }

    public boolean isAbleToBeDamaged() {
        return ableToBeDamaged;
    }

    public void getShield() {
        this.ableToBeDamaged = false;
    }
    public void repair() {
        if(hp < hpMax) {
            hp = hpMax;
        } else {
            lives++;
        }
    }

    public void removeShield() {this.ableToBeDamaged = true;}

    public void checkMovement(float dt) {
        if(Gdx.input.isKeyPressed(keysControl.getLeft())) {
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

        } else if(Gdx.input.isKeyPressed(keysControl.getRight())) {
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

        } else if(Gdx.input.isKeyPressed(keysControl.getUp())) {
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

        } else if(Gdx.input.isKeyPressed(keysControl.getDown())) {
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
