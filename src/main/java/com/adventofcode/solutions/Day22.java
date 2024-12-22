package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Day22 {
  HashMap<Long, Long> secretNumberCache;
  Integer iterations = 2000;

  public Day22() throws FileNotFoundException, URISyntaxException {
    List<Long> input =
        InputReader.readInput("/day22.txt").stream().mapToLong(Long::parseLong).boxed().toList();
    secretNumberCache = new HashMap<>();

    List<Long> results = new ArrayList<>();
    List<List<Long>> priceList = new ArrayList<>();
    Set<List<Long>> changeSequences = new HashSet<>();

    // Part one
    input.forEach(
        secret -> {
          List<Long> prices = new ArrayList<>();
          List<List<Long>> priceSequences = new ArrayList<>();
          prices.add(getLastDigitOfLong(secret));
          Long newResult = secret;
          for (int j = 0; j < iterations; j++) {
            newResult = calculateSecretNumber(newResult);
            prices.add(getLastDigitOfLong(newResult));
            if (prices.size() == 5) {
              priceSequences.add(
                  List.of(
                      prices.get(1) - prices.get(0),
                      prices.get(2) - prices.get(1),
                      prices.get(3) - prices.get(2),
                      prices.get(4) - prices.get(3)));
            } else if (prices.size() > 5) {
              List<Long> previousSequence = new ArrayList<>(priceSequences.getLast());
              previousSequence.removeFirst();
              previousSequence.add(prices.get(j + 1) - prices.get(j));
              priceSequences.add(previousSequence);
            }
          }
          changeSequences.addAll(priceSequences);
          priceList.add(prices);
          results.add(newResult);
        });

    // Part two
    ConcurrentHashMap<List<Long>, Long> profitMap = new ConcurrentHashMap<>();
    changeSequences.parallelStream()
        .filter(sequence -> sequence.stream().reduce(Long::sum).get() > 0)
        .forEach(
            sequence -> {
              Long profits = 0L;
              for (List<Long> pl : priceList) {
                for (int i = 4; i < pl.size(); i++) {
                  if (sequence.get(0).equals(pl.get(i - 3) - pl.get(i - 4))
                      && sequence.get(1).equals(pl.get(i - 2) - pl.get(i - 3))
                      && sequence.get(2).equals(pl.get(i - 1) - pl.get(i - 2))
                      && sequence.get(3).equals(pl.get(i) - pl.get(i - 1))) {
                    profits += pl.get(i);
                    break;
                  }
                }
              }
              profitMap.put(sequence, profits);
            });

    System.out.printf("Part one: %s%n", results.stream().reduce(Long::sum).get());
    System.out.printf("Part two: %s%n", profitMap.values().stream().max(Long::compare).get());
  }

  private Long calculateSecretNumber(Long secret) {
    Long start = secret;
    if (!secretNumberCache.containsKey(start)) {
      secret = (secret ^ (secret * 64)) % 16777216;
      secret = (secret ^ (secret / 32)) % 16777216;
      secret = (secret ^ (secret * 2048)) % 16777216;
      secretNumberCache.put(start, secret);
    }
    return secretNumberCache.get(start);
  }

  private Long getLastDigitOfLong(Long number) {
    return number % 10;
  }
}
