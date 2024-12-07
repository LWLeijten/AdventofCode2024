package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Node;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;

public class Day07 {
  // TODO: Clean up, pretty print
  // Create operators +, *, || and pass these ass parameters to one generateTree function.
  public Day07() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day07.txt");
    Long result = 0L;
    for (String i : input) {
      long goal = Long.parseLong(i.split(":")[0]);
      List<Long> numbers =
          Arrays.stream((i.split(": ")[1]).split(" ")).map(Long::parseLong).toList();
      Node<Long> tree = generateTree(0, numbers.get(0), numbers);
      if (treeContainsLeafWithValue(tree, goal)) {
        result+=goal;
      }
    }
    System.out.println(result);
    result = 0L;
    for (String i : input) {
      long goal = Long.parseLong(i.split(":")[0]);
      List<Long> numbers =
              Arrays.stream((i.split(": ")[1]).split(" ")).map(Long::parseLong).toList();
      Node<Long> tree = generateTreeTwo(0, numbers.get(0), numbers);
      if (treeContainsLeafWithValue(tree, goal)) {
        result+=goal;
      }
    }
    System.out.println(result);
  }

  public boolean treeContainsLeafWithValue(Node<Long> start, Long value) {
    Queue<Node<Long>> queue = new LinkedList<>();
    queue.offer(start);
    while (!queue.isEmpty()) {
      Node<Long> curr = queue.remove();
      if (curr.getChildren().isEmpty() && curr.getValue().equals(value)) {
        return true;
      } else if (curr.getValue() > value) {
        continue;
      }
      queue.addAll(curr.getChildren());
    }
    return false;
  }

  public Node<Long> generateTree(Integer depth, Long value, List<Long> numbers) {
    Node<Long> node;
    if (depth == numbers.size() - 1) {
      node = new Node<>(value, List.of());
    } else {
      node =
          new Node<>(
              value,
              List.of(
                  generateTree(depth + 1, value + numbers.get(depth + 1), numbers),
                  generateTree(depth + 1, value * numbers.get(depth + 1), numbers)));
    }
    return node;
  }

  public Node<Long> generateTreeTwo(Integer depth, Long value, List<Long> numbers) {
    Node<Long> node;
    if (depth == numbers.size() - 1) {
      node = new Node<>(value, List.of());
    } else {
      node =
              new Node<>(
                      value,
                      List.of(
                              generateTreeTwo(depth + 1, value + numbers.get(depth + 1), numbers),
                              generateTreeTwo(depth + 1, value * numbers.get(depth + 1), numbers),
                              generateTreeTwo(depth + 1, Long.parseLong(value.toString().concat(numbers.get(depth + 1).toString())), numbers)));
    }
    return node;
  }
}
