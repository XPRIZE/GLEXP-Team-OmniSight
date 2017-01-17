package com.omnipotence.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.omnipotence.game.util.AssetManager;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MyGdxGame extends defaultScreen {

    public SpriteBatch batch;
    public Stage stage, stage2;
    public Skin skin, skin2;
    public BitmapFont font;
    public TextButton[] textButtons;
    public ImageButton[] imageButtons;
    public Table mainContainer;
    public TextButton.TextButtonStyle buttonStyle;
    public ArrayList<String> arr;
    public String[] choices;
    public int timesCorrect, timesIncorrect, numGems = 0;
    public Main main;
    public String locat, character;
    public double numWrong = 0;
    public float[][] positions;
    public int limit = 0;
    public HashMap<String, Sound> sounds;
    private ImageButton exitbutton;
    public float buttonXSize = 150f;
    public float buttonYSize = 150f;
    private Texture correctTexture, incorrectTexture, gemsTexture;
    private Label correctScore, incorrectScore, gemScore;

    public MyGdxGame(Main gameInstance, String location, String c, ArrayList<String> list,
                     int size, int lim) {
        super();
        this.main = gameInstance;
        this.locat = location;
        this.character = c;
        this.arr = list;
        this.limit = lim;
        this.choices = new String[size];
        this.positions = new float[size][4];
        setPositions();
        this.arr.remove(character);
        randomize();
        this.sounds = new HashMap<String, Sound>();
        this.sounds.put(character, getSound(character));
        setSounds(arr);
        initMainContainer();
        create();
        createExtraAssets();
       // this.sounds.get(character).play();
    }

    protected abstract void setPositions();

    public void setSounds(ArrayList<String> list) {
        for(int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            sounds.put(s,getSound(s));
        }
    }

    private Sound getSound(String s) {
        s = s.replace(".", "");
        System.out.println(Main.language+"/"+locat+"-Sound/"+s+".mp3");
        FileHandle handle = Gdx.files.internal(Main.language+"/"+locat+"-Sound/"+s+".mp3");
        if (!handle.exists()) {
            return Gdx.audio.newSound(Gdx.files.internal("sounds/winner.wav"));
        }
        System.out.println("Check sound Name: " +s);
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

    protected abstract void createExtraAssets();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);//135/255f, 206/255f, 235/255f, 1
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();//bottom//draws the texture
        stage.act();
        batch.begin();
        batch.draw(correctTexture, newWidth(.018f, 60f), newHeight(.9f, 100f), 60f, 100f);
        batch.draw(incorrectTexture, newWidth(.018f, 60f), newHeight(.8f, 100f), 60f, 100f);
        batch.draw(gemsTexture, newWidth(0f, 120f), newHeight(.7f, 100f), 120f, 100f);
        renderExtraStuff();
        batch.end();
        stage2.draw();
    }

    protected abstract void renderExtraStuff();

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
        imageButtons[i] = new ImageButton(getTexRegionDrawable(Main.language+"/"+locat+"/"
                +choices[i] + ".png"));
        imageButtons[i].setPosition(newWidth(positions[i][2], buttonXSize),
                newHeight(positions[i][3], buttonYSize));
        imageButtons[i].setSize(buttonXSize, buttonYSize);
        stage.addActor(imageButtons[i]);
        Gdx.input.setInputProcessor(stage);
        setImageButtonListeners(i);
    }

    public abstract void setImageButtonListeners(final int i);

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
                choiceMade(choices[i]);
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

    protected ImageButton.ImageButtonStyle newStyle(String string) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = getTexRegionDrawable(string);
        return style;
    }

    protected Texture pleaseFindMeATexture(char c) {
        Texture hereIsYourTexture = null;
        if(Character.isLetter(c)) {
            String path = "textures/Symbols/";
            if(Character.isUpperCase(c)) {
                path += "Uppercase/"+c+".png";
            } else {
                path += "Lowercase/"+c+".png";
            }
            hereIsYourTexture = new Texture(Gdx.files.internal(path));
        } else {
            if (!Character.isDigit(c)) {
                String path = "";
                switch (c) {
                    case ',':
                        path = "textures/Symbols/comma.png";
                        break;
                    case '.':
                        path = "textures/Symbols/period.png";
                        break;
                    default:
                        path = "textures/Symbols/question.png";
                        break;
                }
                hereIsYourTexture = new Texture(Gdx.files.internal(path));
            } else {
                String path2 = Main.language + "/Numbers Part 1/" + c + ".png";
                String path3 = Main.language + "/Numbers Part 2/" + c + ".png";
                String path4 = Main.language + "/Numbers Part 3/" + c + ".png";
                String path5 = Main.language + "/Numbers Part 4/" + c + ".png";
                String path6 = Main.language + "/Numbers Part 5/" + c + ".png";
                if (Gdx.files.internal(path2).exists()) {
                    hereIsYourTexture = new Texture(Gdx.files.internal(path2));
                } else if (Gdx.files.internal(path3).exists()) {
                    hereIsYourTexture = new Texture(Gdx.files.internal(path3));
                } else if (Gdx.files.internal(path4).exists()) {
                    hereIsYourTexture = new Texture(Gdx.files.internal(path4));
                } else if (Gdx.files.internal(path5).exists()) {
                    hereIsYourTexture = new Texture(Gdx.files.internal(path5));
                } else if (Gdx.files.internal(path6).exists()) {
                    hereIsYourTexture = new Texture(Gdx.files.internal(path6));
                }
            }
        }
        if(hereIsYourTexture == null) {
            hereIsYourTexture = new Texture(Gdx.files.internal("A.png"));
        }
        return hereIsYourTexture;
    }

    protected void randomize() {
        int rightChoice = (int)(Math.random()*4);
        ArrayList<String> picked = new ArrayList<String>();
        choices[rightChoice] = character;
        for(int i = 0; i < choices.length; i++) {
            if(i != rightChoice) {
                int j = (int) (Math.random() * arr.size()) ;
                choices[i] = arr.get(j);
                picked.add(arr.get(j));
                arr.remove(j);
            }
        }
        arr.addAll(picked);
    }

    protected abstract void change();

    private void choiceMade(String choice) {
        if(character.equals(choice)) {
            timesCorrect++;
            correctScore.setText(timesCorrect+"/"+(limit+1));
            if(timesCorrect < limit) {
                viewDialog(getTexRegionDrawable("Faces/happyFace.png"));
            }
            if(timesCorrect > limit) {
                numGems++;
                gemScore.setText(numGems+"/1");
                float delay = .75f; // seconds

                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        // Do your work
                        main.setScreen(new com.omnipotence.game.Practice.writeScreen(main,
                                10 - (.1*numWrong), stage2, skin, skin2, character));
                    }
                }, delay);
            } else {
                change();
            }
        } else {
            viewDialog(getTexRegionDrawable("Faces/sadFace.png"));
            numWrong += 1.0;
            timesIncorrect++;
            incorrectScore.setText(timesIncorrect+"");
        }
    }

    public void viewDialog(final Drawable drawable) {
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
                stage2.dispose();
                stage2 = new Stage();
            }
        }, delay);

    }

}

