package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day25 {
  static final Integer lockWidth = 5;
  static final Integer lockHeight = 6;

  public Day25() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day25.txt");
    List<List<Integer>> locks = new ArrayList<>();
    List<List<Integer>> keys = new ArrayList<>();

    // Create lists of keys and locks
    List<String> currentObject = new ArrayList<>();
    for (String line : input) {
      if (!line.isEmpty()) {
        currentObject.add(line);
      } else {
        List<Integer> lockOrKey = mapGridToKeyOrLock(currentObject);
        if (currentObject.getFirst().equals("#####")) {
          locks.add(lockOrKey);
        } else {
          keys.add(lockOrKey);
        }
        currentObject = new ArrayList<>();
      }
    }
    int fits =
        keys.stream().mapToInt(k -> locks.stream().filter(l -> fits(k, l)).toList().size()).sum();
    System.out.printf("Part one: %s%n", fits);
  }

  List<Integer> mapGridToKeyOrLock(List<String> keyOrLock) {
    return IntStream.range(0, lockWidth)
        .map(
            i ->
                keyOrLock.stream().map(cO -> cO.charAt(i)).filter(c -> c == '#').toList().size()
                    - 1)
        .boxed()
        .toList();
  }

  boolean fits(List<Integer> key, List<Integer> lock) {
    return IntStream.range(0, 5).mapToObj(i -> key.get(i) + lock.get(i) <= 5).allMatch(t -> t);
  }
}
