package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Node;
import com.adventofcode.utils.Tuple;
import com.adventofcode.utils.TupleUtils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;

public class Day18 {
  private static Integer GOAL_X = 70;
  private static Integer GOAL_Y = 70;
  private static Integer BYTE_COUNT = 1024;

  public Day18() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day18.txt");
    List<Tuple<Integer>> fallingBytes =
        input.stream()
            .map(
                i ->
                    new Tuple<>(
                        Integer.parseInt(i.split(",")[0]), Integer.parseInt(i.split(",")[1])))
            .toList();

    Tuple<Integer> start = new Tuple<>(0, 0);
    Tuple<Integer> goal = new Tuple<>(GOAL_X, GOAL_Y);
    System.out.printf("Part one: %s%n", simulate(fallingBytes, BYTE_COUNT, start, goal));

    // Binary search the first value that fails and print its coordinate
    int left = 0;
    int right = fallingBytes.size() - 2;
    while (left <= right) {
      int mid = (left + right) / 2;
      Long resultA = simulate(fallingBytes, mid, start, goal);
      Long resultB = simulate(fallingBytes, mid + 1, start, goal);
      if (resultA > 0 && resultB < 0) {
        // resultA is the last succeeding amount of bytes
        // resultB is the first failing amount of bytes
        // We get the failing Byte by the list with fallingBytes(mid + 1 - 1), since we access by index and not count
        System.out.printf("Part two: %s%n", fallingBytes.get(mid));
        break;
      } else if (resultA > 0 && resultB > 0) {
        left = mid + 1;
      } else if (resultA < 0 && resultB < 0) {
        right = mid - 1;
      }
    }
  }

  private Long simulate(
      List<Tuple<Integer>> fallingBytes,
      Integer byteCount,
      Tuple<Integer> start,
      Tuple<Integer> goal) {

    // Set the corrupted bytes
    HashSet<Tuple<Integer>> corrupted = new HashSet<>();
    for (int i = 0; i < byteCount; i++) {
      corrupted.add(fallingBytes.get(i));
    }

    // Init the map of distances to positions and the set of visited positions
    HashMap<Tuple<Integer>, Long> distances = new HashMap<>();
    HashSet<Tuple<Integer>> visited = new HashSet<>();
    distances.put(start, 0L);

    // BFS
    Queue<Tuple<Integer>> queue = new LinkedList<>();
    queue.offer(start);
    while (!queue.isEmpty()) {
      Tuple<Integer> pos = queue.remove();
      if (visited.contains(pos)) {
        continue;
      }

      visited.add(pos);
      if (pos.equals(goal)) {
        return distances.get(goal);
      }

      List<Tuple<Integer>> neighbours =
          TupleUtils.getNeighbours(pos).stream()
              .filter(p -> TupleUtils.coordinateIsInBounds(p, GOAL_X + 1, GOAL_Y + 1) && !corrupted.contains(p))
              .toList();
      for (Tuple<Integer> nb : neighbours) {
        distances.put(
            nb, Math.min(distances.getOrDefault(nb, Long.MAX_VALUE), distances.get(pos) + 1));
        queue.add(nb);
      }
    }
    // No solution
    return -1L;
  }
}
