package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.units.BotTank;
import com.isamoilovs.mygdx.game.units.PlayerTank;
import com.isamoilovs.mygdx.game.units.PlayerTankWithMovableTurret;
import com.isamoilovs.mygdx.game.units.Tank;

public class MyGdxGame extends ApplicationAdapter {
	private TextureAtlas atlas;
	private SpriteBatch batch;
	private PlayerTank tank;
	private BulletEmitter bulletEmitter;
	private Map map;
	private BotEmitter botEmitter;
	private float gameTimer;

	public BulletEmitter getBulletEmitter() {
		return bulletEmitter;
	}
	
	@Override
	public void create () {
		atlas = new TextureAtlas("gamePack.pack");
		gameTimer = 0.0f;
		batch = new SpriteBatch();
		bulletEmitter = new BulletEmitter(atlas);
		map = new Map(atlas);
		tank = new PlayerTank(this, atlas);
		botEmitter = new BotEmitter(this, atlas);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		ScreenUtils.clear(0, 0.6f, 0, 1);
		batch.begin();
		map.render(batch);
		tank.render(batch);
		botEmitter.render(batch);
		bulletEmitter.render(batch);
		batch.end();
	}

	public void update(float dt) {
		gameTimer += dt;
		if(gameTimer >= 2.0f) {
			gameTimer = 0;
			botEmitter.activate(MathUtils.random(0, Gdx.graphics.getWidth()), MathUtils.random(0, Gdx.graphics.getHeight()));
		}
		tank.update(dt);
		botEmitter.update(dt);
		bulletEmitter.update(dt);
		checkCollisions();
	}

	public void checkCollisions() {
		for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
			Bullet bullet = bulletEmitter.getBullets()[i];
			if(bullet.isActive()) {
				for (int j = 0; j < botEmitter.getBots().length; j++) {
					BotTank bot = botEmitter.getBots()[j];
					if(bot.isActive()) {
						if(bot.getCircle().contains(bullet.getPosition())){
							bullet.disActivate();
							bot.takeDamage(bullet.getDamage());
						}
					}
				}
			}
		}
	}

	@Override
	public void dispose () {
	}
}
