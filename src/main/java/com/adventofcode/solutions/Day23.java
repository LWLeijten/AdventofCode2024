package com.adventofcode.solutions;

import com.adventofcode.utils.InputReader;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

public class Day23 {
  public Day23() throws FileNotFoundException, URISyntaxException {
    List<String> input = InputReader.readInput("/day23.txt");

    LanParty lanParty = new LanParty();
    for (String line : input) {
      String pcNameA = line.split("-")[0];
      String pcNameB = line.split("-")[1];

      Optional<Computer> optionalPcA = lanParty.getComputer(pcNameA);
      Optional<Computer> optionalPcB = lanParty.getComputer(pcNameB);

      if (optionalPcA.isPresent() && optionalPcB.isPresent()) {
        optionalPcA.get().addConnection(optionalPcB.get());
        optionalPcB.get().addConnection(optionalPcA.get());
      } else if (optionalPcA.isPresent()) {
        Computer pcB = new Computer(pcNameB);
        optionalPcA.get().addConnection(pcB);
        pcB.addConnection(optionalPcA.get());
        lanParty.computers.add(pcB);
      } else if (optionalPcB.isPresent()) {
        Computer pcA = new Computer(pcNameA);
        optionalPcB.get().addConnection(pcA);
        pcA.addConnection(optionalPcB.get());
        lanParty.computers.add(pcA);
      } else {
        Computer pcA = new Computer(pcNameA);
        Computer pcB = new Computer(pcNameB);
        pcA.addConnection(pcB);
        pcB.addConnection(pcA);
        lanParty.computers.addAll(List.of(pcA, pcB));
      }
    }

    System.out.printf("Part one: %s%n", lanParty.getInterconnectedComputers().size());
    System.out.printf(
        "Part two: %s%n",
        String.join(
            ",", lanParty.getLargestNetwork().stream().map(Computer::getName).sorted().toList()));
  }

  @Getter
  @Setter
  private static class LanParty {
    private List<Computer> computers;

    public LanParty() {
      computers = new ArrayList<>();
    }

    public Optional<Computer> getComputer(String name) {
      return computers.stream().filter(c -> c.getName().equals(name)).findFirst();
    }

    public Set<List<Computer>> getInterconnectedComputers() {
      Set<List<Computer>> interconnectedComputers = new HashSet<>();
      computers.stream()
          .filter(c -> c.getName().startsWith("t"))
          .forEach(
              c -> {
                List<Computer> connections = c.getConnections();
                for (int i = 0; i < connections.size(); i++) {
                  Computer computer = connections.get(i);
                  List<Computer> mutuals =
                      connections.subList(i + 1, connections.size()).stream()
                          .filter(cc -> cc.getConnections().contains(computer))
                          .toList();
                  for (Computer mutual : mutuals) {
                    List<Computer> ccc = new ArrayList<>(List.of(c, computer, mutual));
                    ccc.sort(Comparator.comparing(Computer::getName));
                    interconnectedComputers.add(ccc);
                  }
                }
              });
      return interconnectedComputers;
    }

    public List<Computer> getLargestNetwork() {
      List<Computer> largestNetwork = List.of();
      for (Computer computer : computers) {
        List<Computer> largestComputerNetwork = getLargestNetworkIncludingComputer(computer);
        if (largestComputerNetwork.size() > largestNetwork.size()) {
          largestNetwork = largestComputerNetwork;
        }
      }
      return largestNetwork;
    }

    private List<Computer> getLargestNetworkIncludingComputer(Computer computer) {
      List<List<Computer>> subsets = new ArrayList<>();
      List<Computer> startSubset = new ArrayList<>();
      getSubsets(computer.getConnections(), 0, subsets, startSubset);
      subsets.sort((Comparator.comparingInt(List::size)));
      for (List<Computer> subset : subsets.reversed()) {
        boolean valid = true;
        for (int i = 0; i < subset.size(); i++) {
          HashSet<Computer> connections = new HashSet<>(subset.get(i).connections);
          List<Computer> sublist = subset.subList(i + 1, subset.size());
          if (!connections.containsAll(sublist)) {
            valid = false;
            break;
          }
        }
        if (valid) {
          subset.add(computer);
          return subset;
        }
      }
      return List.of();
    }

    private void getSubsets(
        List<Computer> computers, int i, List<List<Computer>> subsets, List<Computer> subset) {
      if (i == computers.size() - 1) {
        subsets.add(new ArrayList<>(subset));
        return;
      }
      subset.add(computers.get(i));
      getSubsets(computers, i + 1, subsets, subset);
      subset.removeLast();
      getSubsets(computers, i + 1, subsets, subset);
    }
  }

  @Getter
  @Setter
  private static class Computer {
    private String name;
    private List<Computer> connections;

    public Computer(String name) {
      this.name = name;
      connections = new ArrayList<>();
    }

    void addConnection(Computer computer) {
      connections.add(computer);
    }
  }
}
