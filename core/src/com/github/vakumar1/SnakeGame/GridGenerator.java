package com.github.vakumar1.snakegame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GridGenerator {
    /** The GridGenerator Class creates a grid to store a game and methods to update the game parameters */

    /* Grid dimensions and "buffer zone" (in which no food will be created) */
    public final static int GRID_WIDTH = 80;
    public final static int GRID_HEIGHT = 40;
    public final static int BUFFER = 5;

    /* integer representations of blank space, snake, and food points in the grid */
    public final static  int NOTHING_POINT = 0;
    public final static int SNAKE_POINT = 1;
    public final static int FOOD_POINT = 2;

    public final static Random RANDOM = new Random();
    public final static List<Character> DIRECTIONS = new ArrayList<Character>() {
        {
            add('U');
            add('D');
            add('R');
            add('L');
        }
    };

    /* grid stores the integer representations of each point in the game */
    private int[][] grid;

    /* head, tail, and food point to their respective points in the grid
    * snakeList stores the Point representations of each point in the snake */
    private Point head;
    private Point tail;
    private Point food;
    private LinkedList<Point> snakeList;

    /* direction stores the current direction of the snake's movement */
    private char direction;

    /* score stores the current player's score (length of the snake - 1) */
    private int score;

    public GridGenerator() {
        grid = new int[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                grid[x][y] = NOTHING_POINT;
            }
        }

        head = new Point();
        setHeadX(RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER);
        setHeadY(RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER);
        tail = head;
        food = new Point();
        generateNewFood();

        snakeList = new LinkedList<>();
        snakeList.add(head);

        grid[getHeadX()][getHeadY()] = SNAKE_POINT;
        grid[getFoodX()][getFoodY()] = FOOD_POINT;

        direction = 'N';
        score = 0;
    }

    /** public access methods */

    public int getGridCell(int x, int y) {
        return grid[x][y];
    }

    public Point getSnakePoint(int i) { return snakeList.get(i); }

    public int getSnakeSize() { return snakeList.size(); }

    public int getSnakeIndexOfPoint(Point p) { return snakeList.indexOf(p); }

    public boolean snakeHasPoint (Point p) { return snakeList.contains(p); }

    public int getHeadX() { return head.x; }

    public int getHeadY() { return head.y; }

    public void setHeadX(int x) { head.x = x; }

    public void setHeadY(int y) { head.y = y; }

    public int getTailX() { return tail.x; }

    public int getTailY() { return tail.y; }

    public int getFoodX() { return food.x; }

    public int getFoodY() { return food.y; }

    public int getScore() { return score; }

    public char getDirection() { return direction; }

    /** state update methods */

    /** changes direction if NEW_DIRECTION is valid */
    public void updateDirection(char newDirection) {
        if (!DIRECTIONS.contains(newDirection)) {
            return;
        }
        if (newDirection == getOppositeDirection(direction)) {
            return;
        }
        direction = newDirection;
    }

    /** returns false if the game is over after updating the snake's state with the current direction */
    public boolean updateSnake() {
        if (!DIRECTIONS.contains(direction)) {
            return true;
        }
        Point nextHead = new Point(head);
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
        }
        if (isValid(nextHead.x, nextHead.y)) {
            /* add head to the front of snake */
            head = nextHead;
            snakeList.addFirst(head);
            grid[getHeadX()][getHeadY()] = SNAKE_POINT;

            /* update the tail and food */
            if (!head.equals(food)) {
                tail = snakeList.removeLast();
                grid[getTailX()][getTailY()] = NOTHING_POINT;
            } else {
                score += 1;
                generateNewFood();
            }
            return true;
        }
        return false;
    }

    /** public helper methods */

    /** returns true if the point is on the grid and is not part of the snake */
    public boolean isValid(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        return grid[x][y] != SNAKE_POINT;
    }

    /** return the "opposite" direction of DIR */
    public static char getOppositeDirection(char dir) {
        switch (dir) {
            case 'U':
                return 'D';
            case 'D':
                return 'U';
            case 'R':
                return 'L';
            case 'L':
                return 'R';
            default:
                return 'N';
        }
    }

    /** return the point that will result from moving in DIR from
     * the point given by CURR_X and CURR_Y */
    public static Point getNextPoint(int currX, int currY, char dir) {
        switch (dir) {
            case 'U':
                return new Point(currX, currY + 1);
            case 'D':
                return new Point(currX, currY - 1);
            case 'R':
                return new Point(currX + 1, currY);
            case 'L':
                return new Point(currX - 1, currY);
            default:
                return new Point(currX, currY);
        }
    }

    /** return the "clockwise" direction of DIR */
    public static char rotateClockwiseDirection(char dir) {
        switch (dir) {
            case 'U':
                return 'R';
            case 'D':
                return 'L';
            case 'R':
                return 'D';
            case 'L':
                return 'U';
            default:
                return 'N';
        }
    }

    /** return the "counter-clockwise" direction of DIR */
    public static char rotateCounterclockwiseDirection(char dir) {
        switch (dir) {
            case 'U':
                return 'L';
            case 'D':
                return 'R';
            case 'R':
                return 'U';
            case 'L':
                return 'D';
            default:
                return 'N';
        }
    }

    /** private helper methods */

    /** create a food particle in the grid that is not in the buffer zone
     *  and is not a snake
     *  !!! assumes that a valid point exists (otherwise will loop forever) */
    private void generateNewFood() {
        food.x = RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER;
        food.y = RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER;

        if (isValid(food.x, food.y)) {
            grid[food.x][food.y] = FOOD_POINT;
        } else {
            generateNewFood();
        }
    }
}
