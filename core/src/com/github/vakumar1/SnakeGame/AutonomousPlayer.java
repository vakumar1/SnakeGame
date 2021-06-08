package com.github.vakumar1.snakegame;

import sun.awt.image.ImageWatched;

import java.awt.Point;
import java.util.*;

import static com.github.vakumar1.snakegame.GridGenerator.GRID_HEIGHT;
import static com.github.vakumar1.snakegame.GridGenerator.GRID_WIDTH;

public class AutonomousPlayer {
    private GridGenerator generator;
    private LinkedList<Point> path;

    public AutonomousPlayer(GridGenerator generator) {
        this.generator = generator;
        this.path = new LinkedList<>();
    }

    public void pathfinderUpdateDirection() {
        if (path.size() == 0) {
            path = getAStarPath();
            if (path == null) {
                path = new LinkedList<>();
                smartUpdateDirection();
                return;
            }
        }
        Point next = path.removeFirst();
        char nextDir = GridGenerator.getNextDirection(generator.getHeadX(), generator.getHeadY(), next.x, next.y);
        generator.updateDirection(nextDir);
    }

    /** pathfinder helper functions */

    private LinkedList<Point> getAStarPath() {
        LinkedList<Point> path = new LinkedList<>();
        boolean[][] marked = new boolean[GRID_WIDTH][GRID_HEIGHT];
        double[][] startDistances = new double[GRID_WIDTH][GRID_WIDTH];
        double[][] endDistances = new double[GRID_WIDTH][GRID_WIDTH];
        int[][][] previous = new int[GRID_WIDTH][GRID_WIDTH][2];
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                marked[x][y] = false;
            }
        }
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                startDistances[x][y] = 99999;
            }
        }
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                endDistances[x][y] = 99999;
            }
        }
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                previous[x][y] = new int[]{-1, -1};
            }
        }

        int[] start = new int[]{generator.getHeadX(), generator.getHeadY()};
        int[] end = new int[]{generator.getFoodX(), generator.getFoodY()};
        int[] curr;
        startDistances[start[0]][start[1]] = 0;
        endDistances[start[0]][start[1]] = heuristic(start, end);
        curr = start;
        boolean reached = false;
        while (!reached && curr != null) {
            System.out.println(curr[0] + " " + curr[1]);
            if (Arrays.equals(curr, end)) {
                reached = true;
                break;
            }
            marked[curr[0]][curr[1]] = true;
            for (int[] n: neighbors(curr)) {
                if (Arrays.equals(n, end)) {
                    reached = true;
                    break;
                }
                astarIteration(n, curr, end, marked, startDistances, endDistances, previous);
            }
            curr = getMinPoint(endDistances, marked);
        }

        if (curr == null) {
            return null;
        }

        boolean backToStart = false;
        while (!backToStart) {
            backToStart = pathIteration(curr, start, path);
            curr = previous[curr[0]][curr[1]];
        }
        path.addLast(new Point(end[0], end[1]));
        return path;
    }

    private void astarIteration(int[] n, int[] curr, int[] end, boolean[][] marked, double[][] startDistances, double[][] endDistances, int[][][] previous) {
        double startDist = startDistances[curr[0]][curr[1]] + 1;
        double endDist = heuristic(n, end);
        if (!marked[n[0]][n[1]] && startDist < startDistances[n[0]][n[1]]) {
            startDistances[n[0]][n[1]] = startDist;
            endDistances[n[0]][n[1]] = endDist;
            previous[n[0]][n[1]] = curr.clone();
        }
    }

    public boolean pathIteration(int[] curr, int[] start, LinkedList<Point> path) {
        if (!generator.isValid(curr[0], curr[1]) || Arrays.equals(curr, start)) {
            return true;
        }
        path.addFirst(new Point(curr[0], curr[1]));
        return false;
    }

    private List<int[]> neighbors(int[] curr) {
        int x = curr[0];
        int y = curr[1];
        List<int[]> neighbors = new ArrayList<int[]>();

        int[][] points = new int[][]{
                new int[]{x, y + 1},
                new int[]{x + 1, y},
                new int[]{x, y - 1},
                new int[]{x - 1, y}
        };

        for (int[] point: points) {
            if (generator.isValid(point[0], point[1])) {
                neighbors.add(point);
            }
        }
        return neighbors;
    }

    private double heuristic(int[] p1, int[] p2) {
        int x1 = p1[0];
        int y1 = p1[1];
        int x2 = p2[0];
        int y2 = p2[1];
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private int[] getMinPoint(double[][] endDistances, boolean[][] marked) {
        int[] minPoint = null;
        double minDist = 99999;
        for (int x = 0; x < GRID_WIDTH; x += 1) {
            for (int y = 0; y < GRID_HEIGHT; y += 1) {
                if (!marked[x][y] && endDistances[x][y] < minDist) {
                    minPoint = new int[]{x, y};
                    minDist = endDistances[x][y];
                }
            }
        }
        return minPoint;
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
