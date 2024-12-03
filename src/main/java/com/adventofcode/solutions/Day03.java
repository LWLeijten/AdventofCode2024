package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
  String regexPatternPartOne = "mul\\((?<number1>\\d{1,3}),(?<number2>\\d{1,3})\\)";
  String regexPatternPartTwo = "mul\\((?<number1>\\d{1,3}),(?<number2>\\d{1,3})\\)|(?<do>do\\(\\))|(?<dont>don't\\(\\))";

  public Day03() throws FileNotFoundException, URISyntaxException {
    String input = InputReader.readAsString("/day03.txt");
    System.out.printf("Part one: %s%n", partOne(input));
    System.out.printf("Part two: %s%n", partTwo(input));
  }

  private long partOne(String input) {
    long result = 0L;
    Matcher matcher = Pattern.compile(regexPatternPartOne).matcher(input);
    while (matcher.find()) {
      result += Long.parseLong(matcher.group("number1")) * Long.parseLong(matcher.group("number2"));
    }
    return result;
  }

  private long partTwo(String input) {
    long result = 0L;
    boolean enabled = true;
    Matcher matcher = Pattern.compile(regexPatternPartTwo).matcher(input);
    while (matcher.find()) {
      if(matcher.group("do") != null) {
        enabled = true;
      } else if (matcher.group("dont") != null) {
        enabled = false;
      } else if (enabled) {
        result += Long.parseLong(matcher.group("number1")) * Long.parseLong(matcher.group("number2"));
      }
    }
    return result;
  }
}
