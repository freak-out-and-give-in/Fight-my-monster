package com.fmm.service;

import com.fmm.model.User;
import com.fmm.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetOtherUsers() {
        User user1 = new User("pedal", "numbers45");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);

        User user2 = new User("pigg", "spider");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);

        User user3 = new User("linda", "newer");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);

        when(userRepository.findAll()).thenReturn(new LinkedList<>(Arrays.asList(user1, user2, user3)));
        List<User> resultUsers = userService.getOtherUsers(user1);

        assertThat(resultUsers).isEqualTo(Arrays.asList(user2, user3));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        User user = new User("pedal", "numbers45");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User resultUser = userService.getUser(1L);

        assertThat(user).isEqualTo(resultUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByUsername() {
        User user = new User("frank", "passw");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);

        when(userRepository.findByUsername("frank")).thenReturn(user);
        User resultUser = userService.getUser("frank");

        assertThat(user).isEqualTo(resultUser);
        verify(userRepository, times(1)).findByUsername("frank");
    }
}