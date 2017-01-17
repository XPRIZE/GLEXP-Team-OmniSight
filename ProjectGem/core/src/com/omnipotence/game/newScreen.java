package com.omnipotence.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.omnipotence.game.Practice.defaultPracticeMode;
import com.omnipotence.game.util.AssetManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by khuramchaudhry on 12/2/16.
 *
 */

public class newScreen extends defaultScreen {

    private ImageButton centralButton, nextButton;
    public SpriteBatch batch;
    private Stage stage, stage2;
    private Skin skin, skin2;
    public BitmapFont font;
    private TextButton[] textButtons;
    public ImageButton[] imageButtons;
    private Table mainContainer;
    public TextButton.TextButtonStyle buttonStyle;
    private ArrayList<String> arr;
    public String[] choices;
    private int timesCorrect, timesIncorrect, numGems = 0, scene = 0, limit = 0;
    public Main main;
    private String locat, character;
    public double numWrong = 0;
    public float[][] positions, buttonPosition;
    public HashMap<String, Sound> sounds;
    private ImageButton exitbutton;
    private float buttonXSize = 150f, buttonYSize = 150f, elapsedTime = 0f;
    private Texture correctTexture, incorrectTexture, gemsTexture;
    private Label correctScore, incorrectScore, gemScore;
    TextureAtlas textureAtlas;
    Animation animationa;

    public newScreen(Main main) {
        this.main = main;

        this.locat = main.gameStages.get(0).getStageName();
        this.character =  main.gameStages.get(0).getLevelName(0);
        this.arr = new ArrayList<String>();
        for (int i = 0; i < main.gameStages.get(0).getLevelNameSize(); i++) {
            String s = main.gameStages.get(0).getLevelName(i);
            if (!s.equals("BATTLE")) {
                arr.add(s);
            }
        }
        this.limit = 9;
        this.choices = new String[4];
        this.positions = new float[4][4];
        textureAtlas = new TextureAtlas(Gdx.files.internal("FingerPress.pack"));
        animationa = new Animation(1/15f, textureAtlas.getRegions());
        setPositions();
        this.arr.remove(character);
        randomize();
        this.sounds = new HashMap<String, Sound>();
        this.sounds.put(character, getSound(character));
        setSounds(arr);
        initMainContainer();
        create();
        createExtraAssets();
    }

    private void setPositions() {
        this.positions[0] = new float[]{0f, .5f, .1f, .5f};
        this.positions[1] = new float[]{1f, .5f, .9f, .5f};
        this.positions[2] = new float[]{0f, .2f, .1f, .2f};
        this.positions[3] = new float[]{1f, .2f, .9f, .2f};
        this.buttonPosition = new float[][]{
                new float[]{newWidth(.45f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.70f, textureAtlas.getRegions().get(0).getRegionHeight())},
          /*CLick*/      new float[]{newWidth(.9f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.8f, textureAtlas.getRegions().get(0).getRegionHeight())},
                new float[]{newWidth(-.11f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.38f, textureAtlas.getRegions().get(0).getRegionHeight())},
          /*CLick*/       new float[]{newWidth(.9f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.8f, textureAtlas.getRegions().get(0).getRegionHeight())},
                new float[]{newWidth(-.11f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(-.08f, textureAtlas.getRegions().get(0).getRegionHeight())},
          /*CLick*/       new float[]{newWidth(.9f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.8f, textureAtlas.getRegions().get(0).getRegionHeight())},
                new float[]{newWidth(-.11f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.38f, textureAtlas.getRegions().get(0).getRegionHeight())},
          /*CLick*/       new float[]{newWidth(.9f, textureAtlas.getRegions().get(0).getRegionWidth()),
                newHeight(.8f, textureAtlas.getRegions().get(0).getRegionHeight())},
                new float[]{newWidth(.01f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.38f, textureAtlas.getRegions().get(0).getRegionHeight())},
                new float[]{newWidth(-.11f, textureAtlas.getRegions().get(0).getRegionWidth()),
                        newHeight(.38f, textureAtlas.getRegions().get(0).getRegionHeight())},
          /*CLick*/       new float[]{newWidth(.9f, textureAtlas.getRegions().get(0).getRegionWidth()),
                newHeight(.8f, textureAtlas.getRegions().get(0).getRegionHeight())}
        };
    }

    private void createExtraAssets() {
        centralButton = new ImageButton(getTexRegionDrawable("speaker.png"));
        centralButton.setPosition(newWidth(.5f, 150f), newHeight(.75f, 150f));
        centralButton.setSize(150f, 150f);
        stage.addActor(centralButton);
        Gdx.input.setInputProcessor(stage);
        centralButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(scene == 0) {
                    sounds.get(character).play();
                    scene += 1;
                }
                return true;
            }
        });
        nextButton = new ImageButton(getTexRegionDrawable("Button_up.png"));
        nextButton.setPosition(newWidth(.9f, 250f), newHeight(.8f, 250f));
        nextButton.setSize(250f, 250f);
        stage.addActor(nextButton);
        Gdx.input.setInputProcessor(stage);
        nextButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(scene%2 == 1|| scene == 10) {
                    switch (scene) {
                        case 3: correctScore.setText("0/"+(limit+1)); break;
                        case 5: incorrectScore.setText("0/"+(limit+1));
                            correctScore.setText(((limit)+"/"+(limit+1))); break;
                        case 7:  correctScore.setText(("0/"+(limit+1)));
                            gemScore.setText(("0/1")); change(); break;
                        case 10: correctScore.setText("0/"+(limit+1)); startMainGame(); break;
                        default: break;
                    }
                    if(scene != 10) {
                        scene++;
                    }
                }

                return true;
            }
        });
    }

    private void setImageButtonListeners(final int i) {
        imageButtons[i].addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(i == 0 && scene == 8) {
                    sounds.get(character).play();
                    scene++;
                }
                return true;
            }
        });
    }

    protected void change() {
        centralButton.setStyle(newStyle(Main.language+"/"+locat + "/" + character + ".png"));
        for (ImageButton imageButton : imageButtons) {
            imageButton.setStyle(newStyle("speaker.png"));
            imageButton.setSize(100f, 150f);

        }
    }

    public void setSounds(ArrayList<String> list) {
        for(int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            sounds.put(s,getSound(s));
        }
    }

    private Sound getSound(String s) {
        FileHandle handle = Gdx.files.internal(Main.language+"/"+locat+"-Sound/"+s+".mp3");
        if (!handle.exists()) {
            return Gdx.audio.newSound(Gdx.files.internal("sounds/winner.wav"));
        }
        return Gdx.audio.newSound(handle);
    }

    private void initMainContainer() {
        mainContainer = new Table();
        mainContainer.setBackground(AssetManager.getInstance()
                .convertTextureToDrawable("practiceBackground"));
        mainContainer.setFillParent(true);
    }

    public void create () {
        batch = new SpriteBatch();

        //Stuff -_-
        skin2 = new Skin(Gdx.files.internal("skin/uiskin.json"));
        stage = new Stage();
        stage2 = new Stage();
        skin = new Skin();
        font = new BitmapFont();
        TextureAtlas buttonAtlas = new TextureAtlas("buttons/button.pack");
        skin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("ButtonUnpressed");
        buttonStyle.down = skin.getDrawable("ButtonsPressed");
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        skin.add("largeFont", new BitmapFont(Gdx.files
                .internal("skin/fonts/chalkboard-font-large.fnt")));
        Label.LabelStyle largeLabelStyle = new Label.LabelStyle();
        largeLabelStyle.font = skin.getFont("largeFont");
        largeLabelStyle.fontColor = Color.BLACK;
        skin.add("largeLabel", largeLabelStyle);
        stage.addActor(mainContainer);

        //Side UI Labels
        correctScore = new Label(timesCorrect+"/"+(limit+1), skin, "largeLabel");
        correctScore.setPosition(newWidth(.06f, correctScore.getWidth()),
                newHeight(.92f, correctScore.getHeight()));
        incorrectScore = new Label(timesIncorrect+"", skin, "largeLabel");
        incorrectScore.setPosition(newWidth(.06f, incorrectScore.getWidth()),
                newHeight(.82f, incorrectScore.getHeight()));
        gemScore = new Label(numGems+"/1", skin, "largeLabel");
        gemScore.setPosition(newWidth(.06f, gemScore.getWidth()),
                newHeight(.72f, gemScore.getHeight()));
        stage.addActor(correctScore);
        stage.addActor(incorrectScore);
        stage.addActor(gemScore);

        //Side UI Textures
        correctTexture = new Texture(Gdx.files.internal("Faces/happyFace.png"));
        incorrectTexture = new Texture(Gdx.files.internal("Faces/sadFace.png"));
        gemsTexture = new Texture(Gdx.files.internal("Chest_Gem.png"));

        textButtons = new TextButton[choices.length];
        imageButtons = new ImageButton[choices.length];
        for(int i = 0; i < textButtons.length; i++) {
            setTextButtons(i);
            setImageButton(i);
        }
        exitbutton = new ImageButton(getTexRegionDrawable("Exit.png"));
        exitbutton.setPosition(newWidth(.95f, 150f), newHeight(.95f, 150f));
        exitbutton.setSize(150f, 150f);
        stage.addActor(exitbutton);
        Gdx.input.setInputProcessor(stage);
        exitbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new LevelScreen(main, false));
            }
        });

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);//135/255f, 206/255f, 235/255f, 1
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsedTime += Gdx.graphics.getDeltaTime();
        stage.draw();//bottom//draws the texture
        stage.act();
        batch.begin();
        batch.draw(animationa.getKeyFrame(elapsedTime,true),buttonPosition[scene][0],
                buttonPosition[scene][1]);
        batch.draw(correctTexture, newWidth(.018f, 60f), newHeight(.9f, 100f), 60f, 100f);
        batch.draw(incorrectTexture, newWidth(.018f, 60f), newHeight(.8f, 100f), 60f, 100f);
        batch.draw(gemsTexture, newWidth(0f, 120f), newHeight(.7f, 100f), 120f, 100f);
        batch.end();
        stage2.draw();
    }

    @Override
    public void dispose() {
        //Make sure to dispose of it
        batch.dispose();
        stage.dispose();
        stage2.dispose();
        skin.dispose();
        skin2.dispose();
        for(String s : sounds.keySet()) {
            sounds.get(s).dispose();
        }
    }

    private void setImageButton(final int i) {
        imageButtons[i] = new ImageButton(getTexRegionDrawable(Main.language + "/" + locat + "/"
                + choices[i] + ".png"));
        imageButtons[i].setPosition(newWidth(positions[i][2], buttonXSize),
                newHeight(positions[i][3], buttonYSize));
        imageButtons[i].setSize(buttonXSize, buttonYSize);
        stage.addActor(imageButtons[i]);
        Gdx.input.setInputProcessor(stage);
        setImageButtonListeners(i);
    }

    private void setTextButtons(final int i) {
        textButtons[i] = new TextButton("", buttonStyle);
        textButtons[i].setPosition(newWidth(positions[i][0], buttonXSize),
                newHeight(positions[i][1], buttonYSize));
        textButtons[i].setSize(buttonXSize, buttonYSize);
        textButtons[i].getLabel().setFontScale(3, 3);
        textButtons[i].getLabel().setAlignment(Align.center);
        stage.addActor(textButtons[i]);
        Gdx.input.setInputProcessor(stage);
        textButtons[i].addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(scene == 2) {
                    correctScore.setText("1/"+(limit+1));
                    scene++;
                } else if(scene == 4 && i == 2) {
                    incorrectScore.setText("1/"+(limit+1));
                    scene++;
                } else if(scene == 6) {
                    correctScore.setText((limit+1)+"/"+(limit+1));
                    gemScore.setText("1/1");
                    scene++;
                } else if(scene == 9) {
                    correctScore.setText("1/"+(limit+1));
                    scene++;
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

    private ImageButton.ImageButtonStyle newStyle(String string) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = getTexRegionDrawable(string);
        return style;
    }

    public void startMainGame() {
        final Drawable drawable = getTexRegionDrawable("Faces/happyFace.png");
        new Dialog("", skin2, "dialog") {
            {
                pad(50);
                setColor(Color.WHITE);
                setBackground(drawable);
            }
        }.show(stage2);
        float delay = .75f; // seconds
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                // Do your work
                main.setScreen(new defaultPracticeMode(main, locat, character, arr));
            }
        }, delay);
    }

    private void randomize() {
        int rightChoice = 0;
        ArrayList<String> picked = new ArrayList<String>();
        choices[rightChoice] = character;
        for(int i = 1; i < choices.length; i++) {
            int j = (int) (Math.random() * arr.size()) ;
            choices[i] = arr.get(j);
            picked.add(arr.get(j));
            arr.remove(j);
        }
        arr.addAll(picked);
    }

    private void choiceMade(String choice) {

    }

}
