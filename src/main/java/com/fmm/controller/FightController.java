package com.fmm.controller;

import com.fmm.dto.BattleDto;
import com.fmm.dto.FightRequestDto;
import com.fmm.model.Message;
import com.fmm.service.MessageService;
import com.fmm.model.Monster;
import com.fmm.service.MonsterService;
import com.fmm.model.UserInfo;
import com.fmm.service.UserInfoService;
import com.fmm.model.User;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.*;

@RestController
public class FightController {

    private final UserService userService;

    private final MonsterService monsterService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    @Autowired
    public FightController(UserService userService, MonsterService monsterService, UserInfoService userInfoService,
                           MessageService messageService) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
    }

    @GetMapping("/fmm/fight-request")
    public ModelAndView showFightRequestPage(HttpServletRequest request,
                                             @ModelAttribute("FightRequestDto") FightRequestDto fightRequestDto) {
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();
        User myUser = userService.getUser(myId);

        User opponentUser = userService.getUser(fightRequestDto.getOpponentUsername());
        Monster opponentMonster = monsterService.getMonster(opponentUser.getId(), fightRequestDto.getOpponentMonsterName());


        List<Monster> myMonsters = monsterService.getMonsters(myId);

        int n = fightRequestDto.getN();
        if (n > myMonsters.size() - 1) {
            n = 0;
        } else if (n < 0) {
            n = myMonsters.size() - 1;
        }


        ModelAndView mav = new ModelAndView("/parts/fight/fight-request");
        mav.addObject("User", myUser);
        mav.addObject("OpponentUser", opponentUser);
        mav.addObject("MyMonsters", myMonsters);
        mav.addObject("OpponentMonster", opponentMonster);
        mav.addObject("Nuggets", userInfoService.getUserInfo(myId).getNuggets());
        mav.addObject("N", n);
        mav.addObject("OptionIndex", fightRequestDto.getOptionIndex());
        mav.addObject("TypeOfFight", fightRequestDto.getTypeOfFight());
        mav.addObject("NuggetsForAccepting", fightRequestDto.getNuggetsForAccepting());

        return mav;
    }

    @GetMapping("/fmm/messages/fight-offer")
    public ModelAndView showFightOfferPage(HttpServletRequest request, @ModelAttribute("MessageId") long messageId) {
        Message message = messageService.getMessage(messageId);
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();

        List<Monster> myMonsters = Collections.singletonList(monsterService.getMonster(myId, message.getToMonsterName()));

        Monster opponentMonster = monsterService.getMonster(message.getUser().getId(), message.getFromMonsterName());


        ModelAndView mav = new ModelAndView("/parts/fight/fight-offer");
        mav.addObject("Message", message);
        mav.addObject("MyMonsters", myMonsters);
        mav.addObject("OpponentMonster", opponentMonster);
        mav.addObject("N", 0);
        mav.addObject("OptionIndex", 0);

        return mav;
    }

    @Transactional
    @GetMapping("/fmm/battle")
    public ModelAndView showBattlePage(HttpServletRequest request, @ModelAttribute("BattleDto") BattleDto battleDto) {
        Message message = messageService.getMessage(battleDto.getMessageId());
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        User opponentUser = message.getUser();

        Monster myMonster = monsterService.getMonster(myUser.getId(), message.getToMonsterName());
        Monster opponentMonster = monsterService.getMonster(opponentUser.getId(), message.getFromMonsterName());

        ModelAndView mav = new ModelAndView("/parts/fight/battle");
        mav.addObject("MyMonster", myMonster);
        mav.addObject("OpponentMonster", opponentMonster);

        battleDto.setBattleIndex(battleDto.getBattleIndex() + 1);
        if (battleDto.getBattleIndex() == 1) { //aka on the first time this method is called
            battleDto.setPercentageChanceToWin(calculatePercentageChanceToWin(myMonster, opponentMonster));
            battleDto.setDegreesChance(calculateDegreesChance());
            battleDto.setDidIWin(calculateTheWinner(battleDto.getDegreesChance(), battleDto.getPercentageChanceToWin()));
            fight(message, battleDto.isDidIWin(), myUser, opponentUser, myMonster, opponentMonster);
        }

        double chanceToWinGraphic = (battleDto.getPercentageChanceToWin() * 5.08); //the width of the win-wheel is 508px

        mav.addObject("PercentageChanceToWin", (int) battleDto.getPercentageChanceToWin());
        mav.addObject("ChanceToWinGraphic", chanceToWinGraphic);
        mav.addObject("DegreesChance", battleDto.getDegreesChance());
        mav.addObject("DidIWin", battleDto.isDidIWin());
        mav.addObject("BattleBackground", new Random().nextInt(17));
        mav.addObject("BattleIndex", battleDto.getBattleIndex());

        return mav;
    }

    private void fight(Message message, boolean didIWin, User myUser, User opponentUser, Monster myMonster, Monster opponentMonster) {
        exchangeNuggetsForAccepting(message, myUser, opponentUser);

        if (didIWin) {
            executeWinCondition(myUser, opponentUser, message.getTypeOfFight(), myMonster, opponentMonster);
        } else {
            executeWinCondition(opponentUser, myUser, message.getTypeOfFight(), opponentMonster, myMonster);
        }
    }

    private void exchangeNuggetsForAccepting(Message message, User myUser, User opponentUser) {
        if (message.getNuggetsForAccepting() > 0) {
            UserInfo opponentUserInfo = userInfoService.getUserInfo(opponentUser.getId());
            opponentUserInfo.setNuggets(opponentUserInfo.getNuggets().subtract(BigInteger.valueOf(message.getNuggetsForAccepting())));
            userInfoService.updateUserInfo(opponentUserInfo);

            UserInfo myUserInfo = userInfoService.getUserInfo(myUser.getId());
            myUserInfo.setNuggets(myUserInfo.getNuggets().add(BigInteger.valueOf(message.getNuggetsForAccepting())));
            userInfoService.updateUserInfo(myUserInfo);
        }
    }

    private double calculatePercentageChanceToWin(Monster myMonster, Monster opponentMonster) {
        long myMonsterTotalStats = myMonster.getAttack() + myMonster.getDefence() + myMonster.getTricks() + myMonster.getBrains();
        long opponentMonsterTotalStats = opponentMonster.getAttack() + opponentMonster.getDefence() + opponentMonster.getTricks() +
                opponentMonster.getBrains();


        return (double) myMonsterTotalStats / (myMonsterTotalStats + opponentMonsterTotalStats) * 100;
    }

    private List<Integer> calculateDegrees(double percentageChanceToWin) {
        int degreesWinOneSide = (int) ((percentageChanceToWin * 3.6) / 2);
        int degreesWinUpperLimit = 270 + degreesWinOneSide;
        if (degreesWinUpperLimit >= 360) {
            degreesWinUpperLimit -= 360;
        }
        int degreesWinLowerLimit = 270 - degreesWinOneSide;

        return Arrays.asList(degreesWinOneSide, degreesWinLowerLimit, degreesWinUpperLimit);
    }

    private int calculateDegreesChance() {
        return (int) ((new Random().nextDouble(100) + 1) * 3.6);
    }

    private boolean calculateTheWinner(int degreesChance, double percentageChanceToWin) {
        List<Integer> listOfDegrees = calculateDegrees(percentageChanceToWin);
        int degreesWinOneSide = listOfDegrees.get(0);
        int degreesWinLowerLimit = listOfDegrees.get(1);
        int degreesWinUpperLimit = listOfDegrees.get(2);

        boolean didIWin;
        if (degreesWinOneSide < 90) {
            didIWin = (degreesChance >= degreesWinLowerLimit && degreesChance <= degreesWinUpperLimit);
        } else {
            didIWin = (degreesChance <= degreesWinUpperLimit || degreesChance >= degreesWinLowerLimit);
        }

        return didIWin;
    }

    private void executeWinCondition(User winningUser, User losingUser, String typeOfFight, Monster winningMonster,
                                     Monster losingMonster) {
        if (typeOfFight.equals("COLLECT")) {
            messageService.deleteMessagesWithThisMonster(losingUser.getId(), losingMonster);

            losingMonster.setUser(winningUser);
            monsterService.updateMonster(losingMonster);
        } else { // BITE or EAT
            double divisor;
            if (typeOfFight.equals("BITE")) {
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
