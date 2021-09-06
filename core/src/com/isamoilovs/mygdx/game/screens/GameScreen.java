package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
    private Boolean dialogFlag;
    private Dialog quitGame;
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
        this.dialogFlag = false;
        this.atlas = new TextureAtlas("gamePack.pack");
        this.cursor = new TextureRegion(atlas.findRegion("cursor"));
        this.gameTimer = 0.0f;
        this.bulletEmitter = new BulletEmitter(atlas);
        this.mousePosition = new Vector2();
        this.worldTimer = 0;
        this.map = new Map(atlas);
        this.players = new ArrayList<>();
        this.players.add(new PlayerTank(1, KeysControl.createStandardControl1(),  this, atlas));
        if(gameType == GameType.TWO_PLAYERS) {
            players.add(new PlayerTank(2, KeysControl.createStandardControl2(), this, atlas));
        }

        this.botEmitter = new BotEmitter(this, atlas);
        this.perksEmitter = new PerksEmitter(this, atlas);
        this.stage = new Stage();
        this.font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        this.quitGame = createDialog();

        this.stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.ESCAPE) {
                    dialogFlag =! dialogFlag;
                    if (dialogFlag) {
                        paused = true;
                        quitGame.show(stage);
                    } else {
                        paused = false;
                        quitGame.hide();
                    }
                } else {
                    return false;
                }
                return true;
            }
        });

        loadButtons();
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setCursorCatched(true);
    }

    public Dialog createDialog() {
        Window.WindowStyle windowStyle = new Window.WindowStyle(font24, Color.GRAY, new RectDrawable(Color.GRAY, 1));
        TextureRegion buttonDialog = new TextureRegion(atlas.findRegion("simpleButton"));
        Skin skin = new Skin();
        skin.add("buttonDialog", buttonDialog);
        Label.LabelStyle labelStyleHeader = new Label.LabelStyle(font24, new Color(1.0f, 1.0f, 1.0f, 1.0f));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonDialog");
        textButtonStyle.font = font24;
        skin.setScale(1);
        final TextButton dialogYes = new TextButton("YES", textButtonStyle);
        final TextButton dialogNo = new TextButton("NO", textButtonStyle);
        final Dialog quitGame = new Dialog("", windowStyle);
        final Label label = new Label("Go to menu?", labelStyleHeader);
        label.setAlignment(Align.center);
        label.setFontScale(2);
        quitGame.text(label);

        Pixmap dialog_sbg = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);
        dialog_sbg.setColor(new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 1f));
        dialog_sbg.fill();
        windowStyle.stageBackground = new Image(new Texture(dialog_sbg)).getDrawable();

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
                dialogFlag = false;
                quitGame.hide();
            }
        });
        quitGame.button(dialogYes);
        quitGame.button(dialogNo);
        return quitGame;
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
            if (gameTimer >= 10.0f) {
                gameTimer = 0;
                perksEmitter.activate(map);
            }

            if ((int)gameTimer % 3 >= 1) {
                botEmitter.activate(map);
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
        map.checkWallsAndBulletsCollisionsNew(bulletEmitter);
        map.getEagle().checkEagleAndBulletCollisions(bulletEmitter);
        bulletEmitter.checkBulletCollisions();
    }

    public void loadButtons() {
        TextureRegion buttonMenu = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][0]);
        TextureRegion buttonPause = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][1]);
        TextureRegion buttonContinue = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][2]);
        TextureRegion buttonExit = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][3]);
        TextureRegion buttonDialog = new TextureRegion(atlas.findRegion("simpleButton"));
        TextureRegion buttonTip = new TextureRegion(atlas.findRegion("simpleButton1"));

        Skin skin = new Skin();
        skin.add("buttonPause", buttonPause);
        skin.add("buttonContinue", buttonContinue);
        skin.add("buttonExit", buttonExit);
        skin.add("buttonMenu", buttonMenu);
        skin.add("buttonDialog", buttonDialog);
        skin.add("buttonTip", buttonTip);

        Window.WindowStyle windowStyle = new Window.WindowStyle(font24, Color.WHITE, new RectDrawable(Color.GRAY, 1));
        Label.LabelStyle labelStyleHeader = new Label.LabelStyle(font24, new Color(1.0f, 1.0f, 1.0f, 1.0f));
        TextButton.TextButtonStyle styleOfDialogButtons = new TextButton.TextButtonStyle();
        TextButton.TextButtonStyle styleOfTipButton = new TextButton.TextButtonStyle();

        styleOfDialogButtons.up = skin.getDrawable("buttonDialog");
        styleOfDialogButtons.font = font24;

        styleOfTipButton.up = skin.getDrawable("buttonTip");
        styleOfTipButton.font = font24;

        skin.setScale(1);

        Group group = new Group();
        final ImageButton pause = new ImageButton(skin.getDrawable("buttonPause"), skin.getDrawable("buttonContinue"), skin.getDrawable("buttonContinue"));
        final ImageButton exit = new ImageButton(skin.getDrawable("buttonExit"));
        final ImageButton menu = new ImageButton(skin.getDrawable("buttonMenu"));
        final TextButton showTips = new TextButton(" Show tips ", styleOfTipButton);
        final Dialog quitGame = createDialog();
        final Label label = new Label("Quit the game?", labelStyleHeader);
        label.setAlignment(Align.center);
        label.setFontScale(2);

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

        showTips.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (showTips.getText().toString()) {
                    case " Show tips " :
                        showTips.setText(" Hide tips ");
                        break;
                    default :
                        showTips.setText(" Show tips ");
                }
            }
        });

        int space = (int)pause.getWidth() / 10;
        menu.setPosition(0,0);
        pause.setPosition(space * 11, 0);
        exit.setPosition(space * 22, 0);
        showTips.setPosition(space * 22, 0);

        group.addActor(menu);
        group.addActor(pause);
        //group.addActor(exit);
        group.addActor(showTips);
        stage.addActor(group);

        group.setPosition(5, 5);
    }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
