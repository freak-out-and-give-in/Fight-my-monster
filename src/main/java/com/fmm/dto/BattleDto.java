package com.fmm.dto;

public class BattleDto {

    private long messageId;

    private int battleIndex;

    private double percentageChanceToWin;

    private boolean didIWin;

    private int degreesChance;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getBattleIndex() {
        return battleIndex;
    }

    public void setBattleIndex(int battleIndex) {
        this.battleIndex = battleIndex;
    }

    public double getPercentageChanceToWin() {
        return percentageChanceToWin;
    }

    public void setPercentageChanceToWin(double percentageChanceToWin) {
        this.percentageChanceToWin = percentageChanceToWin;
    }

    public boolean isDidIWin() {
        return didIWin;
    }

    public void setDidIWin(boolean didIWin) {
        this.didIWin = didIWin;
    }

    public int getDegreesChance() {
        return degreesChance;
    }

    public void setDegreesChance(int degreesChance) {
        this.degreesChance = degreesChance;
    }
}
