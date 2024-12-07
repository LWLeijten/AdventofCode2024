package com.adventofcode.utils;

import lombok.Data;

import java.util.List;

@Data
public class Node<T> {
  final T value;
  final List<Node<T>> children;

  public Node(T value, List<Node<T>> children) {
    this.value = value;
    this.children = children;
  }
}
