package com.fmm.controller;

import com.fmm.dto.MonsterDto;
import com.fmm.model.Monster;
import com.fmm.model.UserInfo;
import com.fmm.service.*;
import com.fmm.model.User;
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
@RequestMapping("/fmm")
public class MyProfileController {

    private final UserService userService;

    private final MonsterService monsterService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final OpponentProfileService opponentProfileService;

    private final ModelMapper modelMapper;

    @Autowired
    public MyProfileController(UserService userService, MonsterService monsterService, UserInfoService userInfoService, MessageService messageService,
                               OpponentProfileService opponentProfileService, ModelMapper modelMapper) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
        this.opponentProfileService = opponentProfileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/my-profile")
    public ModelAndView showMyProfilePage(HttpServletRequest request) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        Long myId = myUser.getId();
        UserInfo myUserInfo = userInfoService.getUserInfo(myId);

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(myId);
        List<MonsterDto> monsterDtoList = opponentProfileService.getFirstPageAliveMonsters(aliveMonsterList);

        int totalPages = opponentProfileService.calculateTotalPagesWithLimit(aliveMonsterList);

        ModelAndView mav = new ModelAndView("/parts/profile/my-profile");
        mav.addObject("User", myUser);
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", myUserInfo.getCurrentBackground());
        mav.addObject("Nuggets", myUserInfo.getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());
        mav.addObject("pageNumber", 1);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    @GetMapping(value = "/my-profile", params = "pageNumber")
    public ModelAndView showMyProfilePage(HttpServletRequest request, @ModelAttribute("pageNumber") int pageNumber) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        Long myId = myUser.getId();
        UserInfo myUserInfo = userInfoService.getUserInfo(myId);

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(myId);
        List<MonsterDto> monsterDtoList = opponentProfileService.getSpecificPageAliveMonsters(pageNumber, aliveMonsterList);

        int totalPages = opponentProfileService.calculateTotalPagesWithLimit(aliveMonsterList);

        ModelAndView mav = new ModelAndView("/parts/profile/my-profile");
        mav.addObject("User", myUser);
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", myUserInfo.getCurrentBackground());
        mav.addObject("Nuggets", myUserInfo.getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());
        mav.addObject("pageNumber", pageNumber);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }

}
