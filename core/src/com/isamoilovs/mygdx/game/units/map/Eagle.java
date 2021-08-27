package com.isamoilovs.mygdx.game.units.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.ScreenManager;
import com.isamoilovs.mygdx.game.units.map.emitters.BulletEmitter;
import com.isamoilovs.mygdx.game.units.weapon.Bullet;
import com.isamoilovs.mygdx.game.utils.GameConsts;
import com.isamoilovs.mygdx.game.utils.TankOwner;

public class Eagle {
    private int hp;
    private int hpMax;

    public Vector2 getPosition() {
        return position;
    }

    private Vector2 position;
    private TextureRegion hpTexture;

    public Circle getCircle() {
        return circle;
    }

    private Circle circle;
    private TextureRegion textureRegion;

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        this.circle.setPosition(position);
    }

    public Eagle(TextureAtlas atlas) {
        this.hpMax = 10;
        this.hp = 4;
        this.position = new Vector2(0.0f,0.0f);
        this.textureRegion = atlas.findRegion("eagle");
        this.hpTexture = atlas.findRegion("hp");
        this.circle = new Circle();
        circle.setPosition(position);
        this.circle.radius = GameConsts.MAP_DEFAULT_CELL_SIZE *2;
    }

    public void render(SpriteBatch batch) {
        if(hp < hpMax) {
            batch.setColor(0, 0, 0, 0.8f);
            batch.draw(hpTexture, position.x, position.y + circle.radius / 2 - 2, 36, 12);
            batch.setColor(0, 1, 0, 0.5f);
            batch.draw(hpTexture, position.x - circle.radius / 2, position.y + circle.radius / 2 , (float) hp / hpMax * hpTexture.getRegionWidth(), hpTexture.getRegionHeight());
            batch.setColor(1, 1, 1, 1);
            batch.draw(textureRegion, position.x-circle.radius / 2, position.y - circle.radius/2, circle.radius, circle.radius);
        } else {
            batch.draw(textureRegion, position.x-circle.radius / 2, position.y - circle.radius/2, circle.radius, circle.radius);
        }
    }

    public void update(float dt) {
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if(hp <= 0) {
            ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_OVER);
        }
    }

    public void checkEagleAndBulletCollisions(BulletEmitter bulletEmitter) {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if (bullet.isActive() && circle.contains(bullet.getPosition()) && bullet.getOwner().getOwnerType() != TankOwner.PLAYER) {
                takeDamage(bullet.getDamage());
                bullet.disActivate();
            }
        }
    }
}
