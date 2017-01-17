package com.omnipotence.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.omnipotence.game.Practice.graphemesMode;
import com.omnipotence.game.Practice.mathPracticeMode;
import com.omnipotence.game.Practice.rimeFamilyMode;
import com.omnipotence.game.Practice.noTexturePracticeMode;
import com.omnipotence.game.Practice.defaultPracticeMode;
import com.omnipotence.game.Practice.questionMode;
import com.omnipotence.game.Practice.toolsMode;
import com.omnipotence.game.level.LevelData;
import com.omnipotence.game.util.AssetManager;
import com.omnipotence.game.Battle.battleMode;

import java.util.ArrayList;
/**
 * The level selection screen.
 */
public class LevelScreen extends defaultScreen {

	/** Determines the amount of pages shown on the level screen. */

	private Main gameInstance;

	private Skin skin;
	private Stage stage;
    private Table levelContainer;
    private float time;

	public LevelScreen(Main gameInstance) {
		this.gameInstance = gameInstance;
	}

    public LevelScreen(Main gameInstance, boolean nextLevel) {
        this.gameInstance = gameInstance;
        if(nextLevel) {
            this.gameInstance.levelManager.incrementLevel(0);
        }
    }


    @Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		initializeSkin();

		// Allow this stage to accept input
		Gdx.input.setInputProcessor(stage);

		// Master table that holds all level buttons and logos
        Table container = new Table();
		stage.addActor(container);
		container.setFillParent(true);

		initLevelContainer();

		constructLevelPackPages();

		// Add the levels to a scroll pane
		ScrollPane scroll = new ScrollPane(levelContainer, skin);
		scroll.setFadeScrollBars(false); // Make scroll bars always show
		container.add(scroll).expand().fill().row();
	}

    private void showDirectories() {
        FileHandle dirHandle;
        if (Gdx.app.getType() == Application.ApplicationType.Android ||
                Gdx.app.getType() == Application.ApplicationType.iOS ) {
            dirHandle = Gdx.files.internal("English/English.Alphabet");
        } else {
            // ApplicationType.Desktop ..
            dirHandle = Gdx.files.internal("./bin/English/English.Alphabet");
        }
        if(dirHandle != null) {
            for (FileHandle entry: dirHandle.list()) {
                System.out.println("File name: " + entry.name());
            }
        }
    }

	/**
	 * Initialize the level container with a background. The level container is
	 * the table which holds each level pack's levels and is added to the Scroll
	 * pane.
	 */
	private void initLevelContainer() {
		levelContainer = new Table();
		//levelContainer.setBackground(AssetManager.getInstance()
		//		.convertTextureToDrawable("background1"));
	}

	/**
	 * Creates the screens for each level pack and adds them to the level
	 * container.
	 */
	private void constructLevelPackPages() {
        int size;
        int totalSize = 0;
		for (int page = 0; page < gameInstance.gameStages.size(); page++) {
            size = gameInstance.gameStages.get(page).getLevelNameSize();
            System.out.println("Page: "+page+" Size:"+size);
            constructLevelPackPage(page, totalSize);
            totalSize += size;
		}
        System.out.println("Total Size: "+totalSize);
	}

	/**
	 * Creates a screen for each stage.
	 */
	private void constructLevelPackPage(int page, int size) {
		int currentLevelID = 0;

        currentLevelID += size;
        int columns = (int)Math.sqrt(gameInstance.gameStages.get(page).getLevelNameSize());
        if(columns == 1) {
            columns++;
        }
        int rows = (int)Math.ceil(gameInstance.gameStages.get(page).getLevelNameSize()/ (float)columns);

		Table levels = new Table();
		// Sets default padding to 20px top/bottom, 40px left/right
    //    if(gameInstance.language.equals("Swahili") && page == 2) {
    //        levels.defaults().pad(10, 40, 10, 40);
    //    } else {
            levels.defaults().pad(0, 40, 0, 40);
     //   }
        Label label = new Label(gameInstance.gameStages.get(page).getStageName(), skin, "largeLabel");
        label.setAlignment(Align.center);
		levels.add(label).colspan(columns).row(); // Logo spans 3 columns
        if(page % 2 == 0) {
            levels.setBackground(AssetManager.getInstance().convertTextureToDrawable("background1"));
        } else {
            levels.setBackground(AssetManager.getInstance().convertTextureToDrawable("background4"));
        }
        int index = 0;
		for (int y = 0; y < rows; y++) {
			levels.row();
			for (int x = 0; x < columns && index < gameInstance.gameStages.get(page).getLevelNameSize(); x++) {
                currentLevelID++;
				levels.add(setLevelButton(page, index, currentLevelID)).expand().fill();
                System.out.println("Page: "+page+" index: "+index+" currentID: "+currentLevelID);
                index++;
			}
		}
        levels.row();
        Table score = new Table();
        int userScore = gameInstance.gameStages.get(page).getStageScore(
                (size+1), gameInstance.levelManager, columns);
        for(int i = 0; i < columns; i++) {
            if(userScore > 0) {
                score.add(new Image(new TextureRegion(new Texture(Gdx.files.internal("Chest_Gem.png")))));
                userScore--;
            } else {
                score.add(new Image(new TextureRegion(new Texture(Gdx.files.internal("Chest_Close.png")))));
            }
        }
        levels.add(score).colspan(columns).row();

        //Creates a new row after every 4 columns
        if((page+1) % 3 == 0) {
            levelContainer.add(levels).row();
        } else {
            levelContainer.add(levels).colspan(1);
        }
	}

	/**
	 * Creates a button to represent the level.
	 */
	private Button setLevelButton(int page, int level, int levelKey) {
		Button button = new Button(skin);
		ButtonStyle style = button.getStyle();
		style.up = style.down = null;

		// Create the label to show the level name
        String levelName = "";
        //Breaks down the levelName by maxChars allowable per Button Width
        int maxChars = 25;
        if(gameInstance.gameStages.get(page).getLevelName(level).length() < maxChars+1) {
            levelName = gameInstance.gameStages.get(page).getLevelName(level);
        } else {
            String s = gameInstance.gameStages.get(page).getLevelName(level);
            while(s.length() > maxChars-1) {
                levelName += (s.substring(0, maxChars) + "\n");
                s = s.substring(maxChars);
            }
            levelName += s;
        }
        Boolean isBattle = levelName.contains("BATTLE");
		Label label = new Label((isBattle) ? "BATTLE" : levelName, skin, "default");
		label.setAlignment(Align.center);

		// Get the LevelData for the level
		LevelData data = null;
		if (gameInstance.levelManager.isLevel(levelKey)) {
			data = gameInstance.levelManager.getLevel(levelKey).levelData;
		}

		// False if data is not found (i.e. level hasn't been played yet)
		boolean unlocked = data != null && data.isUnlocked();
        Image buttonImage;
        if(unlocked) {
            buttonImage = new Image(skin.getDrawable("top"));
        } else if(isBattle) {
            buttonImage = new Image(skin.getDrawable("battle"));
        } else {
            buttonImage = new Image(skin.getDrawable("locked-level"));
        }
        button.stack(buttonImage, label).width(gameInstance.gameStages.get(page).getButtonWidth())
                .height(gameInstance.gameStages.get(page).getButtonHeight()).expand().fill();

		button.setName(Integer.toString(level));
        addClickListener(button, page, level, levelKey);
		return button;
	}

	/**
	 * Initialize the skin. The skin is a LibGDX API that tells UI widgets how to look.
	 */
	private void initializeSkin() {
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"), new TextureAtlas(
				"skin/uiskin.atlas"));
		// Set the unlocked level buttons to be green.
		skin.add("top", skin.newDrawable("default-round", Color.valueOf("#70ff7c")),
				Drawable.class);
		// Set the locked level buttons to be blue.
		skin.add("locked-level", skin.newDrawable("default-round", new Color(0f, 151f, 167f, .9f)),
				Drawable.class);
        // Set the battle level buttons to be red.
        skin.add("battle", skin.newDrawable("default-round", Color.valueOf("#ff7070")),
                Drawable.class);
        // Set regular font label
        skin.add("regularFont", new BitmapFont(Gdx.files
                .internal("skin/fonts/chalkboard-font.fnt")));
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("regularFont");
        skin.add("default", labelStyle);
        // Set large font label
        skin.add("largeFont", new BitmapFont(Gdx.files
                .internal("skin/fonts/chalkboard-font-large.fnt")));
        Label.LabelStyle largeLabelStyle = new Label.LabelStyle();
        largeLabelStyle.font = skin.getFont("largeFont");
        largeLabelStyle.fontColor = Color.valueOf("#ff7070");
        skin.add("largeLabel", largeLabelStyle);
	}

	/**
	 * Handle a button click/tap by going to the level that was tapped.
	 */
   private void addClickListener(Button button, final int page, final int level, final int levelKey) {
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String levelName = gameInstance.gameStages.get(page).getLevelName(level);
                if(gameInstance.levelManager.isLevel(levelKey) && gameInstance.levelManager.getLevel(levelKey).levelData.isUnlocked()) {
                    AssetManager.getInstance().getSound("button-click").play();

                    ArrayList<String> arr = new ArrayList<String>();
                    if(levelName.equals("BATTLE")) {
                        activateBattleMode(page, levelKey-1, level-1);
                    } else {
                       if(levelKey == 1) {
                           gameInstance.levelManager.setCurrentLevel(gameInstance.levelManager.
                                   getLevel(levelKey), levelKey);
                           gameInstance.setScreen(new newScreen(gameInstance));
                        } else {
                           gameInstance.levelManager.setCurrentLevel(gameInstance.levelManager.
                                   getLevel(levelKey), levelKey);
                           for (int i = 0; i < gameInstance.gameStages.get(page).getLevelNameSize(); i++) {
                               String s = gameInstance.gameStages.get(page).getLevelName(i);
                               if (!s.equals("BATTLE")) {
                                   arr.add(s);
                               }
                           }
                           int index = levelName.indexOf("-") + levelName.indexOf("+")
                                   + levelName.indexOf("*") + 2;
                           if (index > -1 && levelName.substring(levelName.length() - 1)
                                   .matches("[0-9]+")) {
                               activateMathPracticeMode(page, levelName, index);
                           } else if (levelName.contains("?")) {
                               arr.removeAll(arr);
                               for (String o : gameInstance.answerKeyForTheQuestions.values()) {
                                   arr.add(o);
                               }
                               gameInstance.setScreen(new questionMode(gameInstance,
                                       gameInstance.gameStages.get(page).getStageName(),
                                       gameInstance.gameStages.get(page).getLevelName(level),
                                       gameInstance.answerKeyForTheQuestions.get(gameInstance.
                                               gameStages.get(page).getLevelName(level)), arr));
                           } else if (gameInstance.gameStages.get(page).getStageName().equals("Tools")) {
                               gameInstance.setScreen(new toolsMode(gameInstance,
                                       gameInstance.gameStages.get(page).getStageName(),
                                       levelName, arr));
                           } else if (gameInstance.gameStages.get(page).getStageName().equals("Graphemes")
                                   && gameInstance.language == "English") {
                               gameInstance.setScreen(new graphemesMode(gameInstance,
                                       gameInstance.gameStages.get(page).getStageName(),
                                       levelName, arr));
                           } else if (gameInstance.gameStages.get(page).getStageName().equals("Rime Family") ||
                                   gameInstance.gameStages.get(page).getStageName().equals("Noun Classes")) {
                               gameInstance.setScreen(new rimeFamilyMode(gameInstance,
                                       gameInstance.gameStages.get(page).getStageName(),
                                       levelName, arr));

                           } else if (!(Gdx.files.internal(Main.language + "/" + gameInstance.
                                   gameStages.get(page).getStageName() + "/" + gameInstance.gameStages.
                                   get(page).getLevelName(level) + ".png").exists())) {
                               gameInstance.setScreen(new noTexturePracticeMode(gameInstance,
                                       gameInstance.gameStages.get(page).getStageName(),
                                       levelName, arr));
                           } else {
                               gameInstance.setScreen(new defaultPracticeMode(gameInstance,
                                       gameInstance.gameStages.get(page).getStageName(),
                                       levelName, arr));
                           }
                       }
                    }
                }
            }
        });
    }

    private void activateBattleMode(int page, int i, int j) {
        ArrayList<String> arr = new ArrayList<String>();
        String s = gameInstance.gameStages.get(page).getLevelName(j);
        ArrayList<String> ans = new ArrayList<String>();
        double wisePoint = 0;
        while(j > -1 && !gameInstance.gameStages.get(page).getLevelName(j).equals("BATTLE")) {
            arr.add(gameInstance.gameStages.get(page).getLevelName(j));
            if(s.contains("?")) {
                ans.add(gameInstance.answerKeyForTheQuestions.get(gameInstance.gameStages.get(page).getLevelName(j)));
            }
            wisePoint += gameInstance.levelManager.getLevel(i).levelData.getScore();
            j--;
            i--;
        }
        if((s.indexOf("-") + s.indexOf("+") + s.indexOf("*") + 2) > -1) {
            gameInstance.setScreen(new battleMode(gameInstance,
                    gameInstance.gameStages.get(page).getStageName(),
                    arr, (int) Math.ceil(wisePoint/arr.size()), 1));
        } else if(s.contains("?")) {
            gameInstance.setScreen(new battleMode(gameInstance,
                    gameInstance.gameStages.get(page).getStageName(),
                    arr, (int) Math.ceil(wisePoint/arr.size()), 3));
        } else if(!(Gdx.files.internal(Main.language+"/"+gameInstance.
                gameStages.get(page).getStageName()+"/"+gameInstance.gameStages.
                get(page).getLevelName(0)+".png").exists())) {
            gameInstance.setScreen(new battleMode(gameInstance,
                    gameInstance.gameStages.get(page).getStageName(),
                    arr, (int) Math.ceil(wisePoint/arr.size()), 2));
        } else {
            gameInstance.setScreen(new battleMode(gameInstance,
                    gameInstance.gameStages.get(page).getStageName(),
                    arr, (int) Math.ceil(wisePoint/arr.size()), 0));
        }
    }

    private void activateMathPracticeMode(int page, String levelName, int index) {
        ArrayList<String> arr = new ArrayList<String>();
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
        for(int i = 0; i < gameInstance.gameStages.get(page).getLevelNameSize(); i++) {
            int num = (int) ((Math.random() * answer) + (Math.random() * (101 - answer)));
            arr.add(""+num);
        }

        gameInstance.setScreen(new mathPracticeMode(gameInstance,
                gameInstance.gameStages.get(page).getStageName(), answer, arr, firstNum, secondNum, levelName));
    }

	//SCREEN CLASSES
    @Override
    public void render(float delta) {
        // Update and render the stage
        stage.act(Gdx.graphics.getDeltaTime());
        time = delta;
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
