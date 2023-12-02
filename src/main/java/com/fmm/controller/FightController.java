package com.fmm.controller;

import com.fmm.dto.BattleForm;
import com.fmm.dto.FightRequestDto;
import com.fmm.dto.MessageDto;
import com.fmm.dto.MonsterDto;
import com.fmm.model.Message;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import com.fmm.service.MessageService;
import com.fmm.service.MonsterService;
import com.fmm.service.UserInfoService;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/fmm")
public class FightController {

    private final UserService userService;

    private final MonsterService monsterService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    @Autowired
    public FightController(UserService userService, MonsterService monsterService, UserInfoService userInfoService,
                           MessageService messageService, ModelMapper modelMapper) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/fight-request")
    public ModelAndView showFightRequestPage(HttpServletRequest request,
                                             @ModelAttribute("FightRequestDto") FightRequestDto fightRequestDto) {
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();
        User myUser = userService.getUser(myId);

        User opponentUser = userService.getUser(fightRequestDto.getOpponentUsername());
        MonsterDto opponentMonster = convertToDto(monsterService.getMonster(opponentUser.getId(), fightRequestDto.getOpponentMonsterName()));


        List<MonsterDto> myMonsters = monsterService.getMonsters(myId).stream().map(this::convertToDto).collect(Collectors.toList());

        int myChanceBarAmount = 1;
        int n = fightRequestDto.getN();
        if (n >= myMonsters.size()) {
            fightRequestDto.setN(0);
        } else if (n < 0) {
            fightRequestDto.setN(myMonsters.size() - 1);
        }

        n = fightRequestDto.getN();
        if (n != 0) {
            MonsterDto myCurrentMonster = myMonsters.get(n);
            myChanceBarAmount = getMyChanceBarAmount(myCurrentMonster, opponentMonster);
            fightRequestDto.setMyChanceBarAmount(myChanceBarAmount);
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
        mav.addObject("MyChanceBarAmount", myChanceBarAmount);

        return mav;
    }

    private int getMyChanceBarAmount(MonsterDto myCurrentMonster, MonsterDto opponentMonster) {
        long myTotalStats = myCurrentMonster.getAttack() + myCurrentMonster.getDefence() + myCurrentMonster.getTricks() + myCurrentMonster.getBrains();
        long opponentTotalStats = opponentMonster.getAttack() + opponentMonster.getDefence() + opponentMonster.getTricks() + opponentMonster.getBrains();
        int myChanceBarAmount = (int) Math.round(((double) myTotalStats / (myTotalStats + opponentTotalStats) * 10));

        if (myChanceBarAmount <= 0) {
            myChanceBarAmount = 1;
        } else if (myChanceBarAmount >= 10) {
            myChanceBarAmount = 9;
        }

        return myChanceBarAmount;
    }

    @GetMapping("/messages/fight-offer")
    public ModelAndView showFightOfferPage(HttpServletRequest request, @ModelAttribute("MessageId") long messageId) {
        Message message = messageService.getMessage(messageId);
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();

        List<MonsterDto> myMonsters = Stream.of
                (monsterService.getMonster(myId, message.getToMonsterName())).map(this::convertToDto).collect(Collectors.toList());
        MonsterDto opponentMonster = convertToDto(monsterService.getMonster(message.getUser().getId(), message.getFromMonsterName()));

        int myChanceBarAmount = getMyChanceBarAmount(myMonsters.get(0), opponentMonster);

        ModelAndView mav = new ModelAndView("/parts/fight/fight-offer");
        mav.addObject("Message", message);
        mav.addObject("MyMonsters", myMonsters);
        mav.addObject("OpponentMonster", opponentMonster);
        mav.addObject("N", 0);
        mav.addObject("OptionIndex", 0);
        mav.addObject("MyChanceBarAmount", myChanceBarAmount);

        return mav;
    }

    @Transactional
    @GetMapping("/battle/1")
    public ModelAndView showBattlePagePart1(HttpServletRequest request, @ModelAttribute("MessageId") long messageId) {
        Message message = messageService.getMessage(messageId);
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        User opponentUser = message.getUser();
        MessageDto messageDto = convertToDto(message);

        Monster myMonster = monsterService.getMonster(myUser.getId(), messageDto.getToMonsterName());
        Monster opponentMonster = monsterService.getMonster(opponentUser.getId(), messageDto.getFromMonsterName());

        MonsterDto myMonsterDto = convertToDto(myMonster);
        MonsterDto opponentMonsterDto = convertToDto(opponentMonster);

        double realPercentageChanceToWin = calculatePercentageChanceToWin(myMonsterDto, opponentMonsterDto);

        BattleForm battleForm = new BattleForm();
        battleForm.setMyMonsterBefore(myMonsterDto);
        battleForm.setOpponentMonsterBefore(opponentMonsterDto);
        battleForm.setChanceToWinGraphic((int) (realPercentageChanceToWin * 5.08)); //the width of the win-wheel is 508px
        battleForm.setBattleBackground(new Random().nextInt(17));
        battleForm.setBattleIndex(1);

        battleForm.setShownPercentageChanceToWin((int) realPercentageChanceToWin);
        battleForm.setDegreesChance(calculateDegreesChance());
        battleForm.setDidIWin(calculateTheWinner(battleForm.getDegreesChance(), realPercentageChanceToWin));
        fight(messageDto, battleForm.isDidIWin(), myUser, opponentUser, myMonster, opponentMonster);
        messageService.deleteMessage(message);

        battleForm.setMyMonsterAfter(convertToDto(myMonster));
        battleForm.setOpponentMonsterAfter(convertToDto(opponentMonster));

        ModelAndView mav = new ModelAndView("/parts/fight/battle");
        mav.addObject("BattleForm", battleForm);

        return mav;
    }

    @GetMapping("/battle/2")
    public ModelAndView showBattlePagePart2(@ModelAttribute("BattleForm") BattleForm battleForm) {
        ModelAndView mav = new ModelAndView("/parts/fight/battle");

        battleForm.setBattleIndex(2);
        mav.addObject("BattleForm", battleForm);

        return mav;
    }

    @GetMapping("/battle/3")
    public ModelAndView showBattlePagePart3(@ModelAttribute("BattleForm") BattleForm battleForm) {
        ModelAndView mav = new ModelAndView("/parts/fight/battle");

        battleForm.setBattleIndex(3);
        mav.addObject("BattleForm", battleForm);

        return mav;
    }

    private void fight(MessageDto messageDto, boolean didIWin, User myUser, User opponentUser, Monster myMonster, Monster opponentMonster) {
        exchangeNuggetsForAccepting(messageDto, myUser, opponentUser);

        if (didIWin) {
            executeWinCondition(myUser, opponentUser, messageDto.getTypeOfFight(), myMonster, opponentMonster);
        } else {
            executeWinCondition(opponentUser, myUser, messageDto.getTypeOfFight(), opponentMonster, myMonster);
        }
    }

    private void exchangeNuggetsForAccepting(MessageDto messageDto, User myUser, User opponentUser) {
        if (messageDto.getNuggetsForAccepting() > 0) {
            UserInfo opponentUserInfo = userInfoService.getUserInfo(opponentUser.getId());
            opponentUserInfo.setNuggets(opponentUserInfo.getNuggets().subtract(BigInteger.valueOf(messageDto.getNuggetsForAccepting())));
            userInfoService.updateUserInfo(opponentUserInfo);

            UserInfo myUserInfo = userInfoService.getUserInfo(myUser.getId());
            myUserInfo.setNuggets(myUserInfo.getNuggets().add(BigInteger.valueOf(messageDto.getNuggetsForAccepting())));
            userInfoService.updateUserInfo(myUserInfo);
        }
    }

    private double calculatePercentageChanceToWin(MonsterDto myMonsterDto, MonsterDto opponentMonsterDto) {
        long myMonsterTotalStats = myMonsterDto.getAttack() + myMonsterDto.getDefence() + myMonsterDto.getTricks() + myMonsterDto.getBrains();
        long opponentMonsterTotalStats = opponentMonsterDto.getAttack() + opponentMonsterDto.getDefence() + opponentMonsterDto.getTricks() +
                opponentMonsterDto.getBrains();


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

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }

    private MessageDto convertToDto(Message message) {
        return modelMapper.map(message, MessageDto.class);
    }

}
