package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Map {
    private TextureRegion grassTexture;
    private java.util.Map<String, TextureRegion> textureRegionWallMap;
    private TextureRegion wallTextureRegion[][];
    private TextureRegion wallTexture;
    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 23;
    public static final int CELL_SIZE = 32;
    private int obstaclesMapArray[][];

    public Map(TextureAtlas atlas) {
        this.grassTexture = atlas.findRegion("grass");
        loadTextures(atlas);
        this.obstaclesMapArray = new int[SIZE_X][SIZE_Y];
        this.obstaclesMapArray[1][1] = 5;
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < 3; j++) {
                this.obstaclesMapArray[i][SIZE_Y - 1 - j] = 5;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                batch.draw(grassTexture, i*CELL_SIZE, j*CELL_SIZE);
            }
        }
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                if(obstaclesMapArray[i][j] > 0) {
                    batch.draw(textureRegionWallMap.get("stone"+ obstaclesMapArray[i][j]), i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }

    public void checkWallsAndBulletsCollisions(Bullet bullet) {
        int cx = (int) bullet.getPosition().x / CELL_SIZE;
        int cy = (int) bullet.getPosition().y / CELL_SIZE;

        if(cx >= 0 && cy >= 0 && cx <= SIZE_X && cy <= SIZE_Y) {
            if(obstaclesMapArray[cx][cy] > 0) {
                obstaclesMapArray[cx][cy] -= bullet.getDamage();
                bullet.disActivate();
            }
        }
    }

    public boolean isAreaClear(float x, float y, float halfSize) {
        int leftX = (int) ((x - halfSize) / CELL_SIZE);
        int rightX = (int) ((x + halfSize) / CELL_SIZE);

        int bottomY = (int) ((y - halfSize) / CELL_SIZE);
        int topY = (int) ((y + halfSize) / CELL_SIZE);

        if (leftX < 0) {
            leftX = 0;
        }
        if(rightX >= SIZE_X) {
            rightX = SIZE_X - 1;
        }
        if (bottomY < 0) {
            bottomY = 0;
        }
        if(topY >= SIZE_Y) {
            topY = SIZE_Y - 1;
        }

        for (int i = leftX; i <= rightX; i++) {
            for (int j = bottomY; j <= topY; j++) {
                if(obstaclesMapArray[i][j] > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void loadTextures(TextureAtlas atlas) {
        textureRegionWallMap = new HashMap<String, TextureRegion>();
        wallTexture = new TextureRegion(atlas.findRegion("stones"));
        wallTextureRegion = TextureRegion.split(wallTexture.getTexture(), wallTexture.getRegionWidth() / 5, wallTexture.getRegionHeight());
        textureRegionWallMap.put("stone5", wallTextureRegion[0][0]);
        textureRegionWallMap.put("stone4", wallTextureRegion[0][1]);
        textureRegionWallMap.put("stone3", wallTextureRegion[0][2]);
        textureRegionWallMap.put("stone2", wallTextureRegion[0][3]);
        textureRegionWallMap.put("stone1", wallTextureRegion[0][4]);
    }

    public void update(float dt) {

    }
}
