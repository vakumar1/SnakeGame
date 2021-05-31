package com.github.vakumar1.SnakeGame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.concurrent.TimeUnit;


public class GameScreen extends ApplicationAdapter implements Screen, InputProcessor {
    public static final TiledMapTileLayer.Cell NOTHING_CELL = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("blue.gif"))));
    public static final TiledMapTileLayer.Cell SNAKE_CELL = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("indigo.png"))));
    public static final TiledMapTileLayer.Cell FOOD_CELL = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("pumpkin.png"))));

    public static final int MIN_CINCH_INDEX = 5;
    public static final double CINCH_DIVISOR = 2.;
    private SnakeGame game;
    private boolean play;
    private OrthographicCamera camera;
    private TiledMap map;
    private TiledMapTileLayer mapLayer;
    private TiledMapRenderer renderer;
    private GridGenerator generator;
    private AutonomousPlayer aplayer;
    private boolean gameOver;

    public GameScreen(final SnakeGame game, boolean play) {
        this.game = game;
        this.play = play;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SnakeGame.SCREEN_WIDTH, SnakeGame.SCREEN_HEIGHT);

        map = new TiledMap();
        mapLayer = new TiledMapTileLayer(GridGenerator.GRID_WIDTH, GridGenerator.GRID_HEIGHT, SnakeGame.TILE_SIZE, SnakeGame.TILE_SIZE);
        map.getLayers().add(mapLayer);
        renderer = new OrthogonalTiledMapRenderer(map);
        Gdx.input.setInputProcessor(this);

        generator = new GridGenerator(MIN_CINCH_INDEX, CINCH_DIVISOR);
        aplayer = new AutonomousPlayer(generator);
        gameOver = false;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        renderer.setView(camera);
        renderer.render();

        if (!gameOver) {
            if (!generator.updateSnake()) {
                gameOver = true;
            }
            updateMap();

            game.batch.begin();
            GlyphLayout scoreLayout = new GlyphLayout();
            scoreLayout.setText(game.subtitleFont, "Score: " + generator.getScore());
            game.subtitleFont.draw(game.batch, scoreLayout, (SnakeGame.SCREEN_WIDTH - scoreLayout.width) / 2, SnakeGame.SCREEN_HEIGHT - 25);
            if (play) {
                GlyphLayout controlInstructions = new GlyphLayout();
                controlInstructions.setText(game.subtitleFont, "END GAME: Q \nUP: ^ \nDOWN: v \nLEFT: < \nRIGHT: >");
                game.subtitleFont.draw(game.batch, controlInstructions, 5, SnakeGame.SCREEN_HEIGHT - 25);
                try {
                    TimeUnit.MILLISECONDS.sleep(35);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                GlyphLayout controlInstructions = new GlyphLayout();
                controlInstructions.setText(game.subtitleFont, "END GAME: Q");
                game.subtitleFont.draw(game.batch, controlInstructions, 5, SnakeGame.SCREEN_HEIGHT - 25);
                aplayer.smartUpdateDirection();
            }
            game.batch.end();
        } else {
            game.batch.begin();
            GlyphLayout endLayout = new GlyphLayout();
            endLayout.setText(game.subtitleFont, "Your score was " + generator.getScore() + ". Press 'R' to return to the Main Menu.");
            game.subtitleFont.draw(game.batch, endLayout, (SnakeGame.SCREEN_WIDTH - endLayout.width) / 2, SnakeGame.SCREEN_HEIGHT / 2);
            game.batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void show() {}
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose () {}

    @Override
    public boolean keyDown(int keycode) {
        if (gameOver) {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        } else if (keycode == Input.Keys.Q) {
            gameOver = true;
        } else if (play) {
            switch (keycode) {
                case Input.Keys.UP:
                    generator.updateDirection('U');
                    break;
                case Input.Keys.DOWN:
                    generator.updateDirection('D');
                    break;
                case Input.Keys.RIGHT:
                    generator.updateDirection('R');
                    break;
                case Input.Keys.LEFT:
                    generator.updateDirection('L');
                    break;
                default:
                    break;
            }
        }
        return false;
    }
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (gameOver) {
            if (Gdx.input.isTouched()) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void updateMap() {
        for (int x = 0; x < mapLayer.getWidth(); x += 1) {
            for (int y = 0; y < mapLayer.getHeight(); y += 1) {
                switch (generator.getGridCell(x, y)) {
                    case GridGenerator.NOTHING_POINT:
                        mapLayer.setCell(x, y, NOTHING_CELL);
                        break;
                    case GridGenerator.SNAKE_POINT:
                        mapLayer.setCell(x, y, SNAKE_CELL);
                        break;
                    case GridGenerator.FOOD_POINT:
                        mapLayer.setCell(x, y, FOOD_CELL);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
