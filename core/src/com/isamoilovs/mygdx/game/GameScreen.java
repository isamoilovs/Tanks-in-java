package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.units.BotTank;
import com.isamoilovs.mygdx.game.units.PlayerTank;
import com.isamoilovs.mygdx.game.units.PlayerTankWithMovableTurret;
import com.isamoilovs.mygdx.game.units.Tank;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;
    private PlayerTank playerTank;
    private BulletEmitter bulletEmitter;
    private Map map;
    private BotEmitter botEmitter;
    private float gameTimer;
    private static final boolean FRIENDLY_FIRE = false;
    private boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;
    float worldTimer;
    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }


    public PlayerTank getPlayer() {
        return playerTank;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("gamePack.pack");
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        gameTimer = 0.0f;
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        bulletEmitter = new BulletEmitter(atlas);
        map = new Map(atlas);
        playerTank = new PlayerTankWithMovableTurret(this, atlas);
        botEmitter = new BotEmitter(this, atlas);
        stage = new Stage();
        mousePosition = new Vector2();
        this.worldTimer = 0;
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
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        pauseButton.setPosition(0, 40);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        stage.addActor(group);
        group.setPosition(1280-140-20, 720-64-20);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0.0f, 0, 1);

//        ScreenManager.getInstance().getCamera().position.set(playerTank.getPosition().x, playerTank.getPosition().y, 0);
//        ScreenManager.getInstance().getCamera().update();

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        map.render(batch);
        playerTank.render(batch);
        botEmitter.render(batch);
        bulletEmitter.render(batch);
        playerTank.renderHUD(batch, font24);
        batch.draw(cursor, mousePosition.x - 16, mousePosition.y - 16,
                cursor.getRegionWidth() / 2, cursor.getRegionHeight() / 2, cursor.getRegionWidth(), cursor.getRegionHeight(), 1, 1, -worldTimer*50);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void resume() {

    }


    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public void update(float dt) {
        worldTimer += dt;
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);

        if(!paused) {
            gameTimer += dt;
            if (gameTimer >= 10.0f) {
                gameTimer = 0;

                float coordX, coordY;
                do {
                    coordX = MathUtils.random(0, Gdx.graphics.getWidth());
                    coordY = MathUtils.random(0, Gdx.graphics.getHeight());
                } while (!map.isAreaClear(coordX, coordY, 32));
                botEmitter.activate(coordX, coordY);
            }
            playerTank.update(dt);
            botEmitter.update(dt);
            bulletEmitter.update(dt);
            checkCollisions();
        }
        stage.act(dt);
    }
    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if(bullet.isActive()) {
                for (int j = 0; j < botEmitter.getBots().length; j++) {
                    BotTank bot = botEmitter.getBots()[j];
                    if(bot.isActive()) {
                        if(checkBulletOwner(bot, bullet) && bot.getCircle().contains(bullet.getPosition())){
                            bullet.disActivate();
                            bot.takeDamage(bullet.getDamage());
                            break;
                        }
                    }
                }
                if(checkBulletOwner(playerTank, bullet) && playerTank.getCircle().contains(bullet.getPosition())) {
                    bullet.disActivate();
                    playerTank.takeDamage(bullet.getDamage());
                }
                map.checkWallsAndBulletsCollisions(bullet);
            }
        }
    }

    public boolean checkBulletOwner(Tank tank, Bullet bullet) {
        if(!FRIENDLY_FIRE) {
            return tank.getOwnerType() != bullet.getOwner().getOwnerType();
        } else {
            return tank != bullet.getOwner();
        }
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
