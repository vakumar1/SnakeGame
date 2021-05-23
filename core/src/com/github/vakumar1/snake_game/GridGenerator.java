package com.github.vakumar1.snake_game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

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
    private LinkedList<Point> snakeList;
    private Point food;
    private char direction;
    private int score;

    public class Point {
        int x;
        int y;

        public Point() {
            this.x = -1;
            this.y = -1;
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (other instanceof Point) {
                Point otherPoint = (Point) other;
                return this.x == otherPoint.x && this.y == otherPoint.y;
            }
            return false;
        }
    }

    public GridGenerator() {
        grid = new Cell[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                grid[x][y] = GameScreen.NOTHING_CELL;
            }
        }
    }

    public void initGrid() {
        Point head = new Point();
        food = new Point();
        snakeList = new LinkedList<Point>();
        score = 0;

        head.x = RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER;
        head.y = RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER;
        generateNewFood();

        snakeList.add(head);

        grid[head.x][head.y] = GameScreen.SNAKE_CELL;
        grid[food.x][food.y] = GameScreen.FOOD_CELL;
    }

    public int getScore() {
        return score;
    }

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
        Point head = snakeList.get(0);
        int nextX = head.x;
        int nextY = head.y;
        switch (direction) {
            case 'U':
                nextY += 1;
                break;
            case 'D':
                nextY -= 1;
                break;
            case 'R':
                nextX += 1;
                break;
            case 'L':
                nextX -= 1;
                break;
            default:
                break;
        }

        Point nextHead = new Point(nextX, nextY);
        if (nextHead.equals(head)) {
            return true;
        }
        if (isValid(nextHead)) {
            snakeList.addFirst(nextHead);
            grid[nextHead.x][nextHead.y] = GameScreen.SNAKE_CELL;
            if (!nextHead.equals(food)) {
                Point tail = snakeList.removeLast();
                grid[tail.x][tail.y] = GameScreen.NOTHING_CELL;
            } else {
                score += 1;
                generateNewFood();
            }
            return true;
        }
        return false;
    }

    private boolean isValid(Point p) {
        boolean inBounds = p.x >= 0 && p.x < GRID_WIDTH && p.y >= 0 && p.y < GRID_HEIGHT;
        boolean isNotSnake = !snakeList.contains(p);
        return inBounds && isNotSnake;
    }

    private void generateNewFood() {
        food.x = RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER;
        food.y = RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER;

        if (isValid(food)) {
            grid[food.x][food.y] = GameScreen.FOOD_CELL;
        } else {
            generateNewFood();
        }
    }
}
