package com.isamoilovs.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.isamoilovs.mygdx.game.units.PlayerTank;
import com.isamoilovs.mygdx.game.units.PlayerTankWithMovableTurret;
import com.isamoilovs.mygdx.game.units.Tank;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private PlayerTank tank;
	private BulletEmitter bulletEmitter;
	private Map map;

	public BulletEmitter getBulletEmitter() {
		return bulletEmitter;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bulletEmitter = new BulletEmitter();
		map = new Map();
		tank = new PlayerTank(this);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		ScreenUtils.clear(0, 0.6f, 0, 1);
		batch.begin();
		map.render(batch);
		tank.render(batch);
		bulletEmitter.render(batch);
		batch.end();
	}

	public void update(float dt) {
		tank.update(dt);
		bulletEmitter.update(dt);
	}

	@Override
	public void dispose () {
		batch.dispose();
		tank.dispose();
	}
}
