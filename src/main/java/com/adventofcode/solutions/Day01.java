package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day01 {

  public Day01() throws URISyntaxException, FileNotFoundException {
    List<List<Long>> numberLists = InputReader.readAsLongMatrix("/day01.txt");
    List<Long> leftList = new ArrayList<>(numberLists.stream().map(List::getFirst).toList());
    leftList.sort(Long::compareTo);
    List<Long> rightList = new ArrayList<>(numberLists.stream().map(List::getLast).toList());
    rightList.sort(Long::compareTo);

    System.out.printf("Part one: %s%n", partOne(leftList, rightList));
    System.out.printf("Part two: %s%n", partTwo(leftList, rightList));
  }

  private long partOne(List<Long> left, List<Long> right) {
    return IntStream.range(0, left.size()).mapToLong(i -> Math.abs(left.get(i) - right.get(i))).sum();
  }

  private long partTwo(List<Long> left, List<Long> right) {
    return left.stream()
                    .mapToLong(i -> right.stream().filter(x -> x.equals(i)).toList().size() * i)
                    .sum();
  }
}
