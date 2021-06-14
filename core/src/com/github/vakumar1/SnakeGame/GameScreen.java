package com.github.vakumar1.snakegame;

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
    /** game screen for continuously displaying and updating Snake Game */

    /* cell representations of nothing, snake, and food squares */
    public static final TiledMapTileLayer.Cell NOTHING_CELL = new TiledMapTileLayer.Cell().setTile(
            new StaticTiledMapTile(new TextureRegion(new Texture("blue.gif"))));
    public static final TiledMapTileLayer.Cell SNAKE_CELL = new TiledMapTileLayer.Cell().setTile(
            new StaticTiledMapTile(new TextureRegion(new Texture("indigo.png"))));
    public static final TiledMapTileLayer.Cell FOOD_CELL = new TiledMapTileLayer.Cell().setTile(
            new StaticTiledMapTile(new TextureRegion(new Texture("pumpkin.png"))));

    private SnakeGame game;
    private OrthographicCamera camera;

    /* map stores grid of snake game as a TiledMap */
    private TiledMap map;
    private TiledMapTileLayer mapLayer;
    private TiledMapRenderer renderer;

    /* abstract snake game instances (grid generator and autonomous player */
    private GridGenerator generator;
    private AutonomousPlayer aplayer;

    /* playerControl is true if the player is actively playing */
    private boolean playerControl;

    /*
    gameState defines the current state of the game:
        0: pre-game
        1: in-game
        2: post-game
     */
    private int gameState;

    public GameScreen(final SnakeGame game, boolean playerControl) {
        this.game = game;
        this.playerControl = playerControl;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SnakeGame.SCREEN_WIDTH, SnakeGame.SCREEN_HEIGHT);

        map = new TiledMap();
        mapLayer = new TiledMapTileLayer(GridGenerator.GRID_WIDTH, GridGenerator.GRID_HEIGHT,
                SnakeGame.TILE_SIZE, SnakeGame.TILE_SIZE);
        map.getLayers().add(mapLayer);
        renderer = new OrthogonalTiledMapRenderer(map);
        Gdx.input.setInputProcessor(this);

        generator = new GridGenerator();
        aplayer = new AutonomousPlayer(generator);
        gameState = 0;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        switch (gameState) {
            case 0:
                /* set initial pre-game instructions */
                updateMap();
                game.batch.begin();
                GlyphLayout instructions = new GlyphLayout();
                if (playerControl) {
                    instructions.setText(game.subtitleFont,
                            "USE ARROW KEYS TO CHANGE DIRECTION\n" +
                                    "(PRESS ANY DIRECTION TO START)\n" +
                                    "                      --------\n" +
                                    "                     |    ^    |\n" +
                                    "            -------- -------- --------\n" +
                                    "           |    <    |    v    |    >    |\n" +
                                    "            -------- -------- --------\n"
                    );
                } else {
                    instructions.setText(game.subtitleFont,"PRESS ANY DIRECTION TO START");
                }
                game.subtitleFont.draw(game.batch, instructions, (SnakeGame.SCREEN_WIDTH - instructions.width) / 2,
                        SnakeGame.SCREEN_HEIGHT / 2 + 50);
                game.batch.end();
                break;
            case 1:
                /* set in-game instructions and information */
                if (!generator.updateSnake()) {
                    gameState = 2;
                }
                updateMap();
                game.batch.begin();
                GlyphLayout scoreLayout = new GlyphLayout();
                scoreLayout.setText(game.subtitleFont, "Score: " + generator.getScore());
                game.subtitleFont.draw(game.batch, scoreLayout, (SnakeGame.SCREEN_WIDTH - scoreLayout.width) / 2,
                        SnakeGame.SCREEN_HEIGHT - 25);
                GlyphLayout controlInstructions = new GlyphLayout();
                controlInstructions.setText(game.subtitleFont, "PRESS 'Q' TO END GAME");
                game.subtitleFont.draw(game.batch, controlInstructions, 5, SnakeGame.SCREEN_HEIGHT - 25);
                game.batch.end();
                if (playerControl) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(35);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    aplayer.greedyUpdateDirection();
                }
                break;
            default:
                /* set post-game instructions and information */
                game.batch.begin();
                GlyphLayout endLayout = new GlyphLayout();
                endLayout.setText(game.subtitleFont, "Your score was " + generator.getScore() +
                        ". Press 'R' to return to the Main Menu.");
                game.subtitleFont.draw(game.batch, endLayout, (SnakeGame.SCREEN_WIDTH - endLayout.width) / 2,
                        SnakeGame.SCREEN_HEIGHT / 2);
                game.batch.end();
                break;
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

    /** key strokes are registered when:
     *      (i) the game is pre-game or in-game, and either
     *          (a) the player quits the game (moves to post-game), or
     *          (b) the player switches directions when either
     *              (i) the player is actively playing the game
     *              (ii) the player is in pre-game
     *      (ii) the game is post-game and the player returns to the main menu
     */
    @Override
    public boolean keyDown(int keycode) {
        if (gameState < 2) {
            if (keycode == Input.Keys.Q) {
                gameState = 2;
            } else if (playerControl || gameState == 0) {
                int updatedGameState = 1;
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
                        updatedGameState = gameState;
                        break;
                }
                gameState = updatedGameState;
            }
        } else if (keycode == Input.Keys.R) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
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

    /** update map cells */
    private void updateMap() {
        mapLayer.setCell(generator.getHeadX(), generator.getHeadY(), SNAKE_CELL);
        mapLayer.setCell(generator.getTailX(), generator.getTailY(), NOTHING_CELL);
        mapLayer.setCell(generator.getFoodX(), generator.getFoodY(), FOOD_CELL);
        if (gameState == 0) {
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
}
