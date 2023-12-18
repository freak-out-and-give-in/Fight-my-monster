package com.fmm.service;

import com.fmm.dto.MessageDto;
import com.fmm.dto.MonsterDto;
import com.fmm.enumeration.TypeOfFight;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class BattleService {

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final MonsterService monsterService;

    @Autowired
    public BattleService(UserInfoService userInfoService, MessageService messageService, MonsterService monsterService) {
        this.userInfoService = userInfoService;
        this.messageService = messageService;
        this.monsterService = monsterService;
    }

    public void fight(MessageDto messageDto, boolean didIWin, User myUser, User opponentUser, Monster myMonster, Monster opponentMonster) {
        userInfoService.exchangeNuggetsForAccepting(messageDto.getNuggetsForAccepting(), opponentUser.getId(), myUser.getId());

        if (didIWin) {
            executeWinCondition(myUser, opponentUser, messageDto.getTypeOfFight(), myMonster, opponentMonster);
        } else {
            executeWinCondition(opponentUser, myUser, messageDto.getTypeOfFight(), opponentMonster, myMonster);
        }
    }

    public double calculatePercentageChanceToWin(Monster myMonster, Monster opponentMonster, MonsterDto myMonsterDto, MonsterDto opponentMonsterDto) {
        long myMonsterTotalStats = changeStatsByPotion(myMonsterDto);
        long opponentMonsterTotalStats = changeStatsByPotion(opponentMonsterDto);

        myMonsterTotalStats = changeStatsByComplexPotion(myMonsterDto, myMonsterTotalStats, opponentMonsterDto.getPotion());
        opponentMonsterTotalStats = changeStatsByComplexPotion(opponentMonsterDto, opponentMonsterTotalStats, myMonsterDto.getPotion());

        monsterService.decreasePotionUse(myMonster);
        monsterService.decreasePotionUse(opponentMonster);

        return (double) myMonsterTotalStats / (myMonsterTotalStats + opponentMonsterTotalStats) * 100;
    }

    public long changeStatsByPotion(MonsterDto monsterDto) {
        long attack = monsterDto.getAttack();
        long defence = monsterDto.getDefence();
        long tricks = monsterDto.getTricks();
        long brains = monsterDto.getBrains();

        long monsterTotalStats = attack + defence + tricks + brains;

        if (monsterDto.getPotion() == null) {
            return monsterTotalStats;
        }

        switch (monsterDto.getPotion()) {
            case("DEMON_ATTACK") ->  monsterTotalStats = (attack * 4) + defence + tricks + brains;
            case("TRICKS_MAKER") -> monsterTotalStats = attack + defence + (tricks * 4) + brains;
            case("TOUGH_GUY") -> monsterTotalStats = attack + (defence * 2) + tricks + (brains * 2);
            case("MYSTERIO") -> monsterTotalStats = (attack * 2) + defence + (tricks * 2) + brains;
            case("INCREDIBLE_HULK") -> monsterTotalStats = (attack * 4) + (defence * 4) + (tricks * 4) + (brains * 4);
            case("MYSTERIO_RAGE") -> monsterTotalStats = (attack * 5) + defence + (tricks * 5) + brains;

        }

        return monsterTotalStats;
    }

    public long changeStatsByComplexPotion(MonsterDto myMonsterDto, long myMonsterTotalStats, String opponentPotionName) {
        if (opponentPotionName == null) {
            return myMonsterTotalStats;
        }

        switch (opponentPotionName) {
            case("ADVANTAGE_KILLER") -> myMonsterTotalStats = myMonsterDto.getAttack() + myMonsterDto.getDefence() +
                    myMonsterDto.getTricks() + myMonsterDto.getBrains();
            case("GIANT_SLAYER") -> myMonsterTotalStats = (long) (0.1 * (myMonsterDto.getAttack() + myMonsterDto.getDefence() +
                    myMonsterDto.getTricks() + myMonsterDto.getBrains()));
        }

        return myMonsterTotalStats;
    }

    public List<Integer> calculateDegrees(double percentageChanceToWin) {
        int degreesWinOneSide = (int) ((percentageChanceToWin * 3.6) / 2);
        int degreesWinUpperLimit = 270 + degreesWinOneSide;
        if (degreesWinUpperLimit >= 360) {
            degreesWinUpperLimit -= 360;
        }
        int degreesWinLowerLimit = 270 - degreesWinOneSide;

        return Arrays.asList(degreesWinOneSide, degreesWinLowerLimit, degreesWinUpperLimit);
    }

    public int calculateRandomDegrees() {
        return (int) ((new Random().nextDouble(100) + 1) * 3.6);
    }

    public boolean calculateTheWinner(int randomDegrees, double percentageChanceToWin) {
        List<Integer> listOfDegrees = calculateDegrees(percentageChanceToWin);
        int degreesWinOneSide = listOfDegrees.get(0);
        int degreesWinLowerLimit = listOfDegrees.get(1);
        int degreesWinUpperLimit = listOfDegrees.get(2);

        boolean didIWin;
        if (degreesWinOneSide < 90) {
            didIWin = (randomDegrees >= degreesWinLowerLimit && randomDegrees <= degreesWinUpperLimit);
        } else {
            didIWin = (randomDegrees <= degreesWinUpperLimit || randomDegrees >= degreesWinLowerLimit);
        }

        return didIWin;
    }

    public void executeWinCondition(User winningUser, User losingUser, TypeOfFight typeOfFight, Monster winningMonster,
                                    Monster losingMonster) {
        if (typeOfFight == TypeOfFight.COLLECT) {
            messageService.deleteMessagesWithThisMonster(losingUser.getId(), losingMonster);

            losingMonster.setUser(winningUser);
            monsterService.updateMonster(losingMonster);
        } else { // BITE || EAT
            double divisor;
            if (typeOfFight == TypeOfFight.BITE) {
                divisor = 4;
            } else { //EAT
                messageService.deleteMessagesWithThisMonster(losingUser.getId(), losingMonster);

                losingMonster.setAlive(false);
                divisor = 1.5;
            }

            winningMonster.setAttack((long) (winningMonster.getAttack() + (losingMonster.getAttack() / divisor)));
            winningMonster.setDefence((long) (winningMonster.getDefence() + (losingMonster.getDefence() / divisor)));
            winningMonster.setBrains((long) (winningMonster.getBrains() + (losingMonster.getBrains() / divisor)));
            winningMonster.setTricks((long) (winningMonster.getTricks() + (losingMonster.getTricks() / divisor)));

            losingMonster.setAttack((long) (losingMonster.getAttack() - (losingMonster.getAttack() / divisor)));
            losingMonster.setDefence((long) (losingMonster.getDefence() - (losingMonster.getDefence() / divisor)));
            losingMonster.setBrains((long) (losingMonster.getBrains() - (losingMonster.getBrains() / divisor)));
            losingMonster.setTricks((long) (losingMonster.getTricks() - (losingMonster.getTricks() / divisor)));

            monsterService.updateMonster(losingMonster);
            monsterService.updateMonster(winningMonster);
        }
    }
}
