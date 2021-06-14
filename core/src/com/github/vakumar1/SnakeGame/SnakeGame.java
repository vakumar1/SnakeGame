package com.github.vakumar1.snakegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;


public class SnakeGame extends Game {
	/** The SnakeGame class is a LibGDX Game instance
	 * used to move between:
	 * 	(i) Main Menu Screen: initial instructions
	 * 	(ii) Game Screen:  snake game grid
	 */

	/* game dimensions for rendering */
	public static final int TILE_SIZE = 16;
	public static final int SCREEN_WIDTH = GridGenerator.GRID_WIDTH * TILE_SIZE;
	public static final int SCREEN_HEIGHT = GridGenerator.GRID_HEIGHT * TILE_SIZE;

	/* game parameters (batch and fonts) */
	public SpriteBatch batch;
	public BitmapFont titleFont;
	public BitmapFont subtitleFont;
	public BitmapFont numFont;

	@Override
	public void create () {
		/* create batch and define fonts */
		batch = new SpriteBatch();
		FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Oswald-Regular.ttf"));
		FreeTypeFontParameter titleParam = new FreeTypeFontParameter();
		titleParam.size = 30;
		titleParam.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()[]^<>?:|-";
		titleFont = fontGenerator.generateFont(titleParam);

		FreeTypeFontParameter subtitleParam = new FreeTypeFontParameter();
		subtitleParam.size = 20;
		subtitleParam.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()[]^<>?:|-";
		subtitleFont = fontGenerator.generateFont(subtitleParam);

		FreeTypeFontParameter numParam = new FreeTypeFontParameter();
		numParam.size = 12;
		numParam.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.!'()[]^<>?:|-";
		numFont = fontGenerator.generateFont(numParam);

		/* create and set Main Menu Screen instance */
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
