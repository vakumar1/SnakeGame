package com.github.vakumar1.snake_game;

import java.awt.*;

public class AutonomousPlayer {
    private GridGenerator generator;

    public AutonomousPlayer(GridGenerator generator) {
        this.generator = generator;
    }

    public void updateDirection() {
        char currDir = generator.getDirection();
        int hx = generator.head.x;
        int hy = generator.head.y;
        int isAbove = hy - generator.food.y;
        int isRightOf = hx - generator.food.x;
        if (currDir == 'U' || currDir == 'D') {
            if (isRightOf > 0) {
                if (generator.isValid(hx - 1, hy)) {
                    generator.updateDirection('L');
                } else {
                    if (generator.isValid(hx - 1, hy) && generator.isValid(hx + 1, hy)) {
                        if (turnRelativeLeft()) {
                            if (currDir == 'U') {
                                generator.updateDirection('R');
                            } else {
                                generator.updateDirection('L');
                            }
                        } else {
                            if (currDir == 'U') {
                                generator.updateDirection('L');
                            } else {
                                generator.updateDirection('R');
                            }
                        }
                    } else if (generator.isValid(hx - 1, hy)) {
                        generator.updateDirection('L');
                    } else if (generator.isValid(hx + 1, hy)) {
                        generator.updateDirection('R');
                    }
                }
            } else if (isRightOf < 0) {
                if (generator.isValid(hx + 1, hy)) {
                    generator.updateDirection('R');
                } else {
                    if (generator.isValid(hx - 1, hy) && generator.isValid(hx + 1, hy)) {
                        if (turnRelativeLeft()) {
                            if (currDir == 'U') {
                                generator.updateDirection('R');
                            } else {
                                generator.updateDirection('L');
                            }
                        } else {
                            if (currDir == 'U') {
                                generator.updateDirection('L');
                            } else {
                                generator.updateDirection('R');
                            }
                        }
                    } else if (generator.isValid(hx - 1, hy)) {
                        generator.updateDirection('L');
                    } else if (generator.isValid(hx + 1, hy)) {
                        generator.updateDirection('R');
                    }
                }
            } else {
                if ((currDir == 'U') && (isAbove > 0 || !generator.isValid(hx, hy + 1)) ||
                        (currDir == 'D') && (isAbove < 0 || !generator.isValid(hx, hy - 1))) {
                    if (generator.isValid(hx - 1, hy) && generator.isValid(hx + 1, hy)) {
                        if (turnRelativeLeft()) {
                            if (currDir == 'U') {
                                generator.updateDirection('R');
                            } else {
                                generator.updateDirection('L');
                            }
                        } else {
                            if (currDir == 'U') {
                                generator.updateDirection('L');
                            } else {
                                generator.updateDirection('R');
                            }
                        }
                    } else if (generator.isValid(hx - 1, hy)) {
                        generator.updateDirection('L');
                    } else if (generator.isValid(hx + 1, hy)) {
                        generator.updateDirection('R');
                    }
                }
            }
        } else {
            if (isAbove > 0) {
                if (generator.isValid(hx, hy - 1)) {
                    generator.updateDirection('D');
                } else {
                    if (generator.isValid(hx, hy + 1) && generator.isValid(hx, hy - 1)) {
                        if (turnRelativeLeft()) {
                            if (currDir == 'R') {
                                generator.updateDirection('U');
                            } else {
                                generator.updateDirection('D');
                            }
                        } else {
                            if (currDir == 'R') {
                                generator.updateDirection('D');
                            } else {
                                generator.updateDirection('U');
                            }
                        }
                    } else if (generator.isValid(hx, hy + 1)) {
                        generator.updateDirection('U');
                    } else if (generator.isValid(hx, hy - 1)) {
                        generator.updateDirection('D');
                    }
                }
            } else if (isAbove < 0) {
                if (generator.isValid(hx, hy + 1)) {
                    generator.updateDirection('U');
                } else {
                    if (generator.isValid(hx, hy + 1) && generator.isValid(hx, hy - 1)) {
                        if (turnRelativeLeft()) {
                            if (currDir == 'R') {
                                generator.updateDirection('U');
                            } else {
                                generator.updateDirection('D');
                            }
                        } else {
                            if (currDir == 'R') {
                                generator.updateDirection('D');
                            } else {
                                generator.updateDirection('U');
                            }
                        }
                    } else if (generator.isValid(hx, hy + 1)) {
                        generator.updateDirection('U');
                    } else if (generator.isValid(hx, hy - 1)) {
                        generator.updateDirection('D');
                    }
                }
            } else {
                if ((currDir == 'R') && (isRightOf > 0 || !generator.isValid(hx + 1, hy)) ||
                        (currDir == 'L') && (isRightOf < 0 || !generator.isValid(hx - 1, hy))) {
                    if (generator.isValid(hx, hy + 1) && generator.isValid(hx, hy - 1)) {
                        if (turnRelativeLeft()) {
                            if (currDir == 'R') {
                                generator.updateDirection('U');
                            } else {
                                generator.updateDirection('D');
                            }
                        } else {
                            if (currDir == 'L') {
                                generator.updateDirection('D');
                            } else {
                                generator.updateDirection('U');
                            }
                        }
                    } else if (generator.isValid(hx, hy + 1)) {
                        generator.updateDirection('U');
                    } else if (generator.isValid(hx, hy - 1)) {
                        generator.updateDirection('D');
                    }
                }
            }
        }





            /*
        {
            if (isRightOf > 0) {
                if (generator.isValid(hx - 1, hy)) {
                    generator.updateDirection('L');
                } else if ((currDir == 'U' && !generator.isValid(hx, hy + 1))
                        || (currDir == 'D' && !generator.isValid(hx, hy - 1))) {
                    generator.updateDirection('R');
                }
            } else if (isRightOf < 0 || (currDir == 'U' && isAbove > 0) || (currDir == 'D' && isAbove < 0)) {
                if (generator.grid[generator.head.x + 1][generator.head.y] != GameScreen.SNAKE_CELL) {
                    generator.updateDirection('R');
                } else if ((currDir == 'U' && generator.grid[generator.head.x][generator.head.y + 1] == GameScreen.SNAKE_CELL)
                        || (currDir == 'D' && generator.grid[generator.head.x][generator.head.y - 1] == GameScreen.SNAKE_CELL)) {
                    generator.updateDirection('L');
                }
            }
        } else {
            if (isAbove > 0) {
                if (generator.grid[generator.head.x][generator.head.y - 1] != GameScreen.SNAKE_CELL) {
                    generator.updateDirection('D');
                } else if ((currDir == 'R' && generator.grid[generator.head.x + 1][generator.head.y] == GameScreen.SNAKE_CELL)
                        || (currDir == 'L' && generator.grid[generator.head.x - 1][generator.head.y] == GameScreen.SNAKE_CELL)) {
                    generator.updateDirection('U');
                }
            } else if (isAbove < 0 || (currDir == 'R' && isRightOf > 0) || (currDir == 'L' && isRightOf < 0)) {
                if (generator.grid[generator.head.x][generator.head.y + 1] != GameScreen.SNAKE_CELL) {
                    generator.updateDirection('U');
                } else if ((currDir == 'R' && generator.grid[generator.head.x + 1][generator.head.y] == GameScreen.SNAKE_CELL)
                        || (currDir == 'L' && generator.grid[generator.head.x - 1][generator.head.y] == GameScreen.SNAKE_CELL)) {
                    generator.updateDirection('D');
                }
            }
        }


        if (currDir == 'U' || currDir == 'D') {
            if (generator.food.x < generator.head.x) {
                if (generator.grid[generator.head.x - 1][generator.head.y] != GameScreen.SNAKE_CELL) {
                    generator.updateDirection('L');
                } else if (generator.grid[generator.head.x][generator.head.y + 1] == GameScreen.SNAKE_CELL) {
                    generator.updateDirection('R');
                }
            } else if (generator.food.x > generator.head.x && generator.grid[generator.head.x + 1][generator.head.y] != GameScreen.SNAKE_CELL) {
                generator.updateDirection('R');
            } else if (generator.food.y != generator.head.y) {
                generator.updateDirection('R');
            }
        } else {
            if (generator.food.y < generator.head.y && generator.grid[generator.head.x][generator.head.y - 1] != GameScreen.SNAKE_CELL) {
                generator.updateDirection('D');
            } else if (generator.food.y > generator.head.y && generator.grid[generator.head.x][generator.head.y + 1] != GameScreen.SNAKE_CELL) {
                generator.updateDirection('U');
            } else if (generator.food.x != generator.head.x) {
                generator.updateDirection('U');
            }
        }

          */
    }

    private boolean turnRelativeLeft() {
        int midX;
        int midY;
        Point[] orderedClockwise;
        switch (generator.getDirection()) {
            case 'U':
                midX = generator.head.x;
                midY = generator.head.y + 1;
                orderedClockwise = new Point[]{
                        new Point(midX - 1, midY - 1),
                        new Point(midX - 1, midY),
                        new Point(midX - 1, midY + 1),
                        new Point(midX, midY + 1),
                        new Point(midX + 1, midY + 1),
                        new Point(midX + 1, midY),
                        new Point(midX + 1, midY - 1)
                };
                break;
            case 'D':
                midX = generator.head.x;
                midY = generator.head.y - 1;
                orderedClockwise = new Point[]{
                        new Point(midX + 1, midY + 1),
                        new Point(midX + 1, midY),
                        new Point(midX + 1, midY - 1),
                        new Point(midX, midY + 1),
                        new Point(midX - 1, midY - 1),
                        new Point(midX - 1, midY),
                        new Point(midX - 1, midY + 1),
                };
                break;
            case 'R':
                midX = generator.head.x + 1;
                midY = generator.head.y;
                orderedClockwise = new Point[]{
                        new Point(midX - 1, midY + 1),
                        new Point(midX, midY + 1),
                        new Point(midX + 1, midY + 1),
                        new Point(midX + 1, midY),
                        new Point(midX + 1, midY - 1),
                        new Point(midX, midY - 1),
                        new Point(midX - 1, midY - 1),
                };
                break;
            default:
                midX = generator.head.x - 1;
                midY = generator.head.y;
                orderedClockwise = new Point[]{
                        new Point(midX + 1, midY - 1),
                        new Point(midX, midY - 1),
                        new Point(midX - 1, midY - 1),
                        new Point(midX - 1, midY),
                        new Point(midX - 1, midY + 1),
                        new Point(midX, midY + 1),
                        new Point(midX + 1, midY + 1),
                };
                break;
        }

        Point firstLeftmost = null;
        for (Point p: orderedClockwise) {
            if (generator.snakeList.contains(p)) {
                firstLeftmost = p;
                break;
            }
        }

        if (firstLeftmost == null) {
            return true;
        }
        return generator.snakeList.indexOf(firstLeftmost) - generator.snakeList.indexOf(new Point(midX, midY)) > 0;
    }

    /*
    public boolean turnRelativeLeft() {
        boolean leftIsNotCycle = true;
        Point front;
        switch (generator.getDirection()) {
            case 'U':
                if (generator.firstIsCloserToHead(new Point(generator.head.x - 1, generator.head.y + 1),
                    new Point(generator.head.x, generator.head.y + 1))) {
                    leftIsNotCycle = false;
                }
                break;
            case 'D':
                if (generator.firstIsCloserToHead(new Point(generator.head.x + 1, generator.head.y - 1),
                        new Point(generator.head.x, generator.head.y - 1))) {
                    leftIsNotCycle = false;
                }
                break;
            case 'R':
                if (generator.firstIsCloserToHead(new Point(generator.head.x + 1, generator.head.y + 1),
                        new Point(generator.head.x + 1, generator.head.y))) {
                    leftIsNotCycle = false;
                }
                break;
            case 'L':
                if (generator.firstIsCloserToHead(new Point(generator.head.x - 1, generator.head.y - 1),
                        new Point(generator.head.x - 1, generator.head.y))) {
                    leftIsNotCycle = false;
                }
                break;
        }
        return leftIsNotCycle;
    }
     */
}
