package com.adventofcode.utils;

public enum Direction {
  NORTH,
  EAST,
  SOUTH,
  WEST;

  public Direction getNextDirection() {
    switch (this) {
      case NORTH -> {
        return EAST;
      }
      case EAST -> {
        return SOUTH;
      }
      case SOUTH -> {
        return WEST;
      }
      case WEST -> {
        return NORTH;
      }
    }
    return this;
  }
}
