package com.jamesrskemp.tiledexample;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.jamesrskemp.tiledexample.util.OrthoCamController;

import java.util.Iterator;

public class TiledExample extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	OrthoCamController cameraController;

	TiledMap map;
	TiledMapTileLayer layer;
	IsometricTiledMapRenderer renderer;
	//OrthogonalTiledMapRenderer renderer2;
	Texture img;

	/**
	 * 0,0 = west | 0,x-1 = north | x-1,0 = south
	 */
	Vector2 centeredCameraWorldPosition = new Vector2();

	@Override
	public void create () {
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float cellsInRow = 5 * 64;

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, cellsInRow * screenWidth / screenHeight, cellsInRow);

		cameraController = new OrthoCamController(camera);

		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		// Gesture detector should be first.
		inputMultiplexer.addProcessor(new GestureDetector(cameraController));
		inputMultiplexer.addProcessor(cameraController);
		Gdx.input.setInputProcessor(inputMultiplexer);

		map = new TmxMapLoader().load("SimpleCityTutorialMap.tmx");
		layer = (TiledMapTileLayer)map.getLayers().get(0);

		//camera.position.set(layer.getWidth() * 128 / 2, layer.getHeight() * 64 / 4, 0);
		camera.position.set(screenWidth / 2, screenHeight / 2, 0);
		Gdx.app.log(TiledExample.class.getName(), "Camera: <" + camera.position.x + "," + camera.position.y + ">");
		camera.position.set(layer.getWidth() * layer.getTileWidth() / 2, layer.getHeight() * layer.getTileHeight() / 2, 0);
		Gdx.app.log(TiledExample.class.getName(), "Camera: <" + camera.position.x + "," + camera.position.y + ">");

		Gdx.app.log(TiledExample.class.getName(), "Mod width: " + layer.getWidth() % 2);
		Gdx.app.log(TiledExample.class.getName(), "Mod height: " + layer.getHeight() % 2);

		Gdx.app.log(TiledExample.class.getName(), "Half width: " + layer.getWidth() / 2);
		Gdx.app.log(TiledExample.class.getName(), "Half height: " + layer.getHeight() / 2);

		// x and y will between 0 and tiles - 1. So we need to account for the width of a tile.
		centeredCameraWorldPosition.x = layer.getWidth() / 2 * layer.getTileWidth();
		centeredCameraWorldPosition.y = layer.getHeight() / 2 * layer.getTileHeight();
		if (layer.getWidth() % 2 == 1) {
			centeredCameraWorldPosition.x += layer.getTileWidth() / 2;
		}
		if (layer.getHeight() % 2 == 1) {
			centeredCameraWorldPosition.y += layer.getTileHeight() / 2;
		}
		// This puts the south-most tile at the bottom of the camera view.
		centeredCameraWorldPosition.y = layer.getTileHeight() / 2;
		// This puts the south-most tile top (and bit of the innards) at the bottom of the camera view.
		centeredCameraWorldPosition.y = layer.getTileHeight();

		Gdx.app.log(TiledExample.class.getName(), "Center cell: <" + centeredCameraWorldPosition.x + "," + centeredCameraWorldPosition.y + ">");
		camera.position.set(
				centeredCameraWorldPosition.x,
				centeredCameraWorldPosition.y,
				0);
		Gdx.app.log(TiledExample.class.getName(), "Camera: <" + camera.position.x + "," + camera.position.y + ">");

		TiledMapTileLayer.Cell centerCell = layer.getCell(0,2);
		Iterator<String> iterator = centerCell.getTile().getProperties().getKeys();
		String keyName;
		while (iterator.hasNext()) {
			keyName = iterator.next();
			Gdx.app.log(TiledExample.class.getName(), "Key: " + keyName);
		}
		//centerCell.getTile().getProperties().getKeys();

		renderer = new IsometricTiledMapRenderer(map, 1f, batch);
		//renderer2 = new OrthogonalTiledMapRenderer(map, batch);

		Gdx.app.log(TiledExample.class.getName(), "Map: <" + layer.getWidth() + "," + layer.getHeight() + ">");
		Gdx.app.log(TiledExample.class.getName(), "Tile: <" + layer.getTileWidth() + "," + layer.getTileHeight() + ">");
		Gdx.app.log(TiledExample.class.getName(), "Layer: <" + layer.getWidth() + "," + layer.getHeight() + ">");
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
