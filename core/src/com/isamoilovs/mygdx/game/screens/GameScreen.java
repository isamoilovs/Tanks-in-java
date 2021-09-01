package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.units.map.emitters.BotEmitter;
import com.isamoilovs.mygdx.game.units.map.emitters.BulletEmitter;
import com.isamoilovs.mygdx.game.units.map.emitters.PerksEmitter;
import com.isamoilovs.mygdx.game.units.tanks.BotTank;
import com.isamoilovs.mygdx.game.units.tanks.PlayerTank;
import com.isamoilovs.mygdx.game.units.tanks.Tank;
import com.isamoilovs.mygdx.game.units.weapon.Bullet;
import com.isamoilovs.mygdx.game.utils.GameConsts;
import com.isamoilovs.mygdx.game.utils.GameType;
import com.isamoilovs.mygdx.game.utils.KeysControl;
import com.isamoilovs.mygdx.game.utils.RectDrawable;

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

    public void setPaused(boolean paused) {
        this.paused = paused;
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
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        loadButtons();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
        System.out.println(Gdx.graphics.getWidth());
        System.out.println(Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(new Color(0x747474ff));
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
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_OVER, players.get(0).getScore());
            }
        } else {
            if(!players.get(0).isActive()) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_OVER, players.get(0).getScore());
                System.out.println("GAME OVER");
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
            map.update(dt);
        }

        stage.act(dt);
    }
    public void checkCollisions() {
        perksEmitter.checkPerkAndPlayerCollision(getPlayers());
        bulletEmitter.checkTankAndBulletCollisions(botEmitter.getBots(), players, FRIENDLY_FIRE);
        map.checkWallsAndBulletsCollisions(bulletEmitter);
    }

    public void loadButtons() {
        TextureRegion buttonMenu = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][0]);
        TextureRegion buttonPause = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][1]);
        TextureRegion buttonContinue = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][2]);
        TextureRegion buttonExit = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][3]);
        TextureRegion buttonDialog = new TextureRegion(atlas.findRegion("simpleButton"));

        Skin skin = new Skin();
        skin.add("buttonPause", buttonPause);
        skin.add("buttonContinue", buttonContinue);
        skin.add("buttonExit", buttonExit);
        skin.add("buttonMenu", buttonMenu);
        skin.add("buttonDialog", buttonDialog);

        Window.WindowStyle windowStyle = new Window.WindowStyle(font24, Color.WHITE, new RectDrawable(Color.GRAY, 1));
        Label.LabelStyle labelStyleHeader = new Label.LabelStyle(font24, new Color(1.0f, 1.0f, 1.0f, 1.0f));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonDialog");
        textButtonStyle.font = font24;
        skin.setScale(1);

        Group group = new Group();
        final ImageButton pause = new ImageButton(skin.getDrawable("buttonPause"), skin.getDrawable("buttonContinue"), skin.getDrawable("buttonContinue"));
        final ImageButton exit = new ImageButton(skin.getDrawable("buttonExit"));
        final ImageButton menu = new ImageButton(skin.getDrawable("buttonMenu"));
        final TextButton dialogYes = new TextButton("YES", textButtonStyle);
        final TextButton dialogNo = new TextButton("NO", textButtonStyle);
        final Dialog quitGame = new Dialog("", windowStyle);
        final Label label = new Label("Quit the game?", labelStyleHeader);
        label.setAlignment(Align.center);
        label.setFontScale(2);
        quitGame.text(label);


        pause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });

        exit.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Gdx.app.exit();
           }
        });

        menu.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               setPaused(true);
               quitGame.show(stage);
           }
        });

        dialogYes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = false;
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME_OVER, players.get(0).getScore());
            }
        });

        dialogNo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = false;
                quitGame.hide();
            }
        });
        quitGame.button(dialogYes);
        quitGame.button(dialogNo);

        int space = (int)pause.getWidth() / 10;
        menu.setPosition(0,0);
        pause.setPosition(space * 11, 0);
        exit.setPosition(space * 22, 0);

        group.addActor(menu);
        group.addActor(pause);
        group.addActor(exit);
        stage.addActor(group);

        group.setPosition(5, 5);
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
