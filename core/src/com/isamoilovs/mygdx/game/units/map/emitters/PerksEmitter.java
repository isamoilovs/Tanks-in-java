package com.isamoilovs.mygdx.game.units.map.emitters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.isamoilovs.mygdx.game.screens.GameScreen;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.units.tanks.Animation;
import com.isamoilovs.mygdx.game.units.tanks.PlayerTank;
import org.graalvm.compiler.loop.MathUtil;

import java.util.List;

public class PerksEmitter {

    public enum PerkType {
        MED_KIT(0.5f, 1, 30.0f, -1.0f), SHIELD(0.5f, 0, 30.0f, 10.0f);

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
        private Circle circle;

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
            this.circle.setPosition(-33.0f, -33.0f);
            this.active = false;
        }


        public Circle getPerkCircle() {
            return circle;
        }

        public Perk(PerkType perkType) {
            this.perkType = perkType;
            this.active = false;
            this.position = new Vector2(0, 0);
            this.circle = new Circle();
            circle.setPosition(position);
        }
    }

    private GameScreen gameScreen;
    private static int PERKS_AMOUNT = 10;


    private Perk[] perks;
    private TextureRegion[][] perksTextures;
    final float MAX_FRAME_TIME = 0.125f;

    public Perk[] getPerks() {
        return perks;
    }

    public PerksEmitter(GameScreen gameScreen, TextureAtlas atlas) {
        this.gameScreen = gameScreen;
        this.perksTextures = new TextureRegion(atlas.findRegion("perks")).split( 32, 32);
        this.perks = new Perk[PERKS_AMOUNT];
        for (int i = 0; i < perks.length; i++) {
            perks[i] = new Perk(PerkType.values()[MathUtils.random(0, PerkType.values().length - 1)]);
        }
    }

    public void activate(Map map) {
        float cordX, cordY;
        do {
            cordX = MathUtils.random(0, Gdx.graphics.getWidth());
            cordY = MathUtils.random(0, Gdx.graphics.getHeight());
        } while (!map.isAreaClear(cordX, cordY, 32));

        for (int i = 0; i < perks.length; i++) {
            if(!perks[i].isActive()) {
                perks[i].position.set(cordX, cordY);
                perks[i].circle.setPosition(cordX, cordY);
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

                if (perks[i].currentTime > MAX_FRAME_TIME) {
                    perks[i].frame++;
                    perks[i].currentTime = 0;
                }
                if(perks[i].frame >= 8)
                    perks[i].frame = 0;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < perks.length; i++) {
            if(perks[i].isActive()){
                batch.draw(perksTextures[perks[i].perkType.index][perks[i].frame],perks[i].position.x, perks[i].position.y);
            }
        }
    }


    public void checkPerkAndPlayerCollision(List<PlayerTank> playerTanks) {

        for (int i = 0; i < playerTanks.size(); i++) {
            Circle playerCircle = new Circle(playerTanks.get(i).getCircle());
            for (int j = 0; j < perks.length; j++) {
                Circle perkCircle = new Circle(perks[j].getPerkCircle());
                if(perkCircle.overlaps(playerCircle)
                        && (playerTanks.get(i).isAbleToBeDamaged())
                        && perks[j].getPerkType() == PerksEmitter.PerkType.SHIELD) {
                    playerTanks.get(i).getShield();
                   perks[j].disActivate();
                }
                if(perkCircle.overlaps(playerCircle)
                        && perks[j].getPerkType() == PerksEmitter.PerkType.MED_KIT) {
                    perks[j].disActivate();
                    playerTanks.get(i).repair();
                    break;
                }
            }
        }
    }
}
