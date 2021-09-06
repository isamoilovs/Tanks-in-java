package com.isamoilovs.mygdx.game.units.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.isamoilovs.mygdx.game.units.map.emitters.BulletEmitter;
import com.isamoilovs.mygdx.game.units.tanks.BotTank;
import com.isamoilovs.mygdx.game.units.tanks.PlayerTank;
import com.isamoilovs.mygdx.game.units.weapon.Bullet;
import com.isamoilovs.mygdx.game.utils.GameConsts;

import java.util.List;

public class Map {
    public enum WallType {
        STONE_WALL(1, 0, false, false, false),
        BRICK_WALL(1, 2, true, false, false),
        OBSIDIAN(1, 1, false, false, false),
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
        Rectangle rectangle;

        public Cell(WallType type, int x, int y) {
            this.type = type;
            this.hp = type.maxHP;
            this.rectangle = new Rectangle(x, y, GameConsts.MAP_DEFAULT_CELL_SIZE, GameConsts.MAP_DEFAULT_CELL_SIZE);
            System.out.println("x = " + x + " y = " + y);
            System.out.println(GameConsts.MAP_DEFAULT_DX + " " + GameConsts.MAP_DEFAULT_DY);
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
    final float waterTime = 0.3f;
    private int waterFrame;
    private int prevWaterFrame;
    private float currentTime;

    public Eagle getEagle() {
        return eagle;
    }

    private Eagle eagle;


    private Cell cells[][];

    public Map(TextureAtlas atlas) {
        this.grassTexture = atlas.findRegion("earth");
        loadTextures(atlas);
        this.eagle = new Eagle(atlas);
        waterFrame = 0;
        currentTime = 0;
        prevWaterFrame = -1;
        this.cells = new Cell[GameConsts.MAP_SIZE_CX][GameConsts.MAP_SIZE_CY];
        for (int i = 0; i < GameConsts.MAP_SIZE_CX; i++) {
            for (int j = 0; j < GameConsts.MAP_SIZE_CY; j++) {
                cells[i][j] = new Cell(WallType.NONE, GameConsts.MAP_DEFAULT_DX + GameConsts.MAP_DEFAULT_CELL_SIZE * i, GameConsts.MAP_DEFAULT_DY + GameConsts.MAP_DEFAULT_CELL_SIZE * j);
                int cx = (int) (i / 4);
                int cy = (int) (j / 4);
                if(cy % 2 ==0 && cx % 2 ==0) {
                    if(MathUtils.random() < 0.8f) {
                        this.cells[i][j].changeType(WallType.BRICK_WALL);
                    } else {
                        this.cells[i][j].changeType(WallType.STONE_WALL);
                    }
                }
            }
        }
        eagle.setPosition(GameConsts.MAP_DEFAULT_DX +GameConsts.MAP_WIDTH /2, GameConsts.MAP_DEFAULT_DY + eagle.getRectangle().getWidth()/2);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < GameConsts.MAP_SIZE_CX; i++) {
            for (int j = 0; j < GameConsts.MAP_SIZE_CY; j++) {
                batch.draw(grassTexture, i*GameConsts.MAP_DEFAULT_CELL_SIZE + GameConsts.MAP_DEFAULT_DX, j*GameConsts.MAP_DEFAULT_CELL_SIZE + GameConsts.MAP_DEFAULT_DY, GameConsts.MAP_DEFAULT_CELL_SIZE, GameConsts.MAP_DEFAULT_CELL_SIZE);
            }
        }
        for (int i = 0; i < GameConsts.MAP_SIZE_CX; i++) {
            for (int j = 0; j < GameConsts.MAP_SIZE_CY; j++) {
                if(cells[i][j].type != WallType.NONE && cells[i][j].type != WallType.WATER) {
                    batch.draw(wallsTextures[cells[i][j].type.index][cells[i][j].hp - 1], i * GameConsts.MAP_DEFAULT_CELL_SIZE + GameConsts.MAP_DEFAULT_DX, j * GameConsts.MAP_DEFAULT_CELL_SIZE + GameConsts.MAP_DEFAULT_DY,  GameConsts.MAP_DEFAULT_CELL_SIZE, GameConsts.MAP_DEFAULT_CELL_SIZE);
                } else if(cells[i][j].type == WallType.WATER) {
                    if (currentTime > waterTime){
                        currentTime = 0;
                        prevWaterFrame = waterFrame;
                        do {waterFrame = MathUtils.random(0, 2);
                        } while (waterFrame == prevWaterFrame);
                    }
                    batch.draw(wallsTextures[cells[i][j].type.index][waterFrame], i * GameConsts.MAP_DEFAULT_CELL_SIZE + GameConsts.MAP_DEFAULT_DX, j * GameConsts.MAP_DEFAULT_CELL_SIZE + GameConsts.MAP_DEFAULT_DY,  GameConsts.MAP_DEFAULT_CELL_SIZE, GameConsts.MAP_DEFAULT_CELL_SIZE);
                }
            }
        }
        eagle.render(batch);
    }

    public void checkWallsAndBulletsCollisions(BulletEmitter bulletEmitter) {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if(bullet.isActive()) {
                int cx = (int) ((bullet.getPosition().x - GameConsts.MAP_DEFAULT_DX) / GameConsts.MAP_DEFAULT_CELL_SIZE);
                int cy = (int) ((bullet.getPosition().y - GameConsts.MAP_DEFAULT_DY) / GameConsts.MAP_DEFAULT_CELL_SIZE);

                if(cx >= 0 && cy >= 0 && cx < GameConsts.MAP_SIZE_CX && cy < GameConsts.MAP_SIZE_CY) {
                    if(!cells[cx][cy].type.bulletPassable) {
                        cells[cx][cy].damage();
                        bullet.disActivate();
                    }
                }
            }
        }
    }

    public void checkWallsAndBulletsCollisionsNew(BulletEmitter bulletEmitter) {
        Bullet[] bullets;
        bullets = bulletEmitter.getBullets();

        for(int i = 0; i < bullets.length; i++) {
            Bullet bullet = bullets[i];
            if (bullet.isActive()) {
                for (int j = 0; j < GameConsts.MAP_SIZE_CX; j++) {
                    for (int k = 0; k < GameConsts.MAP_SIZE_CY; k++) {
                        if((bullets[i].getBulletRectangle().overlaps(cells[j][k].rectangle) || cells[j][k].rectangle.contains(bullets[i].getBulletRectangle())) && !cells[j][k].type.bulletPassable){
                            cells[j][k].damage();
                            bullets[i].disActivate();
                        }
                    }
                }
            }
        }
    }

    public boolean isAreaClear(float x, float y, float halfSize) {
        int leftX = (int) ((x - halfSize - GameConsts.MAP_DEFAULT_DX) / GameConsts.MAP_DEFAULT_CELL_SIZE);
        int rightX = (int) ((x + halfSize - GameConsts.MAP_DEFAULT_DX) / GameConsts.MAP_DEFAULT_CELL_SIZE);

        int bottomY = (int) ((y - halfSize - GameConsts.MAP_DEFAULT_DY) / GameConsts.MAP_DEFAULT_CELL_SIZE);
        int topY = (int) ((y + halfSize - GameConsts.MAP_DEFAULT_DY) / GameConsts.MAP_DEFAULT_CELL_SIZE);

        if (leftX < 0) {
            leftX = 0;
        }
        if(rightX >= GameConsts.MAP_SIZE_CX) {
            rightX = GameConsts.MAP_SIZE_CX - 1;
        }
        if (bottomY < 0) {
            bottomY = 0;
        }
        if(topY >= GameConsts.MAP_SIZE_CY) {
            topY = GameConsts.MAP_SIZE_CY - 1;
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
        wallsTextures = new TextureRegion(atlas.findRegion("walls")).split(8, 8);
    }

    public void update(float dt) {
        currentTime += dt;
    }
}
