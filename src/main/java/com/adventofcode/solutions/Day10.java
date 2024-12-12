package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;
import com.adventofcode.utils.TupleUtils;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Day10 {
  public Day10() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day10.txt");
    List<List<Long>> map = inputToLongMap(input);
    HashSet<Tuple<Integer>> startingPoints = getStartingPoints(map);
    System.out.printf("Part one: %s%n", scoreTrailHeads(startingPoints, map, true));
    System.out.printf("Part two: %s%n", scoreTrailHeads(startingPoints, map, false));
  }

  private long scoreTrailHeads(HashSet<Tuple<Integer>> startingPoints, List<List<Long>> map,
                               boolean trackVisits) {
    HashMap<Tuple<Integer>, Long> trails = new HashMap<>();
    startingPoints.forEach(p -> trails.put(p, 0L));

    for (Tuple<Integer> startingPoint : startingPoints) {
      Queue<Tuple<Integer>> queue = new LinkedList<>(List.of(startingPoint));
      Set<Tuple<Integer>> visited = new HashSet<>(List.of(startingPoint));

      while (!queue.isEmpty()) {
        Tuple<Integer> currentPoint = queue.remove();
        Long currentHeight = map.get(currentPoint.elem2()).get(currentPoint.elem1());
        if (currentHeight == 9) {
          trails.put(startingPoint, trails.get(startingPoint) + 1);
          continue;
        }
        List<Tuple<Integer>> neighbours = TupleUtils.getNeighbours(currentPoint);
        neighbours.stream()
            .filter(nb -> (!visited.contains(nb) || !trackVisits)
                && TupleUtils.coordinateIsInBounds(nb, map.getFirst().size(), map.size())
                && map.get(nb.elem2()).get(nb.elem1()) == currentHeight + 1)
            .forEach(nb -> {
              queue.offer(nb);
              visited.add(nb);
            });
      }
    }
    return trails.values().stream().reduce(0L, Long::sum);
  }

  private HashSet<Tuple<Integer>> getStartingPoints(List<List<Long>> map) {
    HashSet<Tuple<Integer>> startingPoints = new HashSet<>();
    for (int y = 0; y < map.size(); y++) {
      for (int x = 0; x < map.get(y).size(); x++) {
        if (map.get(y).get(x) == 0) {
          startingPoints.add(new Tuple<>(x, y));
        }
      }
    }
    return startingPoints;
  }

  private List<List<Long>> inputToLongMap(List<String> input) {
    List<List<Long>> map = new ArrayList<>();
    input.forEach(row -> {
      List<Long> list = new ArrayList<>();
      for (char c : row.toCharArray()) {
        list.add((long) Character.getNumericValue(c));
      }
      map.add(list);
    });
    return map;
  }
}
