package com.jamesrskemp.libgdx.canyonbunny.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.jamesrskemp.libgdx.canyonbunny.util.Constants;

/**
 * Created by James on 3/11/2015.
 */
public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;

	public AssetBunny bunny;
	public AssetRock rock;
	public AssetGoldCoin goldCoin;
	public AssetFeather feather;
	public AssetLevelDecoration levelDecoration;

	// Singleton to prevent instantiation from other classes.
	private Assets() {}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		// Set error handler.
		assetManager.setErrorListener(this);
		// Load texture atlas.
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		// Wait until all assets are finished.
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);

		for (String a : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + a);
		}

		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);

		// Enable texture filtering for pixel smoothing.
		for (Texture t : atlas.getTextures()) {
			t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}

		// Create game resource objects.
		bunny = new AssetBunny(atlas);
		rock = new AssetRock(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		feather = new AssetFeather(atlas);
		levelDecoration = new AssetLevelDecoration(atlas);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
	}

	public class AssetBunny {
		public final TextureAtlas.AtlasRegion head;

		public AssetBunny(TextureAtlas atlas) {
			head = atlas.findRegion("bunny_head");
		}
	}

	public class AssetRock {
		public final TextureAtlas.AtlasRegion edge;
		public final TextureAtlas.AtlasRegion middle;

		public AssetRock(TextureAtlas atlas) {
			edge = atlas.findRegion("rock_edge");
			middle = atlas.findRegion("rock_middle");
		}
	}

	public class AssetGoldCoin {
		public final TextureAtlas.AtlasRegion goldCoin;

		public AssetGoldCoin(TextureAtlas atlas) {
			goldCoin = atlas.findRegion("item_gold_coin");
		}
	}

	public class AssetFeather {
		public final TextureAtlas.AtlasRegion feather;

		public AssetFeather(TextureAtlas atlas) {
			feather = atlas.findRegion("item_feather");
		}
	}

	public class AssetLevelDecoration {
		public final TextureAtlas.AtlasRegion cloud01;
		public final TextureAtlas.AtlasRegion cloud02;
		public final TextureAtlas.AtlasRegion cloud03;
		public final TextureAtlas.AtlasRegion mountainLeft;
		public final TextureAtlas.AtlasRegion mountainRight;
		public final TextureAtlas.AtlasRegion waterOverlay;

		public AssetLevelDecoration(TextureAtlas atlas) {
			cloud01 = atlas.findRegion("cloud01");
			cloud02 = atlas.findRegion("cloud02");
			cloud03 = atlas.findRegion("cloud03");
			mountainLeft = atlas.findRegion("mountain_left");
			mountainRight = atlas.findRegion("mountain_right");
			waterOverlay = atlas.findRegion("water_overlay");
		}
	}
}
