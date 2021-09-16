package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.units.map.Map;
import com.isamoilovs.mygdx.game.utils.GameType;
import com.isamoilovs.mygdx.game.utils.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScoresScreen extends AbstractScreen{
    private FileHandle file = Gdx.files.local("scores.txt");
    private ArrayList<Player> players = new ArrayList<Player>();
    private SpriteBatch batch;
    private TextureAtlas atlas;
    private BitmapFont font24;
    private Stage stage;
    public ScoresScreen(SpriteBatch batch) {
        this.batch = batch;
    }



    @Override
    public void show() {
        atlas = new TextureAtlas("images/gamePack.pack");
        font24 = new BitmapFont(Gdx.files.internal("fonts/font24.fnt"));
        stage = new Stage();
        loadButtons();
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {if(keycode == Input.Keys.ESCAPE) {
                    ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
                    return true;
                } else {
                    return false;
                }
            }
        });

        if(file.readString().length() > 0) {
            loadTable();
        }
        Gdx.input.setInputProcessor(stage);
    }

    public void parseFile() {
        players.clear();
        String strToParse = file.readString();

        Integer playerIndex = 0;
        Integer indexOfSpace = strToParse.indexOf(" ");
        Integer indexOfEnd = strToParse.indexOf("\n");
        String tmpStr = strToParse.substring(0, indexOfEnd);
        Boolean flag = false;

        while (indexOfSpace != -1){
            String nameOfCurrentPlayer = tmpStr.substring(0, indexOfSpace);
            Integer scoreOfCurrentPlayer = Integer.parseInt(tmpStr.substring(indexOfSpace + 1));
            if(players.size() > 0) {
                for (int i = 0; i < players.size(); i++) {
                    if ((players.get(i).getName().equals(nameOfCurrentPlayer)) && (players.get(i).getScore() < scoreOfCurrentPlayer)){
                        players.get(i).setScore(scoreOfCurrentPlayer);
                        flag = false;
                        break;
                    }
                    else if (players.get(i).getName().equals(nameOfCurrentPlayer)){
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    players.add(new Player());
                    players.get(playerIndex).setName(nameOfCurrentPlayer);
                    players.get(playerIndex).setScore(scoreOfCurrentPlayer);
                    playerIndex++;
                }
                flag = true;
            } else {
                players.add(new Player());
                players.get(playerIndex).setName(nameOfCurrentPlayer);
                players.get(playerIndex).setScore(scoreOfCurrentPlayer);
                playerIndex++;
            }

            strToParse = strToParse.substring(indexOfEnd + 1);
            indexOfEnd = strToParse.indexOf("\n");
            if(indexOfEnd != -1) {
                tmpStr = strToParse.substring(0, indexOfEnd);
            } else {
                break;
            }
            indexOfSpace = tmpStr.indexOf(" ");
        }

        for (int out = players.size() - 1; out >= 1 ; out--) {
            for (int in = 0; in < out; in++) {
                if (players.get(in).getScore() > players.get(in + 1).getScore()) {
                    Player tmpPlayer = new Player();
                    tmpPlayer.changeTo(players.get(in));
                    players.get(in).changeTo(players.get(in + 1));
                    players.get(in + 1).changeTo(tmpPlayer);
                }
            }
        }
        Collections.reverse(players);
    }

    public void loadTable() {
        if (file.readString().length() > 0) {
            parseFile();
        } else {
            players.clear();
        }
        Skin skin = new Skin();
        Label.LabelStyle labelStyleLines = new Label.LabelStyle(font24, new Color(1.0f, 0.0f, 0.0f, 1.0f));
        Label.LabelStyle labelStyleHeader = new Label.LabelStyle(font24, new Color(1.0f, 1.0f, 1.0f, 1.0f));

        Container tableContainer = new Container();
        tableContainer.setSize(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
        tableContainer.setPosition((Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 4) / 2.0f, (Gdx.graphics.getHeight()/2)/2.0f);
        tableContainer.fillX();
        Table table = new Table(skin);
        Label header = new Label("SCORES", labelStyleHeader);
        header.setAlignment(Align.center);
        table.row().colspan(2).expandX().fillX();
        table.add(header).fillX();

        int playerIndex = 0;

        while (playerIndex < 10 && playerIndex < players.size()) {
            Label labelName = new Label(players.get(playerIndex).getName(), labelStyleLines);
            labelName.setAlignment(Align.left);
            Label labelScore = new Label(players.get(playerIndex).getScore().toString(), labelStyleLines);
            labelScore.setAlignment(Align.right);
            playerIndex++;
            table.row().expandX().fillX();
            table.add(labelName).expandX().fillX();
            table.add(labelScore).expandX().fillX();
        }
        tableContainer.setActor(table);
        stage.addActor(tableContainer);
    }

    public void loadButtons() {
        Skin skin = new Skin();
        TextureRegion buttonMenu = new TextureRegion(atlas.findRegion("buttons").split(50, 50)[0][0]);
        TextureRegion buttonClear = new TextureRegion(atlas.findRegion("simpleButton1"));

        skin.add("buttonMenu", buttonMenu);
        skin.add("buttonClear", buttonClear);
        skin.setScale(1);

        final ImageButton menu = new ImageButton(skin.getDrawable("buttonMenu"));
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.MENU);
            }
        });

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonClear");
        textButtonStyle.font = font24;

        final TextButton clear = new TextButton("Clear", textButtonStyle);
        clear.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                file.writeString("", false);
                ScreenManager.getInstance().setScreen(ScreenManager.ScreenType.SCORES);
            }
        });

        Group group = new Group();

        menu.setPosition(0,0);
        clear.setPosition(menu.getWidth() + 5, 0);
        group.addActor(menu);
        group.addActor(clear);
        group.setPosition(5, 5);

        stage.addActor(group);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0, 0, 0, 1);
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
