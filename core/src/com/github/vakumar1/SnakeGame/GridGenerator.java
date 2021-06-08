package com.github.vakumar1.snakegame;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GridGenerator {
    public final static int GRID_WIDTH = 80;
    public final static int GRID_HEIGHT = 40;
    public final static int BUFFER = 5;

    public static final int NOTHING_POINT = 0;
    public static final int SNAKE_POINT = 1;
    public static final int FOOD_POINT = 2;

    public final static Random RANDOM = new Random();
    public final static List<Character> DIRECTIONS = new LinkedList<Character>(){
        {
            add('U');
            add('D');
            add('R');
            add('L');
        }
    };

    public int[][] grid;
    private LinkedList<Point> snakeList;
    private Point head;
    private Point tail;
    private Point food;

    private int cinch;
    private int minCinchIndex;
    private double cinchDivisor;

    private boolean loopIsAbove;
    private boolean loopIsBelow;
    private boolean loopIsRight;
    private boolean loopIsLeft;

    private char direction;
    private int score;

    public GridGenerator(int minCinchIndex, double cinchDivisor) {
        grid = new int[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                grid[x][y] = NOTHING_POINT;
            }
        }

        snakeList = new LinkedList<>();
        head = new Point();
        snakeList.add(head);
        setHeadX(RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER);
        setHeadY(RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER);
        tail = head;
        food = new Point();
        generateNewFood();

        cinch = 0;
        this.minCinchIndex = minCinchIndex;
        this.cinchDivisor = cinchDivisor;

        loopIsAbove = false;
        loopIsBelow = false;
        loopIsRight = false;
        loopIsLeft = false;

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

    public boolean isLoopAbove() { return loopIsAbove; }

    public boolean isLoopBelow() { return loopIsBelow; }

    public boolean isLoopRight() { return loopIsRight; }

    public boolean isLoopLeft() { return loopIsLeft; }

    public int getScore() { return score; }

    public char getDirection() { return direction; }

    /** state update methods */
    public void updateDirection(char newDirection) {
        if (!DIRECTIONS.contains(newDirection)) {
            return;
        }
        if (newDirection == getOppositeDirection(direction)) {
            return;
        }
        direction = newDirection;
    }

    public boolean updateSnake() {
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

        if (nextHead.equals(head)) {
            return true;
        }
        if (isValid(nextHead.x, nextHead.y)) {
            head = nextHead;
            snakeList.addFirst(head);
            grid[getHeadX()][getHeadY()] = SNAKE_POINT;
            if (!head.equals(food)) {
                tail = snakeList.removeLast();
                grid[getTailX()][getTailY()] = NOTHING_POINT;
            } else {
                score += 1;
                generateNewFood();
            }
            setCinch();
            setLoopLocation();
            return true;
        }
        return false;
    }

    /** public helper methods */
    public boolean isValid(int x, int y) {
        if (x < 0 || x >= GRID_WIDTH || y < 0 || y >= GRID_HEIGHT) {
            return false;
        }
        return grid[x][y] != SNAKE_POINT;
    }

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

    public static char getNextDirection(int currX, int currY, int nextX, int nextY) {
        int xDiff = nextX - currX;
        int yDiff = nextY - currY;
        if (xDiff == 0 && yDiff == 1) {
            return 'U';
        } else if (xDiff == 0 && yDiff == -1) {
            return 'D';
        } else if (xDiff == 1 && yDiff == 0) {
            return 'R';
        } else if (xDiff == -1 && yDiff == 0) {
            return 'L';
        } else {
            return 'N';
        }
    }

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
    private void generateNewFood() {
        food.x = RANDOM.nextInt(GRID_WIDTH - 2 * BUFFER) + BUFFER;
        food.y = RANDOM.nextInt(GRID_HEIGHT - 2 * BUFFER) + BUFFER;

        if (isValid(food.x, food.y)) {
            grid[food.x][food.y] = FOOD_POINT;
        } else {
            generateNewFood();
        }
    }

    private void setCinch() {
        if (snakeList.size() == 1) {
            return;
        }
        int minInd = 0;
        double minDistSqrd = 99999;
        for (int i = minCinchIndex; i < snakeList.size(); i += 1) {
            Point curr = snakeList.get(i);
            double pointDistSqrd = Math.pow(getHeadX() - curr.x, 2) + Math.pow(getHeadY() - curr.y, 2);
            if (pointDistSqrd <= minDistSqrd) {
                minInd = i;
                minDistSqrd = pointDistSqrd;
            }
        }

        double maxNormalDist = minInd / cinchDivisor;
        if (minDistSqrd < maxNormalDist) {
            cinch = minInd;
        } else {
            cinch = 0;
        }
    }

    private void setLoopLocation() {
        if (cinch < minCinchIndex) {
            loopIsAbove = false;
            loopIsBelow = false;
            loopIsRight = false;
            loopIsLeft = false;
        } else {
            Point minXPoint = head;
            int minXInd = 0;
            for (int i = 0; i <= cinch; i += 1) {
                Point curr = snakeList.get(i);
                if (curr.x < minXPoint.x || (curr.x == minXPoint.x && curr.y < minXPoint.y)) {
                    minXPoint = curr;
                    minXInd = i;
                }
            }
            int beforeInd = minXInd - 1;
            int afterInd = minXInd + 1;
            if (beforeInd < 0) {
                beforeInd = cinch;
            }
            if (afterInd > cinch) {
                afterInd = 0;
            }
            Point beforeXPoint = snakeList.get(beforeInd);
            Point afterXPoint = snakeList.get(afterInd);
            int det = (minXPoint.x - beforeXPoint.x) * (afterXPoint.y - beforeXPoint.y) -
                    (afterXPoint.x - beforeXPoint.x) * (minXPoint.y - beforeXPoint.y);

            Point cinchPoint = snakeList.get(cinch);
            int xDiff = cinchPoint.x - getHeadX();
            int yDiff = cinchPoint.y - getHeadY();
            if (det < 0) {
                loopIsAbove = xDiff > 0;
                loopIsBelow = xDiff < 0;
                loopIsRight = yDiff < 0;
                loopIsLeft = yDiff > 0;
            } else if (det > 0) {
                loopIsAbove = xDiff < 0;
                loopIsBelow = xDiff > 0;
                loopIsRight = yDiff > 0;
                loopIsLeft = yDiff < 0;
            }
        }
    }
}
