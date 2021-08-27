package com.isamoilovs.mygdx.game.units.tanks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.*;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.weapon.Weapon;
import com.isamoilovs.mygdx.game.utils.Direction;
import com.isamoilovs.mygdx.game.utils.GameConsts;
import com.isamoilovs.mygdx.game.utils.TankOwner;

public class BotTank extends Tank {
    float aiTimerTo;
    float aiTimer;
    float fireTimer;
    float pursuitRadius;
    Direction preferredDirection;
    Vector3 lastPosition;
    int scoreAmount;
    float timeOfHunt;
    float currentTimeOfHunt;
    private Rectangle preferredTarget;
    float dst;

    public void render(SpriteBatch batch) {
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

    public void activate(float x, float y) {
        active = true;
        this.hp = hpMax;
        position.set(x, y);
        rectangle.setPosition(x - rectangle.getWidth()/2, y- rectangle.getHeight()/2);
        aiTimer = 0.0f;
        preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
    }

    public BotTank(GameScreen gameScreen, TextureAtlas atlas) {
        super(gameScreen, atlas);
        this.texture = atlas.findRegion("playerTankLvl1").split(GameConsts.TANK_TEXTURE_RESOLUTION, GameConsts.TANK_TEXTURE_RESOLUTION);
        this.active = false;
        this.speed = 100.0f;
        this.weapon = new Weapon(atlas);
        this.lastPosition = new Vector3(0.0f, 0.0f, 0.0f);
        this.hpMax = 5;
        this.pursuitRadius = 500.0f;
        this.hp = hpMax;
        this.aiTimerTo = 3.0f;
        this.timeOfHunt = 10.0f;
        this.aiTimer = 0.0f;
        this.ownerType = TankOwner.AI;
        this.preferredDirection = Direction.DOWN;
        this.position = new Vector2(0.0f, 0.0f);
        this.rectangle = new Rectangle(position.x, position.y, (float) GameConsts.TANK_WIDTH, (float) GameConsts.TANK_HEIGHT);
        this.scoreAmount = 10;
    }

    @Override
    public void update(float dt) {
        fireTimer+=dt;
        aiTimer += dt;
        fireTimer += dt;
        checkMovement(dt);
        rectangle.setPosition(position.x - rectangle.getWidth() / 2, position.y - rectangle.getHeight() / 2);

//        if (currentTimeOfHunt >= timeOfHunt) {
//            currentTimeOfHunt = 0;
//            this.preferredTarget = null;
//            float minDst = Float.MAX_VALUE;
//            dst = this.position.dst(gameScreen.getMap().getEagle().getPosition());
//            if (dst < minDst) {
//                minDst = dst;
//                preferredTarget = gameScreen.getMap().getEagle().getCircle();
//            }
//            for (int i = 0; i < gameScreen.getPlayers().size(); i++) {
//                PlayerTank player = gameScreen.getPlayers().get(i);
//                dst = this.position.dst(player.getPosition());
//                if (dst < minDst) {
//                    minDst = dst;
//                    preferredTarget = player.getCircle();
//                }
//            }
//            dst = position.dst2(preferredTarget.x, preferredTarget.y);
//            System.out.println("TARGET X: " + preferredTarget.x + "; TARGET Y: " + preferredTarget.y);
//            System.out.println("MY X: " + position.x + "; MY Y: " + position.y);
//        } else if(dst <= pursuitRadius && currentTimeOfHunt <= timeOfHunt && preferredTarget != null) {
//            currentTimeOfHunt += dt;
//            if(position.x >= preferredTarget.x - preferredTarget.radius
//                    && position.x <= preferredTarget.x + preferredTarget.radius) {
//                if(preferredTarget.y - position.y >= 0) {
//                    preferredDirection = Direction.UP;
//                } else {
//                    preferredDirection = Direction.DOWN;
//                }
//                if(fireTimer >= weapon.getFirePeriod()) {
//                    fire();
//                    fireTimer = 0;
//                }
//            } else if(position.y >= preferredTarget.y - preferredTarget.radius
//                    && position.y <= preferredTarget.y + preferredTarget.radius) {
//                if(preferredTarget.x - position.x >= 0) {
//                    preferredDirection = Direction.RIGHT;
//                } else {
//                    preferredDirection = Direction.LEFT;
//                }
//                if(fireTimer >= weapon.getFirePeriod()) {
//                    fire();
//                    fireTimer = 0;
//                }
//            } else if(position.x < preferredTarget.x - preferredTarget.radius) {
//                preferredDirection = Direction.RIGHT;
//            } else if(position.x > preferredTarget.x + preferredTarget.radius) {
//                preferredDirection = Direction.LEFT;
//            } else if(position.y < preferredTarget.y - preferredTarget.radius) {
//                preferredDirection = Direction.UP;
//            } else if(position.y > preferredTarget.y + preferredTarget.radius) {
//                preferredDirection = Direction.DOWN;
//            }
//
//        } else
        if(aiTimer >= aiTimerTo) {
            aiTimer = 0;
            aiTimerTo = MathUtils.random(3.5f, 6.0f);
            preferredDirection = Direction.values()[MathUtils.random(1, Direction.values().length - 1)];
        }
        if(fireTimer >= weapon.getFirePeriod()) {
            fire();
            fireTimer = 0;
        }

        if(Math.abs(position.x - lastPosition.x) < 0.5f && Math.abs(position.y - lastPosition.y) < 0.5f) {
            lastPosition.z += dt;
            if(lastPosition.z > 0.2f) {
                aiTimer += 10.0f;
            }
        } else {
            lastPosition.x = position.x;
            lastPosition.y = position.y;
            lastPosition.z = 0.0f;
        }
    }

    public void fire() {
        double angle = Math.toRadians(preferredDirection.getAngle());
        gameScreen.getBulletEmitter().activate(this,
                position.x + MathUtils.cos((float)angle)*GameConsts.TANK_WIDTH/2,
                position.y + MathUtils.sin((float)angle)*GameConsts.TANK_WIDTH/2,
                weapon.getBulletSpeed() * preferredDirection.getVx(),
                weapon.getBulletSpeed() * preferredDirection.getVy(),
                weapon.getDamage(), weapon.getBulletLifetime());
    }

    public void takeDamage(int damage, Tank playerTank) {
        hp -= damage;
        if (hp <= 0) {
            destroy();
            playerTank.addScore(scoreAmount);
        }
    }

    public void destroy(){
        active = false;
    }

    public void checkMovement(float dt) {
        if (position.x - (float) GameConsts.TANK_WIDTH / 2 < GameConsts.MAP_DEFAULT_DX) {
            position.x = GameConsts.MAP_DEFAULT_DX + (float) GameConsts.TANK_WIDTH / 2;
        }
        if(position.y - (float) (GameConsts.TANK_HEIGHT / 2) < GameConsts.MAP_DEFAULT_DY) {
            position.y = GameConsts.MAP_DEFAULT_DY + (float) (GameConsts.TANK_HEIGHT / 2);
        }
        if(position.x + (float) GameConsts.TANK_WIDTH / 2 > Gdx.graphics.getWidth() - GameConsts.MAP_DEFAULT_DX) {
            position.x = Gdx.graphics.getWidth() - (float) GameConsts.TANK_WIDTH / 2 - GameConsts.MAP_DEFAULT_DX;
        }
        if(position.y + (float) (GameConsts.TANK_HEIGHT / 2) > Gdx.graphics.getHeight() - GameConsts.MAP_DEFAULT_DY) {
            position.y = Gdx.graphics.getHeight() - (float)(GameConsts.TANK_HEIGHT / 2) - GameConsts.MAP_DEFAULT_DY;
        }

        if(preferredDirection == Direction.LEFT) {
            move(Direction.LEFT, dt);
        } else if(preferredDirection == Direction.RIGHT) {
            move(Direction.RIGHT, dt);
        } else if(preferredDirection == Direction.UP) {
            move(Direction.UP, dt);
        } else if(preferredDirection == Direction.DOWN)
            move(Direction.DOWN, dt);
    }
}

