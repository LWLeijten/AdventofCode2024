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

    Reindeer reindeer = new Reindeer(new Tuple<>(0, 0), Direction.EAST);
    Tuple<Integer> goal = null;
    List<Tuple<Integer>> walls = new ArrayList<>();
    List<Tuple<Integer>> spaces = new ArrayList<>();

    for (int y = 0; y < input.size(); y++) {
      for (int x = 0; x < input.get(y).length(); x++) {
        char c = input.get(y).charAt(x);
        switch (c) {
          case '#' -> walls.add(new Tuple<>(x, y));
          case 'E' -> goal = new Tuple<>(x, y);
          case 'S' -> reindeer = new Reindeer(new Tuple<>(x, y), Direction.EAST);
          default -> spaces.add(new Tuple<>(x, y));
        }
      }
    }

    final Tuple<Integer> finalGoal = goal;
    HashMap<Reindeer, Long> distances = new HashMap<>();
    spaces.forEach(
        s ->
            Arrays.stream(Direction.values())
                .forEach(d -> distances.put(new Reindeer(s, d), Long.MAX_VALUE)));
      Reindeer finalReindeer = reindeer;
    Arrays.stream(Direction.values()).forEach(d -> distances.put(finalReindeer.withDirection(d), Long.MAX_VALUE));
    Arrays.stream(Direction.values())
        .forEach(d -> distances.put(new Reindeer(finalGoal, d), Long.MAX_VALUE));
    HashSet<Reindeer> visited = new HashSet<>();
    PriorityQueue<Reindeer> priorityQueue = new PriorityQueue<>(Comparator.comparingLong(distances::get));
    priorityQueue.add(reindeer);
    distances.put(reindeer, 0L);
    while (!priorityQueue.isEmpty()) {
        Reindeer current = priorityQueue.remove();
        if (visited.contains(current)) {
            continue;
        }
        visited.add(current);
        // Turn
        Reindeer clockwise = current.withDirection(current.direction.getNextDirection());
        Long clockwiseDistance = distances.get(current) + 1000;
        if (clockwiseDistance < distances.get(clockwise)) {
            distances.put(clockwise, clockwiseDistance);
            priorityQueue.add(clockwise);
        }
        Reindeer counterClockwise = current.withDirection(current.direction.getPreviousDirection());
        Long counterClockwiseDistance = distances.get(current) + 1000;
        if (counterClockwiseDistance < distances.get(counterClockwise)) {
            distances.put(counterClockwise, counterClockwiseDistance);
            priorityQueue.add(counterClockwise);
        }
        // Move
        Reindeer move = null;
        switch (current.direction) {
            case Direction.NORTH -> move = new Reindeer(new Tuple<>(current.position.elem1(), current.position.elem2() - 1), current.direction);
            case Direction.EAST -> move = new Reindeer(new Tuple<>(current.position.elem1() + 1, current.position.elem2()), current.direction);
            case Direction.SOUTH -> move = new Reindeer(new Tuple<>(current.position.elem1(), current.position.elem2() + 1), current.direction);
            case Direction.WEST -> move = new Reindeer(new Tuple<>(current.position.elem1() - 1, current.position.elem2()), current.direction);
        }
        if (spaces.contains(move.position) || move.position.equals(goal) || move.position.equals(reindeer.position)) {
            Long moveDistance = distances.get(current) + 1;
            if (moveDistance < distances.get(move)) {
                distances.put(move, moveDistance);
                priorityQueue.add(move);
            }
        }
    }
    List<Long> finishes = distances
            .keySet()
            .stream()
            .filter(rd -> rd.position.equals(finalGoal))
            .map(distances::get)
            .toList();
    System.out.println("Done");
  }

  record Reindeer(Tuple<Integer> position, Direction direction) {
      public Reindeer withDirection(Direction direction) {
          return  new Reindeer(position, direction);
      }
  }
}
