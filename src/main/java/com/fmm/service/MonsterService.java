package com.fmm.service;

import com.fmm.enumeration.Potion;
import com.fmm.exception.CouldNotFindMonsterException;
import com.fmm.model.Monster;
import com.fmm.repository.MonsterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MonsterService {

    private final MonsterRepository monsterRepository;

    @Autowired
    public MonsterService(MonsterRepository monsterRepository) {
        this.monsterRepository = monsterRepository;
    }

    public List<Monster> getMonsters(Long id) {
        List<Monster> monsterList = new ArrayList<>();
        for (Monster monster : monsterRepository.findAll()) {
            if (Objects.equals(monster.getUser().getId(), id)) {
                monsterList.add(monster);
            }
        }

        return monsterList;
    }

    public List<Monster> getAliveMonsters(Long id) {
        List<Monster> monsterList = new ArrayList<>();
        for (Monster monster : monsterRepository.findAll()) {
            if (Objects.equals(monster.getUser().getId(), id) && monster.isAlive()) {
                monsterList.add(monster);
            }
        }

        return monsterList;
    }

    public Monster getMonster(Long id, String monsterName) {
        for (Monster monster : monsterRepository.findAll()) {
            if (Objects.equals(monster.getUser().getId(), id) && monster.getName().equals(monsterName)) {
                return monster;
            }
        }

        throw new CouldNotFindMonsterException("No monster for this account has the name " + monsterName);
    }

    public void decreasePotionUse(Monster monster) {
        if (monster.getPotion() != null) {
            int newPotionUses = monster.getPotionUses() - 1;
            if (newPotionUses <= 0) {
                monster.setPotion("");
                monster.setPotionUses(0);
            } else {
                monster.setPotionUses(newPotionUses);
            }

            monsterRepository.save(monster);
        }
    }

    public void addMonster(Monster monster) {
        monsterRepository.save(monster);
    }

    public void updateMonster(Monster monster) {
        monsterRepository.save(monster);
    }
}
