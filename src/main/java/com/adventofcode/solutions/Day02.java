package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

public class Day02 {
  public Day02() throws URISyntaxException, FileNotFoundException {
    List<List<Long>> reports = InputReader.readAsLongMatrix("/day02.txt");
    System.out.println(reports.stream().filter(this::reportIsSafe).toList().size());
  }

  private boolean reportIsSafe(List<Long> report) {
    boolean ascending = false;
    boolean descending = false;
    boolean invalidGap = false;
    for (int i = 0; i < report.size() - 1; i++) {
      long diff = report.get(i) - report.get(i + 1);
      if (diff < 0) {
        ascending = true;
      } else if (diff > 0 ) {
        descending = true;
      }
      if (Math.abs(diff) == 0 || Math.abs(diff) > 3) {
        invalidGap = true;
      }
    }
    return !(ascending && descending) && !invalidGap;
  }
}
