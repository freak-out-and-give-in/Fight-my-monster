package com.fmm.controller;

import com.fmm.dto.BattleForm;
import com.fmm.dto.MessageDto;
import com.fmm.dto.MonsterDto;
import com.fmm.model.Message;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import com.fmm.service.*;
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
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/fmm")
public class BattleController {

    private final UserService userService;

    private final MonsterService monsterService;

    private final MessageService messageService;

    private final BattleService battleService;

    private final ModelMapper modelMapper;

    @Autowired
    public BattleController(UserService userService, MonsterService monsterService, MessageService messageService, BattleService battleService,
                            ModelMapper modelMapper) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
        this.battleService = battleService;
    }

    @Transactional
    @GetMapping("/battle/1")
    public ModelAndView showBattlePagePart1(HttpServletRequest request, @ModelAttribute("MessageId") long messageId) {
        Message message = messageService.getMessage(messageId);
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        User opponentUser = message.getFromUser();
        MessageDto messageDto = convertToDto(message);

        Monster myMonster = monsterService.getMonster(myUser.getId(), messageDto.getToMonsterName());
        Monster opponentMonster = monsterService.getMonster(opponentUser.getId(), messageDto.getFromMonsterName());

        MonsterDto myMonsterDto = convertToDto(myMonster);
        MonsterDto opponentMonsterDto = convertToDto(opponentMonster);

        double realPercentageChanceToWin = battleService.calculatePercentageChanceToWin(myMonster, opponentMonster, myMonsterDto, opponentMonsterDto);

        BattleForm battleForm = new BattleForm();
        battleForm.setMyMonsterBefore(myMonsterDto);
        battleForm.setOpponentMonsterBefore(opponentMonsterDto);
        battleForm.setChanceToWinGraphic((int) (realPercentageChanceToWin * 5.08)); //the width of the win-wheel is 508px
        battleForm.setBattleBackground(new Random().nextInt(17));
        battleForm.setBattleIndex(1);

        battleForm.setShownPercentageChanceToWin((int) realPercentageChanceToWin);
        battleForm.setRandomDegrees(battleService.calculateRandomDegrees());
        battleForm.setDidIWin(battleService.calculateTheWinner(battleForm.getRandomDegrees(), realPercentageChanceToWin));
        battleService.fight(messageDto, battleForm.isDidIWin(), myUser, opponentUser, myMonster, opponentMonster);
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

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }

    private MessageDto convertToDto(Message message) {
        return modelMapper.map(message, MessageDto.class);
    }
}
