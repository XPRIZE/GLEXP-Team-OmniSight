package com.omnipotence.game.Practice;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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

public class rimeFamilyMode extends MyGdxGame {

    private ImageButton centralButton;
    private String[] characters;
    private Music mp3Sound2;
    private Texture[] textures;

    public rimeFamilyMode(Main main, String location, String string, ArrayList<String> list) {
        super(main, location, string, list, 4, 5);
    }

    @Override
    protected void setPositions() {
        Object objects[] = getSub(character);
        System.out.println(objects.length);
        characters = new String[objects.length];
        for(int i = 0; i < objects.length; i++) {
            characters[i] = objects[i].toString();
        }
        limit = (characters.length*2)-1;
        this.positions[0] = new float[]{0f, .5f, .1f, .5f};
        this.positions[1] = new float[]{1f, .5f, .9f, .5f};
        this.positions[2] = new float[]{0f, .2f, .1f, .2f};
        this.positions[3] = new float[]{1f, .2f, .9f, .2f};
    }

    private Object[] getSub(String s) {
        ArrayList<String> arrayList = new ArrayList<String>();
        FileHandle dirHandle;
        if (Gdx.app.getType() == Application.ApplicationType.Android ||
                Gdx.app.getType() == Application.ApplicationType.iOS ) {
            dirHandle = Gdx.files.internal(Main.language+"/"+locat+"/"+s);
        } else {
            // ApplicationType.Desktop ..
            dirHandle = Gdx.files.internal("./bin/"+Main.language+"/"+locat+"/"+s);
        }
        if(dirHandle != null) {
            for (FileHandle entry: dirHandle.list()) {
              //  System.out.println(entry.toString());
                arrayList.add(entry.name());
            }
        }
        return arrayList.toArray();
    }

    @Override
    protected void createExtraAssets() {
        centralButton = new ImageButton(getTexRegionDrawable("speaker.png"));
        centralButton.setPosition(newWidth(.5f, 150f), newHeight(.75f, 150f));
        centralButton.setSize(150f, 150f);
        stage.addActor(centralButton);
        speakerSpeech();
        changeBottomWord();
        Gdx.input.setInputProcessor(stage);
        for (ImageButton imageButton : imageButtons) {
            imageButton.setStyle(newStyle("speaker.png"));
            imageButton.setSize(100f, 150f);
        }
        sounds.clear();
        ArrayList<String> list = new ArrayList<String>();
        for(String s : arr) {
            for(Object s2 : getSub(s)) {
                System.out.println(s+"/"+s2.toString().replace(".png", ""));
                list.add(s+"/"+s2.toString().replace(".png", ""));
            }
        }
        for(Object s2 : getSub(character)) {
            System.out.println(character+"/"+s2.toString().replace(".png", ""));
            list.add(character+"/"+s2.toString().replace(".png", ""));
        }
        setSounds(list);
    }

    private void speakerSpeech() {
        final Music mp3Sound = getMusic("sounds/speech4.mp3");
        mp3Sound.setLooping(false);
        mp3Sound2 = getMusic(Main.language+"/"+ locat+"-Sound/"+character+"/"+
                characters[timesCorrect/2].replace(".png", "")+".mp3");
        mp3Sound2.setLooping(false);
        mp3Sound.setOnCompletionListener(new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                mp3Sound2.play();
            }
        });
        Gdx.input.setInputProcessor(stage);
        centralButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                mp3Sound.play();
                return true;
            }
        });
    }

    private Music getMusic(String s) {
        FileHandle fileHandle = Gdx.files.internal(s);
        if(fileHandle.exists()) {
            return Gdx.audio.newMusic(Gdx.files.internal(s));
        }
        return Gdx.audio.newMusic(Gdx.files.internal("sounds/error.wav"));
    }

    private void changeBottomWord() {
        textures = new Texture[characters[timesCorrect/2].replace(".png", "").length()];
        for(int i = 0; i < textures.length; i++) {
            char c = characters[timesCorrect / 2].replace(".png", "").charAt(i);
            textures[i] = (c != ' ') ? pleaseFindMeATexture(c) : null;
        }
    }

    @Override
    protected void renderExtraStuff() {
        float startIndex = ((textures.length/2) < 13) ? (5f - ((textures.length/2)/2f)) *
                .1f : .1f;
        float intervals = .03f;
        for(int i = 0; i < textures.length; i++) {
            if(textures[i] != null) {
                if(character.contains(".") && i == textures.length-1) {
                    batch.draw(textures[i], newWidth(startIndex + intervals * (i % 24), 30f),
                            newHeight(.6f - (.075f * (i / 24)), 60f), 30f, 60f);
                } else {
                    batch.draw(textures[i], newWidth(startIndex + intervals * (i % 24), 60f),
                            newHeight(.6f - (.075f * (i / 24)), 60f), 60f, 60f);
                }
            }
        }
    }

    @Override
    public void setImageButtonListeners(final int i) {
        imageButtons[i].addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(choices[i].equals(character)) {
                    int soundIndex = (int) (Math.random()*characters.length);
                    System.out.println("Answer: "+
                            choices[i]+"/"+characters[soundIndex].replace(".png", ""));
                    sounds.get(choices[i]+"/"+characters[soundIndex].replace(".png", "")).play();
                } else {
                    String s = (getSub(choices[i])[0]+"").replace(".png", "");
                    System.out.println("Choice: "+choices[i]+"/"+s);
                    if(getSub(choices[i])[0] != null) {
                        sounds.get(choices[i]+"/"+s).play();
                    }
                }
                //}
                return true;
            }
        });
    }

    @Override
    protected void change() {
        if(timesCorrect%2 == 0) {
            mp3Sound2 =  Gdx.audio.newMusic(Gdx.files.internal(Main.language+"/"+
                    locat+"-Sound/"+character+"/"+characters[timesCorrect/2].replace(".png", "")+".mp3"));
            arr.add(characters[(timesCorrect/2)-1]);
            arr.remove(characters[timesCorrect/2]);
            randomize();
            changeBottomWord();
        } else {
            randomize();
        }
    }

}
