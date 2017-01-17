package com.omnipotence.game.level;

/**
 * Stores data about the level. This class is serialized and deserialized
 * into/out of JSON in the LevelManager.
 * 
 * @author Faizaan Datoo
 */
public class LevelData {

	private boolean unlocked = false;
	private double score = 0;

	public boolean isUnlocked() {
		return unlocked;
	}

	void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

    public void setScore(double s) {
        this.score = s;
    }

    public double getScore() {
        return this.score;
    }

}
