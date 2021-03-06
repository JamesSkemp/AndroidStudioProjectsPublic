package com.jamesrskemp.tiledexample.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by James on 3/20/2015.
 */
public class OrthoCamController implements InputProcessor, GestureDetector.GestureListener {
	// was extending from GestureDetector.GestureAdapter
	// InputAdapter
	private static final String TAG = OrthoCamController.class.getName();

	final OrthographicCamera camera;
	final Vector3 curr = new Vector3();
	final Vector3 last = new Vector3(-1, -1, -1);
	final Vector3 delta = new Vector3();
	final Vector3 selectedPosition = new Vector3();

	float initialZoomScale = 1;

	public OrthoCamController (OrthographicCamera camera) {
		this.camera = camera;
		initialZoomScale = camera.zoom;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		Gdx.app.log(TAG, "panStop");
		//camera.position.set(x, y, 0);
		return false;
	}

	@Override
	public boolean touchDragged (int x, int y, int pointer) {
		Gdx.app.log(TAG, "touchDragged");
		camera.unproject(curr.set(x, y, 0));
		if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
			camera.unproject(delta.set(last.x, last.y, 0));
			delta.sub(curr);
			camera.position.add(delta.x, delta.y, 0);
		}
		last.set(x, y, 0);
		return false;
	}

	@Override
	public boolean touchUp (int x, int y, int pointer, int button) {
		Gdx.app.log(TAG, "touchUp");
		last.set(-1, -1, -1);
		initialZoomScale = camera.zoom;
		Gdx.app.log(TAG, "Zoom level: " + initialZoomScale);
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		Gdx.app.log(TAG, "zoom");
		camera.zoom = initialZoomScale * initialDistance / distance;
		return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		Gdx.app.log(TAG, "pinch");
		// TODO
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		Gdx.app.log(TAG, "fling");
		// TODO
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		Gdx.app.log(TAG, "longPress");
		camera.unproject(selectedPosition.set(x, y, 0));
		Gdx.app.log(TAG, "Long press position (camera): <" + x + "," + y + ">");
		Gdx.app.log(TAG, "Long press position (world): <" + selectedPosition.x + "," + selectedPosition.y + ">");
		// TODO
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		Gdx.app.log(TAG, "touchDown");
		camera.unproject(selectedPosition.set(x, y, 0));
		Gdx.app.log(TAG, "Touch down (camera): <" + x + "," + y + ">");
		Gdx.app.log(TAG, "Touch down (world): <" + selectedPosition.x + "," + selectedPosition.y + ">");
		// TODO
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		Gdx.app.log(TAG, "tap");
		camera.unproject(selectedPosition.set(x, y, 0));
		Gdx.app.log(TAG, "Tap position (camera): <" + x + "," + y + ">");
		Gdx.app.log(TAG, "Tap position (world): <" + selectedPosition.x + "," + selectedPosition.y + ">");
		// TODO
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		Gdx.app.log(TAG, "pan");
		/*Gdx.app.log(TAG, "Camera: <" + camera.position.x + "," + camera.position.y + "> zoom: " + camera.zoom);
		Gdx.app.log(TAG, "Current before: <" + x + "," + y + ">");
		//camera.unproject(curr.set(x, y, 0));
		Gdx.app.log(TAG, "Current after: <" + x + "," + y + ">");
		//Gdx.app.log(TAG, "Current 2: <" + curr.x + "," + curr.y + ">");
		Gdx.app.log(TAG, "Delta: <" + deltaX + "," + deltaY + ">");
		Gdx.app.log(TAG, "Delta 2: <" + delta.x + "," + delta.y + ">");
		//camera.unproject(delta.set(deltaX, deltaY, 0));
		//Gdx.app.log(TAG, "Last: <" + last.x + "," + last.y + ">");
		*//*if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
			//camera.unproject(delta.set(last.x, last.y, 0));
			delta.sub(curr);
			camera.position.add(delta.x, delta.y, 0);
		}*//*
		//camera.unproject(delta.set(deltaX, deltaY, 0));
		//camera.project(delta.set(deltaX, deltaY, 0));
		camera.position.add(-delta.x * camera.zoom, delta.y * camera.zoom, 0);

		if (camera.position.x > 1000 || camera.position.x < -1000) {
			camera.position.x = 0;
		}
		if (camera.position.y > 1000 || camera.position.y < -1000) {
			camera.position.y = 0;
		}*/
		//last.set(x, y, 0);
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.log(TAG, "keyDown");
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Gdx.app.log(TAG, "keyUp");
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		Gdx.app.log(TAG, "keyTyped");
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Gdx.app.log(TAG, "touchDown");
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		Gdx.app.log(TAG, "mouseMoved");
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		Gdx.app.log(TAG, "scrolled");
		return false;
	}
}