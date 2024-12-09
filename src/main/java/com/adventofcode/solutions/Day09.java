package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Day09 {
  public Day09() throws FileNotFoundException, URISyntaxException {
    String input = InputReader.readAsString("/day09.txt");
    List<Block> compactedBlocks = compactBlocks(getBlocks(input));
    List<Block> compactedFiles = compactFiles(getBlocks(input));
    System.out.printf("Part one: %s%n", calculateChecksum(compactedBlocks));
    System.out.printf("Part two: %s%n", calculateChecksum(compactedFiles));
  }

  private static long calculateChecksum(List<Block> compactedBlocks) {
    long result = 0L;
    for (int i = 0; i < compactedBlocks.size(); i++) {
      Long value = compactedBlocks.get(i).fileId();
      if (value != null) {
        result += i * value;
      }
    }
    return result;
  }

  private List<Block> compactFiles(List<Block> blocks) {
    List<Block> compactedBlocks = new ArrayList<>(blocks);
    Long curFile =
        compactedBlocks.stream()
            .map(b -> b.fileId() != null ? b.fileId() : -1L)
            .max(Long::compare)
            .get();
    while (curFile >= 0) {
      int end = compactedBlocks.stream().map(Block::fileId).toList().lastIndexOf(curFile);
      int start = end;
      while (start > 0 && Objects.equals(compactedBlocks.get(start).fileId(), curFile)) {
        start--;
      }
      start++;
      int fileSize = (end - start) + 1;
      int curGapStart = -1;
      int curGapSize = 0;
      for (int i = 0; i <= start; i++) {
        if (curGapSize == fileSize) {
          for (int j = curGapStart; j < curGapStart + curGapSize; j++) {
            compactedBlocks.set(j, new Block(curFile));
          }
          for (int j = start; j <= end; j++) {
            compactedBlocks.set(j, new Block(null));
          }
          break;
        }
        if (compactedBlocks.get(i).fileId() == null) {
          curGapSize++;
          if (curGapStart == -1) {
            curGapStart = i;
          }
        } else {
          curGapSize = 0;
          curGapStart = -1;
        }
      }
      curFile--;
    }
    return compactedBlocks;
  }

  private List<Block> compactBlocks(List<Block> blocks) {
    List<Block> compactedBlocks = new ArrayList<>();
    int leftIndex = 0;
    int rightIndex = blocks.size() - 1;
    while (leftIndex <= rightIndex) {
      if (blocks.get(leftIndex).fileId() != null) {
        compactedBlocks.add(blocks.get(leftIndex));
      } else {
        while (blocks.get(rightIndex).fileId() == null) {
          rightIndex--;
        }
        compactedBlocks.add(blocks.get(rightIndex));
        rightIndex--;
      }
      leftIndex++;
    }
    return compactedBlocks;
  }

  private List<Block> getBlocks(String input) {
    List<Block> blocks = new ArrayList<>();
    boolean readingBlock = true;
    long fileId = 0;
    for (char c : input.toCharArray()) {
      long blockCount = Long.parseLong(String.valueOf(c));
      if (readingBlock) {
        for (long i = 0; i < blockCount; i++) {
          blocks.add(new Block(fileId));
        }
        fileId++;
        readingBlock = false;
      } else {
        for (long i = 0; i < blockCount; i++) {
          blocks.add(new Block(null));
        }
        readingBlock = true;
      }
    }
    return blocks;
  }
}

record Block(Long fileId) {

}
