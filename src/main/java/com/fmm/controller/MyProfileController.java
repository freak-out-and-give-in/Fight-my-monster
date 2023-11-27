package com.fmm.controller;

import com.fmm.service.MessageService;
import com.fmm.service.MonsterService;
import com.fmm.service.UserInfoService;
import com.fmm.model.User;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MyProfileController {

    private final UserService userService;

    private final MonsterService monsterService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    @Autowired
    public MyProfileController(UserService userService, MonsterService monsterService, UserInfoService userInfoService,
                               MessageService messageService) {
        this.userService = userService;
        this.monsterService = monsterService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
    }

    @GetMapping("/fmm/my-profile")
    public ModelAndView showMyProfilePage(HttpServletRequest request) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        User user = userService.getUser(id);

        ModelAndView mav = new ModelAndView("/parts/profile/my-profile");
        mav.addObject("User", user);
        mav.addObject("Monsters", monsterService.getAliveMonsters(id));
        mav.addObject("Background", userInfoService.getUserInfo(id).getCurrentBackground());
        mav.addObject("Nuggets", userInfoService.getUserInfo(id).getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(id).size());

        return mav;
    }

}
