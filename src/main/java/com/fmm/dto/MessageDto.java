package com.fmm.dto;

public class MessageDto {

    private Long toAccountId;

    private String typeOfFight;

    private String toMonsterName;

    private String fromMonsterName;

    private Long nuggetsForAccepting;

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getTypeOfFight() {
        return typeOfFight;
    }

    public void setTypeOfFight(String typeOfFight) {
        this.typeOfFight = typeOfFight;
    }

    public String getToMonsterName() {
        return toMonsterName;
    }

    public void setToMonsterName(String toMonsterName) {
        this.toMonsterName = toMonsterName;
    }

    public String getFromMonsterName() {
        return fromMonsterName;
    }

    public void setFromMonsterName(String fromMonsterName) {
        this.fromMonsterName = fromMonsterName;
    }

    public Long getNuggetsForAccepting() {
        return nuggetsForAccepting;
    }

    public void setNuggetsForAccepting(Long nuggetsForAccepting) {
        this.nuggetsForAccepting = nuggetsForAccepting;
    }
}
