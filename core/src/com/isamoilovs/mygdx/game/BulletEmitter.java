package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.units.Tank;

public class BulletEmitter {
    private TextureRegion bulletTexture;
    private Bullet[] bullets;
    public static final int MAX_BULLETS_COUNT = 500;

    public Bullet[] getBullets() {
        return bullets;
    }

    public BulletEmitter(TextureAtlas atlas) {
        this.bulletTexture = atlas.findRegion("bullet");
        this.bullets = new Bullet[MAX_BULLETS_COUNT];
        for(int i = 0; i < bullets.length ; i++) {
            this.bullets[i] = new Bullet();
        }
    }

    public void activate(Tank owner, float x, float y, float vx, float vy, int damage, float maxTime) {
        for(int i = 0; i < bullets.length; i++) {
            if(!bullets[i].isActive()) {
                bullets[i].activate(owner, x, y, vx, vy, damage, maxTime);
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < bullets.length; i++) {
            if(bullets[i].isActive())
                batch.draw(bulletTexture, bullets[i].getPosition().x - bulletTexture.getRegionWidth()/2, bullets[i].getPosition().y - bulletTexture.getRegionHeight()/2);
        }
    }

    public void update(float dt) {
        for(int i = 0; i < bullets.length; i++) {
            if(bullets[i].isActive()) {
                bullets[i].update(dt);
            }
        }
    }
}
