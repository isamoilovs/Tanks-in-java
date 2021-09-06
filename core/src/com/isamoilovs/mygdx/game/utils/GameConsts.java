package com.isamoilovs.mygdx.game.utils;

import com.badlogic.gdx.Gdx;

public interface GameConsts {
    int MAP_DEFAULT_CELL_SIZE = ((Gdx.graphics.getHeight() / 32) / 8) * 7;
    int MAP_HEIGHT = MAP_DEFAULT_CELL_SIZE * 32;
    int MAP_WIDTH = MAP_DEFAULT_CELL_SIZE * 48;
    int MAP_SIZE_CX = MAP_WIDTH / MAP_DEFAULT_CELL_SIZE;
    int MAP_SIZE_CY = MAP_HEIGHT / MAP_DEFAULT_CELL_SIZE;
    int MAP_DEFAULT_DX = (Gdx.graphics.getWidth() - MAP_WIDTH) / 2;
    int MAP_DEFAULT_DY = (Gdx.graphics.getHeight() - MAP_HEIGHT) / 2;

    int TANK_TEXTURE_RESOLUTION = 16;
    int TANK_MULTIPLIER = 2;
    float TANK_WIDTH = GameConsts.MAP_DEFAULT_CELL_SIZE * 2;
    float TANK_HEIGHT = GameConsts.MAP_DEFAULT_CELL_SIZE * 2;
    float TANK_FRAME_TIME = 0.2f;
    float BULLET_WIDTH = TANK_WIDTH / 2;
}
