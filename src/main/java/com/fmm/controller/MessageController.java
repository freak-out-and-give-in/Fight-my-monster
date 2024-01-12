package com.fmm.controller;

import com.fmm.dto.MessageDto;
import com.fmm.dto.MonsterDto;
import com.fmm.enumeration.TypeOfFight;
import com.fmm.model.Message;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import com.fmm.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fmm/messages")
public class MessageController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final MonsterService monsterService;

    private final OpponentProfileService opponentProfileService;

    @Autowired
    public MessageController(UserService userService, UserInfoService userInfoService, MonsterService monsterService,  MessageService messageService,
                             OpponentProfileService opponentProfileService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.monsterService = monsterService;
        this.messageService = messageService;
        this.opponentProfileService = opponentProfileService;
    }

    @GetMapping("")
    public ModelAndView showMessagesPage(HttpServletRequest request) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        Long myId = myUser.getId();

        ModelAndView mav = new ModelAndView("/parts/messages/messages");
        mav.addObject("User", myUser);
        mav.addObject("Background", userInfoService.getUserInfo(myId).getCurrentBackground());
        mav.addObject("Nuggets", userInfoService.getUserInfo(myId).getNuggets());
        mav.addObject("MessagesReceived", messageService.getMessagesForMe(myId));
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(myId).size());

        return mav;
    }

    @PostMapping("/send-message")
    public ModelAndView sendMessage(HttpServletRequest request, @ModelAttribute("MessageDto") MessageDto messageDto) {
        User myUser = userService.getUser(request.getUserPrincipal().getName());
        UserInfo myUserInfo = userInfoService.getUserInfo(myUser.getId());

        messageService.addMessage(new Message(myUserInfo, messageDto.getToAccountId(), messageDto.getTypeOfFight(),
                messageDto.getToMonsterName(), messageDto.getFromMonsterName(), messageDto.getNuggetsForAccepting()));


        //below returns to opponent profile
        User opponentUser = userService.getUser(messageDto.getToAccountId());
        Long opponentId = opponentUser.getId();

        List<Monster> aliveMonsterList = monsterService.getAliveMonsters(opponentId);
        List<MonsterDto> monsterDtoList = opponentProfileService.getFirstPageAliveMonsters(aliveMonsterList);

        int totalPages = opponentProfileService.calculateTotalPages(aliveMonsterList);

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

    @PostMapping("/decline-fight-offer")
    public RedirectView declineFightOffer(@ModelAttribute("MessageId") Long messageId) {
        messageService.deleteMessage(messageService.getMessage(messageId));

        return new RedirectView("/fmm/messages");
    }

}
