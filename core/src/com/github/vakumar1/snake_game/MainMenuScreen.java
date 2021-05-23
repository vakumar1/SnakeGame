package com.github.vakumar1.snake_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen{
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

        String title = "Snake Game";
        GlyphLayout titleLayout = new GlyphLayout();
        titleLayout.setText(game.font, title);
        game.font.draw(game.batch, titleLayout, (SnakeGame.SCREEN_WIDTH - titleLayout.width) / 2, (SnakeGame.SCREEN_HEIGHT / 2));

        String subtitle = "Press any key to begin";
        GlyphLayout subtitleLayout = new GlyphLayout();
        subtitleLayout.setText(game.font, subtitle);
        game.font.draw(game.batch, subtitleLayout, (SnakeGame.SCREEN_WIDTH - subtitleLayout.width) / 2, (SnakeGame.SCREEN_HEIGHT / 3));

        game.batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new GameScreen(game));
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
