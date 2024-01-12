package com.fmm.service;

import com.fmm.model.Message;
import com.fmm.model.Monster;
import com.fmm.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesForMe(Long id) {
        List<Message> messageList = new ArrayList<>();
        for (Message message : messageRepository.findAll()) {
            if (message.getToAccountId().equals(id)) {
                messageList.add(message);
            }
        }

        return messageList;
    }

    public Message getMessage(Long id) {
        return messageRepository.findById(id).get();
    }

    public void addMessage(Message message) {
        messageRepository.save(message);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    public void deleteMessagesWithThisMonster(Long id, Monster monster) {
        for (Message message : getMyMessages(id)) {
            if (message.getFromMonsterName().equals(monster.getName()) || message.getToMonsterName().equals(monster.getName())) {
                messageRepository.delete(message);
            }
        }
    }

    public List<Message> getMyMessages(Long id) {
        List<Message> messageList = new ArrayList<>();
        for (Message message : messageRepository.findAll()) {
            if (Objects.equals(message.getToAccountId(), id) || Objects.equals(message.getFromUserInfo().getId(), id)) {
                messageList.add(message);
            }
        }

        return messageList;
    }
}
