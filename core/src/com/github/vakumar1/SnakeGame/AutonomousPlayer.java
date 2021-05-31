package com.github.vakumar1.SnakeGame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AutonomousPlayer {
    private GridGenerator generator;

    public AutonomousPlayer(GridGenerator generator) {
        this.generator = generator;
    }

    public void smartUpdateDirection() {
        char currDir = generator.getDirection();
        Map<Character, Double> dirMap = neighborExpectations();
        char[] sortedDirs = getSortedDirections(dirMap);
        /*
        for (char c: sortedDirs) {
            System.out.print(c + ": " + dirMap.get(c) + " ");
        }
        System.out.println(generator.isLoopAbove() + " " + generator.isLoopRight() + " " +
                generator.isLoopBelow() + " " + generator.isLoopLeft());

         */
        if (currDir == 'U' || currDir == 'D') {
            for (char dir : sortedDirs) {
                if (dir == currDir) {
                    return;
                } else if (dir == 'R' || dir == 'L') {
                    generator.updateDirection(dir);
                    return;
                }
            }
        } else {
            for (char dir : sortedDirs) {
                if (dir == currDir) {
                    return;
                } else if (dir == 'U' || dir == 'D') {
                    generator.updateDirection(dir);
                    return;
                }
            }
        }
    }

    /** smart update helper methods */
    private Map<Character, Double> neighborExpectations() {
        Map<Character, Double> dirMap = new HashMap<Character, Double>();
        for (char dir: GridGenerator.DIRECTIONS) {
            Point next = GridGenerator.getNextPoint(generator.getHeadX(), generator.getHeadY(), dir);
            dirMap.put(dir, pointExpectation(next.x, next.y));
        }
        return dirMap;
    }

    private char[] getSortedDirections(Map<Character, Double> dirMap) {
        char[] sortedDirs = new char[4];
        for (int i = 0; i < 4; i += 1) {
            char currChar = GridGenerator.DIRECTIONS.get(i);
            sortedDirs[i] = currChar;
            int j = i;
            while (j > 0 && dirMap.get(currChar) < dirMap.get(sortedDirs[j - 1])) {
                sortedDirs[j] = sortedDirs[j - 1];
                sortedDirs[j - 1] = currChar;
                j -= 1;
            }
        }
        return sortedDirs;
    }

    private double pointExpectation(int x, int y) {
        if (!generator.isValid(x, y)) {
            return 99999.;
        } else if (badNeighborCount(x, y) == 4) {
            return 99998.;
        } else if (itsATrap(x, y)) {
            return 99997.;
        }
        return Math.sqrt(Math.pow(x - generator.getFoodX(), 2) + Math.pow(y - generator.getFoodY(), 2));
        /*
        if (!generator.isValid(x, y)) {
            return 99999.;
        } else if (badNeighbors(x, y)) {
            return 99998.;
        } else if (itsATrap(x, y)){
            return 99997.;
        } else {
            return Math.pow(x - generator.getFoodX(), 2) + Math.pow(y - generator.getFoodY(), 2);
        }
         */
    }

    private int badNeighborsMultiplier(int x, int y) {
        int badCount = 1;
        for (char dir: GridGenerator.DIRECTIONS) {
            Point next = GridGenerator.getNextPoint(x, y, dir);
            if (!generator.isValid(next.x, next.y)) {
                badCount *= 2;
            }
        }
        return badCount;
    }

    private int badNeighborCount(int x, int y) {
        int badCount = 0;
        for (char dir: GridGenerator.DIRECTIONS) {
            Point next = GridGenerator.getNextPoint(x, y, dir);
            if (!generator.isValid(next.x, next.y)) {
                badCount += 1;
            }
        }
        return badCount;
    }

    private boolean itsATrap(int x, int y) {
        int xDiff = x - generator.getHeadX();
        int yDiff = y - generator.getHeadY();
        if (Math.abs(yDiff) >= Math.abs(xDiff)) {
            if (yDiff > 0 && generator.isLoopAbove()) {
                return true;
            }
            if (yDiff < 0 && generator.isLoopBelow()) {
                return true;
            }
        }
        if (Math.abs(yDiff) <= Math.abs(xDiff)) {
            if (xDiff > 0 && generator.isLoopRight()) {
                return true;
            }
            if (xDiff < 0 && generator.isLoopLeft()) {
                return true;
            }
        }
        return false;
    }

    /** simple update method + helper*/
    public void stupidUpdateDirection() {
        char currDir = generator.getDirection();
        int hx = generator.getHeadX();
        int hy = generator.getHeadY();
        int isAbove = hy - generator.getFoodY();
        int isRightOf = hx - generator.getFoodX();
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
    }
    private boolean turnRelativeLeft() {
        int midX;
        int midY;
        Point[] orderedClockwise;
        switch (generator.getDirection()) {
            case 'U':
                midX = generator.getHeadX();
                midY = generator.getHeadY() + 1;
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
                midX = generator.getHeadX();
                midY = generator.getHeadY() - 1;
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
                midX = generator.getHeadX() + 1;
                midY = generator.getHeadY();
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
                midX = generator.getHeadX() - 1;
                midY = generator.getHeadY();
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
            if (generator.snakeHasPoint(p)) {
                firstLeftmost = p;
                break;
            }
        }

        if (firstLeftmost == null) {
            return true;
        }
        return generator.getSnakeIndexOfPoint(firstLeftmost) - generator.getSnakeIndexOfPoint(new Point(midX, midY)) > 0;
    }
}
