package com.fmm.service;

import com.fmm.model.User;
import com.fmm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getOtherUsers(User userMe) {
        List<User> userList = userRepository.findAll();
        userList.remove(userMe);

        return userList;
    }

    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }
}
