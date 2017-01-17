package com.omnipotence.game.Battle;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

public class attackSpriteCol {

    private Sprite[] sprites;
    private Texture img;
    private Sprite current;
    Random randomGenerator;

    public attackSpriteCol(){
        sprites = new Sprite[18];
    }

    public void setup(){
        img = new Texture("attacks\\Cam.png");
        sprites[0] = new Sprite(img);

        img = new Texture("attacks\\Cap.png");
        sprites[1] = new Sprite(img);

        img = new Texture("attacks\\Dam.png");
        sprites[2] = new Sprite(img);

        img = new Texture("attacks\\Gap.png");
        sprites[3] = new Sprite(img);

        img = new Texture("attacks\\Ham.png");
        sprites[4] = new Sprite(img);

        img = new Texture("attacks\\Jam.png");
        sprites[5] = new Sprite(img);

        img = new Texture("attacks\\Lam.png");
        sprites[6] = new Sprite(img);

        img = new Texture("attacks\\Lap.png");
        sprites[7] = new Sprite(img);

        img = new Texture("attacks\\Map.png");
        sprites[8] = new Sprite(img);

        img = new Texture("attacks\\Nap.png");
        sprites[9] = new Sprite(img);

        img = new Texture("attacks\\Ram.png");
        sprites[10] = new Sprite(img);

        img = new Texture("attacks\\Rap.png");
        sprites[11] = new Sprite(img);

        img = new Texture("attacks\\Sap.png");
        sprites[12] = new Sprite(img);

        img = new Texture("attacks\\Tam.png");
        sprites[13] = new Sprite(img);

        img = new Texture("attacks\\Tap.png");
        sprites[14] = new Sprite(img);

        img = new Texture("attacks\\Yam.png");
        sprites[15] = new Sprite(img);

        img = new Texture("attacks\\Yap.png");
        sprites[16] = new Sprite(img);

        img = new Texture("attacks\\Zap.png");
        sprites[17] = new Sprite(img);

        for (int i=0; i<18; i++){
            sprites[i].setY(350);
        }

        current = sprites[0];
    }

    public void purge(){
        img.dispose();
    }

    public Sprite pickAttack(){
        randomGenerator = new Random();
        return sprites[randomGenerator.nextInt(18)];
    }
}
