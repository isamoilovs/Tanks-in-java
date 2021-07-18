package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
    private Texture grassTexture;
    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 23;
    public static final int CELL_SIZE = 32;

    public Map() {
        this.grassTexture = new Texture("grass.jpg");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                batch.draw(grassTexture, i*CELL_SIZE, j*CELL_SIZE);
            }
        }
    }
}
