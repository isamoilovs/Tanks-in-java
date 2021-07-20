package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.interfaces.IRotateCannon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class BotTank extends Tank implements IRotateCannon {
    Direction preferredDirection;
    float aiTimerTo;
    float aiTimer = 0.0f;
    boolean active;
    float pursuitRadius;
    final int ORIGIN_X;
    final int ORIGIN_Y;
    final int WIDTH;
    final int HEIGHT;


    public boolean isActive() {
        return active;
    }

    public void activate(float x, float y) {
        active = true;
        this.hp = hpMax;
        position.set(x, y);
        aiTimer = 0.0f;
        preferredDirection = Direction.values()[MathUtils.random(0, 1)];
    }

    public BotTank(MyGdxGame game, TextureAtlas atlas) {
        super(game, atlas);
        this.active = false;
        this.texture = atlas.findRegion("emptyBotTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(500.0f, 500.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 5;
        this.pursuitRadius = 300.0f;
        this.hp = hpMax;
        this.aiTimerTo = 3.0f;
        this.ownerType = TankOwner.AI;
        this.preferredDirection = Direction.UP;
        this.ORIGIN_X = (tankAnimation.getFrame().getRegionWidth() / 2 - CORRECTOR_OF_CENTER) * MULTIPLIER;
        this.ORIGIN_Y = (tankAnimation.getFrame().getRegionHeight()/2) * MULTIPLIER;
        this.WIDTH = tankAnimation.getFrame().getRegionWidth() * MULTIPLIER;
        this.HEIGHT = tankAnimation.getFrame().getRegionHeight()* MULTIPLIER;
        this.circle = new Circle(position.x, position.y, WIDTH / 2);
    }

    @Override
    public void update(float dt) {
        fireTimer += dt;
        aiTimer += dt;
        checkMovement(dt);
        circle.setPosition(position);
        if(aiTimer >= aiTimerTo) {
            aiTimer = 0;
            aiTimerTo = MathUtils.random(2.5f, 4.0f);
            preferredDirection = Direction.values()[MathUtils.random(0, 1)];
        }

        float dist = this.position.dst(game.getPlayer().getPosition());
        if(dist <= pursuitRadius) {
            rotateCannonToPoint(game.getPlayer().getPosition().x, game.getPlayer().getPosition().y, dt);
            fire(dt);
        } else {
            cannonRotation = Utils.makeRotation(cannonRotation, rotationAngle, 180, dt);
            rotationAngle = Utils.checkAngleValue(rotationAngle);
            cannonRotation = Utils.checkAngleValue(cannonRotation);
        }
    }

    public void destroy(){
        active = false;
    }

    public void rotateCannonToPoint(float pointX, float pointY,float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        cannonRotation = Utils.makeRotation(cannonRotation, angleTo, 180, dt);
        cannonRotation = Utils.checkAngleValue(cannonRotation);
    }

    @Override
    public void checkMovement(float dt) {
        if(preferredDirection == Direction.LEFT) {
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

        } else if(preferredDirection == Direction.RIGHT) {


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

        } else if(preferredDirection == Direction.UP) {


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

        } else if(preferredDirection == Direction.DOWN) {


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
