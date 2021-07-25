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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.utils.GameType;

import java.awt.geom.Rectangle2D;

public class GameOverScreen extends AbstractScreen {
    StringBuilder tmpString;
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
        tmpString = new StringBuilder("GAME OVER");
        name = new String();
        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("simpleButton")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font24;
        textFieldStyle.messageFont = font24;
        textFieldStyle.fontColor = new Color(1, 0, 0, 1);
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        TextField.TextFieldListener textFieldListener = new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                name += c;
                FileHandle file = Gdx.files.local("scores.txt");
                file.writeString(name + " " + score, true);
            }
        };

        class Rectangle extends Actor {

            private Texture texture;

            public Rectangle(float x, float y, float width, float height, Color color) {
                createTexture((int)width, (int)height, color);

                setX(x);
                setY(y);
                setWidth(width);
                setHeight(height);
            }

            private void createTexture(int width, int height, Color color) {
                Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
                pixmap.setColor(color);
                pixmap.fillRectangle(0, 0, width, height);
                texture = new Texture(pixmap);
                pixmap.dispose();
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                batch.draw(texture, getX(), getY(), getWidth(), getHeight());
            }
        }

        TextField textField = new TextField("", textFieldStyle);
        textField.setMessageText("Enter Your Name...");
        textField.setTextFieldListener(textFieldListener);

        Group group = new Group();
        group.setWidth(500);
        group.setHeight(500);
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
        textField.setPosition(0, 400);
        group.addActor(new Rectangle(-50, 380, group.getWidth() / 2, 50, new Color(0X3c3c3cff)));
        group.addActor(goToMenu);
        group.addActor(exitButton);
        group.addActor(label);
        group.addActor(textField);
        group.setColor(new Color(0X3C3C3Cff));
        group.setPosition(580, 0);
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

