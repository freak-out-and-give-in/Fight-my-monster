package com.fmm.dto;

import com.fmm.enumeration.Level;
import com.fmm.model.Monster;
import com.fmm.model.Role;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    void ConvertUserToDto() {
        User user = new User("lake", "new322");
        user.setId(1L);
        user.setRoles(List.of(new Role("ROLE_USER")));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        List<String> userRolesListToString = user.getRoles().stream().map(r -> Objects.toString(r, null)).toList();

        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(userRolesListToString).isEqualTo(userDto.getRoles());
    }
}