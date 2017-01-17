package com.omnipotence.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

/**
 * Stores map and player data for the level.
 * 
 * @author Faizaan Datoo
 */
public class Level {

	public String levelAssetKey; // Assigned to this level in AssetManager

	public LevelData levelData; // Data about the level
	private Json json; // JSON object for file writing.

	/**
	 * This constructor is to be used only in the LevelManager class. GameScreen
	 * should use the prepare method to pass in a camera and a player object.
	 */
	Level(String levelAssetKey) {
		this.levelAssetKey = levelAssetKey;

		this.levelData = new LevelData();
		this.json = new Json(OutputType.minimal);
		loadData();
	}

	/**
	 * Load data about this map
	 */
	private void loadData() {
		FileHandle levelHandle = Gdx.files.local("levels/" + levelAssetKey + ".json");
		if (!levelHandle.exists()) save(); // Create the LevelData file.

		this.levelData = json.fromJson(LevelData.class, levelHandle);
	}

	/**
	 * Save the data about this map
	 */
	void save() {
		FileHandle levelHandle = Gdx.files.local("levels/" + levelAssetKey + ".json");
		levelHandle.writeString(json.toJson(levelData), false);
	}

}
