package com.github.vakumar1.SnakeGame;

import java.awt.*;
import java.util.*;
import java.util.List;

public class tests {
    public static void testCinch() {
        Point p = GridGenerator.getNextPoint(20, 30, 'L');
        System.out.println(p.x + " " + p.y);
        /*
        LinkedList<Point> testSnakeList = new LinkedList<Point>();
        Point head = new Point(2, 1);
        testSnakeList.add(head);
        testSnakeList.add(new Point(2, 2));
        testSnakeList.add(new Point(2, 3));
        testSnakeList.add(new Point(1, 3));
        testSnakeList.add(new Point(0, 3));
        testSnakeList.add(new Point(0, 2));
        testSnakeList.add(new Point(0, 1));
        testSnakeList.add(new Point(0, 0));
        testSnakeList.add(new Point(1, 0));
        testSnakeList.add(new Point(2, 0));
        testSnakeList.add(new Point(2, 0));

         */
    }
    public static void main(String[] args) {
        testCinch();
    }
}
