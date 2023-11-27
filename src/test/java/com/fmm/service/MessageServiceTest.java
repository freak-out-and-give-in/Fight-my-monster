package com.fmm.service;

import com.fmm.enumeration.Level;
import com.fmm.model.Message;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Test
    void testGetMessagesForMe() {
        User user1 = new User("meerkat", "insects");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);
        user1.setId(1L);

        User user2 = new User("dog23", "aqua");
        user2.setAcceptTermsAndConditions(true);
        user2.setEnabled(true);
        user2.setId(2L);

        Message message1 = new Message(user1, 2L, "EAT",
                "monster1", "monster2", 5L);

        Message message2 = new Message(user2, 1L, "BITE",
                "monster3", "monster4", 1111L);

        Message message3 = new Message(user1, 2L, "COLLECT",
                "monster5", "monster6", 0L);


        when(messageRepository.findAll()).thenReturn(Arrays.asList(message1, message2, message3));
        List<Message> resultMessageList = messageService.getMessagesForMe(1L);

        assertThat(resultMessageList).isEqualTo(List.of(message2));
        verify(messageRepository, times(1)).findAll();
    }

    @Test
    void testGetMessage() {
        User user = new User("meerkat", "insects");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);

        Message message = new Message(user, 2L, "EAT",
                "toMonsterName", "fromMonsterName", 5L);
        message.setMessageId(13L);
        when(messageRepository.findById(13L)).thenReturn(Optional.of(message));

        Message resultMessage = messageService.getMessage(13L);

        assertThat(resultMessage).isEqualTo(message);
        verify(messageRepository, times(1)).findById(13L);
    }

    @Test
    void testAddMessage() {
        User user = new User("12jack", "bug");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);

        Message message = new Message(user, 2L, "COLLECT",
                "toMonsterName", "fromMonsterName", 52L);

        messageService.addMessage(message);

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getToAccountId()).isEqualTo(2L);
        assertThat(captor.getValue().getTypeOfFight()).isEqualTo("COLLECT");
        assertThat(captor.getValue().getToMonsterName()).isEqualTo("toMonsterName");
        assertThat(captor.getValue().getFromMonsterName()).isEqualTo("fromMonsterName");
        assertThat(captor.getValue().getNuggetsForAccepting()).isEqualTo(52L);
    }

    @Test
    void testDeleteMessage() {
        User user = new User("tree", "55");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);

        Message message = new Message(user, 1L, "EAT",
                "toMonsterName", "fromMonsterName", 0L);
        message.setMessageId(3L);

        messageService.deleteMessage(message);

        verify(messageRepository, times(1)).delete(eq(message));
    }

    @Test
    void testDeleteMessagesWithThisMonster() {
        User user1 = new User("tree", "55");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);
        user1.setId(1L);

        User user2 = new User("tree", "55");
        user2.setAcceptTermsAndConditions(true);
        user2.setEnabled(true);
        user2.setId(2L);

        Message message1 = new Message(user1, 2L, "EAT",
                "Ridley", "Batman", 100000L);

        Message message2 = new Message(user1, 2L, "BITE",
                "Scott", "Batman", 0L);

        Message message3 = new Message(user1, 2L, "EAT",
                "Matt", "Bob", 888L);

        Message message4 = new Message(user2, 1L, "BITE",
                "Batman", "Joseph", 0L);

        Monster batmanMonster = new Monster(user1, "Batman", Level.EXTRA);

        when(messageRepository.findAll()).thenReturn(Arrays.asList(message1, message2, message3, message4));
        messageService.deleteMessagesWithThisMonster(1L, batmanMonster);

        verify(messageRepository, times(1)).delete(eq(message1));
        verify(messageRepository, times(1)).delete(eq(message2));
        verify(messageRepository, times(1)).delete(eq(message4));
    }
}