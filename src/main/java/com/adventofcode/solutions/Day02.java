package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Day02 {
  public Day02() throws URISyntaxException, FileNotFoundException {
    List<List<Long>> reports = InputReader.readAsLongMatrix("/day02.txt");
    System.out.printf("Part one: %s%n", reports.stream().filter(this::checkReport).toList().size());
    System.out.printf(
        "Part two: %s%n", reports.stream().filter(this::checkDampenedReport).toList().size());
  }

  private boolean checkDampenedReport(List<Long> report) {
    boolean safe = checkReport(report);
    if (safe) {
      return true;
    } else {
      List<List<Long>> subReports = new ArrayList<>();
      for (int i = 0; i < report.size(); i++) {
        List<Long> newReport = new ArrayList<>(report);
        newReport.remove(i);
        subReports.add(newReport);
      }
      return subReports.stream().anyMatch(this::checkReport);
    }
  }

  private boolean checkReport(List<Long> report) {
    boolean ascending = false;
    boolean descending = false;
    for (int i = 0; i < report.size() - 1; i++) {
      long diff = report.get(i) - report.get(i + 1);
      if (diff < 0) {
        ascending = true;
      } else if (diff > 0) {
        descending = true;
      }
      if (ascending && descending || (diff == 0 || Math.abs(diff) > 3)) {
        return false;
      }
    }
    return true;
  }
}
