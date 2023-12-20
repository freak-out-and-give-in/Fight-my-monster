package com.fmm.service;

import com.fmm.dto.FightRequestDto;
import com.fmm.dto.MonsterDto;
import com.fmm.enumeration.Level;
import com.fmm.model.Monster;
import com.fmm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class FightServiceTest {

    @InjectMocks
    private FightService fightService;

    @InjectMocks
    private BattleService battleService;

    @DisplayName("Calculate n when n ")
    @Nested
    class calculateN {

        private FightRequestDto fightRequestDto;

        private FightRequestDto resultFightRequestDto;

        @BeforeEach
        void setup() {
            fightRequestDto = new FightRequestDto();
        }

        @DisplayName("= list size")
        @Test
        void calculateN_NEqualToListSize() {
            int n = 3;
            int myMonstersSize = 3;
            fightRequestDto.setN(n);

            resultFightRequestDto = fightService.calculateN(fightRequestDto, myMonstersSize);

            assertThat(resultFightRequestDto.getN()).isEqualTo(0);
        }

        @DisplayName("> list size")
        @Test
        void calculateN_NGreaterThanListSize() {
            int n = 7;
            int myMonstersSize = 6;
            fightRequestDto.setN(n);

            resultFightRequestDto = fightService.calculateN(fightRequestDto, myMonstersSize);

            assertThat(resultFightRequestDto.getN()).isEqualTo(0);
        }

        @DisplayName("< list size")
        @Test
        void calculateN_NLessThanListSize() {
            int n = 4;
            int myMonstersSize = 9;
            fightRequestDto.setN(n);

            resultFightRequestDto = fightService.calculateN(fightRequestDto, myMonstersSize);

            assertThat(resultFightRequestDto.getN()).isEqualTo(4);
        }



        @DisplayName("< 0")
        @Test
        void calculateN_NLessThanZero() {
            int n = -1;
            int myMonstersSize = 11;
            fightRequestDto.setN(n);

            resultFightRequestDto = fightService.calculateN(fightRequestDto, myMonstersSize);

            assertThat(resultFightRequestDto.getN()).isEqualTo(10);
        }


    }

    @DisplayName("Calculate the amount of chance bars with ")
    @Nested
    class calculateMyChanceBarAmount {

        private MonsterDto myCurrentMonsterDto;

        private MonsterDto opponentMonsterDto;

        private FightService fightServiceInjected;

        @BeforeEach
        void setup() {
            myCurrentMonsterDto = new MonsterDto();
            opponentMonsterDto = new MonsterDto();
            fightServiceInjected = new FightService(battleService);
        }

        @DisplayName("no stats")
        @Test
        void calculateMyChanceBarAmount_WithNoStats() {
            myCurrentMonsterDto.setAttack(0);
            myCurrentMonsterDto.setDefence(0);
            myCurrentMonsterDto.setTricks(0);
            myCurrentMonsterDto.setBrains(0);
            opponentMonsterDto.setAttack(100);
            opponentMonsterDto.setDefence(100);
            opponentMonsterDto.setTricks(100);
            opponentMonsterDto.setBrains(100);

            int myChanceBarAmount = fightServiceInjected.calculateMyChanceBarAmount(myCurrentMonsterDto, opponentMonsterDto);

            assertThat(myChanceBarAmount).isEqualTo(1);
        }

        @DisplayName("relatively infinite stats")
        @Test
        void calculateMyChanceBarAmount_WithRelativelyInfiniteStats() {
            myCurrentMonsterDto.setAttack(100);
            myCurrentMonsterDto.setDefence(100);
            myCurrentMonsterDto.setTricks(100);
            myCurrentMonsterDto.setBrains(100);
            opponentMonsterDto.setAttack(0);
            opponentMonsterDto.setDefence(0);
            opponentMonsterDto.setTricks(0);
            opponentMonsterDto.setBrains(0);

            int myChanceBarAmount = fightServiceInjected.calculateMyChanceBarAmount(myCurrentMonsterDto, opponentMonsterDto);

            assertThat(myChanceBarAmount).isEqualTo(9);
        }

        @DisplayName("even stats")
        @Test
        void calculateMyChanceBarAmount_WithEvenStats() {
            myCurrentMonsterDto.setAttack(100);
            myCurrentMonsterDto.setDefence(100);
            myCurrentMonsterDto.setTricks(100);
            myCurrentMonsterDto.setBrains(100);
            opponentMonsterDto.setAttack(100);
            opponentMonsterDto.setDefence(100);
            opponentMonsterDto.setTricks(100);
            opponentMonsterDto.setBrains(100);

            int myChanceBarAmount = fightServiceInjected.calculateMyChanceBarAmount(myCurrentMonsterDto, opponentMonsterDto);

            assertThat(myChanceBarAmount).isEqualTo(5);
        }
    }
}