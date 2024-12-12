package com.adventofcode.utils;

import java.util.ArrayList;
import java.util.List;

public class TupleUtils {
  public static List<Tuple<Integer>> getNeighbours(Tuple<Integer> currentPoint) {
    return new ArrayList<>(List.of(
        new Tuple<>(currentPoint.elem1(), currentPoint.elem2() + 1),
        new Tuple<>(currentPoint.elem1(), currentPoint.elem2() - 1),
        new Tuple<>(currentPoint.elem1() + 1, currentPoint.elem2()),
        new Tuple<>(currentPoint.elem1() - 1, currentPoint.elem2())
    ));
  }

  public static boolean coordinateIsInBounds(Tuple<Integer> currentPoint, int width, int height) {
    return currentPoint.elem1() >= 0
        && currentPoint.elem1() < width
        && currentPoint.elem2() >= 0
        && currentPoint.elem2() < height;

  }
}
