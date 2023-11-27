package com.fmm.dto;

public class MonsterDto {

    private Long id;

    private String name;

    private String genus;

    private String species;

    private long attack;

    private long defence;

    private long brains;

    private long tricks;

    private boolean alive;

    private String potionEquipped;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public long getAttack() {
        return attack;
    }

    public void setAttack(long attack) {
        this.attack = attack;
    }

    public long getDefence() {
        return defence;
    }

    public void setDefence(long defence) {
        this.defence = defence;
    }

    public long getBrains() {
        return brains;
    }

    public void setBrains(long brains) {
        this.brains = brains;
    }

    public long getTricks() {
        return tricks;
    }

    public void setTricks(long tricks) {
        this.tricks = tricks;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getPotionEquipped() {
        return potionEquipped;
    }

    public void setPotionEquipped(String potionEquipped) {
        this.potionEquipped = potionEquipped;
    }
}
