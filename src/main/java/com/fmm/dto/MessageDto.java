package com.fmm.dto;

import com.fmm.enumeration.TypeOfFight;

public class MessageDto {

    private Long toAccountId;

    private TypeOfFight typeOfFight;

    private String toMonsterName;

    private String fromMonsterName;

    private Long nuggetsForAccepting;

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public TypeOfFight getTypeOfFight() {
        return typeOfFight;
    }

    public void setTypeOfFight(TypeOfFight typeOfFight) {
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
