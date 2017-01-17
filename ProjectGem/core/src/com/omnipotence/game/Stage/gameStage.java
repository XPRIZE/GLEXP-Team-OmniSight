package com.omnipotence.game.Stage;

import com.omnipotence.game.level.LevelManager;

import java.util.ArrayList;

/**
 * Created by khuramchaudhry on 12/09/16.
 *
 */

public class gameStage {
    private String stageName;
    public ArrayList<String> levelNames;
    private float buttonHeight, buttonWidth;
    public boolean hasBattleMode;
    public gameStage(String stageName, ArrayList<String> levelNames, float buttonWidth,
                     float buttonHeight, Boolean hasBattleModes) {
        this.stageName = stageName;
        this.levelNames = levelNames;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.hasBattleMode = hasBattleModes;
        if(hasBattleModes) {
            addBattles();
        }
    }

    private void addBattles() {
        int j = levelNames.size();
        while(j > 4) {
            levelNames.add(j, "BATTLE");
            j-=3;
        }
        levelNames.add(j, "BATTLE");
    }

    public boolean isHasBattleMode() {
        return hasBattleMode;
    }

    public String getStageName() {
        return stageName;
    }

    public float getButtonWidth() {
        return buttonWidth;
    }

    public float getButtonHeight() {
        return buttonHeight;
    }

    public ArrayList<String> getLevelNames() {
        return levelNames;
    }

    public int getLevelNameSize() {
        return levelNames.size();
    }

    public String getLevelName(int i) {
        return levelNames.get(i);
    }

    public int getStageScore(int startingPoint, LevelManager main, int max) {
        Double score = 0.0;
        for(int i = startingPoint; i < startingPoint+(levelNames.size()-1); i++) {
            if(main.getLevel(i) != null) {
                score += main.getLevel(i).levelData.getScore();
            } else {
                score += 0.0;
            }
        }
        System.out.println("i: " +startingPoint + " End point: "+
                (startingPoint+(levelNames.size()-1)));
        score /= levelNames.size();
        score *= .1;
        score *= max;
        return score.intValue();
    }
}
