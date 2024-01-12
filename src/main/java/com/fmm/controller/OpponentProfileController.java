package com.fmm.controller;

import com.fmm.dto.MonsterDto;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fmm/opponent-profile")
public class OpponentProfileController {

    private final UserService userService;

    private final MonsterService monsterService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final OpponentProfileService opponentProfileService;

    private final ModelMapper modelMapper;

    @Autowired
    public OpponentProfileController(UserService userService, MonsterService monsterService, UserInfoService userInfoService, MessageService messageService,
                                     OpponentProfileService opponentProfileService, ModelMapper modelMapper) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
        this.opponentProfileService = opponentProfileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ModelAndView showOpponentProfilePage(@ModelAttribute("selectedUser") User opponentUser, HttpServletRequest request) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());

        opponentUser = userService.getUser(opponentUser.getUsername());
        Long opponentId = opponentUser.getId();

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(opponentId);
        List<MonsterDto> monsterDtoList = opponentProfileService.getFirstPageAliveMonsters(aliveMonsterList);

        int totalPages = opponentProfileService.calculateTotalPagesWithLimit(aliveMonsterList);

        ModelAndView mav = new ModelAndView("/parts/profile/opponent-profile");
        mav.addObject("User", opponentUser);
        mav.addObject("LoggedInUsername", myUser.getUsername());
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", userInfoService.getUserInfo(opponentId).getCurrentBackground());
        mav.addObject("Nuggets", userInfoService.getUserInfo(opponentId).getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myUser.getId()).size());
        mav.addObject("pageNumber", 1);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    @GetMapping(value = "", params = "pageNumber")
    public ModelAndView showOpponentProfilePage(@ModelAttribute("selectedUser") User opponentUser, @ModelAttribute("pageNumber") int pageNumber,
                                                HttpServletRequest request) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());

        opponentUser = userService.getUser(opponentUser.getUsername());
        Long opponentId = opponentUser.getId();

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(opponentId);
        List<MonsterDto> monsterDtoList = opponentProfileService.getSpecificPageAliveMonsters(pageNumber, aliveMonsterList);

        int totalPages = opponentProfileService.calculateTotalPagesWithLimit(aliveMonsterList);

        ModelAndView mav = new ModelAndView("/parts/profile/opponent-profile");
        mav.addObject("User", opponentUser);
        mav.addObject("LoggedInUsername", myUser.getUsername());
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", userInfoService.getUserInfo(opponentId).getCurrentBackground());
        mav.addObject("Nuggets", userInfoService.getUserInfo(opponentId).getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myUser.getId()).size());
        mav.addObject("pageNumber", pageNumber);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }

}
