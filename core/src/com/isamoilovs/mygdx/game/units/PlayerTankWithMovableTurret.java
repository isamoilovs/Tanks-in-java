package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.GameScreen;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.ScreenManager;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.interfaces.IRotateCannon;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class PlayerTankWithMovableTurret extends PlayerTank implements IRotateCannon {

    public PlayerTankWithMovableTurret(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);
        this.texture = atlas.findRegion("emptyTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.circle = new Circle(position.x, position.y, WIDTH / 2);

        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 5;
        this.hp = hpMax;
        this.ownerType = TankOwner.PLAYER;
        weapon.setFirePeriod(0.3f);
        float cordX, cordY;
        do {
            cordX = MathUtils.random(0, Gdx.graphics.getWidth());
            cordY = MathUtils.random(0, Gdx.graphics.getHeight());
        } while (!gameScreen.getMap().isAreaClear(cordX, cordY, 32));
        this.position = new Vector2(cordX, cordY);
    }

    public void update(float dt) {
        fireTimer += dt;
        checkMovement(dt);
        circle.setPosition(position);
        rotateCannonToPoint(gameScreen.getMousePosition().x, gameScreen.getMousePosition().y, dt);
        if(Gdx.input.isTouched()) {
            fire();
        }
    }

    public void rotateCannonToPoint(float pointX, float pointY,float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        cannonRotation = Utils.makeRotation(cannonRotation, angleTo, 300, dt);
        cannonRotation = Utils.checkAngleValue(cannonRotation);
    }

}
