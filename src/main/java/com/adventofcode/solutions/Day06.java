package com.adventofcode.solutions;

import com.adventofcode.utils.Direction;
import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day06 {
  public Day06() throws FileNotFoundException, URISyntaxException {
    List<String> map = InputReader.readInput("/day06.txt");
    HashMap<Tuple<Integer>, ArrayList<Direction>> visited = new HashMap<>();
    Direction direction = Direction.NORTH;
    Tuple<Integer> position = getStartPosition(map);

    // part one
    while (true) {
      try {
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
      } catch (IndexOutOfBoundsException e) {
        break;
      }
    }
    System.out.printf("Part one: %s%n", visited.size());
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
