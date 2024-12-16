package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day15 {
  public Day15() throws FileNotFoundException, URISyntaxException {
    List<String> rawInput = InputReader.readInput("/day15.txt");
    boolean readingInstructions = false;
    StringBuilder instructionBuilder = new StringBuilder();

    HashSet<Tuple<Integer>> walls = new HashSet<>();
    HashSet<Tuple<Integer>> boxes = new HashSet<>();
    Tuple<Integer> robot = new Tuple<>(0, 0);

    for (int y = 0; y < rawInput.size(); y++) {
      if (!readingInstructions) {
        if (rawInput.get(y).isBlank()) {
          readingInstructions = true;
          continue;
        }
        for (int x = 0; x < rawInput.get(y).length(); x++) {
          char c = rawInput.get(y).charAt(x);
          switch (c) {
            case '#' -> walls.add(new Tuple<>(x, y));
            case 'O' -> boxes.add(new Tuple<>(x, y));
            case '@' -> robot = new Tuple<>(x, y);
          }
        }
      } else {
        instructionBuilder.append(rawInput.get(y));
      }
    }
    String instructions = instructionBuilder.toString();

    for (char instruction : instructions.toCharArray()) {
      Tuple<Integer> delta;
      switch (instruction) {
        case '<' -> delta = new Tuple<>(-1, 0);
        case '>' -> delta = new Tuple<>(1, 0);
        case '^' -> delta = new Tuple<>(0, -1);
        case 'v' -> delta = new Tuple<>(0, 1);
        default -> delta = new Tuple<>(0, 0);
      }
      Tuple<Integer> newPos =
          new Tuple<>(robot.elem1() + delta.elem1(), robot.elem2() + delta.elem2());
      // No obstacle
      if (!walls.contains(newPos) && !boxes.contains(newPos)) {
        robot = newPos;
      }
      // hit box
      else if (boxes.contains(newPos)) {
        List<Tuple<Integer>> movableBoxes = new ArrayList<>();
        while (boxes.contains(newPos)) {
          movableBoxes.add(newPos);
          newPos = new Tuple<>(newPos.elem1() + delta.elem1(), newPos.elem2() + delta.elem2());
        }
        if (walls.contains(newPos)) {
          continue;
        } else {
          robot = new Tuple<>(robot.elem1() + delta.elem1(), robot.elem2() + delta.elem2());
          movableBoxes.forEach(boxes::remove);
          boxes.addAll(movableBoxes.stream()
              .map(mb -> new Tuple<>(mb.elem1() + delta.elem1(), mb.elem2() + delta.elem2()))
              .toList());
        }
      }
      // print grid
      //      for (int y = 0; y < 8; y++) {
      //        for (int x = 0; x < 8; x++) {
      //          if (walls.contains(new Tuple<>(x, y))) {
      //            System.out.print('#');
      //          } else if (boxes.contains(new Tuple<>(x, y))) {
      //            System.out.print('O');
      //          } else if (robot.equals(new Tuple<>(x, y))) {
      //            System.out.print('@');
      //          } else {
      //            System.out.print('.');
      //          }
      //        }
      //        System.out.println();
      //      }
    }
    System.out.println(boxes.stream().mapToInt(b -> 100 * b.elem2() + b.elem1()).sum());
  }
}
