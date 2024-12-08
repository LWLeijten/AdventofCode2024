package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import com.adventofcode.utils.Tuple;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Day08 {
  public Day08() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day08.txt");
    int height = input.size();
    int width = input.get(0).length();
    HashMap<Character, List<Tuple<Integer>>> antennas = createAntennaMap(height, input, width);

    System.out.printf("Part one: %s%n", getAntiNodes(antennas, width, height, true).size());
    System.out.printf("Part two: %s%n", getAntiNodes(antennas, width, height, false).size());
  }

  private HashMap<Character, List<Tuple<Integer>>> createAntennaMap(
      int height, List<String> input, int width) {
    HashMap<Character, List<Tuple<Integer>>> antennas = new HashMap<>();
    for (int y = 0; y < height; y++) {
      String row = input.get(y);
      for (int x = 0; x < width; x++) {
        Character character = row.charAt(x);
        if (character != '.') {
          if (antennas.containsKey(character)) {
            antennas.get(character).add(new Tuple<>(x, y));
          } else {
            antennas.put(character, new ArrayList<>(List.of(new Tuple<>(x, y))));
          }
        }
      }
    }
    return antennas;
  }

  private HashSet<Tuple<Integer>> getAntiNodes(
      HashMap<Character, List<Tuple<Integer>>> antennas, int width, int height, boolean single) {
    HashSet<Tuple<Integer>> antiNodes = new HashSet<>();
    for (List<Tuple<Integer>> frequencies : antennas.values()) {
      for (int i = 0; i < frequencies.size() - 1; i++) {
        Tuple<Integer> antennaA = frequencies.get(i);
        for (int j = i + 1; j < frequencies.size(); j++) {
          Tuple<Integer> antennaB = frequencies.get(j);
          int dX = antennaB.elem1() - antennaA.elem1();
          int dY = antennaB.elem2() - antennaA.elem2();
          List<Tuple<Integer>> antis;
          if (single) {
            antis =
                List.of(
                    new Tuple<>(antennaA.elem1() - dX, antennaA.elem2() - dY),
                    new Tuple<>(antennaB.elem1() + dX, antennaB.elem2() + dY));
          } else {
            antis = new ArrayList<>();
            Tuple<Integer> negative = new Tuple<>(antennaB.elem1() - dX, antennaB.elem2() - dY);
            while (coordinateInBounds(width, height, negative)) {
              antis.add(negative);
              negative = new Tuple<>(negative.elem1() - dX, negative.elem2() - dY);
            }
            Tuple<Integer> positive = new Tuple<>(antennaA.elem1() + dX, antennaA.elem2() + dY);
            while (coordinateInBounds(width, height, positive)) {
              antis.add(positive);
              positive = new Tuple<>(positive.elem1() + dX, positive.elem2() + dY);
            }
          }
          antis.stream().filter(a -> coordinateInBounds(width, height, a)).forEach(antiNodes::add);
        }
      }
    }
    return antiNodes;
  }

  private boolean coordinateInBounds(int width, int height, Tuple<Integer> coord) {
    return coord.elem1() >= 0
        && coord.elem1() < width
        && coord.elem2() >= 0
        && coord.elem2() < height;
  }
}
