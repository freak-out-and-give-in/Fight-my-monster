package com.fmm.dto;

public class FightRequestDto {

    private String opponentUsername;
    private String opponentMonsterName;
    private int n;
    private int optionIndex;
    private String typeOfFight;
    private long nuggetsForAccepting;

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }

    public String getOpponentMonsterName() {
        return opponentMonsterName;
    }

    public void setOpponentMonsterName(String opponentMonsterName) {
        this.opponentMonsterName = opponentMonsterName;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getOptionIndex() {
        return optionIndex;
    }

    public void setOptionIndex(int optionIndex) {
        this.optionIndex = optionIndex;
    }

    public String getTypeOfFight() {
        return typeOfFight;
    }

    public void setTypeOfFight(String typeOfFight) {
        this.typeOfFight = typeOfFight;
    }

    public long getNuggetsForAccepting() {
        return nuggetsForAccepting;
    }

    public void setNuggetsForAccepting(long nuggetsForAccepting) {
        this.nuggetsForAccepting = nuggetsForAccepting;
    }
}
