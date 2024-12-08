package com.adventofcode.solutions;

import static com.adventofcode.utils.BiFunctions.*;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Node;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.BiFunction;

public class Day07 {

  public Day07() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day07.txt");
    System.out.printf("Part one: %s%n", solve(input, List.of(add, multiply)));
    System.out.printf("Part two: %s%n", solve(input, List.of(add, multiply, concat)));
  }

  private Long solve(List<String> input, List<BiFunction<Long, Long, Long>> operators) {
    long result = 0L;
    for (String i : input) {
      long goal = Long.parseLong(i.split(":")[0]);
      List<Long> numbers =
          Arrays.stream((i.split(": ")[1]).split(" ")).map(Long::parseLong).toList();
      Node<Long> tree = generateTree(0, numbers.get(0), numbers, operators, goal);
      if (treeContainsLeafWithValue(tree, goal)) {
        result += goal;
      }
    }
    return result;
  }

  public boolean treeContainsLeafWithValue(Node<Long> start, Long value) {
    Queue<Node<Long>> queue = new LinkedList<>();
    queue.offer(start);
    while (!queue.isEmpty()) {
      Node<Long> curr = queue.remove();
      if (curr.isLeaf() && curr.getValue().equals(value)) {
        return true;
      }
      queue.addAll(curr.getChildren());
    }
    return false;
  }

  public Node<Long> generateTree(
      Integer depth,
      Long value,
      List<Long> numbers,
      List<BiFunction<Long, Long, Long>> operators,
      Long goal) {
    Node<Long> node;
    // Cancel recursion if we hit max-depth or value already higher than goal
    if (depth == numbers.size() - 1 || value > goal) {
      node = new Node<>(value, List.of());
    } else {
      node =
          new Node<>(
              value,
              operators.stream()
                  .map(
                      op ->
                          generateTree(
                              depth + 1,
                              op.apply(value, numbers.get(depth + 1)),
                              numbers,
                              operators,
                              goal))
                  .toList());
    }
    return node;
  }
}
