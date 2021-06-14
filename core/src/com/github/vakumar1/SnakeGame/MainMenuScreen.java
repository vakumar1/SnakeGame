package com.github.vakumar1.snakegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    /** main menu screen for displaying initial instructions */
    private final SnakeGame game;
    private OrthographicCamera camera;

    public MainMenuScreen(final SnakeGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SnakeGame.SCREEN_WIDTH, SnakeGame.SCREEN_HEIGHT);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        /* title */
        GlyphLayout titleLayout = new GlyphLayout();
        titleLayout.setText(game.titleFont, "SNAKE GAME");
        game.titleFont.draw(game.batch, titleLayout, (SnakeGame.SCREEN_WIDTH - titleLayout.width) / 2,
                (SnakeGame.SCREEN_HEIGHT / 2 + 100));

        /* play instruction */
        GlyphLayout subtitleLayout1 = new GlyphLayout();
        subtitleLayout1.setText(game.subtitleFont, "[P] lay Snake Game");
        game.subtitleFont.draw(game.batch, subtitleLayout1, (SnakeGame.SCREEN_WIDTH - subtitleLayout1.width) / 2,
                (SnakeGame.SCREEN_HEIGHT / 2 - 50));

        /* watch instruction */
        GlyphLayout subtitleLayout2 = new GlyphLayout();
        subtitleLayout2.setText(game.subtitleFont, "[W] atch Greedy Snake Player");
        game.subtitleFont.draw(game.batch, subtitleLayout2, (SnakeGame.SCREEN_WIDTH - subtitleLayout2.width) / 2,
                (SnakeGame.SCREEN_HEIGHT / 2 - 100));
        game.batch.end();

        /* create and set new Game Screen */
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            game.setScreen(new GameScreen(game, true));
            dispose();
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            game.setScreen(new GameScreen(game, false));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


}
