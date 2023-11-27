package com.fmm.enumeration;

public enum Species {
    ORC(Genus.BEASTS),
    FUR_BALL(Genus.BEASTS),
    DINOSAUR(Genus.BEASTS),
    SEABEAST(Genus.BEASTS),

    MUTANT_PET(Genus.MUTANTS),
    ICE(Genus.MUTANTS),
    //ROBOT_MUTANT(Genus.MUTANTS), //only card not found
    VIRUS(Genus.MUTANTS),

    SLIME_BEAST(Genus.ALIENS),
    PLASTICO(Genus.ALIENS),

    DEVIL(Genus.FANTASTICALS),
    VAMPIRE(Genus.FANTASTICALS),
    UNDEAD(Genus.FANTASTICALS),
    SPIRIT(Genus.FANTASTICALS),
    FAIRY(Genus.FANTASTICALS),

    STINGER(Genus.INSECTS),
    ARACHNID(Genus.INSECTS),
    SUPER_CLAW(Genus.INSECTS),

    MECHANOID(Genus.ROBOTS),
    SMASHER_BOT(Genus.ROBOTS),
    INTELLI_BOT(Genus.ROBOTS);

    private final Genus genus;

    Species(Genus genus) {
        this.genus = genus;
    }

    public Genus getGenus() {
        return genus;
    }
}