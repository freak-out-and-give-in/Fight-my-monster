package com.fmm.controller;

import com.fmm.dto.MonsterDto;
import com.fmm.enumeration.Level;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fmm")
public class MonsterController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final MonsterService monsterService;

    private final MessageService messageService;

    private final ModelMapper modelMapper;

    @Autowired
    public MonsterController(UserService userService, UserInfoService userInfoService, MonsterService monsterService, MessageService messageService,
                             ModelMapper modelMapper) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.monsterService = monsterService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{username}/monsters")
    public List<Monster> getMyMonsters(HttpServletRequest request) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();

        return monsterService.getMonsters(id);
    }

    @Transactional
    @PostMapping("/{username}/monsters")
    public ModelAndView growMonster(@RequestParam(value="level") Level level, @ModelAttribute("pageNumber") int pageNumber, HttpServletRequest request) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        Long myId = myUser.getId();

        //max of 42 monsters (7 pages)
        if ((long) monsterService.getAliveMonsters(myId).size() < 42) {
            monsterService.addMonster(new Monster(myUser, level));

            UserInfo userInfo = userInfoService.getUserInfo(myId);
            userInfo.setNuggets(userInfo.getNuggets().subtract(BigInteger.valueOf(level.getCost())));
            userInfoService.updateUserInfo(userInfo);
        }

        // below returns to my-profile, specifically so its on the same page as when it left
        UserInfo userInfo = userInfoService.getUserInfo(myId);

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(myId);
        List<MonsterDto> monsterDtoList = new ArrayList<>();
        for (int i = ((pageNumber - 1) * 6); i < 6 + ((pageNumber - 1) * 6); i++) {
            if (i >= aliveMonsterList.size()) {
                break;
            }

            monsterDtoList.add(convertToDto(aliveMonsterList.get(i)));
        }

        int totalPages = 1 + (aliveMonsterList.size() / 6);

        ModelAndView mav = new ModelAndView("/parts/profile/my-profile");
        mav.addObject("User", myUser);
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", userInfo.getCurrentBackground());
        mav.addObject("Nuggets", userInfo.getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());
        mav.addObject("pageNumber", pageNumber);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }

}
