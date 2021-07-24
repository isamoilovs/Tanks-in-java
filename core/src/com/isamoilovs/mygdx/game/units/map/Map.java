package com.isamoilovs.mygdx.game.units.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.isamoilovs.mygdx.game.units.map.emitters.BulletEmitter;
import com.isamoilovs.mygdx.game.units.weapon.Bullet;

public class Map {
    public enum WallType {
        STONE_WALL(5, 0, true, false, false),
        BRICK_WALL(3, 1, true, false, false),
        OBSIDIAN(1, 2, false, false, false),
        WATER(1, 3, true, false, true),
        NONE(0, 0, false, true, true);

        int index;
        int maxHP;
        boolean unitPassable;
        boolean bulletPassable;
        boolean destructible;

        WallType(int maxHP, int index, boolean destructible, boolean unitPassable, boolean projectilePassable) {
            this.maxHP = maxHP;
            this.index = index;
            this.destructible = destructible;
            this.unitPassable = unitPassable;
            this.bulletPassable = projectilePassable;
        }
    }

    private class Cell {
        WallType type;
        int hp;

        public Cell(WallType type) {
            this.type = type;
            this.hp = type.maxHP;
        }

        public void damage() {
            if(type.destructible) {
                hp--;
                if (hp <= 0) {
                    type = WallType.NONE;
                }
            }
        }

        public void changeType(WallType type) {
            this.type = type;
            this.hp = type.maxHP;
        }
    }

    private TextureRegion grassTexture;
    private TextureRegion wallsTextures[][];
    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 23;
    public static final int CELL_SIZE = 32;
    private Cell cells[][];

    public Map(TextureAtlas atlas) {
        this.grassTexture = atlas.findRegion("grass");
        loadTextures(atlas);
        this.cells = new Cell[SIZE_X][SIZE_Y];
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                cells[i][j] = new Cell(WallType.NONE);
                int cx = (int) (i / 3);
                int cy = (int) (j / 3);
                if(cy % 2 ==0 && cx % 2 ==0) {
                    if(MathUtils.random() < 0.8f) {
                        this.cells[i][j].changeType(WallType.WATER);
                    } else {
                        this.cells[i][j].changeType(WallType.STONE_WALL);
                    }
                }
            }
        }
        for (int i = 0; i < SIZE_X; i++) {
            this.cells[i][0].changeType(WallType.OBSIDIAN);
            this.cells[i][SIZE_Y - 1].changeType(WallType.OBSIDIAN);
        }
        for (int i = 0; i < SIZE_Y; i++) {
            this.cells[0][i].changeType(WallType.OBSIDIAN);
            this.cells[SIZE_X - 1][i].changeType(WallType.OBSIDIAN);
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
                if(cells[i][j].type != WallType.NONE) {
                    batch.draw(wallsTextures[cells[i][j].type.index][cells[i][j].hp - 1], i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }

    public void checkWallsAndBulletsCollisions(BulletEmitter bulletEmitter) {

        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if(bullet.isActive()) {
                int cx = (int) bullet.getPosition().x / CELL_SIZE;
                int cy = (int) bullet.getPosition().y / CELL_SIZE;

                if(cx >= 0 && cy >= 0 && cx <= SIZE_X && cy <= SIZE_Y) {
                    if(!cells[cx][cy].type.bulletPassable) {
                        cells[cx][cy].damage();
                        bullet.disActivate();
                    }
                }
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
                if(!cells[i][j].type.unitPassable) {
                    return false;
                }
            }
        }
        return true;
    }

    public void loadTextures(TextureAtlas atlas) {
        wallsTextures = new TextureRegion(atlas.findRegion("walls")).split(CELL_SIZE, CELL_SIZE);
    }

    public void update(float dt) {


    }
}
