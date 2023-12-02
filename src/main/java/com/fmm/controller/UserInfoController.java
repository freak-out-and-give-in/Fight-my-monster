package com.fmm.controller;

import com.fmm.service.UserInfoService;
import com.fmm.service.UserService;
import com.fmm.model.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigInteger;

@RestController
@RequestMapping("/fmm")
public class UserInfoController {

    private final UserInfoService userInfoService;

    private final UserService userService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService, UserService userService) {
        this.userInfoService = userInfoService;
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/{username}/background")
    public RedirectView setBackground(@RequestParam(value="background") String background, HttpServletRequest request) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        UserInfo userInfo = userInfoService.getUserInfo(id);

        userInfo.setCurrentBackground(background);
        userInfoService.updateUserInfo(userInfo);

        return new RedirectView("/fmm/my-profile");
    }

    @PostMapping("/{username}/nuggets")
    public RedirectView addNuggets(HttpServletRequest request, BigInteger amount) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        UserInfo userInfo = userInfoService.getUserInfo(id);

        userInfo.setNuggets(userInfo.getNuggets().add(amount));
        userInfoService.updateUserInfo(userInfo);

        return new RedirectView("/fmm/my-profile");
    }
}
