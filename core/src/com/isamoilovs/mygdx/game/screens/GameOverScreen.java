package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.utils.GameType;

public class GameOverScreen extends AbstractScreen {
    StringBuilder tmpString;
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;

    public GameOverScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("gamePack.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        stage = new Stage();
        tmpString = new StringBuilder("GAME OVER");
        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("simpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font24, new Color(1.0f, 0.0f, 0.0f, 1.0f));
        final Label label = new Label(tmpString.toString(), labelStyle);
        label.setWidth(400);
        label.setHeight(50);
        final TextButton goToMenu = new TextButton("MENU", textButtonStyle);
        final TextButton exitButton = new TextButton("EXIT", textButtonStyle);

        goToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        goToMenu.setPosition(0, 190);
        exitButton.setPosition(0, 150);
        label.setPosition(0, 300);
        group.addActor(goToMenu);
        group.addActor(exitButton);
        group.addActor(label);
        stage.addActor(group);
        group.setPosition(580, 150);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        stage.draw();
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

