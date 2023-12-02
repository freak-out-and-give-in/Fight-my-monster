package com.fmm.controller;

import com.fmm.dto.MonsterDto;
import com.fmm.model.Monster;
import com.fmm.model.UserInfo;
import com.fmm.service.MessageService;
import com.fmm.service.MonsterService;
import com.fmm.service.UserInfoService;
import com.fmm.model.User;
import com.fmm.service.UserService;
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

    private final ModelMapper modelMapper;

    @Autowired
    public MyProfileController(UserService userService, MonsterService monsterService, UserInfoService userInfoService,
                               MessageService messageService, ModelMapper modelMapper) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/my-profile")
    public ModelAndView showMyProfilePage(HttpServletRequest request) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        User user = userService.getUser(id);
        UserInfo userInfo = userInfoService.getUserInfo(id);

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(id);
        List<MonsterDto> monsterDtoList = new ArrayList<>();

        for (Monster monster: aliveMonsterList) {
            monsterDtoList.add(convertToDto(monster));

            if (monsterDtoList.size() >= 6) {
                break;
            }
        }

        int totalPages = 1 + (aliveMonsterList.size() / 6);

        if (totalPages >= 8) {
            totalPages = 7;
        }

        ModelAndView mav = new ModelAndView("/parts/profile/my-profile");
        mav.addObject("User", user);
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", userInfo.getCurrentBackground());
        mav.addObject("Nuggets", userInfo.getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(id).size());
        mav.addObject("pageNumber", 1);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    @GetMapping(value = "/my-profile", params = "pageNumber")
    public ModelAndView showMyProfilePage(HttpServletRequest request, @ModelAttribute("pageNumber") int pageNumber) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        User user = userService.getUser(id);
        UserInfo userInfo = userInfoService.getUserInfo(id);

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(id);
        List<MonsterDto> monsterDtoList = new ArrayList<>();
        for (int i = ((pageNumber - 1) * 6); i < 6 + ((pageNumber - 1) * 6); i++) {
            if (i >= aliveMonsterList.size()) {
                break;
            }

            monsterDtoList.add(convertToDto(aliveMonsterList.get(i)));
        }

        int totalPages = 1 + (aliveMonsterList.size() / 6);

        if (totalPages >= 8) {
            totalPages = 7;
        }

        ModelAndView mav = new ModelAndView("/parts/profile/my-profile");
        mav.addObject("User", user);
        mav.addObject("Monsters", monsterDtoList);
        mav.addObject("Background", userInfo.getCurrentBackground());
        mav.addObject("Nuggets", userInfo.getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(id).size());
        mav.addObject("pageNumber", pageNumber);
        mav.addObject("totalPages", totalPages);

        return mav;
    }

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }

}
