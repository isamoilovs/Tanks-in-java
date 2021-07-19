package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.MyGdxGame;
import com.isamoilovs.mygdx.game.Weapon;

public abstract class Tank {
    Circle circle;
    TextureRegion texture;
    TextureRegion textureHp;
    Weapon weapon;
    MyGdxGame game;
    TankAnimation tankAnimation;
    Vector2 position;
    float speed;
    float rotationAngle;
    float fireTimer;
    int hp;
    int hpMax;

    final int MULTIPLIER = 2; //множитель размера танка (не влияет на скорость)
    final int LENGTH_OF_CANNON = 20 * MULTIPLIER; //рассчет длины пушки для отрисовки выстрела из дула пушки
    final int CORRECTOR_OF_CENTER = 20 - 16; //число пикселей, на которое смещен желаемый центр вращения текстуры относительно оси У


    public Tank(MyGdxGame game, TextureAtlas atlas) {
        this.game = game;
        textureHp = atlas.findRegion("hp");
    }

    public abstract void render(SpriteBatch batch);
    public abstract void update(float dt);
    public abstract void fire();

    public TankAnimation getTankAnimation() {
        return this.tankAnimation;
    }

    public Circle getCircle() {
        return this.circle;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
            destroy();
        }
    }

    public abstract void destroy();
}
