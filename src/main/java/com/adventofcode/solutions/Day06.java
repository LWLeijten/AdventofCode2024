package com.adventofcode.solutions;

import com.adventofcode.utils.Direction;
import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;

public class Day06 {
  public Day06() throws FileNotFoundException, URISyntaxException {
    List<String> map = InputReader.readInput("/day06.txt");

    HashMap<Tuple<Integer>, ArrayList<Direction>> visited = partOne(map);
    System.out.printf("Part one: %s%n", visited.size());
    System.out.printf("Part two: %s%n", partTwo(visited, map));
  }

  private HashMap<Tuple<Integer>, ArrayList<Direction>> partOne(List<String> map) {
    HashMap<Tuple<Integer>, ArrayList<Direction>> visited = new HashMap<>();
    LocationDirection locationDirection =
        new LocationDirection(getStartPosition(map), Direction.NORTH);
    while (true) {
      try {
        locationDirection = doStep(locationDirection, visited, map);
      } catch (IndexOutOfBoundsException e) {
        break;
      }
    }
    return visited;
  }

  private int partTwo(
      HashMap<Tuple<Integer>, ArrayList<Direction>> possibleObstacles, List<String> map) {
    HashSet<Tuple<Integer>> loopingObstacles = new HashSet<>();
    for (Tuple<Integer> obstacle : possibleObstacles.keySet()) {
      ArrayList<String> newMap = placeObstacle(new ArrayList<>(map), obstacle);
      LocationDirection locationDirection =
          new LocationDirection(getStartPosition(map), Direction.NORTH);
      HashMap<Tuple<Integer>, ArrayList<Direction>> visited = new HashMap<>();
      while (true) {
        try {
          if (visited.containsKey(locationDirection.location())
              && visited
                  .get(locationDirection.location())
                  .contains(locationDirection.direction())) {
            loopingObstacles.add(obstacle);
            break;
          }
          locationDirection = doStep(locationDirection, visited, newMap);
        } catch (IndexOutOfBoundsException e) {
          break;
        }
      }
    }
    return loopingObstacles.size();
  }

  private LocationDirection doStep(
      LocationDirection locationDirection,
      HashMap<Tuple<Integer>, ArrayList<Direction>> visited,
      List<String> map) {
    Tuple<Integer> position = locationDirection.location();
    Direction direction = locationDirection.direction();
    if (!visited.containsKey(position)) {
      visited.put(position, new ArrayList<>(List.of(direction)));
    } else {
      visited.get(position).add(direction);
    }
    Tuple<Integer> newPosition = getNewPosition(direction, position);
    if (map.get(newPosition.elem2()).charAt(newPosition.elem1()) == '#') {
      direction = direction.getNextDirection();
    } else {
      position = newPosition;
    }
    return new LocationDirection(position, direction);
  }

  private ArrayList<String> placeObstacle(ArrayList<String> map, Tuple<Integer> obstacle) {
    String row = map.get(obstacle.elem2());
    map.set(
        obstacle.elem2(),
        row.substring(0, obstacle.elem1()) + '#' + row.substring(obstacle.elem1() + 1));
    return map;
  }

  private Tuple<Integer> getNewPosition(Direction direction, Tuple<Integer> position) {
    switch (direction) {
      case NORTH -> {
        return new Tuple<>(position.elem1(), position.elem2() - 1);
      }
      case EAST -> {
        return new Tuple<>(position.elem1() + 1, position.elem2());
      }
      case SOUTH -> {
        return new Tuple<>(position.elem1(), position.elem2() + 1);
      }
      case WEST -> {
        return new Tuple<>(position.elem1() - 1, position.elem2());
      }
    }
    return position;
  }

  private Tuple<Integer> getStartPosition(List<String> map) {
    for (int y = 0; y < map.size(); y++) {
      for (int x = 0; x < map.get(y).length(); x++) {
        if (map.get(y).charAt(x) == '^') {
          return new Tuple<>(x, y);
        }
      }
    }
    throw new RuntimeException("No starting position found.");
  }
}

record LocationDirection(Tuple<Integer> location, Direction direction) {}
