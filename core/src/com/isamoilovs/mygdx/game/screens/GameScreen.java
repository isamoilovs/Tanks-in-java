package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.units.map.emitters.BotEmitter;
import com.isamoilovs.mygdx.game.units.map.emitters.BulletEmitter;
import com.isamoilovs.mygdx.game.units.map.emitters.PerksEmitter;
import com.isamoilovs.mygdx.game.units.tanks.BotTank;
import com.isamoilovs.mygdx.game.units.tanks.PlayerTank;
import com.isamoilovs.mygdx.game.units.tanks.Tank;
import com.isamoilovs.mygdx.game.units.weapon.Bullet;
import com.isamoilovs.mygdx.game.utils.GameType;
import com.isamoilovs.mygdx.game.utils.KeysControl;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;
    private java.util.List<PlayerTank> players;
    private BulletEmitter bulletEmitter;
    private Map map;
    private BotEmitter botEmitter;
    private float gameTimer;
    private static final boolean FRIENDLY_FIRE = false;
    private boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;
    float worldTimer;
    private GameType gameType;
    private PerksEmitter perksEmitter;
    private Image frameImage;
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
        this.gameType = GameType.ONE_PLAYER;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public Map getMap() {
        return map;
    }

    public List<PlayerTank> getPlayers() {
        return players;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("gamePack.pack");
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        gameTimer = 0.0f;
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        bulletEmitter = new BulletEmitter(atlas);
        frameImage = new Image(atlas.findRegion("frame"));
        mousePosition = new Vector2();
        this.worldTimer = 0;
        map = new Map(atlas);
        players = new ArrayList<>();
        players.add(new PlayerTank(1, KeysControl.createStandardControl1(),  this, atlas));
        if(gameType == GameType.TWO_PLAYERS) {
            players.add(new PlayerTank(2, KeysControl.createStandardControl2(), this, atlas));
        }

        botEmitter = new BotEmitter(this, atlas);
        perksEmitter = new PerksEmitter(this, atlas);
        stage = new Stage();
        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("simpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);
        final TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
                Gdx.input.setCursorCatched(false);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
            }
        });

        pauseButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        stage.addActor(group);
        //stage.addActor(frameImage);
        group.setPosition(1280-140-20, 720-64-20);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0.0f, 0, 1);
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        map.render(batch);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).render(batch);
        }
        botEmitter.render(batch);
        bulletEmitter.render(batch);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).renderHUD(batch, font24);
        }
        perksEmitter.render(batch);
        batch.end();

        stage.draw();

        batch.begin();
        batch.draw(cursor, mousePosition.x - 16, mousePosition.y - 16,
                cursor.getRegionWidth() / 2, cursor.getRegionHeight() / 2, cursor.getRegionWidth(), cursor.getRegionHeight(), 1, 1, -worldTimer*50);
        batch.end();
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public void update(float dt) {

        if(gameType == GameType.TWO_PLAYERS) {
            if(!players.get(0).isActive()  && !players.get(1).isActive()) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_OVER);
            }
        } else {
            if(players.get(0).getLives() <= 0) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_OVER);
            }
        }



        worldTimer += dt;
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);

        if(!paused) {
            gameTimer += dt;
            if (gameTimer >= 3.0f) {
                gameTimer = 0;
                botEmitter.activate(map);
                perksEmitter.activate(map);
            }

            for (int i = 0; i < players.size(); i++) {
                players.get(i).update(dt);
            }

            perksEmitter.update(dt);
            botEmitter.update(dt);
            bulletEmitter.update(dt);
            checkCollisions();
        }

        stage.act(dt);
    }
    public void checkCollisions() {
        perksEmitter.checkPerkAndPlayerCollision(getPlayers());
        bulletEmitter.checkTankAndBulletCollisions(botEmitter.getBots(), players, FRIENDLY_FIRE);
        map.checkWallsAndBulletsCollisions(bulletEmitter);
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
