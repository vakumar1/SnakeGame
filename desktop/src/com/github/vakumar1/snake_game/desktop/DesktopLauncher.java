package com.github.vakumar1.snake_game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.vakumar1.snake_game.SnakeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "drop";
		config.width = SnakeGame.SCREEN_WIDTH;
		config.height = SnakeGame.SCREEN_HEIGHT;
		new LwjglApplication(new SnakeGame(), config);
	}
}
