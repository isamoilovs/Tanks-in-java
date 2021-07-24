package com.isamoilovs.mygdx.game.units.map.emitters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.isamoilovs.mygdx.game.units.tanks.BotTank;
import com.isamoilovs.mygdx.game.units.tanks.PlayerTank;
import com.isamoilovs.mygdx.game.units.weapon.Bullet;
import com.isamoilovs.mygdx.game.units.tanks.Tank;

import java.util.List;

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

    public void checkTankAndBulletCollisions(BotTank[] bots, List<PlayerTank> players, boolean friendlyFire) {
        for (int i = 0; i < bullets.length; i++) {
            Bullet bullet = bullets[i];
            if (bullet.isActive()) {
                for (int j = 0; j < bots.length; j++) {
                    BotTank bot = bots[j];
                    if (bot.isActive()) {
                        if (checkBulletOwner(bot, bullet, friendlyFire) && bot.getCircle().contains(bullet.getPosition())) {
                            bullet.disActivate();
                            bot.takeDamage(bullet.getDamage(), bullet.getOwner());
                            break;
                        }
                    }
                }

                for (int j = 0; j < players.size(); j++) {
                    PlayerTank player = players.get(j);
                    if (checkBulletOwner(player, bullet, friendlyFire) && player.getCircle().contains(bullet.getPosition())) {
                        if (player.isAbleToBeDamaged()) {
                            bullet.disActivate();
                            player.takeDamage(bullet.getDamage());
                        } else {
                            bullet.disActivate();
                        }
                    }
                }
            }
        }
    }

    public boolean checkBulletOwner(Tank tank, Bullet bullet, boolean friendlyFire) {
        if(!friendlyFire) {
            return tank.getOwnerType() != bullet.getOwner().getOwnerType();
        } else {
            return tank != bullet.getOwner();
        }
    }
}
