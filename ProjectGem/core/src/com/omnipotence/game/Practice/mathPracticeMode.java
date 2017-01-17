package com.omnipotence.game.Practice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.omnipotence.game.Main;
import com.omnipotence.game.MyGdxGame;

import java.util.ArrayList;

/**
 * Created by khuramchaudhry on 12/2/16.
 *
 */

public class mathPracticeMode extends MyGdxGame {

    private Label equation;
    private String e;
    private int firstNum, secondNum, answer;
    private Texture[] answerTexture;
    private float groupHeight;

    public mathPracticeMode(Main main, String location, int ans, ArrayList<String> list,
                            int num1, int num2, String equat) {
        super(main, location, ""+ans, list, 4, 12);
        e = equat + " = ?";
        equation.setText(e);
        equation.setPosition(newWidth(.4f, equation.getWidth()),
                newHeight(.75f, equation.getHeight()));
        firstNum = num1;
        secondNum = num2;
        this.answer = ans;
        character = ""+answer;
        for(int i = 0; i < imageButtons.length; i++) {
            imageButtons[i].setStyle(newStyle(pleaseFindMeAPath(choices[i])));
        }
        if(e.contains("*")) {
            answerTexture = new Texture[secondNum];
            for(int i = 0; i < answerTexture.length; i++) {
                answerTexture[i] = new Texture(Gdx.files.internal("Circles/"+firstNum+"CIRCLE.png"));
            }
            groupHeight = 42f*firstNum;
        } else {
            int numTens = ans / 10;
            if (ans % 10 == 0) {
                numTens--;
            }
            int numOnes = ans - (numTens * 10);
            answerTexture = new Texture[numTens + 1];
            for (int i = 0; i < answerTexture.length - 1; i++) {
                answerTexture[i] = new Texture("Circles/10CIRCLE.png");
            }
            answerTexture[answerTexture.length - 1] = new Texture("Circles/" + numOnes + "CIRCLE.png");
        }
        //viewDialog(getTexRegionDrawable(locat+"/"+equat+".png"));

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
        skin.add("largeFont", new BitmapFont(Gdx.files
                .internal("skin/fonts/chalkboard-font-large.fnt")));
        Label.LabelStyle largeLabelStyle = new Label.LabelStyle();
        largeLabelStyle.font = skin.getFont("largeFont");
        skin.add("largeLabel", largeLabelStyle);
        equation = new Label("", skin, "largeLabel");
        stage.addActor(equation);
    }

    @Override
    public void setImageButtonListeners(final int i) {
        imageButtons[i].addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                viewDialog(getTexRegionDrawable(pleaseFindMeAPath(choices[i])));
                return true;
            }
        });
    }

    public String pleaseFindMeAPath(String s) {
        String path = "English/Numbers Part 1/"+s +".png";
        String path2 = "English/Numbers Part 2/"+s +".png";
        String path3 = "English/Numbers Part 3/"+s +".png";
        String path4 = "English/Numbers Part 4/"+s +".png";
        String path5 = "English/Numbers Part 5/"+s +".png";
        if(Gdx.files.internal(path).exists()) {
            return path;
        } else if(Gdx.files.internal(path2).exists()) {
            return path2;
        } else if(Gdx.files.internal(path3).exists()){
            return path3;
        } else if(Gdx.files.internal(path4).exists()) {
            return path4;
        }  else if(Gdx.files.internal(path5).exists()) {
            return path5;
        }
        return "A.png";
    }


    @Override
    protected void change() {
        if(timesCorrect == 4) {
            e = e.replace("?", "")+ answer;
            e = "?" + e.substring((""+firstNum).length());
            character = ""+firstNum;
            equation.setText(e);
        } else if(timesCorrect == 8) {
            e = e.replace("?", ""+firstNum);
            e = e.substring(0, (""+firstNum).length()+3)+"?"
                    +e.substring((""+firstNum).length()+3+(""+secondNum).length());
            character = ""+secondNum;
            equation.setText(e);
        }
        randomize();
        for(int i = 0; i < textButtons.length; i++) {
          //  textButtons[i].setText(choices[i]);
            imageButtons[i].setStyle(newStyle(pleaseFindMeAPath(choices[i])));
        }
    }

    @Override
    protected void renderExtraStuff() {
        Texture groupie = new Texture("group.png");
        if(e.contains("*")) {
            float intervals = (answerTexture.length / 2);
            float startIndex = 4.75f - (intervals / 2f);
            intervals = .06f;
            startIndex *= .1f;
            for (int i = 0; i < answerTexture.length; i++) {
                batch.draw(groupie, newWidth(startIndex + intervals * i, 90f),
                        newHeight(.075f, groupHeight + 75f), 90f, groupHeight + 75f);
                batch.draw(answerTexture[i], newWidth(startIndex + intervals * i, 130f),
                        newHeight(.1f, groupHeight), 130f, groupHeight);
            }
        } else {
            float intervals = (answerTexture.length/2);
            float startIndex = 5.5f - (intervals/1.6f);
            intervals = .0625f;
            startIndex *= .1f;
            for(int i = 0; i < answerTexture.length; i++) {
                batch.draw(groupie, newWidth(startIndex + intervals * i, 90f),
                        newHeight(.0725f, 510f), 90f,  510f);
                batch.draw(answerTexture[i], newWidth(startIndex + intervals*i, 130f),
                        newHeight(.1f, 420f), 130f, (i < answerTexture.length-1) ? 420f : 42f *
                                (answer - (10*(answerTexture.length-1))));
            }
        }
    }

}
