package com.fmm.service;

import com.fmm.dto.MonsterDto;
import com.fmm.enumeration.Genus;
import com.fmm.enumeration.Level;
import com.fmm.enumeration.Species;
import com.fmm.enumeration.TypeOfFight;
import com.fmm.model.Message;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.repository.MessageRepository;
import com.fmm.repository.MonsterRepository;
import com.fmm.repository.UserInfoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    private BattleService battleService;


    @DisplayName("Change monster stats with ")
    @Nested
    class changeStatsByPotionClass {

        private MonsterDto monsterDto = new MonsterDto();

        @BeforeEach
        void setupMonsterDto() {
            monsterDto.setId(1L);
            monsterDto.setName("");
            monsterDto.setGenus(String.valueOf(Genus.ROBOTS));
            monsterDto.setSpecies(String.valueOf(Species.MECHANOID));
            monsterDto.setAttack(7);
            monsterDto.setDefence(43);
            monsterDto.setBrains(67);
            monsterDto.setTricks(83);
            monsterDto.setAlive(true);
        }

        @DisplayName("no potion")
        @Test
        void changeStatsByPotion_NoPotion() {
            monsterDto.setPotion("");
            long monsterDtoTotalStats = monsterDto.getAttack() + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto);

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("demon attack")
        @Test
        void changeStatsByPotion_DemonAttack() {
            monsterDto.setPotion("DEMON_ATTACK");
            long monsterDtoTotalStats = (monsterDto.getAttack() * 4) + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto);

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("tricks maker")
        @Test
        void changeStatsByPotion_TricksMaker() {
            monsterDto.setPotion("TRICKS_MAKER");
            long monsterDtoTotalStats = monsterDto.getAttack() + monsterDto.getDefence() + (monsterDto.getTricks() * 4) + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto);

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("tough guy")
        @Test
        void changeStatsByPotion_ToughGuy() {
            monsterDto.setPotion("TOUGH_GUY");
            long monsterDtoTotalStats = monsterDto.getAttack() + (monsterDto.getDefence() * 2) + monsterDto.getTricks() + (monsterDto.getBrains() * 2);

            long resultStats = battleService.changeStatsByPotion(monsterDto);

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("mysterio")
        @Test
        void changeStatsByPotion_Mysterio() {
            monsterDto.setPotion("MYSTERIO");
            long monsterDtoTotalStats = (monsterDto.getAttack() * 2) + monsterDto.getDefence() + (monsterDto.getTricks() * 2) + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto);

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("incredible hulk")
        @Test
        void changeStatsByPotion_IncredibleHulk() {
            monsterDto.setPotion("INCREDIBLE_HULK");
            long monsterDtoTotalStats = 4 * (monsterDto.getAttack() + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains());

            long resultStats = battleService.changeStatsByPotion(monsterDto);

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("mysterio rage")
        @Test
        void changeStatsByPotion_MysterioRage() {
            monsterDto.setPotion("MYSTERIO_RAGE");
            long monsterDtoTotalStats = (monsterDto.getAttack() * 5) + monsterDto.getDefence() + (monsterDto.getTricks() * 5) + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto);

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }
    }

    @DisplayName("Change monster stats with ")
    @Nested
    class changeStatsByComplexPotionClass {

        private MonsterDto monsterDto = new MonsterDto();

        @BeforeEach
        void setup() {
            monsterDto.setId(1L);
            monsterDto.setName("");
            monsterDto.setGenus(String.valueOf(Genus.ROBOTS));
            monsterDto.setSpecies(String.valueOf(Species.MECHANOID));
            monsterDto.setAttack(7);
            monsterDto.setDefence(43);
            monsterDto.setBrains(67);
            monsterDto.setTricks(83);
            monsterDto.setAlive(true);
        }

        @DisplayName("no potion")
        @Test
        void changeStatsByComplexPotion_NoPotion() {
            monsterDto.setPotion("TRICKS_MAKER");
            long monsterDtoTotalStats = monsterDto.getAttack() + monsterDto.getDefence() + (monsterDto.getTricks() * 4) + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByComplexPotion(monsterDto, monsterDtoTotalStats, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("advantage killer")
        @Test
        void changeStatsByComplexPotion_AdvantageKiller() {
            monsterDto.setPotion("TOUGH_GUY");
            long monsterDtoTotalStatsNoPotion =
                    monsterDto.getAttack() + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();
            long monsterDtoTotalStatsWithPotion =
                    monsterDto.getAttack() + (monsterDto.getDefence() * 2) + monsterDto.getTricks() + (monsterDto.getBrains() * 2);

            long resultStats = battleService.changeStatsByComplexPotion(monsterDto, monsterDtoTotalStatsWithPotion, "ADVANTAGE_KILLER");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStatsNoPotion);
        }

        @DisplayName("giant slayer")
        @Test
        void changeStatsByComplexPotion_GiantSlayer() {
            monsterDto.setPotion("DEMON_ATTACK");
            long monsterDtoTotalStatsNoPotion =
                    monsterDto.getAttack() + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();
            long monsterDtoTotalStatsWithPotion =
                    (monsterDto.getAttack() * 4) + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByComplexPotion(monsterDto, monsterDtoTotalStatsWithPotion, "GIANT_SLAYER");

            assertThat(resultStats).isEqualTo((long) (0.1 * monsterDtoTotalStatsNoPotion));
        }
    }

    @DisplayName("Calculate degrees with ")
    @Nested
    class calculateDegrees {

        @DisplayName(" no chance")
        @Test
        void calculateDegrees_NoChance() {
            double percentChance = 0;

            //(int) ((percentageChanceToWin * 3.6) / 2) = 54

            List<Integer> listOfDegrees = battleService.calculateDegrees(percentChance);
            assertThat(listOfDegrees.get(0)).isEqualTo(0);
            assertThat(listOfDegrees.get(1)).isEqualTo(270);
            assertThat(listOfDegrees.get(2)).isEqualTo(270);
        }

        @DisplayName("a small chance")
        @Test
        void calculateDegrees_SmallChance() {
            double percentChance = 30.5;

            //(int) ((percentageChanceToWin * 3.6) / 2) = 54

            List<Integer> listOfDegrees = battleService.calculateDegrees(percentChance);
            assertThat(listOfDegrees.get(0)).isEqualTo(54);
            assertThat(listOfDegrees.get(1)).isEqualTo(216);
            assertThat(listOfDegrees.get(2)).isEqualTo(324);
        }

        @DisplayName("a high chance")
        @Test
        void calculateDegrees_HighChance() {
            double percentChance = 92.34;

            //(int) ((percentageChanceToWin * 3.6) / 2) = 166

            List<Integer> listOfDegrees = battleService.calculateDegrees(percentChance);
            assertThat(listOfDegrees.get(0)).isEqualTo(166);
            assertThat(listOfDegrees.get(1)).isEqualTo(104);
            assertThat(listOfDegrees.get(2)).isEqualTo(76);
        }

        @DisplayName(" complete certainty")
        @Test
        void calculateDegrees_CompleteCertainty() {
            double percentChance = 100;

            //(int) ((percentageChanceToWin * 3.6) / 2) = 54

            List<Integer> listOfDegrees = battleService.calculateDegrees(percentChance);
            assertThat(listOfDegrees.get(0)).isEqualTo(180);
            assertThat(listOfDegrees.get(1)).isEqualTo(90);
            assertThat(listOfDegrees.get(2)).isEqualTo(90);
        }
    }

    @DisplayName("Calculate the winner with ")
    @Nested
    class calculateTheWinner {

        @DisplayName(" a heavy win")
        @Test
        void calculateTheWinner_HeavyWin() {
            boolean didIWin = battleService.calculateTheWinner(250, 50);

            assertThat(didIWin).isEqualTo(true);
        }

        @DisplayName(" a slight win")
        @Test
        void calculateTheWinner_slightWin() {
            boolean didIWin = battleService.calculateTheWinner(298, 15.8);

            assertThat(didIWin).isEqualTo(true);
        }

        @DisplayName(" a heavy loss")
        @Test
        void calculateTheWinner_HeavyLoss() {
            boolean didIWin = battleService.calculateTheWinner(70, 10);

            assertThat(didIWin).isEqualTo(false);
        }

        @DisplayName(" a slight loss")
        @Test
        void calculateTheWinner_slightLoss() {
            boolean didIWin = battleService.calculateTheWinner(61, 83.4126);

            assertThat(didIWin).isEqualTo(false);
        }
    }

    /*
    @DisplayName("Execute the win condition of ")
    @Nested
    class executeWinConditions {

        private User winningUser;

        private User losingUser;

        private Monster winningMonster;

        private Monster losingMonster;

        private Message winnerMessageRelevant;

        private Message loserMessageRelevant;

        @BeforeEach
        void setup() {
            winningUser = new User("56Frank", "spi1der");
            winningUser.setId(1L);

            losingUser = new User("234", "jhkuerf");
            losingUser.setId(2L);

            winningMonster = new Monster(winningUser, "", Level.SUPER);
            losingMonster = new Monster(losingUser, "eep", Level.STANDARD);

            winnerMessageRelevant = new Message(winningUser, 2L, TypeOfFight.COLLECT, "", "", 0L);
            loserMessageRelevant = new Message(losingUser, 1L, TypeOfFight.EAT, "", "", 4L);
        }

        @DisplayName("biting")
        @Test
        void executeWinCondition() {
            long winningMonsterAttack = winningMonster.getAttack() + (losingMonster.getAttack() / 4);
            long winningMonsterDefence = winningMonster.getDefence() + (losingMonster.getDefence() / 4);
            long winningMonsterBrains = winningMonster.getBrains() + (losingMonster.getBrains() / 4);
            long winningMonsterTricks = winningMonster.getTricks() + (losingMonster.getTricks() / 4);

            long losingMonsterAttack = losingMonster.getAttack() - (losingMonster.getAttack() / 4);
            long losingMonsterDefence = losingMonster.getDefence() - (losingMonster.getDefence() / 4);
            long losingMonsterBrains = losingMonster.getBrains() - (losingMonster.getBrains() / 4);
            long losingMonsterTricks = losingMonster.getTricks() - (losingMonster.getTricks() / 4);

            battleService.executeWinCondition(winningUser, losingUser, TypeOfFight.BITE, winningMonster, losingMonster);

            ArgumentCaptor<Monster> captor = ArgumentCaptor.forClass(Monster.class);
            verify(monsterRepository).save(captor.capture());

            List<Monster> captureList = captor.getAllValues();
            assertThat(captureList.get(0).getAttack()).isEqualTo(losingMonsterAttack);
            assertThat(captureList.get(0).getDefence()).isEqualTo(losingMonsterDefence);
            assertThat(captureList.get(0).getBrains()).isEqualTo(losingMonsterBrains);
            assertThat(captureList.get(0).getTricks()).isEqualTo(losingMonsterTricks);

            assertThat(captureList.get(1).getAttack()).isEqualTo(winningMonsterAttack);
            assertThat(captureList.get(1).getDefence()).isEqualTo(winningMonsterDefence);
            assertThat(captureList.get(1).getBrains()).isEqualTo(winningMonsterBrains);
            assertThat(captureList.get(1).getTricks()).isEqualTo(winningMonsterTricks);
        }
    }

     */

    @DisplayName("Calculate a random degree >= 0 and < 360")
    @RepeatedTest(10)
    void calculateRandomDegrees() {
        int randomDegrees = battleService.calculateRandomDegrees();

        assertThat(randomDegrees).isBetween(0, 359);
    }

    void fight() {
    }

    void calculatePercentageChanceToWin() {
    }

}