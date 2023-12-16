package com.fmm.service;

import com.fmm.enumeration.Level;
import com.fmm.exception.CouldNotFindMonsterException;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.repository.MonsterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonsterServiceTest {

    @InjectMocks
    private MonsterService monsterService;

    @Mock
    private MonsterRepository monsterRepository;

    @Test
    void testGetMonsters() {
        User user1 = new User("one", "free3");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);
        user1.setId(1L);

        User user2 = new User("try", "55");
        user2.setAcceptTermsAndConditions(true);
        user2.setEnabled(true);
        user2.setId(2L);

        Monster monster1 = new Monster(user2, "", Level.CUSTOM);
        monster1.setAlive(false);
        Monster monster2 = new Monster(user1, "", Level.SUPER);
        Monster monster3 = new Monster(user2, "pumpkin", Level.EXTRA);
        Monster monster4 = new Monster(user1, "", Level.STANDARD);

        when(monsterRepository.findAll()).thenReturn(Arrays.asList(monster1, monster2, monster3, monster4));
        List<Monster> resultMonsterList = monsterService.getMonsters(1L);

        assertThat(resultMonsterList).isEqualTo(Arrays.asList(monster2, monster4));
        verify(monsterRepository, times(1)).findAll();
    }

    @Test
    void testGetAliveMonsters() {
        User user1 = new User("liberty", "123");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);
        user1.setId(1L);

        User user2 = new User("g5", "uuu");
        user2.setAcceptTermsAndConditions(true);
        user2.setEnabled(true);
        user2.setId(2L);

        Monster monster1 = new Monster(user2, "", Level.CUSTOM);
        Monster monster2 = new Monster(user1, "", Level.SUPER);
        Monster monster3 = new Monster(user2, "", Level.EXTRA);
        monster3.setAlive(false);
        Monster monster4 = new Monster(user1, "", Level.STANDARD);

        when(monsterRepository.findAll()).thenReturn(Arrays.asList(monster1, monster2, monster3, monster4));
        List<Monster> resultMonsterList = monsterService.getAliveMonsters(2L);

        assertThat(resultMonsterList).isEqualTo(List.of(monster1));
    }

    @Test
    void testGetMonster() {
        User user1 = new User("hoped", "56");
        user1.setAcceptTermsAndConditions(true);
        user1.setEnabled(true);
        user1.setId(1L);

        User user2 = new User("aa", "for");
        user2.setAcceptTermsAndConditions(true);
        user2.setEnabled(true);
        user2.setId(2L);

        Monster monster1 = new Monster(user2, "kin", Level.CUSTOM);
        Monster monster2 = new Monster(user1, "", Level.SUPER);
        Monster monster3 = new Monster(user2, "", Level.EXTRA);
        monster3.setAlive(false);
        Monster monster4 = new Monster(user1, "", Level.STANDARD);

        when(monsterRepository.findAll()).thenReturn(Arrays.asList(monster1, monster2, monster3, monster4));
        Monster resultMonster = monsterService.getMonster(2L, monster3.getName());

        assertThat(resultMonster).isEqualTo(monster3);
        verify(monsterRepository, times(1)).findAll();
    }

    @Test
    void testGetMonsterByCouldNotFindMonster() {
        when(monsterRepository.findAll()).thenReturn(List.of());
        assertThrows(CouldNotFindMonsterException.class, () -> monsterService.getMonster(1L, "b"));
    }

    @Test
    void testAddMonster() {
        User user = new User("drums", "21");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);
        Monster monster = new Monster(user, "er", Level.STANDARD);

        monsterService.addMonster(monster);

        ArgumentCaptor<Monster> captor = ArgumentCaptor.forClass(Monster.class);
        verify(monsterRepository).save(captor.capture());

        assertThat(captor.getValue().getId()).isEqualTo(monster.getId());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getName()).isEqualTo(monster.getName());
        assertThat(captor.getValue().getGenus()).isEqualTo(monster.getGenus());
        assertThat(captor.getValue().getSpecies()).isEqualTo(monster.getSpecies());
        assertThat(captor.getValue().getAttack()).isEqualTo(monster.getAttack());
        assertThat(captor.getValue().getDefence()).isEqualTo(monster.getDefence());
        assertThat(captor.getValue().getBrains()).isEqualTo(monster.getBrains());
        assertThat(captor.getValue().getTricks()).isEqualTo(monster.getTricks());
        assertThat(captor.getValue().isAlive()).isEqualTo(monster.isAlive());
        assertThat(captor.getValue().getPotion()).isEqualTo(monster.getPotion());
    }

    @Test
    void testUpdateMonster() {
        User user = new User("drums", "21");
        user.setAcceptTermsAndConditions(true);
        user.setEnabled(true);
        Monster monster = new Monster(user, "er", Level.STANDARD);
        monster.setPotion("TRICKS_MAKER");

        monsterService.addMonster(monster);

        ArgumentCaptor<Monster> captor = ArgumentCaptor.forClass(Monster.class);
        verify(monsterRepository).save(captor.capture());

        assertThat(captor.getValue().getId()).isEqualTo(monster.getId());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getName()).isEqualTo(monster.getName());
        assertThat(captor.getValue().getGenus()).isEqualTo(monster.getGenus());
        assertThat(captor.getValue().getSpecies()).isEqualTo(monster.getSpecies());
        assertThat(captor.getValue().getAttack()).isEqualTo(monster.getAttack());
        assertThat(captor.getValue().getDefence()).isEqualTo(monster.getDefence());
        assertThat(captor.getValue().getBrains()).isEqualTo(monster.getBrains());
        assertThat(captor.getValue().getTricks()).isEqualTo(monster.getTricks());
        assertThat(captor.getValue().isAlive()).isEqualTo(monster.isAlive());
        assertThat(captor.getValue().getPotion()).isEqualTo(monster.getPotion());
    }
}