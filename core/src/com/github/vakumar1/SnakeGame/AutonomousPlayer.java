package com.github.vakumar1.snakegame;

import java.awt.Point;
import java.util.*;
import static com.github.vakumar1.snakegame.GridGenerator.*;

public class AutonomousPlayer {
    /**
     * The Autonomous Player Class provides a recommended update to the snake's direction (like a player pressing a direction key) using:
     *      (i) a general greedy approach (i.e., only considering the neighboring points)
     *      (ii) a loop-checker (to see if a direction would move the snake into a loop that it cannot escape)
     * */
    private final static double INVALID_POINT_HEUR = 99999.;
    private final static double SURROUNDED_POINT_HEUR = 99998.;
    private final static double TRAP_POINT_HEUR = 99997.;
    private GridGenerator generator;

    public AutonomousPlayer(GridGenerator generator) {
        this.generator = generator;
    }

    /** public update method: updates generator's current direction */
    public void greedyUpdateDirection() {
        Map<Character, Double> dirMap = directionExpectations();
        char[] sortedDirs = getSortedDirections(dirMap);
        generator.updateDirection(sortedDirs[0]);
    }

    /** greedy update helper methods */

    /**
     * returns a map of the "scores" of each potential direction change, represented as:
     *  N (north): move in current direction
     *  W (west): turn counter-clockwise
     *  E (east): turn clockwise
     */
    private Map<Character, Double> directionExpectations() {
        Map<Character, Double> dirMap = new HashMap<>();
        char NDir = generator.getDirection();
        char WDir = rotateCounterclockwiseDirection(generator.getDirection());
        char EDir = rotateClockwiseDirection(generator.getDirection());

        /* 5 "frontal" points represented as cardinal directions
            from perspective of current direction: W, NW, N, NE, E*/
        Point N = getNextPoint(generator.getHeadX(), generator.getHeadY(), NDir);
        Point W = getNextPoint(generator.getHeadX(), generator.getHeadY(), WDir);
        Point E = getNextPoint(generator.getHeadX(), generator.getHeadY(), EDir);
        Point NW = getNextPoint(N.x, N.y, WDir);
        Point NE = getNextPoint(N.x, N.y, EDir);

        /* put point expectations for 3 potential direction changes */
        dirMap.put(NDir, pointExpectation(N.x, N.y));
        dirMap.put(WDir, pointExpectation(W.x, W.y));
        dirMap.put(EDir, pointExpectation(E.x, E.y));
        double NWScore = pointExpectation(NW.x, NW.y);
        double NEScore = pointExpectation(NE.x, NE.y);

        if (dirMap.get(NDir) == INVALID_POINT_HEUR) {
            /* if head-on collision, move away from the formed loop:
             * Example: (H = HEAD, T = trap square, N = NORTH, S = snake, . = empty space)
             *      . . . . . . . . . .
             *      S S S S N S S S S S
             *      S . . T H . . . . .
             *      S . . . S . . . . .
             *      S S S S S . . . . .
             */
            if (loopIsClockwise(N)) {
                dirMap.put(WDir, Math.max(TRAP_POINT_HEUR, dirMap.get(WDir)));
            } else {
                dirMap.put(EDir, Math.max(TRAP_POINT_HEUR, dirMap.get(EDir)));
            }
        } else if (NWScore == INVALID_POINT_HEUR && NEScore == INVALID_POINT_HEUR) {
            /* if "walking into a trap", do not continue forward:
             * if head-on collision, move away from the formed loop:
             * Example: (H = HEAD, T = trap square, S = snake, . = empty space)
             *      . . . . . . . . . .
             *      S S S S S S S S S S
             *      S S S S H T . . . S
             *      . . . . . S S S S S
             *
             */
            dirMap.put(NDir, Math.max(TRAP_POINT_HEUR, dirMap.get(NDir)));
        }
        return dirMap;
    }

    /** return an array of sorted directions
     * !!! insertion sort is unstable (assume dirMap.keySet() returns directions randomly) !!!
     */
    private char[] getSortedDirections(Map<Character, Double> dirMap) {
        List<Character> directions = new LinkedList<>(dirMap.keySet());
        char[] sortedDirs = new char[dirMap.size()];
        for (int i = 0; i < dirMap.size(); i += 1) {
            char currChar = directions.get(i);
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

    /** return the estimated "score" of a point on the grid */
    private double pointExpectation(int x, int y) {
        if (!generator.isValid(x, y)) {
            return INVALID_POINT_HEUR;
        } else if (badNeighborCount(x, y) == 4) {
            return SURROUNDED_POINT_HEUR;
        }

        /* default to Manhattan distance between HEAD and FOOD */
        return Math.abs(x - generator.getFoodX()) + Math.abs(y - generator.getFoodY());
    }

    /** return number of neighbors of point in grid that are invalid (snake or edge of grid) */
    private int badNeighborCount(int x, int y) {
        int badCount = 0;
        for (char dir: GridGenerator.DIRECTIONS) {
            Point next = getNextPoint(x, y, dir);
            if (!generator.isValid(next.x, next.y)) {
                badCount += 1;
            }
        }
        return badCount;
    }

    /** return true if the polygon formed by the snake segment from HEAD -> CINCH -> HEAD is
     *  oriented clockwise
     *
     *  uses Polygon Orientation algorithm: https://en.wikipedia.org/wiki/Curve_orientation
     *  to determine if the polygon formed by the segment of the snake from HEAD -> CINCH is oriented clockwise or counter-clockwise
     *  Example: (H = HEAD, C = CINCH, S = snake, . = empty space)
     *      . . . . . . . . . .
     *      S S S S C S S S S S
     *      S . . . H . . . . .
     *      S . . . S . . . . .
     *      S S S S S . . . . .
     *
     *  The segment from H -> C is clockwise (return true)
     */
    public boolean loopIsClockwise(Point cinch) {
        /* get the index of CINCH */
        int cinchInd = generator.getSnakeIndexOfPoint(cinch);
        if (cinchInd < 0) {
            return false;
        }

        /* return the leftmost-bottommost point in the segment from HEAD -> CINCH */
        int minXPointX = generator.getHeadX();
        int minXPointY = generator.getHeadY();
        int minXInd = 0;
        for (int i = 0; i <= cinchInd; i += 1) {
            Point curr = generator.getSnakePoint(i);
            if (curr.x < minXPointX || (curr.x == minXPointX && curr.y < minXPointY)) {
                minXPointX = curr.x;
                minXPointY = curr.y;
                minXInd = i;
            }
        }

        /* get the points immediately before and after the leftmost-bottommost point */
        int beforeInd = minXInd - 1;
        int afterInd = minXInd + 1;
        if (beforeInd < 0) {
            beforeInd = cinchInd;
        }
        if (afterInd > cinchInd) {
            afterInd = 0;
        }
        Point beforeXPoint = generator.getSnakePoint(beforeInd);
        Point afterXPoint = generator.getSnakePoint(afterInd);

        /* return true if the "determinant" (see algorithm) is less than 0 */
        int det = (minXPointX - beforeXPoint.x) * (afterXPoint.y - beforeXPoint.y) -
                (afterXPoint.x - beforeXPoint.x) * (minXPointY - beforeXPoint.y);
        return det < 0;
    }
}
