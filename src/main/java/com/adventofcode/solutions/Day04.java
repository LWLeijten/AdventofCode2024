package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Day04 {
  public Day04() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day04.txt");
    System.out.printf("Part one: %s%n", partOne(input));
    System.out.printf("Part two: %s%n", partTwo(input));
  }

  private long partOne(List<String> input) {
    long result = 0L;
    for (int y = 0; y < input.size(); y++) {
      for (int x = 0; x < input.get(y).length(); x++) {
        result += getAllWordsFromCharacter(input, x, y)
            .stream()
            .filter(w -> w.equals("XMAS"))
            .count();
      }
    }
    return result;
  }

  private long partTwo(List<String> input) {
    long result = 0L;
    for (int y = 0; y < input.size(); y++) {
      for (int x = 0; x < input.get(y).length(); x++) {
        if (xmasAtLocation(input, x, y)) {
          result++;
        }
      }
    }
    return result;
  }


  private List<String> getAllWordsFromCharacter(List<String> input, int x, int y) {
    List<String> words = new ArrayList<>();
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        if (dx == 0 && dy == 0) {
          continue;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
          sb.append(getNextCharacter(input, x + dx * i, y + dy * i));
        }
        words.add(sb.toString());
      }
    }
    return words;
  }

  private boolean xmasAtLocation(List<String> input, int x, int y) {
    String wordOne = String.valueOf(getNextCharacter(input, x - 1, y - 1)) +
        getNextCharacter(input, x, y) +
        getNextCharacter(input, x + 1, y + 1);
    String wordTwo = String.valueOf(getNextCharacter(input, x - 1, y + 1)) +
        getNextCharacter(input, x, y) +
        getNextCharacter(input, x + 1, y - 1);
    return (wordOne.equals("MAS") || wordOne.equals("SAM")) &&
        (wordTwo.equals("MAS") || wordTwo.equals("SAM"));
  }

  private Character getNextCharacter(List<String> input, int x, int y) {
    try {
      return input.get(y).charAt(x);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }
}
