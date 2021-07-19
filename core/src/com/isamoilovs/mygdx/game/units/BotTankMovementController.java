package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.isamoilovs.mygdx.game.utils.Direction;

public class BotTankMovementController {
    private BotTank tank;
    private final float MOVE_SPEED = 3;
    BotTankMovementController(BotTank tank) {
        this.tank = tank;
    }
    public void checkMovement(float dt) {
        if(tank.preferredDirection == Direction.LEFT) {
            while (tank.rotationAngle != 180) {
                while (tank.rotationAngle < 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += MOVE_SPEED;
                    return;
                }
                while (tank.rotationAngle > 180) {

                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= MOVE_SPEED;
                    return;
                }
            }
            if(tank.position.x < 0) {
                tank.position.x = 0;
                return;
            } else {
                tank.position.x -= tank.speed * dt;
                tank.getTankAnimation().update(dt);
            }

        } else if(tank.preferredDirection == Direction.RIGHT) {
            while (tank.rotationAngle != 0) {
                while (tank.rotationAngle <= 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= MOVE_SPEED;

                    return;
                }
                while (tank.rotationAngle >= 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += MOVE_SPEED;
                    tank.rotationAngle = (tank.rotationAngle >= 360) ? tank.rotationAngle - 360 : tank.rotationAngle;
                    return;
                }
            }
            if(tank.position.x > Gdx.graphics.getWidth()) {
                tank.position.x = Gdx.graphics.getWidth();
                return;
            } else {
                tank.position.x += tank.speed * dt;
                tank.getTankAnimation().update(dt);
            }

        } else if(tank.preferredDirection == Direction.UP) {
            while (tank.rotationAngle != 90) {
                while (tank.rotationAngle < 90 || tank.rotationAngle - 180 >= 90) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += MOVE_SPEED;
                    tank.rotationAngle = (tank.rotationAngle >= 360) ? tank.rotationAngle - 360 : tank.rotationAngle;
                    return;
                }
                while (tank.rotationAngle > 90 && tank.rotationAngle <= 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= MOVE_SPEED;
                    return;
                }
            }
            if(tank.position.y > Gdx.graphics.getHeight()) {
                tank.position.y = Gdx.graphics.getHeight();
                return;
            } else {
                tank.getTankAnimation().update(dt);
                tank.position.y += tank.speed * dt;
            }

        } else if(tank.preferredDirection == Direction.DOWN) {
            while (tank.rotationAngle != 270) {
                while (tank.rotationAngle <= 90 || tank.rotationAngle > 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= MOVE_SPEED;
                    tank.rotationAngle = (tank.rotationAngle <= 0) ? tank.rotationAngle + 360 : tank.rotationAngle;
                    return;
                }
                while (tank.rotationAngle > 90 && tank.rotationAngle < 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += MOVE_SPEED;
                    return;
                }
            }
            tank.rotationAngle = 270;
            if(tank.position.y < 0) {
                tank.position.y = 0;
                return;
            } else {
                tank.getTankAnimation().update(dt);
                tank.position.y -= tank.speed * dt;
            }
        }
    }
}
