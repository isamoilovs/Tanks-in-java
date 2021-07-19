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

public class PlayerTank extends Tank{
    PlayerTankMovementController movementController;
    final int ORIGIN_X;
    final int ORIGIN_Y;
    final int WIDTH;
    final int HEIGHT;
    int lives;
    public PlayerTank(MyGdxGame game, TextureAtlas atlas) {
        super(game, atlas);
        this.fireTimer = 0;
        this.movementController = new PlayerTankMovementController(this);
        this.texture = atlas.findRegion("emptyTankAtlas");
        this.tankAnimation = new TankAnimation(new TextureRegion(texture), 4, 0.6f);
        this.position = new Vector2(0.0f, 0.0f);
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.rotationAngle = 0.0f;
        this.hpMax = 10;
        this.hp = 5;
        this.lives = 5;
        this.ORIGIN_X = (tankAnimation.getFrame().getRegionWidth() / 2 - CORRECTOR_OF_CENTER) * MULTIPLIER;
        this.ORIGIN_Y = (tankAnimation.getFrame().getRegionHeight()/2) * MULTIPLIER;
        this.WIDTH = tankAnimation.getFrame().getRegionWidth() * MULTIPLIER;
        this.HEIGHT = tankAnimation.getFrame().getRegionHeight()* MULTIPLIER;
        this.circle = new Circle(position.x, position.y, WIDTH / 2);
    }



    public void fire() {
        if(fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0;
            float angleRad = (float)Math.toRadians(rotationAngle);
            game.getBulletEmitter().activate(position.x + LENGTH_OF_CANNON * (float)Math.cos(angleRad), position.y + LENGTH_OF_CANNON * (float)Math.sin(angleRad), 300.0f*(float)Math.cos(angleRad), 300.0f*(float)Math.sin(angleRad), weapon.getDamage());
        }
    }

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

    public void update(float dt) {
        fireTimer += dt;
        movementController.checkMovement(dt);
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire();
        }
        circle.setPosition(position);
    }
    public void destroy(){
        lives--;
        hp = hpMax;
    }
}
