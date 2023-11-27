package com.fmm.enumeration;

public enum Level {
    STANDARD(100),
    EXTRA(1000),
    SUPER(10000),
    CUSTOM(101);

    private final int cost;

    Level(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}