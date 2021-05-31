package com.github.vakumar1.SnakeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
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

        GlyphLayout titleLayout = new GlyphLayout();
        titleLayout.setText(game.titleFont, "SNAKE GAME");
        game.titleFont.draw(game.batch, titleLayout, (SnakeGame.SCREEN_WIDTH - titleLayout.width) / 2, (SnakeGame.SCREEN_HEIGHT / 2 + 100));

        GlyphLayout subtitleLayout1 = new GlyphLayout();
        subtitleLayout1.setText(game.subtitleFont, "Press 'P' to Play");
        game.subtitleFont.draw(game.batch, subtitleLayout1, (SnakeGame.SCREEN_WIDTH - subtitleLayout1.width) / 2, (SnakeGame.SCREEN_HEIGHT / 2 - 50));

        GlyphLayout subtitleLayout2 = new GlyphLayout();
        subtitleLayout2.setText(game.subtitleFont, "Press 'W' to Watch");
        game.subtitleFont.draw(game.batch, subtitleLayout2, (SnakeGame.SCREEN_WIDTH - subtitleLayout2.width) / 2, (SnakeGame.SCREEN_HEIGHT / 2 - 100));
        game.batch.end();

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
