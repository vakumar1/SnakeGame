package com.github.vakumar1.SnakeGame;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import java.util.ArrayList;
import java.util.List;

public class ParameterEvaluator {

    public static int testGame(int minInd, double rootParameter) {
        GridGenerator testGenerator = new GridGenerator(minInd, rootParameter);
        AutonomousPlayer aplayer = new AutonomousPlayer(testGenerator);
        while (testGenerator.updateSnake()) {
            aplayer.smartUpdateDirection();
        }
        return testGenerator.getScore();
    }

    public static void main(String[] args) {
        List<Integer> minInds = new ArrayList<Integer>();
        minInds.add(5);
        minInds.add(10);
        //minInds.add(7);
        //minInds.add(8);
        List<Double> rootParams = new ArrayList<Double>();
        rootParams.add(1.5);
        rootParams.add(2.);
        rootParams.add(3.);
        // rootParams.add(2.);
        for (int ind: minInds) {
            for (double param: rootParams) {
                int cumSum = 0;
                int minVal = 99999;
                int maxVal = 0;
                for (int i = 0; i < 25; i += 1) {
                    int score = testGame(ind, param);
                    // System.out.println(i + " " + score);
                    minVal = Math.min(minVal, score);
                    maxVal = Math.max(maxVal, score);
                    cumSum += testGame(ind, param);
                }
                double average = cumSum / 25.;
                System.out.println(ind + ", " + param + ": " + "Average: " + average + " Minimum: " + minVal + " Maximum: " + maxVal);

            }
            System.out.println();
        }
    }
}
