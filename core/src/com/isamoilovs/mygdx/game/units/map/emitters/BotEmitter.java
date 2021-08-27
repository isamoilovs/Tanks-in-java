package com.isamoilovs.mygdx.game.units.map.emitters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.units.tanks.BotTank;
import com.isamoilovs.mygdx.game.utils.GameConsts;

public class BotEmitter {
    private BotTank[] bots;
    public static final int MAX_BOTS_COUNT = 500;

    public BotTank[] getBots() {
        return bots;
    }

    public BotEmitter(GameScreen gameScreen, TextureAtlas atlas) {
        this.bots = new BotTank[MAX_BOTS_COUNT];
        for(int i = 0; i < bots.length; i++) {
            this.bots[i] = new BotTank(gameScreen, atlas);
        }
    }

    public void activate(Map map) {

        float cordX, cordY;

        Rectangle rectangle = new Rectangle(0, 0, bots[0].getRectangle().getWidth(), bots[0].getRectangle().getHeight());
        do {
            cordX = MathUtils.random(GameConsts.MAP_DEFAULT_DX, Gdx.graphics.getWidth() - GameConsts.MAP_DEFAULT_DX);
            cordY = MathUtils.random(GameConsts.MAP_DEFAULT_DY, Gdx.graphics.getHeight() - GameConsts.MAP_DEFAULT_DY);
            rectangle.setPosition(cordX - rectangle.getWidth() / 2, cordY - rectangle.getHeight() / 2);
        } while (!map.isAreaClear(cordX, cordY, rectangle.getWidth() / 2));


        for(int i = 0; i < bots.length; i++) {
            if(!bots[i].isActive()) {
                bots[i].activate(cordX, cordY);
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for(int i = 0; i < bots.length; i++) {
            if(bots[i].isActive()) {
                bots[i].render(batch);
            }
        }
    }

    public void update(float dt) {
        for(int i = 0; i < bots.length; i++) {
            if(bots[i].isActive()) {
                bots[i].update(dt);
            }
        }
    }
}
