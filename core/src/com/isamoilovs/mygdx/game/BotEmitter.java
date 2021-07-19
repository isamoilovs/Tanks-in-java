package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.isamoilovs.mygdx.game.units.BotTank;

public class BotEmitter {
    private BotTank[] bots;
    public static final int MAX_BOTS_COUNT = 100;

    public BotTank[] getBots() {
        return bots;
    }

    public BotEmitter(MyGdxGame game, TextureAtlas atlas) {
        this.bots = new BotTank[MAX_BOTS_COUNT];
        for(int i = 0; i < bots.length; i++) {
            this.bots[i] = new BotTank(game, atlas);
        }
    }

    public void activate(float x, float y) {
        for(int i = 0; i < bots.length; i++) {
            if(!bots[i].isActive()) {
                bots[i].activate(x, y);
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
