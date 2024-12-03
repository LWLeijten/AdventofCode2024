package com.adventofcode.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputReader {
  public static List<String> readInput(String filePath)
      throws URISyntaxException, FileNotFoundException {
    URL is = InputReader.class.getResource(filePath);
    File file = new File(is.toURI());
    List<String> list = new ArrayList<>();
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      list.add(scanner.nextLine());
    }
    scanner.close();
    return list;
  }

  public static List<List<Long>> readAsLongMatrix(String filePath)
      throws FileNotFoundException, URISyntaxException {
    List<String> stringList = readInput(filePath);
    return stringList.stream()
        .map(s -> Arrays.stream(s.split("\\s+"))
            .map(Long::parseLong).toList())
        .toList();
  }

  public static String readAsString(String filePath)
      throws FileNotFoundException, URISyntaxException {
    URL is = InputReader.class.getResource(filePath);
    File file = new File(is.toURI());
    List<String> list = new ArrayList<>();
    return new Scanner(file).useDelimiter("\\Z").next();
  }
}
