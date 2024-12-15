package com.adventofcode.solutions;

import static com.adventofcode.utils.InputReader.readInput;

import com.adventofcode.utils.Tuple;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
  static final String regexPattern = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)";
  static final int GRID_WIDTH = 101;
  static final int GRID_HEIGHT = 103;
  static final int X_LINE =  (GRID_WIDTH - 1) / 2;
  static final int Y_LINE = (GRID_HEIGHT - 1) / 2;

  static final int SECONDS = 100;

  public Day14() throws FileNotFoundException, URISyntaxException {
    List<String> inputs = readInput("/day14.txt");
    List<Robot> robots = new ArrayList<>();
    for (String line : inputs) {
      Matcher matcher = Pattern.compile(regexPattern).matcher(line);
      if (matcher.find()) {
        robots.add(
            new Robot(
                new Tuple<>(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))));
      }
    }
    for (int i = 0; i < SECONDS; i++) {
      robots =
          robots.stream()
              .map(
                  r ->
                      r.withPosition(
                          new Tuple<>(
                                  ((r.position().elem1() + r.xSpeed()) % GRID_WIDTH + GRID_WIDTH) % GRID_WIDTH,
                                  ((r.position().elem2() + r.ySpeed()) % GRID_HEIGHT + GRID_HEIGHT) % GRID_HEIGHT)))
              .toList();
      int quadrantOne = robots.stream().filter(r -> r.position.elem1() < X_LINE && r.position.elem2() < Y_LINE).toList().size();
      int quadrantTwo = robots.stream().filter(r -> r.position.elem1() > X_LINE && r.position.elem2() < Y_LINE).toList().size();
      int quadrantThree = robots.stream().filter(r -> r.position.elem1() < X_LINE && r.position.elem2() > Y_LINE).toList().size();
      int quadrantFour = robots.stream().filter(r -> r.position.elem1() > X_LINE && r.position.elem2() > Y_LINE).toList().size();
      // Print the grid if the safety value is way lower than usual, indicating robots being close to eachother and maybe
      // forming a pattern
      if (quadrantOne * quadrantTwo * quadrantThree * quadrantFour < 118886292) {
        printGrid(i, robots);
      }
    }
    int quadrantOne = robots.stream().filter(r -> r.position.elem1() < X_LINE && r.position.elem2() < Y_LINE).toList().size();
    int quadrantTwo = robots.stream().filter(r -> r.position.elem1() > X_LINE && r.position.elem2() < Y_LINE).toList().size();
    int quadrantThree = robots.stream().filter(r -> r.position.elem1() < X_LINE && r.position.elem2() > Y_LINE).toList().size();
    int quadrantFour = robots.stream().filter(r -> r.position.elem1() > X_LINE && r.position.elem2() > Y_LINE).toList().size();
    System.out.printf("Part one: %s%n",quadrantOne * quadrantTwo * quadrantThree * quadrantFour);
  }

  private void printGrid(int i, List<Robot> robots) {
    System.out.println("#######################################################################");
    System.out.printf("TICK: %d%n", i);
    for(int y = 0; y < GRID_HEIGHT; y++) {
      for (int x = 0; x<GRID_WIDTH; x++) {
        int xFinal = x;
        int yFinal = y;
        if (robots.stream().anyMatch(r -> r.position.elem1().equals(xFinal) && r.position.elem2().equals(yFinal))) {
          System.out.print("X");
        } else {
          System.out.print(" ");
        }
      }
      System.out.print('\n');
    }
  }

  record Robot(Tuple<Integer> position, int xSpeed, int ySpeed) {
    public Robot withPosition(Tuple<Integer> position) {
      return new Robot(position, xSpeed, ySpeed);
    }
  }
}
