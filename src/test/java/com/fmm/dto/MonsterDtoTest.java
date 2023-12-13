package com.fmm.dto;

import com.fmm.enumeration.Level;
import com.fmm.model.Monster;
import com.fmm.model.User;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MonsterDtoTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void testConvertMonsterToDto() {
        User user = new User("fig", "tree");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);
        Monster monster = new Monster(user, "", Level.CUSTOM);

        MonsterDto monsterDto = modelMapper.map(monster, MonsterDto.class);

        assertThat(monster.getId()).isEqualTo(monsterDto.getId());
        assertThat(monster.getName()).isEqualTo(monsterDto.getName());
        assertThat(monster.getGenus()).isEqualTo(monsterDto.getGenus());
        assertThat(monster.getAttack()).isEqualTo(monsterDto.getAttack());
        assertThat(monster.getDefence()).isEqualTo(monsterDto.getDefence());
        assertThat(monster.getBrains()).isEqualTo(monsterDto.getBrains());
        assertThat(monster.getTricks()).isEqualTo(monsterDto.getTricks());
        assertThat(monster.isAlive()).isEqualTo(monsterDto.isAlive());
        assertThat(monster.getPotionEquipped()).isEqualTo(monsterDto.getPotionEquipped());
    }

}