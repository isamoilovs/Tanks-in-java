package com.isamoilovs.mygdx.game.units.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.screens.ScreenManager;
import com.isamoilovs.mygdx.game.units.map.emitters.PerksEmitter;
import com.isamoilovs.mygdx.game.units.weapon.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.GameConsts;
import com.isamoilovs.mygdx.game.utils.KeysControl;
import com.isamoilovs.mygdx.game.utils.TankOwner;

public class PlayerTank extends Tank{
    int lives;
    KeysControl keysControl;
    int index;
    int score;
    float shieldTime;
    float currentShieldTimer;
    StringBuilder tmpString;
    private boolean ableToBeDamaged;

    public PlayerTank(int index, KeysControl keysControl, GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);
        this.texture = atlas.findRegion("playerTankLvl1").split(GameConsts.TANK_TEXTURE_RESOLUTION, GameConsts.TANK_TEXTURE_RESOLUTION);
        this.currentShieldTimer = 0.0f;
        this.keysControl = keysControl;
        this.index = index;
        this.ownerType = TankOwner.PLAYER;
        this.speed = 160.0f;
        this.preferredDirection = Direction.UP;
        this.weapon = new Weapon(atlas);
        this.hpMax = 10;
        this.hp = hpMax;
        this.lives = 3;
        this.active = true;
        this.tmpString = new StringBuilder();
        this.ableToBeDamaged = true;
        float cordX, cordY;
        rectangle = new Rectangle(0, 0, GameConsts.TANK_WIDTH, GameConsts.TANK_HEIGHT);
        do {
            cordX = MathUtils.random(GameConsts.MAP_DEFAULT_DX, Gdx.graphics.getWidth() - GameConsts.MAP_DEFAULT_DX);
            cordY = MathUtils.random(GameConsts.MAP_DEFAULT_DY, Gdx.graphics.getHeight() - GameConsts.MAP_DEFAULT_DY);
            rectangle.setPosition(cordX, cordY);
        } while (!gameScreen.getMap().isAreaClear(cordX, cordY, rectangle.getWidth() / 2));
        this.position = new Vector2(cordX, cordY);
        this.rectangle.setPosition(position.x - rectangle.getWidth() / 2, position.y - rectangle.getHeight() / 2);

    }
     public int getScore() {
        return score;
     }

    public void render(SpriteBatch batch) {
        if(hasBeenDestroyed) {
            renderTankExplosion(batch);
        }
        if(active && !hasBeenDestroyed) {
            batch.draw(texture[0][preferredDirection.getIndex() + currentFrame],
                    position.x - GameConsts.TANK_WIDTH / 2,
                    position.y - GameConsts.TANK_HEIGHT / 2, GameConsts.TANK_WIDTH, GameConsts.TANK_HEIGHT);

            if (hp < hpMax) {
                batch.setColor(0, 0, 0, 0.8f);
                batch.draw(textureHp, position.x - GameConsts.TANK_WIDTH / (2 * GameConsts.TANK_MULTIPLIER) - 2, position.y + GameConsts.TANK_HEIGHT / 2 - 2, 36, 12);
                batch.setColor(0, 1, 0, 0.5f);
                batch.draw(textureHp, position.x - GameConsts.TANK_WIDTH / (2 * GameConsts.TANK_MULTIPLIER), position.y + GameConsts.TANK_HEIGHT / 2, (float) hp / hpMax * textureHp.getRegionWidth(), textureHp.getRegionHeight());
                batch.setColor(1, 1, 1, 1);
            }
        }
    }

    public void fire() {
        if(isAbleToMove) {
            double angle = Math.toRadians(preferredDirection.getAngle());
            gameScreen.getBulletEmitter().activate(this,
                    position.x + MathUtils.cos((float) angle) * GameConsts.TANK_WIDTH / 2,
                    position.y + MathUtils.sin((float) angle) * GameConsts.TANK_WIDTH / 2,
                    weapon.getBulletSpeed() * preferredDirection.getVx(),
                    weapon.getBulletSpeed() * preferredDirection.getVy(),
                    weapon.getDamage(), weapon.getBulletLifetime());
        }
    }

    public void update(float dt) {
        updateTankExplosion(dt);
        rectangle.setPosition(position.x - rectangle.getWidth() / 2, position.y - rectangle.getHeight() / 2);
        checkMovement(dt);
        if(!ableToBeDamaged) {
            currentShieldTimer +=  dt;
        }
        if(currentShieldTimer >= PerksEmitter.PerkType.SHIELD.getActionTime()) {
            removeShield();
            currentShieldTimer = 0;
        }
        if(active && Gdx.input.isKeyJustPressed(keysControl.getFire())) {
            fire();
        }
    }

    public void destroy(){
        hasBeenDestroyed = true;
        if(lives > 1) {
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
        if (active) {
            tmpString.setLength(0);
            tmpString.append("P").append(index);
            tmpString.append("\nScore:").append(score);
            tmpString.append("\nLives:").append(lives);
            font24.draw(batch, tmpString, GameConsts.MAP_DEFAULT_DX * 5 / 4 + GameConsts.MAP_WIDTH,  Gdx.graphics.getHeight() * (0.14f - 0.1f * (index - 2)));
        } else {
            tmpString.setLength(0);
            tmpString.append("P").append(index).append("\nGame over!");
            font24.draw(batch, tmpString, GameConsts.MAP_DEFAULT_DX * 5 / 4 + GameConsts.MAP_WIDTH,  Gdx.graphics.getHeight() * (0.14f - 0.1f * (index - 2)));
        }
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
            if (position.x - (float) GameConsts.TANK_WIDTH / 2 < GameConsts.MAP_DEFAULT_DX) {
                position.x = GameConsts.MAP_DEFAULT_DX + (float) GameConsts.TANK_WIDTH / 2;
            }
            move(Direction.LEFT, dt);

        } else if(Gdx.input.isKeyPressed(keysControl.getRight())) {
            preferredDirection = Direction.RIGHT;
            if(position.x + (float) GameConsts.TANK_WIDTH / 2 > Gdx.graphics.getWidth() - GameConsts.MAP_DEFAULT_DX) {
                position.x = Gdx.graphics.getWidth() - (float) GameConsts.TANK_WIDTH / 2 - GameConsts.MAP_DEFAULT_DX;
            }
            move(Direction.RIGHT, dt);

        } else if(Gdx.input.isKeyPressed(keysControl.getUp())) {
            preferredDirection = Direction.UP;
            if(position.y + (float) (GameConsts.TANK_HEIGHT / 2) > Gdx.graphics.getHeight() - GameConsts.MAP_DEFAULT_DY) {
                position.y = Gdx.graphics.getHeight() - (float)(GameConsts.TANK_HEIGHT / 2) - GameConsts.MAP_DEFAULT_DY;
            }
            move(Direction.UP, dt);

        } else if(Gdx.input.isKeyPressed(keysControl.getDown())) {
            preferredDirection = Direction.DOWN;
            if(position.y - (float) (GameConsts.TANK_HEIGHT / 2) < GameConsts.MAP_DEFAULT_DY) {
                position.y = GameConsts.MAP_DEFAULT_DY + (float) (GameConsts.TANK_HEIGHT / 2);
            }
            move(Direction.DOWN, dt);
        }
    }
}
