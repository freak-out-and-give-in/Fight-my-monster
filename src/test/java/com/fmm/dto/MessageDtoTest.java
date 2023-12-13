package com.fmm.dto;

import com.fmm.model.Message;
import com.fmm.model.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

class MessageDtoTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void testMessageDto() {
        User user = new User("jerry", "sein");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);
        user.setId(1L);

        Message message =  new Message
                (user, 5L, "EAT", "mouse 4223", "fridge", 3L);


        MessageDto messageDto = modelMapper.map(message, MessageDto.class);

        assertThat(message.getToAccountId()).isEqualTo(messageDto.getToAccountId());
        assertThat(message.getTypeOfFight()).isEqualTo(messageDto.getTypeOfFight());
        assertThat(message.getToMonsterName()).isEqualTo(messageDto.getToMonsterName());
        assertThat(message.getFromMonsterName()).isEqualTo(messageDto.getFromMonsterName());
        assertThat(message.getNuggetsForAccepting()).isEqualTo(messageDto.getNuggetsForAccepting());
    }

}