package com.fmm.enumeration;

public enum TypeOfFight {

    BITE("Bite"),
    COLLECT("Collect"),
    EAT("Eat");

    private final String displayName;

    TypeOfFight(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
