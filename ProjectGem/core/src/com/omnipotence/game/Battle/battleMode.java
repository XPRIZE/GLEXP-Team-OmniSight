package com.omnipotence.game.Battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.omnipotence.game.LevelScreen;
import com.omnipotence.game.Main;
import com.omnipotence.game.defaultScreen;
import com.omnipotence.game.util.AssetManager;

import java.util.ArrayList;
import java.util.HashMap;


public class battleMode extends defaultScreen {

	private Stage stage, stage2;
	private BitmapFont font;
	private Skin skin, skin2;
    private SpriteBatch batch;
    private Table mainContainer;
    private Main game;
    private ArrayList<String> arr;
    private String[] choices;
	private Texture mentorHUB, gemcolHUB;
    private Texture[] mentorHEARTS, mentorGEMS, mentorPOINTS, gemcolHEARTS, gemcolGEMS, gemcolPOINTS;
    private ImageButton[] imageButtons;
    private int turn = 0;
    private float buttonSize = 150f;
    private int gemcolatkx=100;
    private int mentoratkx= (int) newWidth(.9f, 0f);
    private attackSpriteCol attacks;
    private Sprite gemcolatksprite;
    private Sprite mentoratksprite;
    private TextureAtlas mentorAtlas;
    private Animation mentoranim;
    private float timePassed = 0;
    private float timePassed2 = 0;
    private TextureAtlas gemcolAtlas;
    private Animation gemcolanim;
    private int mentorhp, gemcolhp, menthearts, gemhearts, mentorgems, gemcolgems, mentorpower,
            gemcolpower;
    private boolean mentoratk;
    private boolean gemcolatk;
    private int secondChance;
    private int gemcolY, mentorY;
    private boolean jumpUP = false, jumpDOWN = false;
    private String locat;
    private HashMap<String, Sound> sounds;
    private ImageButton exitbutton;
    private int battleType; // 0 is default, 1 is math equations, 2 is no Textures,
    // 3 is questions
    private ArrayList<String> answerKey;
    private Texture[][] choicesTextures;
    private float timeSeconds = 0f;
    private int firstN, secondN;
    private char sign;
    public battleMode(Main gameInstance, String location, ArrayList<String> a, int gp, int type) {
        super();
        this.arr = a;
        game = gameInstance;
        secondChance = 0;
        this.locat = location;
        this.battleType = type;
        if(battleType == 2) {
            for(String s: arr) {
                s.replace(".", "");
            }
        }
        choices = new String[a.size()];
        for (int i = 0; i < a.size(); i++) {
            choices[i] = a.get(i);
        }
        if(battleType == 1) {
            for(int i = 0; i < choices.length; i++) {
                String levelName = choices[i];
                int index = levelName.indexOf("-") + levelName.indexOf("+")
                        + levelName.indexOf("*") + 2;
                firstN = Integer.parseInt(levelName.substring(0, index)
                        .replaceAll(" ", ""));
                secondN = Integer.parseInt(levelName.substring(index+1)
                        .replaceAll(" ", ""));
                int answer;
                sign = levelName.charAt(index);
                switch (sign) {
                    case '-': answer = firstN-secondN; break;
                case '*': answer = firstN*secondN; break;
                    default: answer = firstN+secondN; break;
                }
                choices[i] = ""+answer;
                for (String s: arr) {
                    System.out.println("String s:" + s);
                }
            }
        }
        if (battleType == 3) {
            for(int i = 0; i < arr.size(); i++) {
                choices[i] = gameInstance.answerKeyForTheQuestions.get(arr.get(i));
            }
        }
        if(battleType == 2) {
            overrideChoicesTextures();
        }
        sounds = new HashMap<String, Sound>();
        setSounds(a);
        gemcolpower = gp;
        initMainContainer();
        create();
    }

    private void setSounds(ArrayList<String> list) {
        for(int i = 0; i < list.size(); i++) {
            FileHandle handle;
            if(battleType == 3) {
                handle = Gdx.files.internal(Main.language+"/"+locat+"-Sound/question"+
                        list.get(i).substring(0,5)+".mp3");
            } else  {
                handle = Gdx.files.internal(Main.language+"/"+locat+"-Sound/"+list.get(i)+".mp3");
            }

            if (!handle.exists()) {
                sounds.put(list.get(i), Gdx.audio.newSound(Gdx.files.internal("sounds/winner.wav")));
            } else {
                sounds.put(list.get(i), Gdx.audio.newSound(handle));
            }
        }
    }


    //Bad programming but whateva
    public void overrideChoicesTextures() {
        choicesTextures = new Texture[choices.length][];
        for(int i = 0; i < choicesTextures.length; i++) {
            choicesTextures[i] = new Texture[choices[i].length()];
            for(int j = 0; j < choicesTextures[i].length; j++) {
                String s = choices[i].toUpperCase().substring(j,j+1);
                if(s.equals(" ")) {
                    choicesTextures[i][j] = null;
                } else if (s.equals(".")) {
                    choicesTextures[i][j] = new Texture(Gdx.files.internal("period.png"));
                }
                else {
                    choicesTextures[i][j]  = new Texture(pleaseFindMeAPath(s));
                }
            }
        }
    }

    private void initMainContainer() {
        mainContainer = new Table();
        mainContainer.setBackground(AssetManager.getInstance()
                .convertTextureToDrawable("battleBackground"));
        mainContainer.setFillParent(true);
    }

    public void create () {
        //BASIC STUFF
		batch = new SpriteBatch();
        stage = new Stage();
        stage2 = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas("buttons/button.pack");
        skin.addRegions(buttonAtlas);
        font = new BitmapFont();
        font.getData().setScale(5);
        stage.addActor(mainContainer);
        skin2 = new Skin(Gdx.files.internal("skin/uiskin.json"));
        skin.add("largeFont", new BitmapFont(Gdx.files
                .internal("skin/fonts/chalkboard-font-large.fnt")));
        Label.LabelStyle largeLabelStyle = new Label.LabelStyle();
        Label.LabelStyle largeLabelStyle2 = new Label.LabelStyle();
        largeLabelStyle.font = skin.getFont("largeFont");
        largeLabelStyle.fontColor = Color.WHITE;
        skin.add("largeLabel", largeLabelStyle);

        //THE SCREEN SIZE
        buttonSize = 150f;
        float buttonSizeY = 200f;

        //THE HUBS
        mentorHUB = new Texture(Gdx.files.internal("battleMode/HUB-RIGHT.png"));
        gemcolHUB = new Texture(Gdx.files.internal("battleMode/HUB-LEFT.png"));
        mentorHEARTS = new Texture[10];
        mentorGEMS = new Texture[10];
        mentorPOINTS = new Texture[10];
        gemcolHEARTS = new Texture[10];
        gemcolGEMS = new Texture[10];
        gemcolPOINTS = new Texture[10];
        for(int i = 0; i < 10; i++) {
            mentorHEARTS[i] = new Texture(Gdx.files.internal("battleMode/HUB-HEART.png"));
            mentorGEMS[i] = new Texture(Gdx.files.internal("battleMode/HUB-GEM.png"));
            mentorPOINTS[i] = new Texture(Gdx.files.internal("battleMode/" +
                    ((i == 9) ? "HUB-METER-LEFT-EDGE.png" :
                            (i != 0) ? "HUB-METER.png" : "HUB-METER-RIGHT-EDGE.png")));
            gemcolHEARTS[i] = new Texture(Gdx.files.internal("battleMode/HUB-HEART.png"));
            gemcolGEMS[i] = new Texture(Gdx.files.internal("battleMode/HUB-GEM.png"));
            gemcolPOINTS[i] = new Texture(Gdx.files.internal("battleMode/" +
                    ((i == 0) ? "HUB-METER-LEFT-EDGE.png" :
                            (i != 9) ? "HUB-METER.png" : "HUB-METER-RIGHT-EDGE.png")));
        }

        //THE BUTTONS
        ImageButton centralButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(
                new Texture(Gdx.files.internal("speaker.png")))));
        centralButton.setPosition(newWidth(.5f,buttonSize), newHeight(.8f,buttonSize));
        centralButton.setSize(buttonSize,buttonSize);
        stage.addActor(centralButton);
        Gdx.input.setInputProcessor(stage);
        centralButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(battleType == 1) {

                } else {
                    sounds.get(arr.get(turn)).play();
                }
                return true;
            }
        });
        imageButtons = new ImageButton[choices.length];
            float intervals = (choices.length/2);
            float startIndex = 5 - (intervals/1.6f);
            intervals = .075f;
            startIndex *= .1f;
            for (int i = 0; i < choices.length; i++) {
                if(battleType == 1 || battleType == 3) {
                    imageButtons[i] = new ImageButton(newStyle(pleaseFindMeAPath(choices[i])));
                    setImageButton(imageButtons[i], newWidth(startIndex + intervals * i, buttonSize),
                            newHeight(.6f, buttonSizeY), buttonSize, buttonSizeY, i);
                } else if(battleType == 2) {

                    imageButtons[i] = new ImageButton(newStyle("circle_green.png"));
                    setImageButton(imageButtons[i], newWidth(.8f, 75f),
                            newHeight(.66f-(.065f *i), 75f), 75f, 75f, i);
                } else {
                    imageButtons[i] = new ImageButton(newStyle(Main.language + "/" + locat + "/" +
                            choices[i] + ".png"));
                    setImageButton(imageButtons[i], newWidth(startIndex + intervals * i, buttonSize),
                            newHeight(.6f, buttonSizeY), buttonSize, buttonSizeY, i);
                }

            }
        //}
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("ButtonUnpressed");
        buttonStyle.down = skin.getDrawable("ButtonsPressed");
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        TextButton jumpButton = new TextButton("JUMP!", buttonStyle);
        jumpButton.setPosition(newWidth(.05f, 150f), newHeight(.05f,75f));
        jumpButton.setSize(150f,75f);
        stage.addActor(jumpButton);
        Gdx.input.setInputProcessor(stage);
        jumpButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(mentoratk && gemcolpower > 0) {
                    stage2  = new Stage();
                    results();
                    secondChance = 1;
                }
                return true;
            }
        });

        //THE CHARACTERS
        attacks = new attackSpriteCol();
        attacks.setup();
        gemcolatksprite = attacks.pickAttack();
        mentoratksprite = attacks.pickAttack();

        mentorAtlas = new TextureAtlas(Gdx.files.internal("mentor_wand.atlas"));
        mentoranim = new Animation(1/15f, mentorAtlas.getRegions());

        gemcolAtlas = new TextureAtlas(Gdx.files.internal("gemcollector.atlas"));
        gemcolanim = new Animation(1/15f, gemcolAtlas.getRegions());

        gemcolatk = false;
        mentoratk = false;

        gemcolY = 200;
        mentorY = 200;

        mentorhp = 10;
        gemcolhp = 10;

        menthearts = 0;
        gemhearts = 0;
        mentorgems = 0;
        gemcolgems = 0;

        mentorpower = 10;
        exitbutton = new ImageButton(getTexRegionDrawable("Exit.png"));
        exitbutton.setPosition(newWidth(.975f, 150f), newHeight(.975f, 150f));
        exitbutton.setSize(150f, 150f);
        stage.addActor(exitbutton);
        Gdx.input.setInputProcessor(stage);
        exitbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game, false));
            }
        });
	}

    private void setImageButton(final ImageButton imageButton, float x, float y, float w, float h,
                                final int i) {
        imageButton.setPosition(x, y);
        imageButton.setSize(w, h);
        stage.addActor(imageButton);
        Gdx.input.setInputProcessor(stage);
        imageButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                System.out.println("Image Touch: " + i);
                if(turn == i) {
                    change();
                    if(secondChance != 2) {
                        gemcolatk = true;
                    } else {
                        jumpUP = true;
                        gemcolpower--;
                        secondChance = 3;
                    }
                } else {
                    if(secondChance != 2) {
                        mentoratk = true;
                    } else {
                        mentoratkx -= 360;
                        secondChance = 0;
                    }
                }
                return true;
            }
        });
    }

    public TextureRegionDrawable getTexRegionDrawable(String texture) {
        FileHandle handle = Gdx.files.internal(texture);
        if (!handle.exists()) {
            return new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal("A.png"))));
        } else {
            return new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal(texture))));
        }
    }

	private void change() {
        turn = (int) (Math.random() * choices.length);
        ArrayList<String> picked = new ArrayList<String>();
        if(battleType != 3) {
            choices[0] = arr.get(turn);
            picked.add(arr.get(turn));
            arr.remove(arr.get(turn));
            for (int i = 1; i < choices.length; i++) {
                int j = (int) (Math.random() * arr.size());
                choices[i] = arr.get(j);
                picked.add(arr.get(j));
                arr.remove(j);
            }
            arr.addAll(picked);
        } else {
            choices[0] = answerKey.get(turn);
            picked.add(answerKey.get(turn));
            arr.remove(answerKey.get(turn));
            for (int i = 1; i < choices.length; i++) {
                int j = (int) (Math.random() * answerKey.size());
                choices[i] = answerKey.get(j);
                picked.add(answerKey.get(j));
                answerKey.remove(j);
            }
            answerKey.addAll(picked);
        }

        if(battleType == 1) {
            for(int i = 0; i < choices.length; i++) {
                String levelName = choices[i];
                int index = levelName.indexOf("-") + levelName.indexOf("+")
                        + levelName.indexOf("*") + 2;
                int firstNum = Integer.parseInt(levelName.substring(0, index)
                        .replaceAll(" ", ""));
                int secondNum = Integer.parseInt(levelName.substring(index+1)
                        .replaceAll(" ", ""));
                int answer;
                switch (levelName.charAt(index)) {
                    case '-': answer = firstNum-secondNum; break;
                    case '*': answer = firstNum*secondNum; break;
                    default: answer = firstNum+secondNum; break;
                }
                choices[i] = ""+answer;
            }
        }
        if (battleType == 2) {
            overrideChoicesTextures();
        }
        if (battleType == 3) {
            for(int i = 0; i < arr.size(); i++) {
                choices[i] = game.answerKeyForTheQuestions.get(arr.get(i));
            }
        }

        for(int i = 0; i < choices.length; i++) {
            if (battleType == 1) {
                imageButtons[i].setStyle(newStyle(pleaseFindMeAPath(choices[i])));
            } else if(battleType == 2) {
                imageButtons[i] = new ImageButton(newStyle("circle_green.png"));
            } else {
                imageButtons[i].setStyle(newStyle(Main.language + "/" + locat + "/" + choices[i] +
                        ".png"));
            }
        }

	}

    private ImageButton.ImageButtonStyle newStyle(String string) {
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.down = skin.getDrawable("ButtonsPressed");
        buttonStyle.up = skin.getDrawable("ButtonUnpressed");
        Drawable image = getTexRegionDrawable(string);
        image.setMinHeight(buttonSize-32f);
        image.setMinWidth(buttonSize-32f);
        buttonStyle.imageUp = image;
        return buttonStyle;
    }

    private void endGame(boolean failed) {
        float delay = 1f; // seconds
        Timer.schedule(new Timer.Task(){
           @Override
           public void run() {
           }
        }, delay);
        game.setScreen(new LevelScreen(game, !failed));
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act();
        TextureRegion gemcolRegion = gemcolanim.getKeyFrame(timePassed, false);
        if(!gemcolRegion.isFlipX())
            gemcolRegion.flip(true, false);
        TextureRegion mentorRegion = mentoranim.getKeyFrame(timePassed2, false);
        batch.begin();

        //THE HUBS
        batch.draw(mentorHUB, newWidth(.92f, mentorHUB.getWidth()), newHeight(.9f,
                mentorHUB.getHeight()));
        batch.draw(gemcolHUB, newWidth(.08f, gemcolHUB.getWidth()), newHeight(.9f,
                gemcolHUB.getHeight()));
        for(int i = 0; i < menthearts; i++){
            batch.draw(mentorHEARTS[i], newWidth(.8985f-(.024f*(i%5)), mentorHEARTS[i].getWidth()),
                    newHeight(.899f-.028f*(i/5), mentorHEARTS[i].getHeight()));
        }
        for(int i = 0; i < gemhearts; i++){
            batch.draw(gemcolHEARTS[i], newWidth(.1015f+(.024f*(i%5)), gemcolHEARTS[i].getWidth()),
                    newHeight(.899f-.028f*(i/5), gemcolHEARTS[i].getHeight()));
        }
        for(int i = 0; i < mentorgems; i++){
            batch.draw(mentorGEMS[i], newWidth(.935f-(.01375f*i), mentorGEMS[i].getWidth()),
                    newHeight(.83f, mentorGEMS[i].getHeight()));
        }
        for(int i = 0; i < gemcolgems; i++){
            batch.draw(gemcolGEMS[i], newWidth(.065f+(.01375f*i), gemcolGEMS[i].getWidth()),
                    newHeight(.83f, gemcolGEMS[i].getHeight()));
        }
        for(int i = 0; i < mentorpower; i++){
            batch.draw(mentorPOINTS[i], newWidth(.9045f-(.0108f*i), mentorPOINTS[i].getWidth()),
                    newHeight(.787f, mentorPOINTS[i].getHeight()));
        }
        for(int i = 0; i < gemcolpower; i++){
            batch.draw(gemcolPOINTS[i], newWidth(.0955f+(.0108f*i), gemcolPOINTS[i].getWidth()),
                    newHeight(.787f, gemcolPOINTS[i].getHeight()));
        }

        //THE CHARACTERS
        if (gemcolatk) {
            gemcolatksprite.setY(gemcolY);
            gemcolatksprite.draw(batch);
            if (gemcolatkx <  newWidth(.9f, mentorRegion.getRegionWidth())) {
                gemcolatkx = gemcolatkx + 25;
            }
            else {
                mentorhp--;
                menthearts++;
                gemcolgems++;
                gemcolatk = false;
                gemcolatkx = 100;
            }
            gemcolatksprite.setX(gemcolatkx);
            gemcolatksprite.setY(gemcolY);
        }

        if (mentoratk) {
            mentoratksprite.setY(mentorY);
            mentoratksprite.setX(mentoratkx*.85f);
            mentoratksprite.draw(batch);
           if (mentoratkx > 75) {
               if(secondChance == 0) {
                   mentoratkx -= 20;
               } else if(secondChance == 2) {
                   timeSeconds +=Gdx.graphics.getRawDeltaTime();
                   String str = ""+timeSeconds;
                   BitmapFont font2 = skin.getFont("largeFont");
                   font2.setColor(Color.RED);
                   font2.draw(batch, str.substring(0,1)+"s", newWidth(.5f, font.getSpaceWidth()),
                           newHeight(.5f, font.getLineHeight()));
                   if(timeSeconds > 5f) {
                       timeSeconds = 0f;
                       secondChance = 3;
                   }
               } else if(secondChance == 3) {
                   timeSeconds = 0f;
                   mentoratkx -= 360;
                   secondChance = 0;
               }
            } else {
                if(gemcolY < 300) {
                    gemhearts++;
                    gemcolhp--;
                    mentorgems++;
                }
                mentoratk = false;
                mentoratkx = (int) newWidth(.9f, mentorRegion.getRegionWidth());
            }
          //  mentoratksprite.setX(mentoratkx);
          //  mentoratksprite.setY(mentorY);
        }

        timePassed += Gdx.graphics.getDeltaTime();
        timePassed2 += Gdx.graphics.getDeltaTime();
        batch.draw(mentorRegion, newWidth(.9f, mentorRegion.getRegionWidth()), mentorY);
        batch.draw(gemcolRegion, 100, gemcolY);
        if(jumpUP) {
            gemcolY+=20;
            if(gemcolY > 600) {
                jumpDOWN = true;
                jumpUP = false;
            }
        } else if(jumpDOWN) {
            gemcolY-=20;
            if(gemcolY == 200) {
                jumpDOWN = false;
            }
        }

        if (mentorhp <= 0){
            mentorhp = 0;
            menthearts = 10;
            mentorY = mentorY - 30;
            endGame(false);
        }

        if (gemcolhp <= 0){
            gemcolhp = 0;
            gemhearts = 10;
            gemcolY = gemcolY - 30;
            endGame(true);
        }

        if(battleType == 2) {
            float intervals = .025f;
            for(int i = 0; i < choicesTextures.length; i++) {
                for(int j = 0; j < choicesTextures[i].length; j++) {
                    if(choicesTextures[i][j] != null) {
                        float z = -1f * (intervals * (choicesTextures[i].length - j));
                        batch.draw(choicesTextures[i][j], newWidth(.75f + z, 40f),
                                newHeight(.66f-(.065f *i), 40f), 40f, 40f);
                    }
                }
            }
        }

        batch.end();
        stage2.draw();

    }

    @Override
    public void dispose(){
        batch.dispose();
        attacks.purge();
        mentorAtlas.dispose();
        gemcolAtlas.dispose();
    }

    public String pleaseFindMeAPath(String s) {
        //This assumes that the first stage is always the alphabet of the language.
        String path = Main.language+"/"+game.gameStages.get(0).getStageName()+"/"+s +".png";
        String path2 = Main.language+"/"+"Numbers Part 1/"+s +".png";
        String path3 = Main.language+"/"+"Numbers Part 2/"+s +".png";
        String path4 = Main.language+"/"+"Numbers Part 3/"+s +".png";
        String path5 = Main.language+"/"+"Numbers Part 4/"+s +".png";
        String path6 = Main.language+"/"+"Numbers Part 5/"+s +".png";
        if(Gdx.files.internal(path).exists()) {
            return path;
        } else if(Gdx.files.internal(path2).exists()) {
            return path2;
        } else if(Gdx.files.internal(path3).exists()) {
            return path3;
        }
        else if(Gdx.files.internal(path4).exists()) {
            return path4;
        }
        else if(Gdx.files.internal(path5).exists()) {
            return path5;
        }
        else if(Gdx.files.internal(path6).exists()) {
            return path6;
        }
        return "A.png";
    }

    private void results() {
        Gdx.input.setInputProcessor(stage2);
        final Label centerText = new Label("You have\na second\nchance\nto answer\n" +
                "the\nquestion.", skin, "largeLabel");
        centerText.setScale(.5f);
        centerText.setAlignment(Align.center);
        final ImageButton button2 = new ImageButton(getTexRegionDrawable("check.png"));
        button2.getImage().setSize(50f, 50f);
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage2.dispose();
                Gdx.input.setInputProcessor(stage);
                secondChance = 2;
                // stage2 = new Stage();
            }
        });
        Dialog dialog = new Dialog("", skin2, "dialog") {
            {
                setColor(Color.WHITE);
                Drawable drawable = getTexRegionDrawable("textures/secondChance.JPG");
                drawable.setTopHeight(150f);
                drawable.setBottomHeight(150f);
                drawable.setLeftWidth(400f);
                drawable.setRightWidth(400f);
                setBackground(drawable);
                text(centerText).align(Align.center).row();
                row();
                row();
                button(button2).align(Align.bottom).row();
            }
        };

        dialog.show(stage2);
    }

}