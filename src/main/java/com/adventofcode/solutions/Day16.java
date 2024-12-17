package com.adventofcode.solutions;

import com.adventofcode.utils.Direction;
import com.adventofcode.utils.Tuple;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;

import static com.adventofcode.utils.InputReader.readInput;

public class Day16 {
  public Day16() throws FileNotFoundException, URISyntaxException {
    List<String> input = readInput("/day16.txt");

    // Init search space
    Reindeer reindeer = new Reindeer(new Tuple<>(0, 0), Direction.EAST);
    Tuple<Integer> goal = null;
    List<Tuple<Integer>> spaces = new ArrayList<>();
    for (int y = 0; y < input.size(); y++) {
      for (int x = 0; x < input.get(y).length(); x++) {
        char c = input.get(y).charAt(x);
        switch (c) {
          case '#' -> {}
          case 'E' -> goal = new Tuple<>(x, y);
          case 'S' -> reindeer = new Reindeer(new Tuple<>(x, y), Direction.EAST);
          default -> spaces.add(new Tuple<>(x, y));
        }
      }
    }

    // Init all distances as MAX_VALUE longs.
    final Tuple<Integer> finalGoal = goal;
    HashMap<Reindeer, Long> distances = new HashMap<>();
    spaces.forEach(
        s ->
            Arrays.stream(Direction.values())
                .forEach(d -> distances.put(new Reindeer(s, d), Long.MAX_VALUE)));
    Reindeer finalReindeer = reindeer;
    Arrays.stream(Direction.values())
        .forEach(d -> distances.put(finalReindeer.withDirection(d), Long.MAX_VALUE));
    Arrays.stream(Direction.values())
        .forEach(d -> distances.put(new Reindeer(finalGoal, d), Long.MAX_VALUE));

    // Init visited set and priority queue for dijkstra
    HashSet<Reindeer> visited = new HashSet<>();
    PriorityQueue<Reindeer> priorityQueue =
        new PriorityQueue<>(Comparator.comparingLong(distances::get));
    priorityQueue.add(reindeer);
    distances.put(reindeer, 0L);

    // Init predecessors hashmap for backtracking paths
    HashMap<Reindeer, List<Reindeer>> predecessors = new HashMap<>();

    // Dijkstra
    while (!priorityQueue.isEmpty()) {
      Reindeer current = priorityQueue.remove();
      if (visited.contains(current)) {
        continue;
      }
      visited.add(current);
      // Turn
      for (Reindeer turned :
          List.of(
              current.withDirection(current.direction.getNextDirection()),
              current.withDirection(current.direction.getPreviousDirection()))) {
        Long turnedDistance = distances.get(current) + 1000;
        if (turnedDistance < distances.get(turned)) {
          distances.put(turned, turnedDistance);
          priorityQueue.add(turned);
          predecessors.put(turned, new ArrayList<>(List.of(current)));
        } else if (turnedDistance.equals(distances.get(turned))) {
          predecessors.get(turned).add(current);
        }
      }
      // Move
      Reindeer move = getReindeerAfterMove(current);
      if (spaces.contains(move.position)
          || move.position.equals(goal)
          || move.position.equals(reindeer.position)) {
        Long moveDistance = distances.get(current) + 1;
        if (moveDistance < distances.get(move)) {
          distances.put(move, moveDistance);
          priorityQueue.add(move);
          predecessors.put(move, new ArrayList<>(List.of(current)));
        } else if (moveDistance.equals(distances.get(move))) {
          predecessors.get(move).add(current);
        }
      }
    }

    Reindeer finish =
        distances.keySet().stream()
            .filter(rd -> rd.position.equals(finalGoal))
            .min(Comparator.comparing(distances::get))
            .get();

    System.out.printf("Part one: %s%n", distances.get(finish));

    // Backtrack predecessors for part two
    HashSet<Tuple<Integer>> bestPaths = new HashSet<>();
    Queue<Reindeer> queue = new LinkedList<>();
    queue.offer(finish);
    while (!queue.isEmpty()) {
      Reindeer curr = queue.remove();
      predecessors
          .getOrDefault(curr, List.of())
          .forEach(
              p -> {
                bestPaths.add(p.position);
                queue.add(p);
              });
    }

    System.out.printf("Part two: %s%n", bestPaths.size() + 1);
  }

  private Reindeer getReindeerAfterMove(Reindeer current) {
    Reindeer move = null;
    switch (current.direction) {
      case Direction.NORTH ->
          move =
              new Reindeer(
                  new Tuple<>(current.position.elem1(), current.position.elem2() - 1),
                  current.direction);
      case Direction.EAST ->
          move =
              new Reindeer(
                  new Tuple<>(current.position.elem1() + 1, current.position.elem2()),
                  current.direction);
      case Direction.SOUTH ->
          move =
              new Reindeer(
                  new Tuple<>(current.position.elem1(), current.position.elem2() + 1),
                  current.direction);
      case Direction.WEST ->
          move =
              new Reindeer(
                  new Tuple<>(current.position.elem1() - 1, current.position.elem2()),
                  current.direction);
    }
    return move;
  }

  record Reindeer(Tuple<Integer> position, Direction direction) {
    public Reindeer withDirection(Direction direction) {
      return new Reindeer(position, direction);
    }
  }
}
