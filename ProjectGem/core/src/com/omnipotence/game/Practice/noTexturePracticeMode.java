package com.omnipotence.game.Practice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.omnipotence.game.Main;
import com.omnipotence.game.MyGdxGame;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by khuramchaudhry on 12/2/16.
 *
 */

public class noTexturePracticeMode extends MyGdxGame {

    private ImageButton centralButton;
    private Texture[] textures;
    private Texture[][] choicesTextures;

    public noTexturePracticeMode(Main main, String location, String string, ArrayList<String> list) {
        super(main, location, string, list, 4, 9);
        textures = new Texture[character.length()];
        for(int i = 0; i < textures.length; i++) {
            char c = character.charAt(i);
            textures[i] = (c != ' ') ? pleaseFindMeATexture(c) : null;
        }
        for(ImageButton imageButton: imageButtons) {
            stage.getActors().removeValue(imageButton, true);
        }
        overrideChoicesTextures();
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
    protected void setPositions() {
        this.positions[0] = new float[]{0f, .35f, .1f, .365f};
        this.positions[1] = new float[]{1f, .5f, .9f, .515f};
        this.positions[2] = new float[]{0f, .05f, .1f, .065f};
        this.positions[3] = new float[]{1f, .2f, .9f, .215f};
    }

    @Override
    protected void createExtraAssets() {
        centralButton = new ImageButton(getTexRegionDrawable("speaker.png"));
        centralButton.setPosition(newWidth(.5f, 150f), newHeight(.75f, 150f));
        centralButton.setSize(150f, 150f);
        stage.addActor(centralButton);
        Gdx.input.setInputProcessor(stage);
        centralButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(timesCorrect < 5) {
                    sounds.get(character).play();
                }
                return true;
            }
        });
    }

    @Override
    public void setImageButtonListeners(final int i) {
        imageButtons[i].addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(timesCorrect < 5) {
                    viewDialog(getTexRegionDrawable(Main.language+"/"+locat+"/"+choices[i] + ".png"));
                } else {
                    sounds.get(choices[i]).play();
                }
                return true;
            }
        });
    }

    @Override
    protected void change() {
        randomize();
        if(timesCorrect < 5) {
            overrideChoicesTextures();
        }
        if(timesCorrect == 5) {
            stage.getActors().removeValue(centralButton, true);
            for (ImageButton imageButton : imageButtons) {
                stage.addActor(imageButton);
                imageButton.setStyle(newStyle("speaker.png"));
                imageButton.setSize(100f, 150f);
            }
        }
    }

    @Override
    protected void renderExtraStuff() {
        if(timesCorrect > 4) {
            float startIndex = ((textures.length/2) < 13) ? (6f - ((textures.length/2)/2f)) *
                    .1f : .1f;
            float intervals = .03f;
            for(int i = 0; i < textures.length; i++) {
                if(textures[i] != null) {
                    if(character.contains(".") && i == textures.length-1) {
                        batch.draw(textures[i], newWidth(startIndex + intervals * (i % 24), 30f),
                                newHeight(.735f - (.075f * (i / 24)), 60f), 30f, 60f);
                    } else {
                        batch.draw(textures[i], newWidth(startIndex + intervals * (i % 24), 60f),
                                newHeight(.75f - (.075f * (i / 24)), 60f), 60f, 60f);
                    }
                }
            }
        } else if(timesCorrect < 5) {
            float intervals = .025f;
            for(int i = 0; i < choicesTextures.length; i++) {
                for(int j = 0; j < choicesTextures[i].length; j++) {
                    if(choicesTextures[i][j] != null) {
                        if(choices[i].contains(".") && j == choicesTextures[i].length-1) {
                            float z = (positions[i][2] > .5f) ?
                                    -1f * (intervals * (choicesTextures[i].length - j)) : intervals * j;
                            batch.draw(choicesTextures[i][j], newWidth(positions[i][2] + z, 20f),
                                    newHeight(positions[i][3]-.0075f, 40f), 20f, 40f);
                        } else {
                            float z = (positions[i][2] > .5f) ?
                                    -1f * (intervals * (choicesTextures[i].length - j)) : intervals * j;
                            batch.draw(choicesTextures[i][j], newWidth(positions[i][2] + z, 40f),
                                    newHeight(positions[i][3], 40f), 40f, 40f);
                        }
                    }
                }
            }
        }
    }

}
