package com.omnipotence.game.Practice;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.omnipotence.game.LevelScreen;
import com.omnipotence.game.Main;
import com.omnipotence.game.defaultScreen;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class writeScreen extends defaultScreen {

    private ImageButton clearButton, checkButton;
    private SpriteBatch batch;
    private Stage stage, stage2;
    private Skin skin, skin2;
    private Table mainContainer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Main main;
    private double score;
    private String vertexShader, fragmentShader, directory;
    private ShaderProgram shader;
    private DrawablePixmap drawable;
    private int drawableRegionWidth, drawableRegionHeight, w, h;
    private ArrayList<Texture> instructions = new ArrayList<Texture>();
    private Texture whiteBoxTexture;

    public writeScreen(Main main, double score, Stage stage2, Skin skin, Skin skin2, String d){
        this.main = main;
        this.score = (score < 0) ? 0 : score;
        this.stage2 = stage2;
        this.skin = skin;
        this.skin2 = skin2;
        this.directory = d;
        this.whiteBoxTexture = new Texture(Gdx.files.internal("whiteBox.png"));
        create();
    }

    private void createForDrawing() {
        /* I like to keep my shader programs as text files in the assets
		 * directory rather than dealing with horrid Java string format	ting. */
        drawableRegionHeight = h/5;
        drawableRegionWidth = w;
        drawable = new DrawablePixmap(new Pixmap(drawableRegionWidth,
                drawableRegionHeight, Pixmap.Format.Alpha), 1);

        vertexShader = "uniform mat4 u_projTrans;\n" +
                "\n" +
                "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoord0;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    v_color = a_color;\n" +
                "    v_texCoord0 = a_texCoord0;\n" +
                "    gl_Position = u_projTrans * a_position;\n" +
                "}\n";
        fragmentShader = "#ifdef GL_ES\n" +
                "    precision mediump float;\n" +
                "#endif\n" +
                "\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform sampler2D u_mask;\n" +
                "\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoord0;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    vec4 texColor = texture2D(u_texture, v_texCoord0);\n" +
                "    vec4 mask = texture2D(u_mask, v_texCoord0);\n" +
                "    texColor.a *= mask.a;\n" +
                "    gl_FragColor = v_color * texColor;\n" +
                "}";

		/* Bonus: you can set `pedantic = false` while tinkering with your
		 * shaders. This will stop it from crashing if you have unused variables
		 * and so on. */
        ShaderProgram.pedantic = false;

		/* Construct our shader program. Spit out a log and quit if the shaders
		 * fail to compile. */
        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            System.out.println("NOT COMPILING FUCK");
            Gdx.app.log("Shader", shader.getLog());
            Gdx.app.exit();
        }

		/* Tell our shader that u_texture will be in the TEXTURE0 spot and
		 * u_mask will be in the TEXTURE1 spot. We can set these now since
		 * they'll never change; we don't have to send them every render frame. */
        shader.begin();
        shader.setUniformi("u_texture", 0);
        shader.setUniformi("u_mask", 1);
        shader.end();
    }

    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(w, h);
        viewport = new FitViewport(w, h, camera);
        viewport.apply();

        initMainContainer();
        createForDrawing();
        createButtons();
        System.out.println("Checking the textures. ");
        if(directory != null && directory.contains("choice")) {
            directory = directory.replace("choice", "");
        }
        if(directory != null && Gdx.files.internal("Writing/"+directory).exists()) {
            createWriteInstructions();
        }

        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new DrawingInput());
        Gdx.input.setInputProcessor(inputMultiplexer);

        stage.addActor(mainContainer);
        stage.addActor(clearButton);
        stage.addActor(checkButton);
    }

    private void initMainContainer() {
        mainContainer = new Table();
        mainContainer.setBackground(getTexRegionDrawable("Trees rescaled.png"));
        mainContainer.setFillParent(true);
    }

    private void createButtons() {
        clearButton = new ImageButton(getTexRegionDrawable("clear.png"));
        checkButton = new ImageButton(getTexRegionDrawable("check.png"));
        clearButton.setPosition(newWidth(.8f, clearButton.getWidth()),
                newHeight(.25f, clearButton.getHeight()));
        checkButton.setPosition(newWidth(.9f, checkButton.getWidth()),
                newHeight(.25f, checkButton.getHeight()));
        clearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                drawable.clear();
                System.out.println("Button clicked CLEAR 2!");
            }
        });
        checkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                /*saveScreenshot();
                if((Gdx.files.internal("a2_base.png")).exists() &&
                        !Gdx.files.external("a2_base.png").exists()) {
                    System.out.println("Have to create the file");
                    FileHandle from = Gdx.files.internal("a2_base.png");
                    from.copyTo(Gdx.files.external("a2_base.png"));
                    getSSIMScore();
                } else {
                    getSSIMScore();
                }*/
                results();
            }
        });
    }

    private void createWriteInstructions() {
        System.out.println("Creating the textures.");
        FileHandle dirHandle;
        if (Gdx.app.getType() == Application.ApplicationType.Android ||
                Gdx.app.getType() == Application.ApplicationType.iOS ) {
            dirHandle = Gdx.files.internal("Writing/"+directory+"/");
        } else {
            // ApplicationType.Desktop ..
            dirHandle = Gdx.files.internal("./bin/Writing/"+directory+"/");
        }
        for (FileHandle entry: dirHandle.list()) {
            instructions.add(new Texture(Gdx.files.internal("Writing/"+directory+"/"+entry.name())));
        }
    }

    @Override
    public void dispose(){
        batch.dispose();
        drawable.dispose();
        stage.dispose();
        stage2.dispose();
        skin.dispose();
        skin2.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act();
        drawable.update();
        batch.begin();
        drawable.drawTexture(batch);
        batch.setProjectionMatrix(camera.combined);
        int half = 7;
        for(int i = 0; i < instructions.size(); i++) {
            //System.out.println("Drawing the textures.");
            Texture texture = instructions.get(i);
            float x =  newWidth(.1f + .15f * (i%half), texture.getWidth());
            float y = newHeight(.8f - (.3f * (i/half)), texture.getHeight());
            // batch.draw(whiteBoxTexture, x, y, 250f, 250f);
            batch.draw(texture, x, y, 250f, 250f);

        }
        batch.end();
        stage2.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void getSSIMScore() {
        float ssimNum = main.androidSIMM.getSSIM
                (new File(Gdx.files.getExternalStoragePath() + "screenshot.png"),
                        new File(Gdx.files.getExternalStoragePath() + "a2_base.png"));
        System.out.println("The diff is: "+ssimNum);
        System.out.println("Button clicked Check!");
    }

    private static void saveScreenshot(){
        try{
            System.out.println("Beginning to Save.");
            FileHandle fh = new FileHandle(Gdx.files.getExternalStoragePath() + "screenshot.png");
            Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/4, true);
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
            System.out.println("Saved!");
        }catch (Exception e){
            System.out.println("Didn't Save." + e.getMessage());
        }
    }

    private static Pixmap getScreenshot(int x, int y, int w, int h, boolean flipY) {
        Gdx.gl.glPixelStorei(GL20.GL_PACK_ALIGNMENT, 1);

        final Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        ByteBuffer pixels = pixmap.getPixels();
        Gdx.gl.glReadPixels(x, y, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, pixels);

        final int numBytes = w * h * 4;
        byte[] lines = new byte[numBytes];
        if (flipY) {
            final int numBytesPerLine = w * 4;
            for (int i = 0; i < h; i++) {
                pixels.position((h - i - 1) * numBytesPerLine);
                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
            }
            pixels.clear();
            pixels.put(lines);
        } else {
            pixels.clear();
            pixels.get(lines);
        }

        return pixmap;
    }

    private TextureRegionDrawable getTexRegionDrawable(String texture) {
        FileHandle handle = Gdx.files.internal(texture);
        if (!handle.exists()) {
            return new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal("A.png"))));
        } else {
            return new TextureRegionDrawable(new TextureRegion(
                    new Texture(Gdx.files.internal(texture))));
        }
    }

    private void results() {
        Gdx.input.setInputProcessor(stage2);
        final Drawable drawable2 = getTexRegionDrawable("resutlsBackground.jpg");
        drawable2.setBottomHeight(500f);
        drawable2.setTopHeight(500f);
        drawable2.setRightWidth(750f);
        drawable2.setLeftWidth(750f);
        Dialog dialog = new Dialog("", skin2, "dialog") {
            {
                setColor(Color.WHITE);
                setBackground(drawable2);
                setSize(500f, 400f);
                setPosition(newWidth(.5f, 500f), newHeight(.5f, 400f));
                System.out.println("Score: "+score);
                text(new Label("Score: "+score +"\nGems: " + 1,  skin, "largeLabel"));
                //getButtonTable().add(button2);
            }
        };
        dialog.show(stage2);
        Button button2 = new Button(getTexRegionDrawable("check.png"));
        button2.setSize(150f, 150f);
        button2.setPosition(newWidth(.5f, 150f), newHeight(.1f, 150f));
        button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage2.dispose();
                main.levelManager.incrementLevel(score);
                main.setScreen(new LevelScreen(main));
                float delay = 2.75f; // seconds

                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        // Do your work
                        stage2.dispose();
                        //main.levelManager.incrementLevel(score);
                        main.setScreen(new LevelScreen(main));
                    }
                }, delay);
            }
        });
        stage2.addActor(button2);

    }

    /**
     * @author Tyler Coles
     */

    /**
     * Nested (static) class to provide a nice abstraction over Pixmap, exposing
     * only the draw calls we want and handling some of the logic for smoothed
     * (linear interpolated, aka 'lerped') drawing. This will become the 'owner'
     * of the underlying pixmap, so it will need to be disposed.
     */
    private static class DrawablePixmap implements Disposable {

        private final int brushSize = 5; //origianl 10
        private final Color clearColor = new Color(0, 0, 0, 0);
        private final Color drawColor = new Color(1, 1, 1, 1);
        private Pixmap pixmap;
        private Texture texture;
        private boolean dirty;

        public DrawablePixmap(Pixmap pixmap, int textureBinding) {
            this.pixmap = pixmap;
            pixmap.setColor(drawColor);

			/* Create a texture which we'll update from the pixmap. */
            this.texture = new Texture(pixmap);
            this.dirty = false;

			/* Bind the mask texture to TEXTURE<N> (TEXTURE1 for our purposes),
			 * which also sets the currently active texture unit. */
            this.texture.bind(textureBinding);

			/* However SpriteBatch will auto-bind to the current active texture,
			 * so we must now reset it to TEXTURE0 or else our mask will be
			 * overwritten. */
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        }

        /** Write the pixmap onto the texture if the pixmap has changed. */
        void update() {
            if (dirty) {
                texture.draw(pixmap, 0, 0);
                dirty = false;
            }
        }

        void clear() {
            pixmap.setColor(clearColor);
            pixmap.fill();
            pixmap.setColor(drawColor);
            dirty = true;
        }

        private void drawDot(Vector2 spot) {
            pixmap.fillCircle((int) spot.x, (int) spot.y, brushSize);
        }

        void draw(Vector2 spot) {
            drawDot(spot);
            dirty = true;
        }

        void drawTexture(Batch batch) {
            batch.draw(texture, 0, 0);
        }

        void drawLerped(Vector2 from, Vector2 to) {
            float dist = to.dst(from);
			/* Calc an alpha step to put one dot roughly every 1/8 of the brush
			 * radius. 1/8 is arbitrary, but the results are fairly nice. */
            float alphaStep = brushSize / (8f * dist);

            for (float a = 0; a < 1f; a += alphaStep) {
                Vector2 lerped = from.lerp(to, a);
                drawDot(lerped);
            }

            drawDot(to);
            dirty = true;
        }

        @Override
        public void dispose() {
            texture.dispose();
            pixmap.dispose();
        }
    }

    /**
     * Inner (non-static) class to handle mouse and keyboard events. Mostly we
     * want to pass on appropriate draw calls to our DrawablePixmap and this
     * means keeping track of some state (last coordinates drawn and whether or
     * not the left mouse button is pressed) to handle smooth drawing while
     * dragging the mouse.
     */
    private class DrawingInput extends InputAdapter {

        private Vector2 last = null;
        private boolean leftDown = false;

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer,
                                 int button) {
            if (button == Input.Buttons.LEFT) {
                Vector2 curr = new Vector2(screenX, screenY - h/1.25f);
                System.out.println(screenY);
                System.out.println(h-screenY);
                drawable.draw(curr);
                last = curr;
                leftDown = true;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (leftDown) {
                Vector2 curr = new Vector2(screenX, screenY - h/1.25f);
                System.out.println(screenX);
                System.out.println(h-screenY);
                drawable.drawLerped(last, curr);
                last = curr;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (button == Input.Buttons.LEFT) {
                drawable.draw(new Vector2(screenX, screenY - h/1.25f));
                System.out.println(screenY);
                System.out.println(h-screenY);
                last = null;
                leftDown = false;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.ESCAPE:
                case Input.Keys.F5:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Input.Keys.ESCAPE:
                    Gdx.app.exit();
                    return true;
                case Input.Keys.F5:
                    drawable.clear();
                    return true;
                default:
                    return false;
            }
        }
    }

}
