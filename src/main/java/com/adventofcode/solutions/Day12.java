package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;
import com.adventofcode.utils.TupleUtils;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day12 {
  public Day12() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day12.txt");
    List<Set<Tuple<Integer>>> plots = getPlots(input);
    System.out.printf("Part one: %s%n", getFenceCost(plots));
    System.out.printf("Part two: %s%n", getFenceCostBulk(plots));
  }

  private Long getFenceCost(List<Set<Tuple<Integer>>> plots) {
    // Amount of fences * plot size for each plot
    return plots.stream()
        .map(
            p -> p.size() * getCountsOfSidesInPlot(p).values().stream().filter(f -> f == 1).count())
        .reduce(0L, Long::sum);
  }

  private Long getFenceCostBulk(List<Set<Tuple<Integer>>> plots) {
    long cost = 0L;
    for (Set<Tuple<Integer>> plot : plots) {
      // Save coordinates of all fences
      HashMap<Tuple<Float>, Integer> sideCounts = getCountsOfSidesInPlot(plot);
      List<Tuple<Float>> fences = new ArrayList<>(
          sideCounts.entrySet().stream().filter(e -> e.getValue() == 1).map(
              Map.Entry::getKey).toList());
      int sides = 0;
      // Calculate amount of Horizontal sides
      // Sort by Y value then X value so sides are adjacent
      List<Tuple<Float>> horizontalFences =
          new ArrayList<>(fences.stream().filter(f -> f.elem1().toString().endsWith("0")).toList());
      horizontalFences.sort(
          (f1, f2) -> f1.elem2().compareTo(f2.elem2()) == 0 ? f1.elem1().compareTo(f2.elem1()) :
              f1.elem2().compareTo(f2.elem2()));
      for (int i = 0; i < horizontalFences.size(); i++) {
        if (i == 0) {
          sides++;
        } else {
          Tuple<Float> curFence = horizontalFences.get(i);
          Tuple<Float> prevFence = horizontalFences.get(i - 1);
          if ((prevFence.elem1() + 1 != curFence.elem1()   // Not increment of 1 so new side
              || !prevFence.elem2().equals(curFence.elem2())) // Different Y value so new side
              // Edge case of diagonally touching fences
              || (!(plot.contains(
              new Tuple<>(Math.round(prevFence.elem1()),
                  (int) Math.round(prevFence.elem2() - 0.5))) &&
              plot.contains(
                  new Tuple<>(Math.round(curFence.elem1()),
                      (int) Math.round(curFence.elem2() - 0.5)))
          ) && (
              !(plot.contains(
                  new Tuple<>(Math.round(prevFence.elem1()),
                      (int) Math.round(prevFence.elem2() + 0.5))) &&
                  plot.contains(
                      new Tuple<>(Math.round(curFence.elem1()),
                          (int) Math.round(curFence.elem2() + 0.5)))
              )
          ))) {
            sides++;
          }
        }
      }
      // Calculate amount of Vertical sides
      // Sort by X value then Y value so sides are adjacent
      List<Tuple<Float>> verticalFences =
          new ArrayList<>(fences.stream().filter(f -> f.elem2().toString().endsWith("0")).toList());
      verticalFences.sort(
          (f1, f2) -> f1.elem1().compareTo(f2.elem1()) == 0 ? f1.elem2().compareTo(f2.elem2()) :
              f1.elem1().compareTo(f2.elem1()));
      for (int i = 0; i < verticalFences.size(); i++) {
        if (i == 0) {
          sides++;
        } else {
          Tuple<Float> curFence = verticalFences.get(i);
          Tuple<Float> prevFence = verticalFences.get(i - 1);
          if ((prevFence.elem2() + 1 != curFence.elem2() // Not increment of 1 so new side
              || !prevFence.elem1().equals(curFence.elem1()))  // Different X value so new side
              // Edge case of diagonally touching fences
              || (!(plot.contains(
              new Tuple<>((int) Math.round(prevFence.elem1() - 0.5),
                  Math.round(prevFence.elem2()))) &&
              plot.contains(
                  new Tuple<>((int) Math.round(curFence.elem1() - 0.5),
                      Math.round(curFence.elem2())))
          ) && (
              !(plot.contains(
                  new Tuple<>((int) Math.round(prevFence.elem1() + 0.5),
                      Math.round(prevFence.elem2()))) &&
                  plot.contains(
                      new Tuple<>((int) Math.round(curFence.elem1() + 0.5),
                          Math.round(curFence.elem2())))
              )
          ))
          ) {
            sides++;
          }
        }
      }
      cost += (long) sides * plot.size();
    }
    return cost;
  }

  private HashMap<Tuple<Float>, Integer> getCountsOfSidesInPlot(Set<Tuple<Integer>> plot) {
    HashMap<Tuple<Float>, Integer> fenceCounts = new HashMap<>();
    for (Tuple<Integer> p : plot) {
      List<Tuple<Float>> fences = List.of(new Tuple<>((float) p.elem1(), p.elem2() - 0.5f),
          new Tuple<>((float) p.elem1(), p.elem2() + 0.5f),
          new Tuple<>(p.elem1() - 0.5f, (float) p.elem2()),
          new Tuple<>(p.elem1() + 0.5f, (float) p.elem2()));
      fences.forEach(f -> fenceCounts.put(f, fenceCounts.getOrDefault(f, 0) + 1));
    }
    return fenceCounts;
  }

  private List<Set<Tuple<Integer>>> getPlots(List<String> input) {
    List<Set<Tuple<Integer>>> plots = new ArrayList<>();
    // BFS through the plants to find all plots
    for (int y = 0; y < input.size(); y++) {
      for (int x = 0; x < input.get(0).length(); x++) {
        final int finalX = x;
        final int finalY = y;
        if (plots.stream().noneMatch(p -> p.contains(new Tuple<>(finalX, finalY)))) {
          Tuple<Integer> startingPoint = new Tuple<>(x, y);
          Set<Tuple<Integer>> plot = new HashSet<>();
          Set<Tuple<Integer>> visited = new HashSet<>(List.of(startingPoint));
          char c = input.get(y).charAt(x);
          Queue<Tuple<Integer>> queue = new LinkedList<>(List.of(startingPoint));
          while (!queue.isEmpty()) {
            Tuple<Integer> currentPoint = queue.remove();
            plot.add(currentPoint);
            List<Tuple<Integer>> neighbours = TupleUtils.getNeighbours(currentPoint);
            neighbours.stream()
                .filter(nb ->
                    TupleUtils.coordinateIsInBounds(nb, input.getFirst().length(), input.size()) &&
                        !visited.contains(nb) &&
                        input.get(nb.elem2()).charAt(nb.elem1()) == c)
                .forEach(nb -> {
                  visited.add(nb);
                  queue.add(nb);
                });
          }
          plots.add(plot);
        }
      }
    }
    return plots;
  }
}
