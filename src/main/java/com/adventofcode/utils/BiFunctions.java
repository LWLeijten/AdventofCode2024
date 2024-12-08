package com.adventofcode.utils;

import java.util.function.BiFunction;

public class BiFunctions {
    public static BiFunction<Long, Long, Long> add = Long::sum;
    public static BiFunction<Long, Long, Long> multiply = (a, b) -> a * b;
    public static BiFunction<Long, Long, Long> concat = (a, b) -> Long.parseLong(a.toString().concat(b.toString()));
}
