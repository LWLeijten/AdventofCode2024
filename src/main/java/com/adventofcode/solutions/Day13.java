package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {
  final static String BUTTON_REGEX = "Button \\w: X\\+(\\d+), Y\\+(\\d+)";
  final static String PRIZE_REGEX = "Prize: X=(\\d+), Y=(\\d+)";
  public static final long PART_TWO_MODIFIER = 10000000000000L;

  public Day13() throws FileNotFoundException, URISyntaxException {
    System.out.println("Day 13");
    List<String> input = InputReader.readInput("/day13.txt");
    List<Tuple<Long>> results = new ArrayList<>();
    List<Tuple<Long>> results2 = new ArrayList<>();

    Tuple<Long> curA = new Tuple<>(0L, 0L);
    Tuple<Long> curB = new Tuple<>(0L, 0L);
    Tuple<Long> curPrize = new Tuple<>(0L, 0L);

    // Solve the equations for part One and Two in one loop.
    for (String line : input) {
      if (line.isEmpty()) {
        curA = null;
        curB = null;
        curPrize = null;
      } else {
        if (line.startsWith("Button A:")) {
          Matcher m = Pattern.compile(BUTTON_REGEX).matcher(line);
          if (m.find()) {
            curA = new Tuple<>(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)));
          }
        } else if (line.startsWith("Button B:")) {
          Matcher m = Pattern.compile(BUTTON_REGEX).matcher(line);
          if (m.find()) {
            curB = new Tuple<>(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)));
          }
        } else if (line.startsWith("Prize:")) {
          Matcher m = Pattern.compile(PRIZE_REGEX).matcher(line);
          if (m.find()) {
            curPrize = new Tuple<>(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)));
          }
          // After reading the prize location we can solve both part one and two equations
          results.add(solveEquation(curA, curB, curPrize));
          results2.add(solveEquation(curA, curB,
              new Tuple<>(curPrize.elem1() + PART_TWO_MODIFIER, curPrize.elem2() +
                  PART_TWO_MODIFIER)));
        }
      }
    }
    System.out.printf("Part one: %s%n", results.stream()
        .filter(r -> r.elem1() <= 100 && r.elem2() <= 100)
        .mapToLong(r -> r.elem1() * 3 + r.elem2())
        .sum());
    System.out.printf("Part two: %s%n",results2.stream()
        .mapToLong(r -> r.elem1() * 3 + r.elem2())
        .sum());
  }

  private Tuple<Long> solveEquation(Tuple<Long> buttonA, Tuple<Long> buttonB,
                                    Tuple<Long> prize) {
    // Calculate integer amount of aPresses and bPresses.
    Long aPresses = (prize.elem1() * buttonB.elem2() - buttonB.elem1() * prize.elem2()) /
        (buttonA.elem1() * buttonB.elem2() - buttonB.elem1() * buttonA.elem2());
    Long bPresses = (buttonA.elem1() * prize.elem2() - prize.elem1() * buttonA.elem2()) /
        (buttonA.elem1() * buttonB.elem2() - buttonB.elem1() * buttonA.elem2());

    // Check if we have an integer amount of button presses that leads to the prize
    // If integer division did some rounding anywhere we wont find the prize and we should
    // consider this prize as unobtainable.
    if (!(aPresses * buttonA.elem1() + bPresses * buttonB.elem1() == prize.elem1())
        || !(aPresses * buttonA.elem2() + bPresses * buttonB.elem2() == prize.elem2())) {
      return new Tuple<>(0L, 0L);
    }
    return new Tuple<>(aPresses, bPresses);
  }
}
