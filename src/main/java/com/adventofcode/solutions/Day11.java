package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day11 {
  public Day11() throws FileNotFoundException, URISyntaxException {
    List<Long> numbers = Arrays.stream(InputReader
            .readAsString("/day11.txt")
            .split(" "))
        .toList()
        .stream()
        .map(Long::parseLong)
        .toList();

    HashMap<Long, Long> stoneCounts = new HashMap<>();
    for (Long number : numbers) {
      stoneCounts.put(number, stoneCounts.getOrDefault(number, 0L) + 1);
    }
    System.out.printf("Part one: %s%n", simulate(stoneCounts, 25));
    System.out.printf("Part two: %s%n", simulate(stoneCounts, 75));
  }

  private Long simulate(HashMap<Long, Long> stoneCounts, int rounds) {
    for (int i = 0; i < rounds; i++) {
      HashMap<Long, Long> newStoneCounts = new HashMap<>();
      for (Long stone : stoneCounts.keySet()) {
        Long currentCount = stoneCounts.get(stone);
        if (stone == 0) {
          newStoneCounts.put(1L, newStoneCounts.getOrDefault(1L, 0L) + currentCount);
        } else if (stone.toString().length() % 2 == 0) {
          Long left = Long.parseLong(stone.toString().substring(0, stone.toString().length() / 2));
          Long right = Long.parseLong(stone.toString().substring(stone.toString().length() / 2));
          newStoneCounts.put(left, newStoneCounts.getOrDefault(left, 0L) + currentCount);
          newStoneCounts.put(right, newStoneCounts.getOrDefault(right, 0L) + currentCount);
        } else {
          newStoneCounts.put(stone * 2024,
              newStoneCounts.getOrDefault(stone * 2024, 0L) + currentCount);
        }
      }
      stoneCounts = newStoneCounts;
    }
    return stoneCounts.values().stream().reduce(0L, Long::sum);
  }
}
