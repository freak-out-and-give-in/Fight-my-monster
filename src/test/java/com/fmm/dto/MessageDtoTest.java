package com.fmm.dto;

import com.fmm.enumeration.TypeOfFight;
import com.fmm.model.Message;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

class MessageDtoTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    void MessageDto() {
        User user = new User("jerry", "sein");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);
        user.setId(1L);

        UserInfo userInfo = new UserInfo(user);

        Message message =  new Message(userInfo, 5L, TypeOfFight.EAT, "mouse 4223", "fridge", 3L);


        MessageDto messageDto = modelMapper.map(message, MessageDto.class);

        assertThat(message.getToAccountId()).isEqualTo(messageDto.getToAccountId());
        assertThat(message.getTypeOfFight()).isEqualTo(messageDto.getTypeOfFight());
        assertThat(message.getToMonsterName()).isEqualTo(messageDto.getToMonsterName());
        assertThat(message.getFromMonsterName()).isEqualTo(messageDto.getFromMonsterName());
        assertThat(message.getNuggetsForAccepting()).isEqualTo(messageDto.getNuggetsForAccepting());
    }

}