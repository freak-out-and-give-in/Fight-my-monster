package com.fmm.controller;

import com.fmm.model.UserInfo;
import com.fmm.service.MessageService;
import com.fmm.service.UserInfoService;
import com.fmm.model.User;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/fmm/players")
public class PlayerController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    @Autowired
    public PlayerController(UserService userService, UserInfoService userInfoService, MessageService messageService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
    }

    @GetMapping("")
    public ModelAndView showPlayersPage(HttpServletRequest request) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        Long myId = myUser.getId();
        UserInfo myUserInfo = userInfoService.getUserInfo(myId);

        ModelAndView mav = new ModelAndView("/parts/players/players");
        mav.addObject("User", myUser);
        mav.addObject("Users", userService.getOtherUsers(myUser));
        mav.addObject("Background", myUserInfo.getCurrentBackground());
        mav.addObject("Nuggets", myUserInfo.getNuggets());
        mav.addObject("selectedUser", new User());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());

        return mav;
    }

}
