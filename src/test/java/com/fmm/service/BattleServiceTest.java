package com.fmm.service;

import com.fmm.dto.MessageDto;
import com.fmm.dto.MonsterDto;
import com.fmm.enumeration.*;
import com.fmm.model.Message;
import com.fmm.model.Monster;
import com.fmm.model.User;
import com.fmm.model.UserInfo;
import com.fmm.repository.MessageRepository;
import com.fmm.repository.MonsterRepository;
import com.fmm.repository.UserInfoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @InjectMocks
    private BattleService battleService;

    @InjectMocks
    private MonsterService monsterService;

    @Mock
    private MonsterRepository monsterRepository;

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoRepository userInfoRepository;


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

            long resultStats = battleService.changeStatsByPotion(monsterDto, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("demon attack")
        @Test
        void changeStatsByPotion_DemonAttack() {
            monsterDto.setPotion("DEMON_ATTACK");
            long monsterDtoTotalStats = (monsterDto.getAttack() * 4) + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("tricks maker")
        @Test
        void changeStatsByPotion_TricksMaker() {
            monsterDto.setPotion("TRICKS_MAKER");
            long monsterDtoTotalStats = monsterDto.getAttack() + monsterDto.getDefence() + (monsterDto.getTricks() * 4) + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("tough guy")
        @Test
        void changeStatsByPotion_ToughGuy() {
            monsterDto.setPotion("TOUGH_GUY");
            long monsterDtoTotalStats = monsterDto.getAttack() + (monsterDto.getDefence() * 2) + monsterDto.getTricks() + (monsterDto.getBrains() * 2);

            long resultStats = battleService.changeStatsByPotion(monsterDto, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("mysterio")
        @Test
        void changeStatsByPotion_Mysterio() {
            monsterDto.setPotion("MYSTERIO");
            long monsterDtoTotalStats = (monsterDto.getAttack() * 2) + monsterDto.getDefence() + (monsterDto.getTricks() * 2) + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("incredible hulk")
        @Test
        void changeStatsByPotion_IncredibleHulk() {
            monsterDto.setPotion("INCREDIBLE_HULK");
            long monsterDtoTotalStats = 4 * (monsterDto.getAttack() + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains());

            long resultStats = battleService.changeStatsByPotion(monsterDto, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("mysterio rage")
        @Test
        void changeStatsByPotion_MysterioRage() {
            monsterDto.setPotion("MYSTERIO_RAGE");
            long monsterDtoTotalStats = (monsterDto.getAttack() * 5) + monsterDto.getDefence() + (monsterDto.getTricks() * 5) + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto, "");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStats);
        }

        @DisplayName("advantage killer")
        @Test
        void changeStatsByPotion_AdvantageKiller() {
            monsterDto.setPotion("TOUGH_GUY");
            long monsterDtoTotalStatsNoPotion = monsterDto.getAttack() + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto, "ADVANTAGE_KILLER");

            assertThat(resultStats).isEqualTo(monsterDtoTotalStatsNoPotion);
        }

        @DisplayName("giant slayer")
        @Test
        void changeStatsByPotion_GiantSlayer() {
            monsterDto.setPotion("DEMON_ATTACK");
            long monsterDtoTotalStatsNoPotion = monsterDto.getAttack() + monsterDto.getDefence() + monsterDto.getTricks() + monsterDto.getBrains();

            long resultStats = battleService.changeStatsByPotion(monsterDto, "GIANT_SLAYER");

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

            //(int) ((percentageChanceToWin * 3.6) / 2) = 0

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

            //(int) ((percentageChanceToWin * 3.6) / 2) = 180

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

    @DisplayName("Execute the win condition of ")
    @Nested
    class executeWinConditions {

        private User winningUser;

        private User losingUser;

        private Monster winningMonster;

        private Monster losingMonster;

        private Message loserMessageRelevant;

        private BattleService battleServiceInjected;

        @BeforeEach
        void setup() {
            winningUser = new User("56Frank", "spi1der");
            winningUser.setId(1L);

            losingUser = new User("234", "jhkuerf");
            losingUser.setId(2L);

            winningMonster = new Monster(winningUser, Level.SUPER);
            winningMonster.setId(1L);
            losingMonster = new Monster(losingUser, "eep", Level.STANDARD);
            losingMonster.setId(2L);

            loserMessageRelevant = new Message
                    (losingUser, 1L, TypeOfFight.EAT, "HIJKL 234", losingMonster.getName(), 4L);

            battleServiceInjected = new BattleService(userInfoService, messageService, monsterService);
        }

        @DisplayName("collecting")
        @Test
        void executeWinCondition_Collecting() {
            when(messageRepository.findAll()).thenReturn(Collections.singletonList(loserMessageRelevant));

            battleServiceInjected.executeWinCondition(winningUser, losingUser, TypeOfFight.COLLECT, winningMonster, losingMonster);
            verify(messageRepository, times(1)).delete(loserMessageRelevant);
            ArgumentCaptor<Monster> captor = ArgumentCaptor.forClass(Monster.class);
            verify(monsterRepository, times(1)).save(captor.capture());

            assertThat(captor.getValue().getUser()).isEqualTo(winningUser);
        }

        @DisplayName("biting")
        @Test
        void executeWinCondition_Biting() {
            long winningMonsterAttack = (long) (winningMonster.getAttack() + (losingMonster.getAttack() / 4.0));
            long winningMonsterDefence = (long) (winningMonster.getDefence() + (losingMonster.getDefence() / 4.0));
            long winningMonsterBrains = (long) (winningMonster.getBrains() + (losingMonster.getBrains() / 4.0));
            long winningMonsterTricks = (long) (winningMonster.getTricks() + (losingMonster.getTricks() / 4.0));

            long losingMonsterAttack = (long) (losingMonster.getAttack() - (losingMonster.getAttack() / 4.0));
            long losingMonsterDefence = (long) (losingMonster.getDefence() - (losingMonster.getDefence() / 4.0));
            long losingMonsterBrains = (long) (losingMonster.getBrains() - (losingMonster.getBrains() / 4.0));
            long losingMonsterTricks = (long) (losingMonster.getTricks() - (losingMonster.getTricks() / 4.0));

            battleServiceInjected.executeWinCondition(winningUser, losingUser, TypeOfFight.BITE, winningMonster, losingMonster);

            ArgumentCaptor<Monster> captor = ArgumentCaptor.forClass(Monster.class);
            verify(monsterRepository, times(2)).save(captor.capture());

            Monster losingMonster = captor.getAllValues().get(0);
            assertThat(losingMonster.getAttack()).isEqualTo(losingMonsterAttack);
            assertThat(losingMonster.getDefence()).isEqualTo(losingMonsterDefence);
            assertThat(losingMonster.getBrains()).isEqualTo(losingMonsterBrains);
            assertThat(losingMonster.getTricks()).isEqualTo(losingMonsterTricks);

            Monster winningMonster = captor.getAllValues().get(1);
            assertThat(winningMonster.getAttack()).isEqualTo(winningMonsterAttack);
            assertThat(winningMonster.getDefence()).isEqualTo(winningMonsterDefence);
            assertThat(winningMonster.getBrains()).isEqualTo(winningMonsterBrains);
            assertThat(winningMonster.getTricks()).isEqualTo(winningMonsterTricks);
        }

        @DisplayName(" eating")
        @Test
        void executeWinCondition_Eating() {
            long winningMonsterAttack = (long) (winningMonster.getAttack() + (losingMonster.getAttack() / 1.5));
            long winningMonsterDefence = (long) (winningMonster.getDefence() + (losingMonster.getDefence() / 1.5));
            long winningMonsterBrains = (long) (winningMonster.getBrains() + (losingMonster.getBrains() / 1.5));
            long winningMonsterTricks = (long) (winningMonster.getTricks() + (losingMonster.getTricks() / 1.5));

            long losingMonsterAttack = (long) (losingMonster.getAttack() - (losingMonster.getAttack() / 1.5));
            long losingMonsterDefence = (long) (losingMonster.getDefence() - (losingMonster.getDefence() / 1.5));
            long losingMonsterBrains = (long) (losingMonster.getBrains() - (losingMonster.getBrains() / 1.5));
            long losingMonsterTricks = (long) (losingMonster.getTricks() - (losingMonster.getTricks() / 1.5));

            when(messageRepository.findAll()).thenReturn(Collections.singletonList(loserMessageRelevant));

            battleServiceInjected.executeWinCondition(winningUser, losingUser, TypeOfFight.EAT, winningMonster, losingMonster);
            verify(messageRepository, times(1)).delete(loserMessageRelevant);
            ArgumentCaptor<Monster> captor = ArgumentCaptor.forClass(Monster.class);
            verify(monsterRepository, times(2)).save(captor.capture());

            Monster losingMonster = captor.getAllValues().get(0);
            assertThat(losingMonster.getAttack()).isEqualTo(losingMonsterAttack);
            assertThat(losingMonster.getDefence()).isEqualTo(losingMonsterDefence);
            assertThat(losingMonster.getBrains()).isEqualTo(losingMonsterBrains);
            assertThat(losingMonster.getTricks()).isEqualTo(losingMonsterTricks);

            Monster winningMonster = captor.getAllValues().get(1);
            assertThat(winningMonster.getAttack()).isEqualTo(winningMonsterAttack);
            assertThat(winningMonster.getDefence()).isEqualTo(winningMonsterDefence);
            assertThat(winningMonster.getBrains()).isEqualTo(winningMonsterBrains);
            assertThat(winningMonster.getTricks()).isEqualTo(winningMonsterTricks);
        }
    }

    @DisplayName("Calculate a random degree >= 0 and < 360")
    @RepeatedTest(10)
    void calculateRandomDegrees() {
        int randomDegrees = battleService.calculateRandomDegrees();

        assertThat(randomDegrees).isBetween(0, 359);
    }

    @DisplayName("Calculate percentage chance to win with ")
    @Nested
    class calculatePercentageChanceToWin {

        @DisplayName(" even odds")
        @Test
        void calculatePercentageChanceToWin_WithEvenOdds() {
            MonsterDto myMonsterDto = new MonsterDto();
            myMonsterDto.setAttack(100);
            myMonsterDto.setDefence(100);
            myMonsterDto.setTricks(100);
            myMonsterDto.setBrains(100);
            MonsterDto opponentMonsterDto = new MonsterDto();
            opponentMonsterDto.setAttack(100);
            opponentMonsterDto.setDefence(100);
            opponentMonsterDto.setTricks(100);
            opponentMonsterDto.setBrains(100);

            BattleService battleServiceInjected = new BattleService(userInfoService, messageService, monsterService);
            double percentageChanceToWin = battleServiceInjected.calculatePercentageChanceToWin(myMonsterDto, opponentMonsterDto);

            assertThat(percentageChanceToWin).isEqualTo((50));
        }

        @DisplayName(" a potion")
        @Test
        void calculatePercentageChanceToWin_WithPotion() {
            MonsterDto myMonsterDto = new MonsterDto();
            myMonsterDto.setAttack(100);
            myMonsterDto.setDefence(100);
            myMonsterDto.setTricks(100);
            myMonsterDto.setBrains(100);
            MonsterDto opponentMonsterDto = new MonsterDto();
            opponentMonsterDto.setAttack(100);
            opponentMonsterDto.setDefence(100);
            opponentMonsterDto.setTricks(100);
            opponentMonsterDto.setBrains(100);
            opponentMonsterDto.setPotion("TOUGH_GUY");

            BattleService battleServiceInjected = new BattleService(userInfoService, messageService, monsterService);
            double percentageChanceToWin = battleServiceInjected.calculatePercentageChanceToWin(myMonsterDto, opponentMonsterDto);

            assertThat(percentageChanceToWin).isEqualTo((40));
        }

        @DisplayName(" certain odds")
        @Test
        void calculatePercentageChanceToWin_WithCertainOdds() {
            MonsterDto myMonsterDto = new MonsterDto();
            myMonsterDto.setAttack(100);
            myMonsterDto.setDefence(100);
            myMonsterDto.setTricks(100);
            myMonsterDto.setBrains(100);
            MonsterDto opponentMonsterDto = new MonsterDto();
            opponentMonsterDto.setAttack(0);
            opponentMonsterDto.setDefence(0);
            opponentMonsterDto.setTricks(0);
            opponentMonsterDto.setBrains(0);

            BattleService battleServiceInjected = new BattleService(userInfoService, messageService, monsterService);
            double percentageChanceToWin = battleServiceInjected.calculatePercentageChanceToWin(myMonsterDto, opponentMonsterDto);

            assertThat(percentageChanceToWin).isEqualTo((100));
        }

        @DisplayName(" impossible odds")
        @Test
        void calculatePercentageChanceToWin_WithImpossibleOdds() {
            MonsterDto myMonsterDto = new MonsterDto();
            myMonsterDto.setAttack(0);
            myMonsterDto.setDefence(0);
            myMonsterDto.setTricks(0);
            myMonsterDto.setBrains(0);
            MonsterDto opponentMonsterDto = new MonsterDto();
            opponentMonsterDto.setAttack(100);
            opponentMonsterDto.setDefence(100);
            opponentMonsterDto.setTricks(100);
            opponentMonsterDto.setBrains(100);

            BattleService battleServiceInjected = new BattleService(userInfoService, messageService, monsterService);
            double percentageChanceToWin = battleServiceInjected.calculatePercentageChanceToWin(myMonsterDto, opponentMonsterDto);

            assertThat(percentageChanceToWin).isEqualTo((0));
        }
    }

    @DisplayName("That the entire fight occurred")
    @Test
    void fight() {
        User myUser = new User("c", "45129");
        myUser.setId(1L);
        User opponentUser = new User("f", "123");
        opponentUser.setId(2L);
        Monster myMonster = new Monster(myUser, Level.STANDARD);
        Monster opponentMonster = new Monster(opponentUser, Level.STANDARD);

        opponentMonster.setPotion(String.valueOf(Potion.TOUGH_GUY));
        opponentMonster.setPotionUses(Potion.TOUGH_GUY.getUses());

        MessageDto messageDto = new MessageDto();
        messageDto.setNuggetsForAccepting(5L);

        when(userInfoRepository.findById(1L)).thenReturn(Optional.of(new UserInfo(myUser)));
        when(userInfoRepository.findById(2L)).thenReturn(Optional.of(new UserInfo(opponentUser)));

        BattleService battleServiceInjected = new BattleService(userInfoService, messageService, monsterService);
        battleServiceInjected.fight(messageDto, true, myUser, opponentUser, myMonster, opponentMonster);
        verify(userInfoRepository, times(2)).save(any());
        ArgumentCaptor<Monster> monsterCaptor = ArgumentCaptor.forClass(Monster.class);
        verify(monsterRepository, times(3)).save(monsterCaptor.capture());

        List<Monster> monsterCaptors = monsterCaptor.getAllValues();

        assertThat(opponentMonster.getPotion()).isEqualTo(monsterCaptors.get(2).getPotion());
        assertThat(opponentMonster.getPotionUses()).isEqualTo(Potion.TOUGH_GUY.getUses() - 1);

        assertThat(myMonster.getPotion()).isEqualTo(null);
        assertThat(myMonster.getPotionUses()).isEqualTo(0);
    }

}