package com.omnipotence.game.Practice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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

public class toolsMode extends MyGdxGame {

    private ImageButton centralButton, bottomButton;

    public toolsMode(Main main, String location, String string, ArrayList<String> list) {
        super(main, location, string, list, 4, 9);
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
        final Sound mp3Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/speech.mp3"));
        Gdx.input.setInputProcessor(stage);
        centralButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                mp3Sound.play();
                return true;
            }
        });
        bottomButton = new ImageButton(getTexRegionDrawable(Main.language+"/"+locat+"/"+character
                + ".png"));
        bottomButton.setPosition(newWidth(.5f, 200f), newHeight(.6f, 100f));
        bottomButton.setSize(200f, 100f);
        stage.addActor(bottomButton);
        Gdx.input.setInputProcessor(stage);
        for (ImageButton imageButton : imageButtons) {
            imageButton.setStyle(newStyle("speaker.png"));
            imageButton.setSize(100f, 150f);
        }
    }

    @Override
    protected void renderExtraStuff() {

    }

    @Override
    public void setImageButtonListeners(final int i) {
        imageButtons[i].addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                sounds.get(choices[i]).play();
                return true;
            }
        });
    }

    @Override
    protected void change() {
        randomize();
    }

}
