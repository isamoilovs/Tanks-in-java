package com.isamoilovs.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isamoilovs.mygdx.game.utils.GameType;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME, SCORES, SETTINGS, MAP_REDACTOR, GAME_OVER;
    }

    private static ScreenManager ourInstance = new ScreenManager();
    private Game game;
    private GameOverScreen gameOverScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private ScoresScreen scoresScreen;
    private Viewport viewport;
    private Camera camera;
    public static final int WORLD_WIDTH = Gdx.graphics.getWidth();
    public static final int WORLD_HEIGHT = Gdx.graphics.getHeight();

    public  void init(Game game, SpriteBatch batch) {
        this.game = game;
        this.camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        this.camera.update();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);
        this.scoresScreen = new ScoresScreen(batch);
    }

    public static ScreenManager getInstance() { return ourInstance; }

    private ScreenManager() {

    }


    public Camera getCamera() {
        return camera;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }


    public Viewport getViewport() {
        return viewport;
    }


    public void setScreen(ScreenType screenType, Object... args) {
        Gdx.input.setCursorCatched(false);
        Screen currentScreen = game.getScreen();
        switch (screenType) {
            case MENU:
                if(currentScreen != null) {
                    currentScreen.dispose();
                }
                game.setScreen(menuScreen);
                break;
            case GAME:
                if(currentScreen != null) {
                    currentScreen.dispose();
                }
                gameScreen.setGameType((GameType) args[0]);
                game.setScreen(gameScreen);
                gameScreen.setPaused(false);
                break;
            case GAME_OVER:
                if(currentScreen != null) {
                    currentScreen.dispose();
                }
                game.setScreen(gameOverScreen);
                break;
            case SCORES:
                if(currentScreen != null) {
                    currentScreen.dispose();
                }
                game.setScreen(scoresScreen);
                break;
//            case SETTINGS:
//                game.setScreen(settingsScreen);
//            case MAP_REDACTOR:
//                game.setScreen(mapRedactorScreen);

        }
    }

        public void setScreen(ScreenType screenType, int score) {
            Gdx.input.setCursorCatched(false);
            Screen currentScreen = game.getScreen();
            switch (screenType) {
                case GAME_OVER:
                    gameOverScreen.setScore(score);
                    game.setScreen(gameOverScreen);
                    break;

//            case SCORES:
//                game.setScreen(scoresScreen);
//                break;
//            case SETTINGS:
//                game.setScreen(settingsScreen);
//            case MAP_REDACTOR:
//                game.setScreen(mapRedactorScreen);

            }


        if(currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
