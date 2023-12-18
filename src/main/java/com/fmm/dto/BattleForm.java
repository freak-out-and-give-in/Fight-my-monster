package com.fmm.dto;

public class BattleForm {

    private MonsterDto myMonsterBefore;

    private MonsterDto opponentMonsterBefore;

    private MonsterDto myMonsterAfter;

    private MonsterDto opponentMonsterAfter;

    private int battleIndex;

    private int shownPercentageChanceToWin;

    private boolean didIWin;

    private int randomDegrees;

    private int chanceToWinGraphic;

    private int battleBackground;

    public MonsterDto getMyMonsterBefore() {
        return myMonsterBefore;
    }

    public void setMyMonsterBefore(MonsterDto myMonsterBefore) {
        this.myMonsterBefore = myMonsterBefore;
    }

    public MonsterDto getOpponentMonsterBefore() {
        return opponentMonsterBefore;
    }

    public void setOpponentMonsterBefore(MonsterDto opponentMonsterBefore) {
        this.opponentMonsterBefore = opponentMonsterBefore;
    }

    public MonsterDto getMyMonsterAfter() {
        return myMonsterAfter;
    }

    public void setMyMonsterAfter(MonsterDto myMonsterAfter) {
        this.myMonsterAfter = myMonsterAfter;
    }

    public MonsterDto getOpponentMonsterAfter() {
        return opponentMonsterAfter;
    }

    public void setOpponentMonsterAfter(MonsterDto opponentMonsterAfter) {
        this.opponentMonsterAfter = opponentMonsterAfter;
    }

    public int getBattleIndex() {
        return battleIndex;
    }

    public void setBattleIndex(int battleIndex) {
        this.battleIndex = battleIndex;
    }

    public int getShownPercentageChanceToWin() {
        return shownPercentageChanceToWin;
    }

    public void setShownPercentageChanceToWin(int shownPercentageChanceToWin) {
        this.shownPercentageChanceToWin = shownPercentageChanceToWin;
    }

    public boolean isDidIWin() {
        return didIWin;
    }

    public void setDidIWin(boolean didIWin) {
        this.didIWin = didIWin;
    }

    public int getRandomDegrees() {
        return randomDegrees;
    }

    public void setRandomDegrees(int randomDegrees) {
        this.randomDegrees = randomDegrees;
    }

    public int getChanceToWinGraphic() {
        return chanceToWinGraphic;
    }

    public void setChanceToWinGraphic(int chanceToWinGraphic) {
        this.chanceToWinGraphic = chanceToWinGraphic;
    }

    public int getBattleBackground() {
        return battleBackground;
    }

    public void setBattleBackground(int battleBackground) {
        this.battleBackground = battleBackground;
    }
}
