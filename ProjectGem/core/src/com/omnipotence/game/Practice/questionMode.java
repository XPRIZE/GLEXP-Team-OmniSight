package com.omnipotence.game.Practice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.omnipotence.game.Main;
import com.omnipotence.game.MyGdxGame;

import java.util.ArrayList;

/**
 * Created by khuramchaudhry on 12/09/16.
 *
 */

public class questionMode extends MyGdxGame {

    private Texture[] textures;
    private Texture[][] choicesTextures;
    private String sentence;

    public questionMode(Main main, String location, String sentence, String answer,
                        ArrayList<String> list) {
        super(main, location, answer, list, 4, 4);
        for(ImageButton imageButton: imageButtons) {
            stage.getActors().removeValue(imageButton, true);
        }
        this.sentence = sentence;
        textures = new Texture[sentence.length()];
        for(int i = 0; i < textures.length; i++) {
            char c = sentence.charAt(i);
            textures[i] = (c != ' ') ? pleaseFindMeATexture(c) : null;
        }
        overrideChoicesTextures();
    }

    @Override
    protected void setPositions() {
        buttonXSize = 120f;
        buttonYSize = 120f;
        this.positions[0] = new float[]{.075f, .25f, .15f, .275f};
        this.positions[1] = new float[]{.925f, .25f, .85f, .275f};
        this.positions[2] = new float[]{.075f, .1f, .15f, .125f};
        this.positions[3] = new float[]{.925f, .1f, .85f, .125f};
    }

    public void overrideChoicesTextures() {
        choicesTextures = new Texture[choices.length][];
        for(int i = 0; i < choicesTextures.length; i++) {
            choicesTextures[i] = new Texture[choices[i].length()];
            for (int j = 0; j < choicesTextures[i].length; j++) {
                char c = choices[i].charAt(j);
                choicesTextures[i][j] = (c != ' ') ? pleaseFindMeATexture(c) : null;
            }
        }
    }

    @Override
    protected void createExtraAssets() {
    }

    @Override
    public void setImageButtonListeners(int i) {

    }

    @Override
    protected void change() {
        randomize();
        overrideChoicesTextures();
    }

    @Override
    protected void renderExtraStuff() {
        float startIndex = .15f;
        float intervals = .03125f;
        for(int i = 0; i < textures.length; i++) {
            if(textures[i] != null) {
                if(sentence.charAt(i) == '.') {
                    batch.draw(textures[i], newWidth(startIndex + (intervals-.00050f) * (i%24), 20f),
                            newHeight(.9f - (.077f * (i/24)), 40f), 20f, 40f);
                } else {
                    batch.draw(textures[i], newWidth(startIndex + intervals * (i % 24), 40f),
                            newHeight(.9f - (.075f * (i / 24)), 40f), 40f, 40f);
                }
            }
        }
        float intervals2 = .032f;
        for(int i = 0; i < choicesTextures.length; i++) {
            for(int j = 0; j < choicesTextures[i].length; j++) {
                float z = (positions[i][2] > .5f) ?
                        -1f*(intervals2*(choicesTextures[i].length-j)) : intervals2*j;
                batch.draw(choicesTextures[i][j], newWidth(positions[i][2] + z, 60f),
                        newHeight(positions[i][3], 60f), 60f, 60f);
            }
        }
    }
}
