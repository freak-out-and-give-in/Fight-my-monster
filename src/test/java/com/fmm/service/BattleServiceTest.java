package com.fmm.service;

import com.fmm.repository.MessageRepository;
import com.fmm.repository.MonsterRepository;
import com.fmm.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MonsterService monsterService;

    @Mock
    private MonsterRepository monsterRepository;

    @Test
    void fight() {
    }

    @Test
    void exchangeNuggetsForAccepting() {
    }

    @Test
    void calculatePercentageChanceToWin() {
    }

    @Test
    void changeStatsByPotion() {
    }

    @Test
    void changeStatsByComplexPotion() {
    }

    @Test
    void minusPotion() {
    }

    @Test
    void calculateDegrees() {
    }

    @Test
    void calculateDegreesChance() {
    }

    @Test
    void calculateTheWinner() {
    }

    @Test
    void executeWinCondition() {
    }
}

/*

User winningUser = new User("scream", "drown");
        winningUser.setId(1L);
        User losingUser = new User("hear", "yesterday");
        losingUser.setId(2L);
        String typeOfFight = "COLLECT";

        Monster winningMonster = new Monster(winningUser, "submarine", Level.EXTRA);
        Monster losingMonster = new Monster(losingUser, "", Level.STANDARD);

        Message fightMessage = new Message(winningUser, 2L, "COLLECT", "", "submarine", 3L);
        Message winnerMessageMisc = new Message(winningUser, 2L, "BITE", "", "", 1111L);
        Message loserMessageMisc = new Message(losingUser, 1L, "EAT", "", "", 0L);

        when(messageRepository.findAll()).thenReturn(Arrays.asList(fightMessage, winnerMessageMisc, loserMessageMisc));

 */