package com.fmm.controller;

import com.fmm.dto.MessageDto;
import com.fmm.model.Message;
import com.fmm.model.User;
import com.fmm.service.MessageService;
import com.fmm.service.MonsterService;
import com.fmm.service.UserInfoService;
import com.fmm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class MessageController {

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final MessageService messageService;

    private final MonsterService monsterService;

    @Autowired
    public MessageController(UserService userService, UserInfoService userInfoService, MonsterService monsterService,
                             MessageService messageService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.monsterService = monsterService;
        this.messageService = messageService;
    }

    @GetMapping("/fmm/messages")
    public ModelAndView showMessagesPage(HttpServletRequest request) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        User user = userService.getUser(id);

        ModelAndView mav = new ModelAndView("/parts/messages/messages");
        mav.addObject("User", user);
        mav.addObject("Background", userInfoService.getUserInfo(id).getCurrentBackground());
        mav.addObject("Nuggets", userInfoService.getUserInfo(id).getNuggets());
        mav.addObject("MessagesReceived", messageService.getMessagesForMe(id));
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(id).size());

        return mav;
    }

    @PostMapping("/fmm/messages/send-message")
    public ModelAndView sendMessage(HttpServletRequest request, @ModelAttribute("MessageDto") MessageDto messageDto) {
        Long id = userService.getUser(request.getUserPrincipal().getName()).getId();
        User user = userService.getUser(id);

        messageService.addMessage(new Message(user, messageDto.getToAccountId(), messageDto.getTypeOfFight(),
                messageDto.getToMonsterName(), messageDto.getFromMonsterName(), messageDto.getNuggetsForAccepting()));


        //below returns to opponent profile
        User opponentUser = userService.getUser(messageDto.getToAccountId());
        Long opponentId = opponentUser.getId();

        ModelAndView mav = new ModelAndView("/parts/profile/opponent-profile");
        mav.addObject("User", opponentUser);
        mav.addObject("LoggedInUsername", user.getUsername());
        mav.addObject("Monsters", monsterService.getMonsters(opponentId));
        mav.addObject("Background", userInfoService.getUserInfo(opponentId).getCurrentBackground());
        mav.addObject("Nuggets", userInfoService.getUserInfo(opponentId).getNuggets());
        mav.addObject("MessagesReceivedCount", (long) messageService.getMessagesForMe(id).size());

        return mav;
    }

    @PostMapping("/fmm/messages/decline-fight-offer")
    public RedirectView declineFightOffer(@ModelAttribute("MessageId") Long messageId) {
        messageService.deleteMessage(messageService.getMessage(messageId));

        return new RedirectView("/fmm/messages");
    }
}
