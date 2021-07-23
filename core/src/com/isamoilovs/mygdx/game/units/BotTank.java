package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.isamoilovs.mygdx.game.GameScreen;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class BotTank extends Tank {
    float aiTimerTo;
    float aiTimer;
    boolean active;
    float pursuitRadius;
    Direction preferredDirection;
    Vector3 lastPosition;


    public boolean isActive() {
        return active;
    }

    public void activate(float x, float y) {
        active = true;
        this.hp = hpMax;
        position.set(x, y);
        aiTimer = 0.0f;
        preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
    }

    public BotTank(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);
        this.active = false;
        this.texture = atlas.findRegion("emptyBotTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(500.0f, 500.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.lastPosition = new Vector3(0.0f, 0.0f, 0.0f);
        this.hpMax = 5;
        this.pursuitRadius = 300.0f;
        this.hp = hpMax;
        this.aiTimerTo = 3.0f;
        this.aiTimer = 0.0f;
        this.ownerType = TankOwner.AI;
        this.preferredDirection = Direction.UP;
        this.circle = new Circle(position.x, position.y, WIDTH / 2);
    }

    @Override
    public void update(float dt) {
        aiTimer += dt;
        if(aiTimer >= aiTimerTo) {
            aiTimer = 0;
            aiTimerTo = MathUtils.random(3.5f, 6.0f);
            preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        }

        PlayerTank preferredTarget = null;
        if(gameScreen.getPlayers().size() == 1) {
            preferredTarget = gameScreen.getPlayers().get(0);
        } else {
            float minDst = Float.MAX_VALUE;
            for (int i = 0; i < gameScreen.getPlayers().size(); i++) {
                PlayerTank player = gameScreen.getPlayers().get(i);
                float dst = this.position.dst(player.getPosition());
                if(dst < minDst) {
                    minDst = dst;
                    preferredTarget = player;
                }
            }
        }
        float dst = this.position.dst(preferredTarget.getPosition());
        if(dst <= pursuitRadius) {
            rotateCannonToPoint(preferredTarget.getPosition().x, preferredTarget.getPosition().y, dt);
            fire();
        } else {
            cannonRotation = Utils.makeRotation(cannonRotation, rotationAngle, 180, dt);
            rotationAngle = Utils.checkAngleValue(rotationAngle);
            cannonRotation = Utils.checkAngleValue(cannonRotation);
        }

        if(Math.abs(position.x - lastPosition.x) < 0.5f && Math.abs(position.y - lastPosition.y) < 0.5f && rotationAngle == preferredDirection.getAngle()) {
            lastPosition.z += dt;
            if(lastPosition.z > 0.2f) {
                aiTimer += 10.0f;
            }
        } else {
            lastPosition.x = position.x;
            lastPosition.y = position.y;
            lastPosition.z = 0.0f;
        }
        fireTimer += dt;
        checkMovement(dt);
        circle.setPosition(position);
    }

    public void destroy(){
        active = false;
    }

    public void checkMovement(float dt) {
        if(preferredDirection == Direction.LEFT) {
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

        } else if(preferredDirection == Direction.RIGHT) {
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

        } else if(preferredDirection == Direction.UP) {
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

        } else if(preferredDirection == Direction.DOWN) {
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
