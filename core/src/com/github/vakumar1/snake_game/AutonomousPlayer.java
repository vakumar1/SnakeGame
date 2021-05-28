package com.github.vakumar1.snake_game;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AutonomousPlayer {
    private GridGenerator generator;

    public AutonomousPlayer(GridGenerator generator) {
        this.generator = generator;
    }

    public void simpleUpdateDirection() {
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
    }

    public void smartUpdateDirection() {
        char currDir = generator.getDirection();
        Map<Character, Double> dirMap = neighborExpectations();
        char[] sortedDirs = getSortedDirections(dirMap);
        switch (currDir) {
            case 'U':
                for (char dir : sortedDirs) {
                    if (dir == 'U') {
                        return;
                    } else if (dir == 'R' || dir == 'L') {
                        generator.updateDirection(dir);
                        return;
                    }
                }
                break;
            case 'D':
                for (char dir : sortedDirs) {
                    if (dir == 'D') {
                        return;
                    } else if (dir == 'R' || dir == 'L') {
                        generator.updateDirection(dir);
                        return;
                    }
                }
                break;
            case 'R':
                for (char dir : sortedDirs) {
                    if (dir == 'R') {
                        return;
                    } else if (dir == 'U' || dir == 'D') {
                        generator.updateDirection(dir);
                        return;
                    }
                }
                break;
            case 'L':
                for (char dir : sortedDirs) {
                    if (dir == 'L') {
                        return;
                    } else if (dir == 'U' || dir == 'D') {
                        generator.updateDirection(dir);
                        return;
                    }
                }
                break;
            default:
                for (char dir : sortedDirs) {
                    generator.updateDirection(dir);
                }
        }
    }

    /** dumb update helper method */
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

    /** smart update helper methods */
    private Map<Character, Double> neighborExpectations() {
        Map<Character, Double> dirMap = new HashMap<Character, Double>();
        int hx = generator.head.x;
        int hy = generator.head.y;
        Point[] neighbors = new Point[]{
                        new Point(hx, hy + 1),
                        new Point(hx, hy - 1),
                        new Point(hx + 1, hy),
                        new Point(hx - 1, hy)
                };
        for (int i = 0; i < 4; i += 1) {
            dirMap.put(GridGenerator.directions.get(i), pointExpectation(neighbors[i].x, neighbors[i].y));
        }
        return dirMap;
    }

    private char[] getSortedDirections(Map<Character, Double> dirMap) {
        char[] sortedDirs = new char[4];
        for (int i = 0; i < 4; i += 1) {
            char currChar = GridGenerator.directions.get(i);
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
        }
        double rawDist = Math.sqrt(Math.pow(x - generator.food.x, 2) + Math.pow(y - generator.food.y, 2));
        return rawDist * badNeighborMultiplier(x, y);
    }

    private int badNeighborMultiplier(int x, int y) {
        Point[] neighbors = new Point[]{
                new Point(x, y + 1),
                new Point(x, y - 1),
                new Point(x + 1, y),
                new Point(x - 1, y)
        };
        int badMultiplier = 1;
        for (Point n: neighbors) {
            if (!generator.isValid(n.x, n.y) && (!n.equals(generator.head))) {
                badMultiplier *= 2;
            }
        }
        return badMultiplier;
    }

    // private boolean itsATrap(Point )
}
