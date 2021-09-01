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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.utils.GameConsts;
import com.isamoilovs.mygdx.game.utils.GameType;
import com.isamoilovs.mygdx.game.utils.RectDrawable;

public class MenuScreen extends AbstractScreen {
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24 ;
    private Stage stage;
    public MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        loadButtons();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        stage.draw();
    }

    public void loadButtons() {
        atlas = new TextureAtlas("gamePack.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));;
        stage = new Stage();
        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("simpleButton")));
        skin.add("logo", new TextureRegion(atlas.findRegion("logo")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton start1Button = new TextButton("Start 1P", textButtonStyle);
        final TextButton start2Button = new TextButton("Start 2P", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);
        final TextButton scoresButton = new TextButton("Scores", textButtonStyle);
        final Image logo = new Image(skin.getDrawable("logo"));

        start1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME, GameType.ONE_PLAYER);
                Gdx.input.setCursorCatched(false);
            }
        });
        start2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME, GameType.TWO_PLAYERS);
                Gdx.input.setCursorCatched(false);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        scoresButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.SCORES);
                Gdx.input.setCursorCatched(false);
            }
        });

        logo.setPosition( (start1Button.getWidth() - logo.getWidth()) / 2, 0.5f * Gdx.graphics.getHeight());
        start1Button.setPosition(0, 0.24f * Gdx.graphics.getHeight());
        start2Button.setPosition(0, 0.20f * Gdx.graphics.getHeight());
        scoresButton.setPosition(0, 0.16f * Gdx.graphics.getHeight());
        exitButton.setPosition(0, 0.12f * Gdx.graphics.getHeight());

        group.addActor(start1Button);
        group.addActor(start2Button);
        group.addActor(scoresButton);
        group.addActor(exitButton);
        group.addActor(logo);

        float scale = Gdx.graphics.getHeight() / 1080.0f;
        group.setScale(scale);
        stage.addActor(group);
        group.setPosition(Gdx.graphics.getWidth() / 2 - start1Button.getWidth() / 2 * scale, 0);
    }

    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        font24.dispose();
        stage.dispose();
    }
}
