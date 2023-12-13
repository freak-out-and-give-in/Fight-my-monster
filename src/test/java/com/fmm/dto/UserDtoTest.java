package com.fmm.dto;

import com.fmm.enumeration.Level;
import com.fmm.model.Monster;
import com.fmm.model.Role;
import com.fmm.model.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void testConvertUserToDto() {
        User user = new User("lake", "new322");
        user.setId(1L);
        user.setRoles(List.of(new Role("ROLE_USER")));

        Monster monster1 = new Monster(user, "", Level.STANDARD);
        Monster monster2 = new Monster(user, "pokemon", Level.EXTRA);
        user.setMonsters(Arrays.asList(monster1 ,monster2));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        List<String> userRolesListToString = user.getRoles().stream().map(r -> Objects.toString(r, null)).toList();
        List<String> userMonstersListToString = user.getMonsters().stream().map(m -> Objects.toString(m, null)).toList();

        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getUsername()).isEqualTo(userDto.getUsername());
        assertThat(userRolesListToString).isEqualTo(userDto.getRoles());
        assertThat(userMonstersListToString).isEqualTo(userDto.getMonsters());
    }
}