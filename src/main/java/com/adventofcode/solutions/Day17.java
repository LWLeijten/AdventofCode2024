package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import lombok.Getter;
import lombok.Setter;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 {
  public Day17() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day17.txt");
    Long registerA = Long.parseLong(input.get(0).split(" ")[2]);
    Long registerB = Long.parseLong(input.get(1).split(" ")[2]);
    Long registerC = Long.parseLong(input.get(2).split(" ")[2]);
    List<Short> program =
        Arrays.stream(input.get(4).split(" ")[1].split(",")).map(Short::parseShort).toList();

    ChronospatialComputer computer =
        new ChronospatialComputer(program, registerA, registerB, registerC);
    computer.runProgram();
    System.out.printf(
        "Part one: %s%n",
        String.join(",", computer.output.stream().map(Object::toString).toList()));

    List<Long> results = new ArrayList<>();
    findSelfOutputtingPrograms(program, 0L, 0, registerB, registerC, results);
    System.out.printf("Part two: %s%n", results.stream().min(Long::compareTo).get());
  }

  void findSelfOutputtingPrograms(List<Short> program, Long aRegister, Integer programIndex, Long registerB, Long registerC, List<Long> results) {
    for (int i = 0; i < 8; i++) {
      // test if the current value of aRegister + i behind it (bitshift by 3 since 3 bit numbers) adds the expected value in the program
      // going from the back to the front
      Long aAttempt = (aRegister << 3) + i;
      ChronospatialComputer c = new ChronospatialComputer(program, aAttempt, registerB, registerC);
      c.runProgram();

      // Dead end, try next number
      if (!c.output.equals(program.subList(program.size() - programIndex - 1, program.size()))) {
        continue;
      }

      // End case, output is the  same as the program, add found result to results
      if (programIndex == program.size() - 1) {
        results.add(aAttempt);
        return;
      }

      // Recursive call for the next 3bit number.
      findSelfOutputtingPrograms(program, aAttempt, programIndex + 1, registerB, registerC, results);
    }
  }

  @Getter
  @Setter
  private static class ChronospatialComputer {
    private Integer instructionPointer;
    private List<Short> output;
    private List<Short> program;
    private Long registerA;
    private Long registerB;
    private Long registerC;

    public ChronospatialComputer(
        List<Short> program, Long registerA, Long registerB, Long registerC) {
      this.instructionPointer = 0;
      this.output = new ArrayList<>();
      this.program = program;
      this.registerA = registerA;
      this.registerB = registerB;
      this.registerC = registerC;
    }

    void runProgram() {
      while (getInstructionPointer() < program.size()) {
        performNextInstruction();
      }
    }

    private void performNextInstruction() {
      Short opcode = program.get(instructionPointer);
      Short literalOperand = program.get(instructionPointer + 1);
      switch (opcode) {
        case 0 -> registerA = registerA / (int) Math.pow(2, getComboOperand(literalOperand));
        case 1 -> registerB = registerB ^ literalOperand;
        case 2 -> registerB = getComboOperand(literalOperand) % 8;
        case 3 -> {
          if (registerA != 0) {
            instructionPointer = Integer.valueOf(literalOperand);
            instructionPointer -= 2;
          }
        }
        case 4 -> registerB = registerB ^ registerC;
        case 5 -> output.add((short) (getComboOperand(literalOperand) % 8));
        case 6 -> registerB = registerA / (int) Math.pow(2, getComboOperand(literalOperand));
        case 7 -> registerC = registerA / (int) Math.pow(2, getComboOperand(literalOperand));
      }
      instructionPointer += 2;
    }

    Long getComboOperand(Short literalOperand) {
      switch (literalOperand) {
        case 0, 1, 2, 3 -> {
          return Long.valueOf(literalOperand);
        }
        case 4 -> {
          return registerA;
        }
        case 5 -> {
          return registerB;
        }
        case 6 -> {
          return registerC;
        }
      }
      return -1L;
    }
  }
}
