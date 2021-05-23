package com.github.vakumar1.snake_game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SnakeGame extends Game {
	public static final int TILE_SIZE = 16;
	public static final int SCREEN_WIDTH = GridGenerator.GRID_WIDTH * TILE_SIZE;
	public static final int SCREEN_HEIGHT = GridGenerator.GRID_HEIGHT * TILE_SIZE;

	public SpriteBatch batch;
	public BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
