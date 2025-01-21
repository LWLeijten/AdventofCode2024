package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class Day15 {
  public Day15() throws FileNotFoundException, URISyntaxException {
    partOne();
    partTwo();
  }

  private void partOne() throws URISyntaxException, FileNotFoundException {
    List<String> rawInput = InputReader.readInput("/day15.txt");
    boolean readingInstructions = false;
    StringBuilder instructionBuilder = new StringBuilder();
    Set<Tuple<Integer>> walls = new HashSet<>();
    Set<Tuple<Integer>> boxes = new HashSet<>();
    Tuple<Integer> robot = new Tuple<>(0, 0);
    robot =
        parsePartOneInput(rawInput, readingInstructions, walls, boxes, robot, instructionBuilder);
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
        // recursion
        List<Tuple<Integer>> movableBoxes = new ArrayList<>();
        while (boxes.contains(newPos)) {
          movableBoxes.add(newPos);
          newPos = new Tuple<>(newPos.elem1() + delta.elem1(), newPos.elem2() + delta.elem2());
        }
        // pushable
        if (!walls.contains(newPos)) {
          robot = new Tuple<>(robot.elem1() + delta.elem1(), robot.elem2() + delta.elem2());
          movableBoxes.forEach(boxes::remove);
          boxes.addAll(movableBoxes.stream()
              .map(mb -> new Tuple<>(mb.elem1() + delta.elem1(), mb.elem2() + delta.elem2()))
              .toList());
        }
      }
    }
    System.out.printf("Part one: %s%n",
        boxes.stream().mapToInt(b -> 100 * b.elem2() + b.elem1()).sum());
  }

  private Tuple<Integer> parsePartOneInput(List<String> rawInput, boolean readingInstructions,
                                           Set<Tuple<Integer>> walls,
                                           Set<Tuple<Integer>> boxes, Tuple<Integer> robot,
                                           StringBuilder instructionBuilder) {
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
    return robot;
  }

  private void partTwo() throws URISyntaxException, FileNotFoundException {
    List<String> rawInput = InputReader.readInput("/day15.txt");
    boolean readingInstructions = false;
    StringBuilder instructionBuilder = new StringBuilder();
    HashSet<Tuple<Integer>> walls = new HashSet<>();
    HashSet<Tuple<Tuple<Integer>>> boxes = new HashSet<>();
    Tuple<Integer> robot = new Tuple<>(0, 0);
    robot =
        parsePartTwoInput(rawInput, readingInstructions, walls, boxes, robot, instructionBuilder);
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
      if (!walls.contains(newPos) &&
          boxes.stream().noneMatch(b -> b.elem1().equals(newPos) || b.elem2().equals(newPos))) {
        robot = newPos;
      }
      // hit box
      else if (boxes.stream().anyMatch(b -> b.elem1().equals(newPos) || b.elem2().equals(newPos))) {
        Set<Tuple<Tuple<Integer>>> movableBoxes = new HashSet<>();
        boolean walled = false;
        if (instruction == '<') {
          // recursion
          Queue<Tuple<Integer>> frontier = new LinkedList<>();
          frontier.add(newPos);
          while (!frontier.isEmpty()) {
            Tuple<Integer> pos = frontier.poll();
            Optional<Tuple<Tuple<Integer>>> boxHit =
                boxes.stream().filter(b -> b.elem2().equals(pos)).findFirst();
            if (boxHit.isPresent()) {
              movableBoxes.add(boxHit.get());
              frontier.add(new Tuple<>(boxHit.get().elem1().elem1() + delta.elem1(),
                  boxHit.get().elem1().elem2()));
            } else if (walls.contains(pos)) {
              walled = true;
              break;
            }
          }
        } else if (instruction == '>') {
          // recursion
          Queue<Tuple<Integer>> frontier = new LinkedList<>();
          frontier.add(newPos);
          while (!frontier.isEmpty()) {
            Tuple<Integer> pos = frontier.poll();
            Optional<Tuple<Tuple<Integer>>> boxHit =
                boxes.stream().filter(b -> b.elem1().equals(pos)).findFirst();
            if (boxHit.isPresent()) {
              movableBoxes.add(boxHit.get());
              frontier.add(new Tuple<>(boxHit.get().elem2().elem1() + delta.elem1(),
                  boxHit.get().elem2().elem2()));
            } else if (walls.contains(pos)) {
              walled = true;
              break;
            }
          }
        } else if (instruction == '^' || instruction == 'v') {
          Queue<Tuple<Integer>> frontier = new LinkedList<>();
          frontier.add(newPos);
          while (!frontier.isEmpty()) {
            Tuple<Integer> pos = frontier.poll();
            Optional<Tuple<Tuple<Integer>>> boxHit =
                boxes.stream().filter(b -> b.elem1().equals(pos) || b.elem2().equals(pos)).findFirst();
            if (boxHit.isPresent()) {
              movableBoxes.add(boxHit.get());
              frontier.add(new Tuple<>(boxHit.get().elem1().elem1(),
                  boxHit.get().elem1().elem2() + delta.elem2()));
              frontier.add(new Tuple<>(boxHit.get().elem2().elem1(),
                  boxHit.get().elem2().elem2() + delta.elem2()));
            } else if (walls.contains(pos)) {
              walled = true;
              break;
            }
          }
        }
        // pushable
        if (!walled) {
          robot = new Tuple<>(robot.elem1() + delta.elem1(), robot.elem2() + delta.elem2());
          movableBoxes.forEach(boxes::remove);
          boxes.addAll(movableBoxes.stream()
              .map(mb -> new Tuple<>(new Tuple<>(mb.elem1().elem1() + delta.elem1(),
                  mb.elem1().elem2() + delta.elem2()),
                  new Tuple<>(mb.elem2().elem1() + delta.elem1(),
                      mb.elem2().elem2() + delta.elem2())))
              .toList());
        }
      }
    }
    System.out.printf("Part two: %s%n",
        boxes.stream().mapToInt(b -> 100 * b.elem1().elem2() + b.elem1().elem1()).sum());
  }

  private Tuple<Integer> parsePartTwoInput(List<String> rawInput, boolean readingInstructions,
                                           Set<Tuple<Integer>> walls,
                                           Set<Tuple<Tuple<Integer>>> boxes,
                                           Tuple<Integer> robot,
                                           StringBuilder instructionBuilder) {
    for (int y = 0; y < rawInput.size(); y++) {
      if (!readingInstructions) {
        if (rawInput.get(y).isBlank()) {
          readingInstructions = true;
          continue;
        }
        for (int x = 0; x < rawInput.get(y).length() * 2; x += 2) {
          char c = rawInput.get(y).charAt(x / 2);
          switch (c) {
            case '#' -> {
              walls.add(new Tuple<>(x, y));
              walls.add(new Tuple<>(x + 1, y));
            }
            case 'O' -> {
              boxes.add(new Tuple<>(new Tuple<>(x, y), new Tuple<>(x + 1, y)));
            }
            case '@' -> {
              robot = new Tuple<>(x, y);
            }
          }
        }
      } else {
        instructionBuilder.append(rawInput.get(y));
      }
    }
    return robot;
  }
}
