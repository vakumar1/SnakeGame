package com.github.vakumar1.snakegame;

public class AutonomousPlayerRunner {
    /** The ParameterEvaluator class's main method quickly produces aggregate statistics on
     * the results of several games
     * */

    public static int NUM_TESTS = 25;

    public static int testGame() {
        GridGenerator testGenerator = new GridGenerator();
        AutonomousPlayer aplayer = new AutonomousPlayer(testGenerator);
        testGenerator.updateDirection('U');
        while (testGenerator.updateSnake()) {
            aplayer.greedyUpdateDirection();
        }
        return testGenerator.getScore();
    }

    public static void main(String[] args) {
        int cumSum = 0;
        int minVal = 99999;
        int maxVal = 0;
        for (int i = 0; i < NUM_TESTS; i += 1) {
            int score = testGame();
            minVal = Math.min(minVal, score);
            maxVal = Math.max(maxVal, score);
            cumSum += score;
        }
        double average = cumSum / (double) NUM_TESTS;
        System.out.println("Average: " + average + " Minimum: " + minVal + " Maximum: " + maxVal);
    }
}
