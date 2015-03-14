package com.jamesrskemp.libgdx.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class DropGame extends ApplicationAdapter {
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;

	private Array<Rectangle> raindrops;
	// Time will be stored in nanoseconds.
	private long lastDropTime;

	@Override
	public void create () {
		dropImage = new Texture("droplet.png");
		bucketImage = new Texture("bucket.png");

		// Sounds are loaded in memory, and should be < 10 seconds.
		dropSound = Gdx.audio.newSound(Gdx.files.internal("30341__junggle__waterdrop24.wav"));
		// Music is streamed from where it's stored.
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("28283__acclivity__undertreeinrain.mp3"));

		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		batch = new SpriteBatch();

		// Bottom left corner is 0, 0.
		// Our bucket image is 64x64 pixels.
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Good practice to update the camera once per frame.
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();

		// Move the bucket on the x-axis where the user touches/clicks.
		if (Gdx.input.isTouched()) {
			// Vector3 instantiation here is a bad practice. Extra GC work.
			Vector3 touchPosition = new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPosition);
			bucket.x = touchPosition.x - 64 / 2;
		}

		// Move at a consistent 200 units, with no acceleration.
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			bucket.x += 200 * Gdx.graphics.getDeltaTime();
		}
		if (bucket.x < 0) {
			bucket.x = 0;
		}
		if (bucket.x > 800 - 64) {
			bucket.x = 800 - 64;
		}

		// That's 1 second.
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
			spawnRaindrop();
		}

		Iterator<Rectangle> iterator = raindrops.iterator();
		while (iterator.hasNext()) {
			Rectangle raindrop = iterator.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0) {
				iterator.remove();
			}
			if (raindrop.overlaps(bucket)) {
				dropSound.play();
				iterator.remove();
			}
		}
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}
