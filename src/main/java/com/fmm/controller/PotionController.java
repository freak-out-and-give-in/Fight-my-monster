package com.fmm.controller;

import com.fmm.dto.MonsterDto;
import com.fmm.dto.SelectPotionDto;
import com.fmm.enumeration.Potion;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/fmm/village/potions-pete")
public class PotionController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final MonsterService monsterService;

    private final ModelMapper modelMapper;

    @Autowired
    public PotionController(UserService userService, UserInfoService userInfoService, MessageService messageService, MonsterService monsterService,
                            ModelMapper modelMapper) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
        this.monsterService = monsterService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ModelAndView showPotionsPetePage(HttpServletRequest request) {
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();
        User myUser = userService.getUser(myId);

        ModelAndView mav = new ModelAndView("/parts/village/potions-pete/potions-pete");
        mav.addObject("User", myUser);
        mav.addObject("Background", userInfoService.getUserInfo(myId).getCurrentBackground());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());

        return mav;
    }

    @GetMapping("/{potion}")
    public ModelAndView showSelectPotionPage(HttpServletRequest request, @ModelAttribute("selectPotionDto") SelectPotionDto selectPotionDto) {
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();
        User myUser = userService.getUser(myId);

        List<MonsterDto> myMonsters = monsterService.getMonsters(myId).stream().map(this::convertToDto).toList();

        int n = selectPotionDto.getN();
        if (n >= myMonsters.size()) {
            selectPotionDto.setN(0);
        } else if (n < 0) {
            selectPotionDto.setN(myMonsters.size() - 1);
        }

        Potion potion = getPotion(selectPotionDto.getPotionName());
        selectPotionDto.setPotionCost(potion.getCost());

        ModelAndView mav = new ModelAndView("/parts/village/potions-pete/view-potion");
        mav.addObject("User", myUser);
        mav.addObject("Background", userInfoService.getUserInfo(myId).getCurrentBackground());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());
        mav.addObject("MyMonsters", myMonsters);
        mav.addObject("SelectPotionDto", selectPotionDto);

        return mav;
    }

    @Transactional
    @PostMapping("/{potion}")
    public ModelAndView buyPotion(HttpServletRequest request, @ModelAttribute("selectPotionDto") SelectPotionDto selectPotionDto) {
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();
        User myUser = userService.getUser(myId);

        Potion potion = getPotion(selectPotionDto.getPotionName());

        //remove nuggets from account
        UserInfo myUserInfo = userInfoService.getUserInfo(myId);
        BigInteger newNuggetAmount = myUserInfo.getNuggets().subtract(BigInteger.valueOf(potion.getCost()));
        myUserInfo.setNuggets(newNuggetAmount);

        //add potion to monster
        Monster myMonster = monsterService.getMonsters(myId).get(selectPotionDto.getN());
        myMonster.setPotion(String.valueOf(potion));
        myMonster.setPotionUses(potion.getUses());
        monsterService.updateMonster(myMonster);

        ModelAndView mav = new ModelAndView("/parts/village/potions-pete/potions-pete");

        mav.addObject("User", myUser);
        mav.addObject("Background", userInfoService.getUserInfo(myId).getCurrentBackground());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());
        mav.addObject("Potions", Arrays.asList(Potion.values()));

        return mav;
    }

    private Potion getPotion(String potionName) {
        Potion potion = null;

        for (Potion potionT : Potion.values()) {
            if (potionT.toString().equals(potionName)) {
                potion = potionT;
            }
        }
        return potion;
    }

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }
}
