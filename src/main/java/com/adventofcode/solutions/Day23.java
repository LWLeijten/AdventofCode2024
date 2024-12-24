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
      String pcA = line.split("-")[0];
      String pcB = line.split("-")[1];

      Optional<Computer> pcAComputer = lanParty.getComputer(pcA);
      Optional<Computer> pcBComputer = lanParty.getComputer(pcB);

      if (pcAComputer.isPresent() && pcBComputer.isPresent()) {
        pcAComputer.get().addConnection(pcBComputer.get());
        pcBComputer.get().addConnection(pcAComputer.get());
      } else if (pcAComputer.isPresent()) {
        Computer b = new Computer(pcB);
        pcAComputer.get().addConnection(b);
        b.addConnection(pcAComputer.get());
        lanParty.computers.add(b);
      } else if (pcBComputer.isPresent()) {
        Computer a = new Computer(pcA);
        pcBComputer.get().addConnection(a);
        a.addConnection(pcBComputer.get());
        lanParty.computers.add(a);
      } else {
        Computer a = new Computer(pcA);
        Computer b = new Computer(pcB);
        a.addConnection(b);
        b.addConnection(a);
        lanParty.computers.addAll(List.of(a, b));
      }
    }

    System.out.printf("Part one: %s%n", lanParty.getInterconnectedComputers().size());
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
      computers.stream().filter(c -> c.getName().startsWith("t")).forEach(c -> {
        List<Computer> connections = c.getConnections();
        for (int i = 0; i < connections.size(); i++) {
          Computer computer = connections.get(i);
          List<Computer> mutuals = connections.subList(i + 1, connections.size()).stream()
              .filter(cc -> cc.getConnections().contains(computer)).toList();
          for (Computer mutual : mutuals) {
            List<Computer> ccc = new ArrayList<>(List.of(c, computer, mutual));
            ccc.sort(Comparator.comparing(Computer::getName));
            interconnectedComputers.add(ccc);
          }
        }
      });
      return interconnectedComputers;
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
