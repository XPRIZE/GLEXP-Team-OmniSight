package com.omnipotence.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Singleton class that stores all game assets, such as textures, maps, and
 * sounds. All assets are loaded in the Main class, before the user sees
 * anything.
 * 
 * @author Faizaan Datoo
 */
public class AssetManager {

	private static AssetManager instance;

	/**
	 * Statically get the instance of the AssetManager.
	 * 
	 * @return The AssetManager instance.
	 */
	public static AssetManager getInstance() {
		if (instance == null) instance = new AssetManager();
		return instance;
	}

	private ObjectMap<String, Object> assetMap; // <Asset Key, Asset Object>

	/**
	 * Internal initialization only (since this is a Singleton)
	 */
	protected AssetManager() {
		assetMap = new ObjectMap<String, Object>();
	}

	/**
	 * Register an asset to the asset map.
	 * 
	 * @param key
	 *            The asset's key.
	 * @param value
	 *            The asset's initialized object.
	 */
	public void registerAsset(String key, Object value) {
		// If the asset is already registered, remove it so it can be replaced.
		if (getAsset(key) != null) assetMap.remove(key);
		assetMap.put(key, value);
	}

	/**
	 * Create a Texture object and register it to a texture to the asset map.
	 * 
	 * @param key
	 *            The asset's key.
	 * @param internalPath
	 *            The path of the asset within the assets folder.
	 */
	public void registerTexture(String key, String internalPath) {
		FileHandle handle = getHandle(internalPath);
		if (handle != null) registerAsset(key, new Texture(handle));
	}

	/**
	 * Create a Sound object and register it to the asset map.
	 * 
	 * @param key
	 *            The asset's key.
	 * @param internalPath
	 *            The path of the asset within the assets folder.
	 */
	public void registerSound(String key, String internalPath) {
		FileHandle handle = getHandle(internalPath);
		if (handle != null)
			registerAsset(key, Gdx.audio.newSound(Gdx.files.internal(internalPath)));
	}

	/**
	 * Create a Music object and register it to the asset map.
	 * 
	 * @param key
	 *            The asset's key.
	 * @param internalPath
	 *            The path of the asset within the assets folder.
	 */
	public void registerMusic(String key, String internalPath) {
		FileHandle handle = getHandle(internalPath);
		if (handle != null) registerAsset(key, Gdx.audio.newMusic(handle));
	}

	/**
	 * Gets an asset from the map.
	 * 
	 * @param key
	 *            The asset's key.
	 * @return The object you requested, or null if it could not be found.
	 */
	public Object getAsset(String key) {
		return assetMap.get(key);
	}

	/**
	 * Gets an asset from the map and casts it to a Texture object.
	 * 
	 * @param key
	 *            The asset's key.
	 * @return The Texture object you requested, or null if it could not be
	 *         found or is not a Texture object.
	 */
	public Texture getTexture(String key) {
		Object asset = assetMap.get(key);

		// Avoid a class cast exception and null pointer exception by checking
		// if it is an instance of Texture and making sure it is not null.
		if (!(asset instanceof Texture) || asset == null) return null;

		return (Texture) asset;
	}

	/**
	 * Gets an asset from the map and casts it to a Sound object.
	 * 
	 * @param key
	 *            The asset's key.
	 * @return The Sound object you requested, or null if it could not be found
	 *         or is not a Sound object.
	 */
	public Sound getSound(String key) {
		Object asset = assetMap.get(key);

		if (!(asset instanceof Sound)) return (Sound) assetMap.get("");

		return (Sound) asset;
	}

    public FileHandle getVideo(String key) {
        Object asset = assetMap.get(key);

        if (!(asset instanceof FileHandle)) return null;

        return (FileHandle) asset;
    }

	/**
	 * Gets an asset from the map and casts it to a Music object.
	 * 
	 * @param key
	 *            The asset's key.
	 * @return The Music object you requested, or null if it could not be
	 *         found or is not a Music object.
	 */
	public Music getMusic(String key) {
		Object asset = assetMap.get(key);

		if (!(asset instanceof Music) || asset == null) return null;
		
		return (Music) asset;
	}

	/**
	 * Convert a Texture to a drawable Texture, which can be added to a UI
	 * button.
	 * 
	 * @param textureAssetKey
	 *            The asset key of the texture to be converted.
	 * @return The converted texture.
	 */
	public TextureRegionDrawable convertTextureToDrawable(String textureAssetKey) {
		return new TextureRegionDrawable(new TextureRegion(
				getTexture(textureAssetKey)));
	}

	/**
	 * Dispose of an asset. This will check if it is an instance of Texture,
	 * Sound, or TiledMap, and it will dispose of it accordingly.
	 * 
	 * @param key
	 *            The asset's key.
	 */
	public void dispose(String key) {
		Object asset = getAsset(key);
		if (asset == null) return;

		// Dispose the asset object
		if (asset instanceof Texture) {
			((Texture) asset).dispose();
		} else if (asset instanceof Sound) {
			((Sound) asset).stop();
			((Sound) asset).dispose();
		} else if (asset instanceof TiledMap) {
			((TiledMap) asset).dispose();
		} else if(asset instanceof Music) {
			((Music) asset).dispose();
		}
	}

	/**
	 * Dispose of all assets.
	 */
	public void disposeAll() {
		for (String key : assetMap.keys()) {
			dispose(key);
		}
	}

	/**
	 * Gets a file from the internal path specified. If the file was not found,
	 * this method will print an error message and return null.
	 * 
	 * @param internalPath
	 *            The internal path (relative to the "assets/" folder) of this
	 *            asset.
	 * @return The FileHandle object if the file was found, or null if the file
	 *         was not found.
	 */
	private FileHandle getHandle(String internalPath) {
		FileHandle handle = Gdx.files.internal(internalPath);
		// Make sure the file exists before adding it.
		if (!handle.exists()) {
			System.err.println("No asset was found at the internal path "
					+ internalPath + ". It will not be registered.");
			return null;
		}
		return handle;
	}

}