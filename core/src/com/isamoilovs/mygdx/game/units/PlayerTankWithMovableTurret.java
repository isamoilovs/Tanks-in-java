package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.interfaces.IRotateCannon;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class PlayerTankWithMovableTurret extends PlayerTank implements IRotateCannon {

    public PlayerTankWithMovableTurret(MyGdxGame game, TextureAtlas atlas) {
        super(game, atlas);
        this.texture = atlas.findRegion("emptyTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.circle = new Circle(position.x, position.y, WIDTH / 2);
        this.position = new Vector2(500.0f, 500.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 5;
        this.hp = hpMax;
        this.ownerType = TankOwner.PLAYER;
        weapon.setFirePeriod(0.3f);
    }

    public void update(float dt) {
        fireTimer += dt;
        checkMovement(dt);
        if(Gdx.input.isTouched()) {
            fire(dt);
        }
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        this.rotateCannonToPoint(mx, my, dt);
        circle.setPosition(position);
    }

    public void rotateCannonToPoint(float pointX, float pointY,float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        cannonRotation = Utils.makeRotation(cannonRotation, angleTo, 300, dt);
        cannonRotation = Utils.checkAngleValue(cannonRotation);
    }

}
