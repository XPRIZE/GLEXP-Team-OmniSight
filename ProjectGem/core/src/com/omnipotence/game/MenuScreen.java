package com.omnipotence.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.omnipotence.game.util.AssetManager;

/**
 * The main menu for the game.
 * 
 * @author Faizaan Datoo
 */
public class MenuScreen extends defaultScreen {

	private Main gameInstance;
	private Stage stage;
	private Table mainContainer;

	/**
	 * @param gameInstance
	 *            Instance of Main for access to variables stored only in the
	 *            Main class.
	 */
	public MenuScreen(Main gameInstance) {
		this.gameInstance = gameInstance;
	}

	@Override
	public void show() {
		stage = new Stage();

		initMainContainer();

		// Add Textures to the main container
		addLogo();
		addPlayButton();

		stage.addActor(mainContainer);
		Gdx.input.setInputProcessor(stage); // Allows the stage to take in input
	}

	@Override
	public void render(float delta) {
		stage.act();
		stage.draw();
	}

	/**
	 * Initialize the main table (container) with a background. This table holds
	 * and positions all buttons on the menu.
	 */
	private void initMainContainer() {
		mainContainer = new Table();
		mainContainer.setBackground(AssetManager.getInstance()
				.convertTextureToDrawable("menuBackground"));
		mainContainer.setFillParent(true);
	}
	
	/**
	 * Add the logo and center it across the screen.
	 */
	private void addLogo() {
		Image problematicLogo = new Image(AssetManager.getInstance()
				.convertTextureToDrawable("gameLogo"));
		mainContainer.add(problematicLogo).colspan(1).align(Align.center).pad(250f);
	}

	/**
	 * Add the play button to the main container. The play button is responsible
	 * for taking the user to the level screen, where they choose a level.
	 */
	private void addPlayButton() {
		ImageButton playButton = new ImageButton(AssetManager.getInstance()
				.convertTextureToDrawable("playButton"));

		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				AssetManager.getInstance().getSound("button-click").play();
				dispose();
				gameInstance.setScreen(new LevelScreen(gameInstance));
			}
		});

		mainContainer.pad(40f).row();
		mainContainer.add(playButton).pad(160f).row();
	}
}
