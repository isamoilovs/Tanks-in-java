package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

//Подумать насчет соответствия инкапсуляции

public class MovementController {
    private Tank tank;
    MovementController(Tank tank) {
        this.tank = tank;
    }
    public void checkMovement(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            while (tank.rotation != 180) {
                while (tank.rotation < 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotation += 3;
                    return;
                }
                while (tank.rotation > 180) {

                    tank.getTankAnimation().update(dt);
                    tank.rotation -= 3;
                    return;
                }
            }
            tank.position.x -= tank.speed*dt;
            tank.getTankAnimation().update(dt);

        } else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            while (tank.rotation != 0) {
                while (tank.rotation <= 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotation -= 3;

                    return;
                }
                while (tank.rotation >= 180) {
                    tank.getTankAnimation().update(dt);
                    tank.rotation += 3;
                    tank.rotation = (tank.rotation >= 360) ? tank.rotation - 360 : tank.rotation;
                    return;
                }
            }
            tank.rotation = 0;
            tank.getTankAnimation().update(dt);
            tank.position.x += tank.speed*dt;

        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            while (tank.rotation != 90) {
                while (tank.rotation < 90 || tank.rotation - 180 >= 90) {
                    tank.getTankAnimation().update(dt);
                    tank.rotation += 3;
                    tank.rotation = (tank.rotation >= 360) ? tank.rotation - 360 : tank.rotation;
                    return;
                }
                while (tank.rotation > 90 && tank.rotation <= 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotation -= 3;
                    return;
                }
            }
            tank.rotation = 90;
            tank.getTankAnimation().update(dt);
            tank.position.y += tank.speed*dt;

        } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            while (tank.rotation != 270) {
                while (tank.rotation <= 90 || tank.rotation > 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotation -= 3;
                    tank.rotation = (tank.rotation <= 0) ? tank.rotation + 360 : tank.rotation;
                    return;
                }
                while (tank.rotation > 90 && tank.rotation < 270) {
                    tank.getTankAnimation().update(dt);
                    tank.rotation += 3;
                    return;
                }
            }
            tank.rotation = 270;
            tank.getTankAnimation().update(dt);
            tank.position.y -= tank.speed*dt;
        }
    }

}
