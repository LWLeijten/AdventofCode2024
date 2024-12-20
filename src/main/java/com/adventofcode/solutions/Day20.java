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
    HashSet<Tuple<Integer>> visited = new HashSet<>();
    Queue<Tuple<Integer>> queue = new LinkedList<>();
    queue.offer(start);
    distances.put(start, 0);
    while (!queue.isEmpty()) {
      Tuple<Integer> pos = queue.remove();
      visited.add(pos);
      List<Tuple<Integer>> neighbours =
          TupleUtils.getNeighbours(pos).stream()
              .filter(
                  nb ->
                      TupleUtils.coordinateIsInBounds(nb, width, height)
                          && !walls.contains(nb)
                          && !visited.contains(nb))
              .toList();
      neighbours.forEach(
          nb -> {
            distances.put(nb, distances.get(pos) + 1);
            queue.add(nb);
          });
    }

    // Cheat
    HashMap<Integer, Integer> cheatOptionCounts = new HashMap<>();
    for (Tuple<Integer> pos : visited) {
      List<Tuple<Integer>> nbWalls =
          TupleUtils.getNeighbours(pos).stream()
              .filter(
                  nb -> TupleUtils.coordinateIsInBounds(nb, width, height) && walls.contains(nb))
              .toList();
      for (Tuple<Integer> nbWall : nbWalls) {
        Integer cheatDistance =
            TupleUtils.getNeighbours(nbWall).stream()
                    .filter(
                        nb ->
                            TupleUtils.coordinateIsInBounds(nb, width, height)
                                && !walls.contains(nb)
                                && !pos.equals(nb))
                    .map(distances::get)
                    .max(Integer::compareTo)
                    .orElse(0) // Distance score of the space we cheat to
                - distances.get(pos) // Distance score of current space
                - 2; // The two steps we need to take to cheat

        if (cheatDistance > 0) {
          cheatOptionCounts.put(
              cheatDistance, cheatOptionCounts.getOrDefault(cheatDistance, 0) + 1);
        }
      }
    }

    System.out.println(
        cheatOptionCounts.entrySet().stream()
            .filter(es -> es.getKey() >= 100)
            .mapToInt(Map.Entry::getValue)
            .sum());
  }
}
