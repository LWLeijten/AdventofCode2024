package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Day22 {
  ConcurrentHashMap<Long, Long> secretNumberCache;
  ConcurrentHashMap<List<Long>, Long> profitMap;
  Integer iterations = 2000;

  public Day22() throws FileNotFoundException, URISyntaxException {
    List<Long> input =
        InputReader.readInput("/day22.txt").stream().mapToLong(Long::parseLong).boxed().toList();
    secretNumberCache = new ConcurrentHashMap<>();
    profitMap = new ConcurrentHashMap<>();
    List<Long> results = new ArrayList<>();

    input.stream()
        .parallel()
        .forEach(
            secret -> {
              HashSet<List<Long>> seenSequences = new HashSet<>();
              List<Long> prices = new ArrayList<>();
              List<List<Long>> priceSequences = new ArrayList<>();
              prices.add(getLastDigitOfLong(secret));
              for (int j = 0; j < iterations; j++) {
                secret = calculateSecretNumber(secret);
                prices.add(getLastDigitOfLong(secret));
                // The first 4 sequence of deltas we can create
                if (prices.size() == 5) {
                  List<Long> sequence =
                      List.of(
                          prices.get(1) - prices.get(0),
                          prices.get(2) - prices.get(1),
                          prices.get(3) - prices.get(2),
                          prices.get(4) - prices.get(3));
                  priceSequences.add(sequence);
                  seenSequences.add(sequence);
                  profitMap.merge(sequence, prices.get(4), Long::sum);
                }
                // All further deltas, which we can base on the previous delta with 1 number
                // replaced
                else if (prices.size() > 5) {
                  List<Long> previousSequence = new ArrayList<>(priceSequences.getLast());
                  previousSequence.removeFirst();
                  previousSequence.add(prices.get(j + 1) - prices.get(j));
                  priceSequences.add(previousSequence);
                  if (!seenSequences.contains(previousSequence)) {
                    profitMap.merge(previousSequence, prices.get(j + 1), Long::sum);
                    seenSequences.add(previousSequence);
                  }
                }
              }
              results.add(secret);
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
