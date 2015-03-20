package com.jamesrskemp.tiledexample;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;

public class TiledExample extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;

	TiledMap map;
	TiledMapTileLayer layer;
	IsometricTiledMapRenderer renderer;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		map = new TmxMapLoader().load("SimpleCityTutorialMap.tmx");
		layer = (TiledMapTileLayer)map.getLayers().get(0);

		renderer = new IsometricTiledMapRenderer(map, 0.5f, batch);
		renderer.setView(camera);

		Gdx.app.log(TiledExample.class.getName(), "Map: <" + layer.getWidth() + "," + layer.getHeight() + ">");
		Gdx.app.log(TiledExample.class.getName(), "Tile: <" + layer.getTileWidth() + "," + layer.getTileHeight() + ">");

		//img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		renderer.render();
		batch.begin();
		//
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
	}
}
