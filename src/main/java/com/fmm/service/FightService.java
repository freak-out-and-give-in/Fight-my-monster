package com.fmm.service;

import com.fmm.dto.FightRequestDto;
import com.fmm.dto.MonsterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FightService {

    private final BattleService battleService;

    @Autowired
    public FightService(BattleService battleService) {
        this.battleService = battleService;
    }

    public FightRequestDto calculateN(FightRequestDto fightRequestDto, int myMonstersSize) {
        if (fightRequestDto.getN() >= myMonstersSize) {
            fightRequestDto.setN(0);
        } else if (fightRequestDto.getN() < 0) {
            fightRequestDto.setN(myMonstersSize - 1);
        }

        return fightRequestDto;
    }

    public int calculateMyChanceBarAmount(MonsterDto myCurrentMonster, MonsterDto opponentMonster) {
        int myChanceBarAmount = (int) battleService.calculatePercentageChanceToWin(myCurrentMonster, opponentMonster) / 10;

        if (myChanceBarAmount <= 0) {
            myChanceBarAmount = 1;
        } else if (myChanceBarAmount >= 10) {
            myChanceBarAmount = 9;
        }

        return myChanceBarAmount;
    }
}
