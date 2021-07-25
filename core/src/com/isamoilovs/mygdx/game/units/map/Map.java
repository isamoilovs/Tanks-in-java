package com.isamoilovs.mygdx.game.units.map;

import com.badlogic.gdx.Gdx;
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
    public static final int WIDTH = 800;
    public static final int HEIGHT = 576;
    public static final int DEFAULT_CELL_SIZE = 32;
    public static final int SIZE_CX = WIDTH / DEFAULT_CELL_SIZE;
    public static final int SIZE_CY = HEIGHT / DEFAULT_CELL_SIZE;
    public static final int DEFAULT_DX = (Gdx.graphics.getWidth() - WIDTH) / 2;
    public static final int DEFAULT_DY = (Gdx.graphics.getHeight() - HEIGHT) / 2;
    public static final int DEFAULT_DCX = DEFAULT_DX / DEFAULT_CELL_SIZE;
    public static final int DEFAULT_DCY = DEFAULT_DY / DEFAULT_CELL_SIZE;

    public Eagle getEagle() {
        return eagle;
    }

    private Eagle eagle;


    private Cell cells[][];

    public Map(TextureAtlas atlas) {
        this.grassTexture = atlas.findRegion("earth");
        loadTextures(atlas);
        this.eagle = new Eagle(atlas);

        this.cells = new Cell[SIZE_CX][SIZE_CY];
        for (int i = 0; i < SIZE_CX; i++) {
            for (int j = 0; j < SIZE_CY; j++) {
                cells[i][j] = new Cell(WallType.NONE);
                int cx = (int) (i / 4);
                int cy = (int) (j / 4);
//                if(cy % 2 ==0 && cx % 2 ==0) {
//                    if(MathUtils.random() < 0.8f) {
//                        this.cells[i][j].changeType(WallType.WATER);
//                    } else {
//                        this.cells[i][j].changeType(WallType.STONE_WALL);
//                    }
//                }
            }
        }
        float cordX, cordY;
        do {
            cordX = MathUtils.random(Map.DEFAULT_DX, Gdx.graphics.getWidth() - Map.DEFAULT_DX);
            cordY = MathUtils.random(Map.DEFAULT_DY, Gdx.graphics.getHeight() - Map.DEFAULT_DY);
        } while (!isAreaClear(cordX, cordY, eagle.getCircle().radius));
        eagle.setPosition(cordX, cordY);
//        for (int i = 0; i < SIZE_CX; i++) {
//            this.cells[i][0].changeType(WallType.OBSIDIAN);
//            this.cells[i][SIZE_CY - 1].changeType(WallType.OBSIDIAN);
//        }
//        for (int i = 0; i < SIZE_CY; i++) {
//            this.cells[0][i].changeType(WallType.OBSIDIAN);
//            this.cells[SIZE_CX - 1][i].changeType(WallType.OBSIDIAN);
//        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < SIZE_CX; i++) {
            for (int j = 0; j < SIZE_CY; j++) {
                batch.draw(grassTexture, i*DEFAULT_CELL_SIZE + DEFAULT_DX, j*DEFAULT_CELL_SIZE + DEFAULT_DY, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE);
            }
        }
        for (int i = 0; i < SIZE_CX; i++) {
            for (int j = 0; j < SIZE_CY; j++) {
                if(cells[i][j].type != WallType.NONE) {
                    batch.draw(wallsTextures[cells[i][j].type.index][cells[i][j].hp - 1], i * DEFAULT_CELL_SIZE + DEFAULT_DX, j * DEFAULT_CELL_SIZE + DEFAULT_DY,  DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE);
                }
            }
        }
        eagle.render(batch);
    }

    public void checkWallsAndBulletsCollisions(BulletEmitter bulletEmitter) {

        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if(bullet.isActive()) {
                int cx = (int) ((bullet.getPosition().x - DEFAULT_DX) / DEFAULT_CELL_SIZE);
                int cy = (int) ((bullet.getPosition().y - DEFAULT_DY) / DEFAULT_CELL_SIZE);

                if(cx >= 0 && cy >= 0 && cx <= SIZE_CX && cy <= SIZE_CY) {
                    if(!cells[cx][cy].type.bulletPassable) {
                        cells[cx][cy].damage();
                        bullet.disActivate();
                    }
                }
            }
        }
    }

    public boolean isAreaClear(float x, float y, float halfSize) {
        int leftX = (int) ((x - halfSize - DEFAULT_DX) / DEFAULT_CELL_SIZE);
        int rightX = (int) ((x + halfSize - DEFAULT_DX) / DEFAULT_CELL_SIZE);

        int bottomY = (int) ((y - halfSize - DEFAULT_DY) / DEFAULT_CELL_SIZE);
        int topY = (int) ((y + halfSize - DEFAULT_DY) / DEFAULT_CELL_SIZE);

        if (leftX < 0) {
            leftX = 0;
        }
        if(rightX >= SIZE_CX) {
            rightX = SIZE_CX - 1;
        }
        if (bottomY < 0) {
            bottomY = 0;
        }
        if(topY >= SIZE_CY) {
            topY = SIZE_CY - 1;
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
        wallsTextures = new TextureRegion(atlas.findRegion("walls")).split(DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE);
    }

    public void update(float dt) {

    }
}
