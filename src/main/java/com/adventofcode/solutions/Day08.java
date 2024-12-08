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
    HashMap<Character, List<Tuple<Integer>>> antennas = new HashMap<>();
    int height = input.size();
    int width = input.get(0).length();

    // Create map of antennas
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

    HashSet<Tuple<Integer>> antinodes = getAntinodes(antennas, width, height);
    System.out.println(antinodes.size());
    //antinodes.forEach(a -> System.out.println(a));
  }

  private HashSet<Tuple<Integer>> getAntinodes(HashMap<Character, List<Tuple<Integer>>> antennas, int width, int height) {
    HashSet<Tuple<Integer>> antinodes = new HashSet<>();
    for (List<Tuple<Integer>> frequencies : antennas.values()) {
      for (int i = 0; i < frequencies.size() - 1; i++) {
        Tuple<Integer> antennaA = frequencies.get(i);
        for (int j = i + 1; j < frequencies.size(); j++) {
          Tuple<Integer> antennaB = frequencies.get(j);
          int dX = antennaB.elem1() - antennaA.elem1();
          int dY = antennaB.elem2() - antennaA.elem2();
          List<Tuple<Integer>> antis =
                  List.of(
                          new Tuple<>(antennaA.elem1() - dX, antennaA.elem2() - dY),
                          new Tuple<>(antennaB.elem1() + dX, antennaB.elem2() + dY));
          antis.forEach(a -> {
            if (a.elem1() >= 0 && a.elem1() < width && a.elem2() >= 0 && a.elem2() < height) {
              antinodes.add(a);
            }
          });
        }
      }
    }
    return antinodes;
  }
}
