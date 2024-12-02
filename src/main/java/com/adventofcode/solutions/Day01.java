package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Day01 {

  public Day01() throws URISyntaxException, FileNotFoundException {
    List<List<Long>> numberLists = InputReader.readAsLongMatrix("/day01.txt");
    List<Long> leftList =
        new ArrayList<>(numberLists.stream().map(List::getFirst).toList());
    leftList.sort(Long::compareTo);
    List<Long> rightList =
        new ArrayList<>(numberLists.stream().map(List::getLast).toList());
    rightList.sort(Long::compareTo);
    Long result = 0L;
    for (int i = 0; i < numberLists.size(); i++) {
      result += Math.abs(leftList.get(i) - rightList.get(i));
    }
    System.out.println(result);
    Long result2 =
        leftList.stream()
            .mapToLong(i -> rightList.stream().filter(x -> x.equals(i)).toList().size() * i)
            .sum();
    System.out.println(result2);
  }
}
