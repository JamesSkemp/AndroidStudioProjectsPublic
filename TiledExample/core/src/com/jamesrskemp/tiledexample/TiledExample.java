package com.jamesrskemp.tiledexample;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.jamesrskemp.tiledexample.util.OrthoCamController;

public class TiledExample extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	OrthoCamController cameraController;

	TiledMap map;
	TiledMapTileLayer layer;
	IsometricTiledMapRenderer renderer;
	//OrthogonalTiledMapRenderer renderer2;
	Texture img;
	
	@Override
	public void create () {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float cellsInRow = 5 * 64;

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, cellsInRow * screenWidth / screenHeight, cellsInRow);

		cameraController = new OrthoCamController(camera);
		//Gdx.input.setInputProcessor(new GestureDetector(cameraController));
		Gdx.input.setInputProcessor(cameraController);

		map = new TmxMapLoader().load("SimpleCityTutorialMap.tmx");
		layer = (TiledMapTileLayer)map.getLayers().get(0);

		//camera.position.set(layer.getWidth() * 128 / 2, layer.getHeight() * 64 / 4, 0);
		camera.position.set(screenWidth / 2, screenHeight / 2, 0);
		Gdx.app.log(TiledExample.class.getName(), "Camera: <" + camera.position.x + "," + camera.position.y + ">");
		camera.position.set(layer.getWidth() * layer.getTileWidth() / 2, layer.getHeight() * layer.getTileHeight() / 2, 0);
		Gdx.app.log(TiledExample.class.getName(), "Camera: <" + camera.position.x + "," + camera.position.y + ">");

		renderer = new IsometricTiledMapRenderer(map, 1f, batch);
		//renderer2 = new OrthogonalTiledMapRenderer(map, batch);

		Gdx.app.log(TiledExample.class.getName(), "Map: <" + layer.getWidth() + "," + layer.getHeight() + ">");
		Gdx.app.log(TiledExample.class.getName(), "Tile: <" + layer.getTileWidth() + "," + layer.getTileHeight() + ">");
		Gdx.app.log(TiledExample.class.getName(), "Camera: <" + camera.position.x + "," + camera.position.y + ">");

		//img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		renderer.setView(camera);
		renderer.render();
		batch.begin();
		//
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
		renderer.dispose();
		//renderer2.dispose();
	}
}
