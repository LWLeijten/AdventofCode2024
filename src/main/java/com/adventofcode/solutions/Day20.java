package com.adventofcode.solutions;

import com.adventofcode.utils.Direction;
import com.adventofcode.utils.Tuple;
import com.adventofcode.utils.TupleUtils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;

import static com.adventofcode.utils.InputReader.readInput;

public class Day20 {
  public Day20() throws FileNotFoundException, URISyntaxException {
    List<String> input = readInput("/day20.txt");

    // Init search space
    Tuple<Integer> start = null;
    Tuple<Integer> goal = null;
    List<Tuple<Integer>> walls = new ArrayList<>();
    for (int y = 0; y < input.size(); y++) {
      for (int x = 0; x < input.get(y).length(); x++) {
        char c = input.get(y).charAt(x);
        switch (c) {
          case '#' -> walls.add(new Tuple<>(x, y));
          case 'E' -> goal = new Tuple<>(x, y);
          case 'S' -> start = new Tuple<>(x, y);
        }
      }
    }

    int width = walls.stream().map(Tuple::elem1).max(Integer::compareTo).get() + 1;
    int height = walls.stream().map(Tuple::elem2).max(Integer::compareTo).get() + 1;

    // Init the path and distances of all visited spaces
    HashMap<Tuple<Integer>, Integer> distances = new HashMap<>();
    HashSet<Tuple<Integer>> path = new HashSet<>();
    Queue<Tuple<Integer>> queue = new LinkedList<>();
    queue.offer(start);
    distances.put(start, 0);
    while (!queue.isEmpty()) {
      Tuple<Integer> pos = queue.remove();
      path.add(pos);
      List<Tuple<Integer>> neighbours =
          TupleUtils.getNeighbours(pos).stream()
              .filter(
                  nb ->
                      TupleUtils.coordinateIsInBounds(nb, width, height)
                          && !walls.contains(nb)
                          && !path.contains(nb))
              .toList();
      neighbours.forEach(
          nb -> {
            distances.put(nb, distances.get(pos) + 1);
            queue.add(nb);
          });
    }

    // Part One (cheat with distance 2 only)
    HashMap<Integer, Integer> cheatOptionCounts = new HashMap<>();
    cheatWithMaxDistance(path, distances, 2, cheatOptionCounts);
    System.out.printf("Part one: %s%n",
        cheatOptionCounts.entrySet().stream()
            .filter(es -> es.getKey() >= 100)
            .mapToInt(Map.Entry::getValue)
            .sum());

    // Part Two (cheat with all distances op to 20)
    cheatOptionCounts = new HashMap<>();
    for (int i = 0; i <= 20; i++) {
      cheatWithMaxDistance(path, distances, i, cheatOptionCounts);
    }
    System.out.printf("Part two: %s%n",
            cheatOptionCounts.entrySet().stream()
                    .filter(es -> es.getKey() >= 100)
                    .mapToInt(Map.Entry::getValue)
                    .sum());
  }

  private void cheatWithMaxDistance(
      HashSet<Tuple<Integer>> path,
      HashMap<Tuple<Integer>, Integer> distances,
      int cheatDistance,
      HashMap<Integer, Integer> cheatOptionCounts) {
    // O(n^2) could (probably) be optimized
    for (Tuple<Integer> pos : path) {
      int distance = distances.get(pos);
      path.stream()
          .filter(
              c ->
                  distances.get(c) > distance
                      && TupleUtils.manhattanDistance(pos, c) == cheatDistance)
          .forEach(
              c -> {
                int resultingCheat = distances.get(c) - distance - cheatDistance;
                if (resultingCheat > 0) {
                  cheatOptionCounts.put(
                          resultingCheat, cheatOptionCounts.getOrDefault(resultingCheat, 0) + 1);
                }
              });
    }
  }
}
