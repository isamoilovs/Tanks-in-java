package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Map {
    private TextureRegion grassTexture;
    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 23;
    public static final int CELL_SIZE = 32;

    public Map(TextureAtlas atlas) {
        this.grassTexture = atlas.findRegion("grass");
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                batch.draw(grassTexture, i*CELL_SIZE, j*CELL_SIZE);
            }
        }
    }
}
