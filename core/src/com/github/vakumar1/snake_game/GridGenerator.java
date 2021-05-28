package com.github.vakumar1.snake_game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GridGenerator {
    public final static int GRID_WIDTH = 80;
    public final static int GRID_HEIGHT = 40;
    public final static int BUFFER = 5;

    public final static Random RANDOM = new Random();
    public final static List<Character> directions = new LinkedList<Character>(){
        {
            add('U');
            add('D');
            add('R');
            add('L');
        }
    };

    public Cell[][] grid;
    public LinkedList<Point> snakeList;
    public Point head;
    private Point tail;
    public Point food;
    private char direction;
    private int score;

    public GridGenerator() {
        grid = new Cell[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                grid[x][y] = GameScreen.NOTHING_CELL;
            }
        }
    }

    public void initGrid() {
        head = new Point();
        food = new Point();
        snakeList = new LinkedList<Point>();
        score = 0;

        head.x = RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER;
        head.y = RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER;
        generateNewFood();

        snakeList.add(head);
        tail = head;

        grid[head.x][head.y] = GameScreen.SNAKE_CELL;
        grid[food.x][food.y] = GameScreen.FOOD_CELL;
    }

    public int getScore() { return score; }

    public char getDirection() { return direction; }

    public boolean updateDirection(char newDirection) {
        if (direction == 'U' && newDirection == 'D' ||
                direction == 'D' && newDirection == 'U' ||
                direction == 'L' && newDirection == 'R' ||
                direction == 'R' && newDirection == 'L'
        ) {
            return false;
        }
        if (directions.contains(newDirection)) {
            direction = newDirection;
            return true;
        }
        return false;
    }

    public boolean updateSnake() {
        Point nextHead = new Point(head.x, head.y);
        switch (direction) {
            case 'U':
                nextHead.y += 1;
                break;
            case 'D':
                nextHead.y -= 1;
                break;
            case 'R':
                nextHead.x += 1;
                break;
            case 'L':
                nextHead.x -= 1;
                break;
            default:
                break;
        }

        if (nextHead.equals(head)) {
            return true;
        }
        if (isValid(nextHead.x, nextHead.y)) {
            head = nextHead;
            snakeList.addFirst(head);
            grid[head.x][head.y] = GameScreen.SNAKE_CELL;
            if (!head.equals(food)) {
                tail = snakeList.removeLast();
                grid[tail.x][tail.y] = GameScreen.NOTHING_CELL;
            } else {
                score += 1;
                generateNewFood();
            }
            return true;
        }
        return false;
    }

    public boolean isValid(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        if (grid[x][y] == GameScreen.SNAKE_CELL) {
            return false;
        }
        return true;
    }

    public boolean firstIsCloserToGiven(Point given, Point p1, Point p2) {
        double dist1 = Math.sqrt(Math.pow((given.x - p1.x), 2) + Math.pow((given.y - p1.y), 2));
        double dist2 = Math.sqrt(Math.pow((given.x - p2.x), 2) + Math.pow((given.y - p2.y), 2));
        return dist1 < dist2;
    }

    private void generateNewFood() {
        food.x = RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER;
        food.y = RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER;

        if (isValid(food.x, food.y)) {
            grid[food.x][food.y] = GameScreen.FOOD_CELL;
        } else {
            generateNewFood();
        }
    }
}
