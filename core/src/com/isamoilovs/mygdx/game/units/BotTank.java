package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;

public class BotTank extends Tank implements IRotateCannon{

    Direction preferredDirection;
    float aiTimerTo;
    float aiTimer = 0.0f;
    float cannonRotation;
    BotTankMovementController movementController;
    boolean active;

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
        preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
    }

    public BotTank(MyGdxGame game, TextureAtlas atlas) {
        super(game, atlas);
        movementController = new BotTankMovementController(this);
        this.active = false;
        this.texture = atlas.findRegion("emptyBotTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(500.0f, 500.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 5;
        this.hp = hpMax;
        this.aiTimerTo = 3.0f;
        this.preferredDirection = Direction.UP;
        this.ORIGIN_X = (tankAnimation.getFrame().getRegionWidth() / 2 - CORRECTOR_OF_CENTER) * MULTIPLIER;
        this.ORIGIN_Y = (tankAnimation.getFrame().getRegionHeight()/2) * MULTIPLIER;
        this.WIDTH = tankAnimation.getFrame().getRegionWidth() * MULTIPLIER;
        this.HEIGHT = tankAnimation.getFrame().getRegionHeight()* MULTIPLIER;
        this.circle = new Circle(position.x, position.y, WIDTH / 2);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(tankAnimation.getFrame(),
                position.x - ORIGIN_X, position.y - ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y,
                WIDTH, HEIGHT,
                1, 1, rotationAngle);

        batch.draw(weapon.getTexture(),
                position.x - ORIGIN_X, position.y - ORIGIN_Y,
                ORIGIN_X, ORIGIN_Y,
                weapon.getTexture().getRegionWidth() * MULTIPLIER, weapon.getTexture().getRegionHeight() * MULTIPLIER,
                1, 1, rotationAngle);
        if(hp < hpMax) {
            batch.setColor(0, 0, 0, 0.8f);
            batch.draw(textureHp, position.x - WIDTH / (2 * MULTIPLIER) - 2, position.y + HEIGHT / 2 - 2, 36, 12);
            batch.setColor(0, 1, 0, 0.5f);
            batch.draw(textureHp, position.x - WIDTH / (2 * MULTIPLIER), position.y + HEIGHT / 2, (float) hp / hpMax * textureHp.getRegionWidth(), textureHp.getRegionHeight());
            batch.setColor(1,1,1,1);
        }
    }

    @Override
    public void update(float dt) {
        movementController.checkMovement(dt);
        aiTimer += dt;
        if(aiTimer >= aiTimerTo) {
            aiTimer = 0;
            aiTimerTo = MathUtils.random(2.5f, 4.0f);
            preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        }
        circle.setPosition(position);
    }

    @Override
    public void fire() {

    }

    public void destroy(){
        active = false;
    }

    public void rotateCannonToPoint(float pointX, float pointY,float dt) {

    }
}
