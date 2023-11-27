package com.fmm.enumeration;

public enum Potion {
    DEMON_ATTACK(100, 2, "Increases Attack X4. Lasts for 2 fights"),
    TRICKS_MAKER(100, 2, "Increases Tricks X4. Lasts for 2 fights"),
    TOUGH_GUY(250, 4, "Increases Defense and Brains X2. Lasts for 4 fights"),
    ADVANTAGE_KILLER(500, 3, "Cancel the advantage of the opponent. Lasts for 3 fights"),
    MYSTERIO(1000, 2, "Attack and Tricks X2 and your opponent cannot see their fight odds. Lasts 2 fights"),
    INCREDIBLE_HULK(2000, 5, "Increases all powers X4. Lasts 5 fights. Members only"),
    MYSTERIO_RAGE(3000, 2, "Attacks and Tricks X5 and your opponent cannot see their fight odds. Lasts 2 fights. Members only"),
    GIANT_SLAYER(5000, 3, "Your opponents powers are decreased X10. Lasts 3 fights. Members only");

    private final int cost;
    private final int uses;
    private final String description;

    Potion(int cost, int uses, String description) {
        this.cost = cost;
        this.uses = uses;
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public int getUses() {
        return uses;
    }

    public String getDescription() {
        return description;
    }
}