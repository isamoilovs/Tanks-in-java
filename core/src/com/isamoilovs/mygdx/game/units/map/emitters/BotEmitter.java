package com.isamoilovs.mygdx.game.units.map.emitters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.units.tanks.BotTank;

public class BotEmitter {
    private BotTank[] bots;
    public static final int MAX_BOTS_COUNT = 1;

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
        do {
            cordX = MathUtils.random(Map.DEFAULT_DX, Gdx.graphics.getWidth() - Map.DEFAULT_DX);
            cordY = MathUtils.random(Map.DEFAULT_DY, Gdx.graphics.getHeight() - Map.DEFAULT_DY);
        } while (!map.isAreaClear(cordX, cordY, 32));

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
