package com.fmm.controller;

import com.fmm.model.User;
import com.fmm.service.MessageService;
import com.fmm.service.UserInfoService;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class VillageController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    @Autowired
    public VillageController(UserService userService, UserInfoService userInfoService, MessageService messageService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.messageService = messageService;
    }

    @GetMapping("/fmm/village")
    public ModelAndView showVillagePage(HttpServletRequest request) {
        Long myId = userService.getUser(request.getUserPrincipal().getName()).getId();
        User myUser = userService.getUser(myId);

        ModelAndView mav = new ModelAndView("/parts/village/village");
        mav.addObject("User", myUser);
        mav.addObject("Background", userInfoService.getUserInfo(myId).getCurrentBackground());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());

        return mav;
    }
}
