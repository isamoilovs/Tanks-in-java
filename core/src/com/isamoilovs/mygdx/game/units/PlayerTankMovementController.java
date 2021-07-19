package com.isamoilovs.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

//Подумать насчет соответствия инкапсуляции

public class PlayerTankMovementController {
    private Tank tank;
    PlayerTankMovementController(Tank tank) {
        this.tank = tank;
    }
    public void checkMovement(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            while (tank.rotationAngle != 180) {
                while (tank.rotationAngle < 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += 3;
                    return;
                }
                while (tank.rotationAngle > 180) {

                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= 3;
                    return;
                }
            }
            tank.position.x -= tank.speed*dt;
            tank.getTankAnimation().update(dt);

        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            while (tank.rotationAngle != 0) {
                while (tank.rotationAngle <= 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= 3;

                    return;
                }
                while (tank.rotationAngle >= 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += 3;
                    tank.rotationAngle = (tank.rotationAngle >= 360) ? tank.rotationAngle - 360 : tank.rotationAngle;
                    return;
                }
            }
            tank.rotationAngle = 0;
            tank.getTankAnimation().update(dt);
            tank.position.x += tank.speed*dt;

        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            while (tank.rotationAngle != 90) {
                while (tank.rotationAngle < 90 || tank.rotationAngle - 180 >= 90) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += 3;
                    tank.rotationAngle = (tank.rotationAngle >= 360) ? tank.rotationAngle - 360 : tank.rotationAngle;
                    return;
                }
                while (tank.rotationAngle > 90 && tank.rotationAngle <= 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= 3;
                    return;
                }
            }
            tank.rotationAngle = 90;
            tank.getTankAnimation().update(dt);
            tank.position.y += tank.speed*dt;

        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            while (tank.rotationAngle != 270) {
                while (tank.rotationAngle <= 90 || tank.rotationAngle > 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle -= 3;
                    tank.rotationAngle = (tank.rotationAngle <= 0) ? tank.rotationAngle + 360 : tank.rotationAngle;
                    return;
                }
                while (tank.rotationAngle > 90 && tank.rotationAngle < 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotationAngle += 3;
                    return;
                }
            }
            tank.rotationAngle = 270;
            tank.getTankAnimation().update(dt);
            tank.position.y -= tank.speed*dt;
        }
    }

}
