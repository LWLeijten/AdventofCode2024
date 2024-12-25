package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Day24 {
  private static final String gateRegex = "(.+) (AND|OR|XOR) (.+) -> (.+)";
  List<Gate> gates;

  public Day24() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day24.txt");

    boolean readingWires = true;
    List<Wire> setWires = new ArrayList<>();
    gates = new ArrayList<>();

    for (String line : input) {
      if (line.isEmpty()) {
        readingWires = false;
      } else if (readingWires) {
        String[] split = line.split(": ");
        setWires.add(new Wire(split[0], Integer.parseInt(split[1])));
      } else {
        Matcher matcher = Pattern.compile(gateRegex).matcher(line);
        if (matcher.find()) {
          gates.add(
              new Gate(
                  matcher.group(1),
                  matcher.group(3),
                  matcher.group(4),
                  Operand.fromString(matcher.group(2)),
                  false));
        }
      }
    }

    while (!gates.stream().allMatch(Gate::isHandled)) {
      List<Gate> unhandledGates = gates.stream().filter(g -> !g.isHandled()).toList();
      for (Gate gate : unhandledGates) {
        if (new HashSet<>(setWires.stream().map(Wire::getLabel).toList())
            .containsAll(List.of(gate.wireA, gate.wireB))) {
          Wire wireA =
              setWires.stream().filter(w -> w.getLabel().equals(gate.wireA)).findFirst().get();
          Wire wireB =
              setWires.stream().filter(w -> w.getLabel().equals(gate.wireB)).findFirst().get();
          switch (gate.operand) {
            case AND ->
                setWires.add(
                    new Wire(gate.wireOutput, wireA.value == 1 && wireB.value == 1 ? 1 : 0));
            case OR ->
                setWires.add(
                    new Wire(gate.wireOutput, wireA.value == 0 && wireB.value == 0 ? 0 : 1));
            case XOR -> setWires.add(new Wire(gate.wireOutput, wireA.value != wireB.value ? 1 : 0));
          }
          gate.setHandled(true);
        }
      }
    }

    StringBuilder sb = new StringBuilder();
    for (Wire wire :
        setWires.stream()
            .filter(w -> w.label.startsWith("z"))
            .sorted(Comparator.comparing(Wire::getLabel).reversed())
            .toList()) {
      sb.append(wire.value);
    }

    System.out.printf("Part one: %s%n", Long.parseLong(sb.toString(), 2));

    // Every z output is a XOR.
    // First bit : x0 XOR y0 -> z0
    // Second bit: wqc:(y00 AND x00) XOR qtf:(x01 XOR y01) -> z01
    // Third bit : cpb:(djn:(wqc:(x01 XOR y01) AND qtf(y00 AND x00)) OR tnr:(y01 AND x01)) XOR
    // wmr:(x02 XOR y02) -> z02
    // Output always is a combination of that indexes XOR (e.g. x02 XOR yo2 -> z02) and the carry of
    // everything before
    // Expand all the logic gates, print them and spot the pattern
    //
    // The chain of carry bits alternates between GREEN and ORANGE. (AND/OR)/
    // The input pairs X00 Y00 always have a AND and a XOR.
    // The outputs (Zxx) always are formed by XOR, based on ORANGE(OR) carry bit and the RED(XOR) of the inputs.

    System.out.println("Part two: Done by visualizing the graphviz and finding mistakes by eye");
    //printAsGraphviz(gates, setWires);
    // https://github.com/LWLeijten/AdventofCode2024/blob/main/src/main/resources/day24
  }

  private void printAsGraphviz(List<Gate> gates, List<Wire> wires) {
    System.out.println("digraph G {");
    // Colour x, y and z's
    for (Wire wire : wires) {
      if (wire.label.startsWith("x")) {
        System.out.println("%s [color=\"lightblue\", style=\"filled\"]".formatted(wire.label));
      } else if (wire.label.startsWith("y")) {
        System.out.println("%s [color=\"lightgreen\", style=\"filled\"]".formatted(wire.label));
      } else if (wire.label.startsWith("z")) {
        System.out.println("%s [color=\"pink\", style=\"filled\"]".formatted(wire.label));
      }
    }

    // Print all edges and nodes
    for (Gate gate : gates) {
      String color = "black";
      switch (gate.operand) {
        case AND -> color = "green";
        case OR -> color = "orange";
        case XOR -> color = "red";
      }

      System.out.printf("%s -> %s [color=\"%s\"]%n", gate.wireA, gate.wireOutput, color);
      System.out.printf("%s -> %s [color=\"%s\"]%n", gate.wireB, gate.wireOutput, color);
    }
    System.out.println("}");
  }

  private String expandGate(Gate gate) {
    String wireA = gate.wireA;
    String wireB = gate.wireB;
    if (!wireA.startsWith("x")
        && !wireA.startsWith("y")
        && !wireB.startsWith("x")
        && !wireB.startsWith("y")) {
      return "(%s %s %s)"
          .formatted(
              expandGate(gates.stream().filter(g -> g.wireOutput.equals(wireA)).findFirst().get()),
              gate.operand,
              expandGate(gates.stream().filter(g -> g.wireOutput.equals(wireB)).findFirst().get()));
    } else if (!wireA.startsWith("x") && !wireA.startsWith("y")) {
      return "(%s %s %s)"
          .formatted(
              expandGate(gates.stream().filter(g -> g.wireOutput.equals(wireA)).findFirst().get()),
              gate.operand,
              wireB);
    } else if (!wireB.startsWith("x") && !wireB.startsWith("y")) {
      return "(%s %s %s)"
          .formatted(
              wireA,
              gate.operand,
              expandGate(gates.stream().filter(g -> g.wireOutput.equals(wireB)).findFirst().get()));
    }
    return "(%s %s %s)".formatted(wireA, gate.operand, wireB);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  private static class Wire {
    private String label;
    private int value;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  private static class Gate {
    private String wireA;
    private String wireB;
    private String wireOutput;
    private Operand operand;
    private boolean handled;
  }

  private enum Operand {
    XOR,
    OR,
    AND;

    public static Operand fromString(String operandString) {
      switch (operandString) {
        case "XOR" -> {
          return Operand.XOR;
        }
        case "OR" -> {
          return Operand.OR;
        }
        case "AND" -> {
          return Operand.AND;
        }
      }
      return Operand.XOR;
    }
  }
}
