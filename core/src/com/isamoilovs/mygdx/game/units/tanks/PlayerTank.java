package com.isamoilovs.mygdx.game.units.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.units.map.emitters.PerksEmitter;
import com.isamoilovs.mygdx.game.units.weapon.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.KeysControl;
import com.isamoilovs.mygdx.game.utils.TankOwner;
import com.isamoilovs.mygdx.game.utils.Utils;

public class PlayerTank extends Tank{
    KeysControl keysControl;
    int index;
    int lives;
    int score;
    float shieldTime;
    float currentShieldTimer;
    StringBuilder tmpString;
    private boolean ableToBeDamaged;

    public PlayerTank(int index, KeysControl keysControl, GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);
        this.texture = atlas.findRegion("playerTankLvl1").split(TEXTURE_RESOLUTION, TEXTURE_RESOLUTION);
        this.currentShieldTimer = 0.0f;
        this.keysControl = keysControl;
        this.index = index;
        this.ownerType = TankOwner.PLAYER;
        this.speed = 200.0f;
        this.preferredDirection = Direction.UP;
        this.weapon = new Weapon(atlas);
        this.hpMax = 10;
        this.hp = 5;
        this.lives = 1;
        this.active = true;
        this.tmpString = new StringBuilder();
        this.ableToBeDamaged = true;
        float cordX, cordY;
        do {
            cordX = MathUtils.random(Map.DEFAULT_DX, Gdx.graphics.getWidth() - Map.DEFAULT_DX);
            cordY = MathUtils.random(Map.DEFAULT_DY, Gdx.graphics.getHeight() - Map.DEFAULT_DY);
        } while (!gameScreen.getMap().isAreaClear(cordX, cordY, WIDTH / 2));
        this.position = new Vector2(cordX, cordY);
        this.circle = new Circle(position.x, position.y, (float) WIDTH / 2);
    }
     public int getScore() {
        return score;
     }

    public void render(SpriteBatch batch) {
        if(lives > 0) {
            batch.draw(texture[0][preferredDirection.getIndex() + currentFrame],
                    position.x - ORIGIN_X,
                    position.y - ORIGIN_Y, WIDTH, HEIGHT);

            if (hp < hpMax) {
                batch.setColor(0, 0, 0, 0.8f);
                batch.draw(textureHp, position.x - WIDTH / (2 * MULTIPLIER) - 2, position.y + HEIGHT / 2 - 2, 36, 12);
                batch.setColor(0, 1, 0, 0.5f);
                batch.draw(textureHp, position.x - WIDTH / (2 * MULTIPLIER), position.y + HEIGHT / 2, (float) hp / hpMax * textureHp.getRegionWidth(), textureHp.getRegionHeight());
                batch.setColor(1, 1, 1, 1);
            }
        }
    }

    public void fire() {
        double angle = Math.toRadians(preferredDirection.getAngle());
        gameScreen.getBulletEmitter().activate(this,
                position.x + MathUtils.cos((float)angle)*WIDTH/2,
                position.y + MathUtils.sin((float)angle)*WIDTH/2,
                weapon.getBulletSpeed() * preferredDirection.getVx(),
                weapon.getBulletSpeed() * preferredDirection.getVy(),
                weapon.getDamage(), weapon.getBulletLifetime());
    }

    public void update(float dt) {
        circle.setPosition(position);
        checkMovement(dt);
        if(!ableToBeDamaged) {
            currentShieldTimer +=  dt;
        }
        if(currentShieldTimer >= PerksEmitter.PerkType.SHIELD.getActionTime()) {
            removeShield();
            currentShieldTimer = 0;
        }
        if(Gdx.input.isKeyJustPressed(keysControl.getFire())) {
            fire();
        }
    }

    public void destroy(){
        if(lives > 0) {
            lives--;
            hp = hpMax;
        } else {
            active = false;
        }
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        tmpString.setLength(0);
        tmpString.append("Player: ").append(index);
        tmpString.append("\nScore: ").append(score);
        tmpString.append("\nLives: ").append(lives);
        font24.draw(batch, tmpString, 50 + (index - 1) * 200, 680);
    }

    public boolean isAbleToBeDamaged() {
        return ableToBeDamaged;
    }

    public void getShield() {
        this.ableToBeDamaged = false;
    }
    public void repair() {
        if(hp < hpMax) {
            hp = hpMax;
        } else {
            lives++;
        }
    }

    public int getLives() {
        return lives;
    }

    public void removeShield() {this.ableToBeDamaged = true;}

    public void checkMovement(float dt) {
        if(Gdx.input.isKeyPressed(keysControl.getLeft())) {
            preferredDirection = Direction.LEFT;
            if (position.x - (float) WIDTH / 2 < Map.DEFAULT_DX) {
                position.x = Map.DEFAULT_DX + (float) WIDTH / 2;
            }
            move(Direction.LEFT, dt);

        } else if(Gdx.input.isKeyPressed(keysControl.getRight())) {
            preferredDirection = Direction.RIGHT;
            if(position.x + (float) WIDTH / 2 > Gdx.graphics.getWidth() - Map.DEFAULT_DX) {
                position.x = Gdx.graphics.getWidth() - (float) WIDTH / 2 - Map.DEFAULT_DX;
            }
            move(Direction.RIGHT, dt);

        } else if(Gdx.input.isKeyPressed(keysControl.getUp())) {
            preferredDirection = Direction.UP;
            if(position.y + (float) (HEIGHT / 2) > Gdx.graphics.getHeight() - Map.DEFAULT_DY) {
                position.y = Gdx.graphics.getHeight() - (float)(HEIGHT / 2) - Map.DEFAULT_DY;
            }
            move(Direction.UP, dt);

        } else if(Gdx.input.isKeyPressed(keysControl.getDown())) {
            preferredDirection = Direction.DOWN;
            if(position.y - (float) (HEIGHT / 2) < Map.DEFAULT_DY) {
                position.y = Map.DEFAULT_DY + (float) (HEIGHT / 2);
            }
            move(Direction.DOWN, dt);
        }
    }
}
