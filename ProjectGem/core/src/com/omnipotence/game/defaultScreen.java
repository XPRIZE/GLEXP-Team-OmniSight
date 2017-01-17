package com.omnipotence.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by khuramchaudhry on 12/2/16.
 *
 */

public class defaultScreen implements Screen {
    private float hPosition, wPosition;

    public defaultScreen() {
        hPosition = Gdx.graphics.getHeight();
        wPosition = Gdx.graphics.getWidth();
    }

    public float newHeight(float j, float size) {
        return (hPosition-size) * j;
    }

    public float newWidth(float j, float size) {
        return (wPosition-size) * j;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
