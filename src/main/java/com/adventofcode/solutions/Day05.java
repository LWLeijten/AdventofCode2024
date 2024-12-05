package com.adventofcode.solutions;

import static java.util.function.Predicate.not;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Day05 {
  public Day05() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day05.txt");
    List<Tuple<Long>> rules = new ArrayList<>();
    List<List<Long>> updates = new ArrayList<>();
    // Ugly but it works
    boolean readingRules = true;
    for (String s : input) {
      if (readingRules) {
        if (s.isEmpty()) {
          readingRules = false;
          continue;
        }
        List<String> numbers = Arrays.stream(s.split("\\|")).toList();
        rules.add(
            new Tuple<>(Long.parseLong(numbers.getFirst()), Long.parseLong(numbers.getLast())));
      } else {
        updates.add(Arrays.stream(s.split(",")).map(Long::parseLong).toList());
      }
    }
    // Solve the problems
    final List<List<Long>> updatesInOrder = updates.stream()
        .filter(u -> updateIsInCorrectOrder(u, rules)).toList();
    final List<ArrayList<Long>> updatesNotInOrder = updates.stream()
        .filter(u -> !updateIsInCorrectOrder(u, rules)).map(ArrayList::new).toList();

    System.out.printf("Part one: %s%n", updatesInOrder.stream()
        .mapToLong(u -> u.get(u.size() / 2))
        .sum());

    System.out.printf("Part two: %s%n",
        updatesNotInOrder.stream()
            .map(u -> fixUpdate(u, rules))
            .mapToLong(u -> u.get(u.size() / 2))
            .sum()
    );
  }

  private Predicate<Tuple<Long>> updateInAccordanceToRules(List<Long> update) {
    return r -> !(update.contains(r.elem1()) && update.contains(r.elem2())) ||
        (update.indexOf(r.elem1()) < update.indexOf(r.elem2()));
  }

  private boolean updateIsInCorrectOrder(List<Long> update, List<Tuple<Long>> rules) {
    return rules.stream().allMatch(updateInAccordanceToRules(update));
  }

  private List<Long> fixUpdate(ArrayList<Long> update, List<Tuple<Long>> rules) {
    // Shoot a brute-force prayer o7
    while (!updateIsInCorrectOrder(update, rules)) {
      List<Tuple<Long>> brokenRules =
          rules.stream()
              .filter(not(updateInAccordanceToRules(update)))
              .toList();
      Collections.swap(update,
          update.indexOf(brokenRules.getFirst().elem1()),
          update.indexOf(brokenRules.getFirst().elem2()));
    }
    return update;
  }
}
