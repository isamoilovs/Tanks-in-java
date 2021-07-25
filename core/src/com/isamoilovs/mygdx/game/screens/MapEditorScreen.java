package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.utils.GameType;

public class MapEditorScreen extends AbstractScreen{
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;
    public MapEditorScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("gamePack.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        stage = new Stage();

        Skin brickSkin = new Skin();
        Skin stoneSkin = new Skin();
        Skin obsidianSkin = new Skin();
        Skin waterSkin = new Skin();

        brickSkin.add("brick", new TextureRegion(atlas.findRegion("brick")));
        stoneSkin.add("stone", new TextureRegion(atlas.findRegion("stones")).split(32, 32)[0][5]);
        obsidianSkin.add("obsidian", atlas.findRegion("obsidian"));
        waterSkin.add("water", new TextureRegion(atlas.findRegion("water")));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton start1Button = new TextButton("Start 1P", textButtonStyle);
        final TextButton start2Button = new TextButton("Start 2P", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);

        start1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME);
            }
        });
        start2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.GAME);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        start1Button.setPosition(0, 230);
        start2Button.setPosition(0, 190);
        exitButton.setPosition(0, 150);
        group.addActor(start1Button);
        group.addActor(start2Button);
        group.addActor(exitButton);
        stage.addActor(group);
        group.setPosition(580, 150);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0.5f, 0, 1);
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
