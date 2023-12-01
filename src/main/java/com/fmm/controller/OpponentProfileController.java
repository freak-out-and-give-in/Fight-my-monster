package com.fmm.controller;

import com.fmm.dto.MonsterDto;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.service.MessageService;
import com.fmm.service.MonsterService;
import com.fmm.service.UserInfoService;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class OpponentProfileController {

    private final UserService userService;

    private final MonsterService monsterService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    @Autowired
    public OpponentProfileController(UserService userService, MonsterService monsterService, UserInfoService userInfoService,
                                     MessageService messageService, ModelMapper modelMapper) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/fmm/opponent-profile")
    public ModelAndView showOpponentProfilePage(@ModelAttribute("selectedUser") User opponentUser, HttpServletRequest request) {
        User myUser = userService.getUser(userService.getUser(request.getUserPrincipal().getName()).getId());
        String loggedInUsername = myUser.getUsername();

        opponentUser = userService.getUser(opponentUser.getUsername());
        Long opponentId = opponentUser.getId();

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(opponentId);
        List<MonsterDto> monsterDtoList = new ArrayList<>();
        for (Monster monster: aliveMonsterList) {
            monsterDtoList.add(convertToDto(monster));

            if (monsterDtoList.size() >= 6) {
                break;
            }
        }

        int totalPages = 1 + (aliveMonsterList.size() / 6);

        ModelAndView mav = new ModelAndView("/parts/profile/opponent-profile");
        mav.addObject("User", opponentUser);
        mav.addObject("LoggedInUsername", loggedInUsername);
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", userInfoService.getUserInfo(opponentId).getCurrentBackground());
        mav.addObject("Nuggets", userInfoService.getUserInfo(opponentId).getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myUser.getId()).size());
        mav.addObject("pageNumber", 1);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    @GetMapping(value = "/fmm/opponent-profile", params = "pageNumber")
    public ModelAndView showOpponentProfilePage(@ModelAttribute("selectedUser") User opponentUser, @ModelAttribute("pageNumber") int pageNumber,
                                                HttpServletRequest request) {
        User myUser = userService.getUser(userService.getUser(request.getUserPrincipal().getName()).getId());
        String loggedInUsername = myUser.getUsername();

        opponentUser = userService.getUser(opponentUser.getUsername());
        Long opponentId = opponentUser.getId();

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(opponentId);
        List<MonsterDto> monsterDtoList = new ArrayList<>();
        for (int i = ((pageNumber - 1) * 6); i < 6 + ((pageNumber - 1) * 6); i++) {
            if (i >= aliveMonsterList.size()) {
                break;
            }

            monsterDtoList.add(convertToDto(aliveMonsterList.get(i)));
        }

        int totalPages = 1 + (aliveMonsterList.size() / 6);

        ModelAndView mav = new ModelAndView("/parts/profile/opponent-profile");
        mav.addObject("User", opponentUser);
        mav.addObject("LoggedInUsername", loggedInUsername);
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
