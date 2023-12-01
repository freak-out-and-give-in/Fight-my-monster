package com.fmm.controller;

import com.fmm.dto.MonsterDto;
import com.fmm.enumeration.Level;
import com.fmm.model.Monster;
import com.fmm.model.UserInfo;
import com.fmm.service.MonsterService;
import com.fmm.service.UserInfoService;
import com.fmm.model.User;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MonsterController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final MonsterService monsterService;

    @Autowired
    public MonsterController(UserService userService, UserInfoService userInfoService, MonsterService monsterService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.monsterService = monsterService;
    }

    @GetMapping("/fmm/{username}/monsters")
    public List<Monster> getMyMonsters(HttpServletRequest request) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();

        return monsterService.getMonsters(id);
    }

    @Transactional
    @PostMapping("/fmm/{username}/monsters")
    public RedirectView growMonster(@RequestParam(value="level") Level level, HttpServletRequest request) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        User user = userService.getUser(id);

        //max of 42 monsters (7 pages)
        if ((long) monsterService.getAliveMonsters(id).size() < 42) {
            monsterService.addMonster(new Monster(user, "", level));

            UserInfo userInfo = userInfoService.getUserInfo(id);
            userInfo.setNuggets(userInfo.getNuggets().subtract(BigInteger.valueOf(level.getCost())));
            userInfoService.updateUserInfo(userInfo);
        }

        return new RedirectView("/fmm/my-profile");
    }

}
