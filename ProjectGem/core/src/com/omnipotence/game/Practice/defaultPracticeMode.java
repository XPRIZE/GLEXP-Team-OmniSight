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

public class defaultPracticeMode extends MyGdxGame {

    private ImageButton centralButton;
    private Texture[] textures;

    public defaultPracticeMode(Main main, String location, String string, ArrayList<String> list) {
        super(main, location, string, list, 4, 9);
        if(string.matches(".*\\d+.*")) {
            int num = Integer.parseInt(string);
            int numTens = num/10;
            if(num%10 == 0){
                numTens--;
            }
            int numOnes = num - (numTens*10);
            textures = new Texture[numTens+1];
            for(int i = 0; i < textures.length-1; i++){
                textures[i] = new Texture("Circles/10CIRCLE.png");
            }
            textures[textures.length-1] = new Texture("Circles/"+numOnes+"CIRCLE.png");
        }
        viewDialog(getTexRegionDrawable(Main.language+"/"+locat + "/" + character + ".png"));
        playSound(character);
    }

    @Override
    protected void setPositions() {
        this.positions[0] = new float[]{0f, .5f, .1f, .5f};
        this.positions[1] = new float[]{1f, .5f, .9f, .5f};
        this.positions[2] = new float[]{0f, .2f, .1f, .2f};
        this.positions[3] = new float[]{1f, .2f, .9f, .2f};
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
                    playSound(character);
                } else {
                    viewDialog(getTexRegionDrawable(Main.language+"/"+locat+"/"+character + ".png"));
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
                    playSound(choices[i]);
                }
                return true;
            }
        });
    }

    @Override
    protected void change() {
        randomize();
        for(int i = 0; i < textButtons.length; i++) {
            // textButtons[i].setText(choices[i]);
        }
        if(timesCorrect < 5) {
            for(int i = 0; i < imageButtons.length; i++) {
                imageButtons[i].setStyle(newStyle(Main.language+"/"+locat+"/"+choices[i] + ".png"));
            }
        }
        if(timesCorrect == 5) {
            centralButton.setStyle(newStyle(Main.language+"/"+locat + "/" + character + ".png"));
            for (ImageButton imageButton : imageButtons) {
                imageButton.setStyle(newStyle("speaker.png"));
                imageButton.setSize(100f, 150f);
            }
        }
    }

    @Override
    protected void renderExtraStuff() {
        if(character.matches(".*\\d+.*")) {
            float intervals = (textures.length/2);
            float startIndex = 5.5f - (intervals/1.6f);
            intervals = .0625f;
            startIndex *= .1f;
            int num = Integer.parseInt(character);
            int numTens = num/10;
            int numOnes = num - (numTens*10);
            for(int i = 0; i < textures.length; i++) {
                batch.draw(textures[i], newWidth(startIndex + intervals*i, 100f),
                        newHeight(.4f, 250f), 100f, (i < textures.length-1) ? 250f : 25f*numOnes);
            }
        }
    }

}
