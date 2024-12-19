package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day19 {
  private final HashMap<String, Long> cache = new HashMap<>();

  public Day19() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day19.txt");
    List<String> towels = Arrays.stream(input.getFirst().split(", ")).toList();
    List<String> goalPatterns = input.subList(2, input.size());

    List<Long> results =
        goalPatterns.stream().map(gp -> getPossibleTowelPatterns(towels, gp)).toList();

    System.out.printf("Part one: %s%n", results.stream().filter(r -> r > 0).count());
    System.out.printf("Part two: %s%n", results.stream().reduce(0L, Long::sum));
  }

  long getPossibleTowelPatterns(List<String> towels, String goal) {
    // Check cache
    if (cache.containsKey(goal)) {
      return cache.get(goal);
    }

    // Base case
    if (goal.isEmpty()) {
      cache.put(goal, 1L);
      return 1;
    }

    // Dead end
    List<String> matchingTowels = towels.stream().filter(goal::startsWith).toList();
    if (matchingTowels.isEmpty()) {
      cache.put(goal, 0L);
      return 0;
    }

    // Recursion
    return matchingTowels
        .stream()
        .mapToLong(mt -> {
          String newGoal = goal.substring(mt.length());
          if (!cache.containsKey(newGoal)) {
            cache.put(newGoal, getPossibleTowelPatterns(towels, goal.substring(mt.length())));
          }
          return cache.get(newGoal);
        }).sum();
  }
}
