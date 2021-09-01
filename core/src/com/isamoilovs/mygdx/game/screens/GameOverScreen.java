package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.TouchableAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.utils.GameType;
import com.isamoilovs.mygdx.game.utils.RectDrawable;

import java.awt.geom.Rectangle2D;

public class GameOverScreen extends AbstractScreen {
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;
    private int score;
    private String name;
    private TextField.TextFieldStyle textFieldStyle;

    public void setScore(int score) {
        this.score = score;
    }

    public GameOverScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas("gamePack.pack");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        stage = new Stage();
        name = new String();

        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("simpleButton")));
        skin.add("gameOverImage", new TextureRegion(atlas.findRegion("gameOver")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font24;
        textFieldStyle.background = new RectDrawable(Color.GRAY);
        textFieldStyle.messageFont = font24;
        textFieldStyle.messageFontColor = Color.WHITE;
        textFieldStyle.cursor = new TextureRegionDrawable(atlas.findRegion("cursorTextField"));
        textFieldStyle.fontColor = new Color(1, 1, 1, 1);
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        Image gameOverImage = new Image(skin.getDrawable("gameOverImage"));

        final TextField textField = new TextField("", textFieldStyle);
        textField.setMessageText("Enter Your Name...");
        textField.setSize(500, 40);
        textField.setMaxLength(10);
        Group group = new Group();
        group.setWidth(500);
        group.setHeight(500);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font24, new Color(1.0f, 0.0f, 0.0f, 1.0f));
        final Label label = new Label("", labelStyle);
        label.setWidth(400);
        label.setHeight(50);
        final TextButton save = new TextButton("SAVE", textButtonStyle);
        final TextButton quit = new TextButton("  QUIT WITHOUT SAVING  ", textButtonStyle);

        quit.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
           }
        });

        save.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println(textField.getText().toString().length());
                if(textField.getText().toString().length() == 0) {
                    label.setPosition(0, 0.20f*Gdx.graphics.getHeight());
                    label.setText("ENTER YOUR NAME!!!");
                } else {
                    FileHandle file = Gdx.files.local("scores.txt");
                    file.writeString(textField.getText().toString() + " " + score + "\n", true);
                    ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
                }
            }
        });

        save.setPosition(textField.getWidth() + 20, 0.26f * Gdx.graphics.getHeight());
        textField.setPosition(0, 0.26f*Gdx.graphics.getHeight());
        label.setPosition(0, 0.22f*Gdx.graphics.getHeight());
        quit.setPosition((save.getWidth() + textField.getWidth() - quit.getWidth()) / 2, 0.18f*Gdx.graphics.getHeight());
        gameOverImage.setPosition((save.getWidth() + textField.getWidth() - gameOverImage.getWidth()) / 2, 0.6f*Gdx.graphics.getHeight());

        group.addActor(save);
        group.addActor(quit);
        group.addActor(label);
        group.addActor(textField);
        group.addActor(gameOverImage);
        group.setPosition((Gdx.graphics.getWidth() - save.getWidth() - textField.getWidth()) / 2, 0);
        stage.addActor(group);
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

