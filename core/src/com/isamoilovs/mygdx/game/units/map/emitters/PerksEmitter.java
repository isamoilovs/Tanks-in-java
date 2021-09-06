package com.isamoilovs.mygdx.game.units.map.emitters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.units.tanks.PlayerTank;
import com.isamoilovs.mygdx.game.utils.GameConsts;

import java.util.List;

public class PerksEmitter {

    public enum PerkType {
        MED_KIT(0.5f, 5, 10.0f, -1.0f), SHIELD(0.5f, 0, 10.0f, 5.0f);

        PerkType(float probability, int index, float livingTime, float actionTime) {
            this.probability = probability;
            this.index  = index;
            this.livingTime = livingTime;
            this.actionTime = actionTime;
        }

        public int getIndex() {
            return index;
        }

        public float getActionTime() {
            return actionTime;
        }

        int index;
        float probability;
        float livingTime;
        float actionTime;
    }

    public class Perk {
        private PerkType perkType;
        private boolean active;
        private Vector2 position;
        private float currentTime;
        private int frame;
        private Rectangle perkRectangle;

        public PerkType getPerkType() {
            return perkType;
        }

        public boolean isActive() {
            return active;
        }

        public void activate() {
            this.active = true;
        }

        public void disActivate() {
            this.active = false;
        }


        public Rectangle getPerkCircle() {
            return perkRectangle;
        }

        public Perk(PerkType perkType) {
            this.perkType = perkType;
            this.active = false;
            this.position = new Vector2(0, 0);
            this.perkRectangle = new Rectangle();
            perkRectangle.setPosition(position.x - GameConsts.TANK_WIDTH/2, position.y - GameConsts.TANK_WIDTH/2);
            perkRectangle.setHeight(GameConsts.TANK_WIDTH);
            perkRectangle.setWidth(GameConsts.TANK_WIDTH);
        }
    }

    private GameScreen gameScreen;
    private static int PERKS_AMOUNT = 30;


    private Perk[] perks;
    private TextureRegion[][] perksTextures;
    final float MAX_FRAME_TIME = 0.125f;

    public Perk[] getPerks() {
        return perks;
    }

    public PerksEmitter(GameScreen gameScreen, TextureAtlas atlas) {
        this.gameScreen = gameScreen;
        this.perksTextures = new TextureRegion(atlas.findRegion("perks16")).split(16, 16);
        this.perks = new Perk[PERKS_AMOUNT];
        for (int i = 0; i < perks.length; i++) {
            perks[i] = new Perk(PerkType.values()[MathUtils.random(0, PerkType.values().length - 1)]);
        }
    }

    public void activate(Map map) {
        float cordX, cordY;
        do {
            cordX = MathUtils.random(GameConsts.MAP_DEFAULT_DX, Gdx.graphics.getWidth() - GameConsts.MAP_DEFAULT_DX);
            cordY = MathUtils.random(GameConsts.MAP_DEFAULT_DY, Gdx.graphics.getHeight() - GameConsts.MAP_DEFAULT_DY);
        } while (!map.isAreaClear(cordX, cordY, perks[0].perkRectangle.getWidth() / 2));

        for (int i = 0; i < perks.length; i++) {
            if(!perks[i].isActive()) {
                perks[i].position.set(cordX, cordY);
                perks[i].perkRectangle.setPosition(cordX - GameConsts.TANK_WIDTH/2, cordY - GameConsts.TANK_WIDTH/2);
                perks[i].activate();
                break;
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < perks.length; i++) {
            if(perks[i].isActive()) {
                perks[i].currentTime += dt;
                if(perks[i].currentTime > perks[i].perkType.livingTime) {
                    perks[i].disActivate();
                    break;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < perks.length; i++) {
            if(perks[i].isActive()){
                batch.draw(perksTextures[0][perks[i].perkType.index], perks[i].perkRectangle.x, perks[i].perkRectangle.y, GameConsts.TANK_WIDTH, GameConsts.TANK_WIDTH);
            }
        }
    }


    public void checkPerkAndPlayerCollision(List<PlayerTank> playerTanks) {
        for (int i = 0; i < playerTanks.size(); i++) {
            Rectangle playerRectangle = new Rectangle(playerTanks.get(i).getRectangle());
            for (int j = 0; j < perks.length; j++) {
                Rectangle perkCircle = new Rectangle(perks[j].getPerkCircle());
                if((perkCircle.contains(playerRectangle) || perkCircle.overlaps(playerRectangle))
                        && (playerTanks.get(i).isAbleToBeDamaged())
                        && perks[j].getPerkType() == PerksEmitter.PerkType.SHIELD) {
                    playerTanks.get(i).getShield();
                    perks[j].disActivate();
                }
                if((perkCircle.contains(playerRectangle) || perkCircle.overlaps(playerRectangle))
                        && perks[j].getPerkType() == PerksEmitter.PerkType.MED_KIT) {
                    perks[j].disActivate();
                    playerTanks.get(i).repair();
                    break;
                }
            }
        }
    }
}
