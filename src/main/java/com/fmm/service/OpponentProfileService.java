package com.fmm.service;

import com.fmm.dto.MonsterDto;
import com.fmm.model.Monster;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpponentProfileService {

    private final ModelMapper modelMapper;

    @Autowired
    public OpponentProfileService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<MonsterDto> getFirstPageAliveMonsters(List<Monster> aliveMonsterList) {
        List<MonsterDto> monsterDtoList = new ArrayList<>();

        for (Monster monster: aliveMonsterList) {
            monsterDtoList.add(convertToDto(monster));

            if (monsterDtoList.size() >= 6) {
                break;
            }
        }

        return monsterDtoList;
    }

    public List<MonsterDto> getSpecificPageAliveMonsters(int pageNumber, List<Monster> aliveMonsterList) {
        List<MonsterDto> monsterDtoList = new ArrayList<>();

        for (int i = ((pageNumber - 1) * 6); i < 6 + ((pageNumber - 1) * 6); i++) {
            if (i >= aliveMonsterList.size()) {
                break;
            }

            monsterDtoList.add(convertToDto(aliveMonsterList.get(i)));
        }

        return monsterDtoList;
    }

    public int calculateTotalPages(List<Monster> aliveMonsterList) {

        return 1 + (aliveMonsterList.size() / 6);
    }

    public int calculateTotalPagesWithLimit(List<Monster> aliveMonsterList) {
        int totalPages = calculateTotalPages(aliveMonsterList);
        if (totalPages >= 8) {
            totalPages = 7;
        }

        return totalPages;
    }

    private MonsterDto convertToDto(Monster monster) {
        return modelMapper.map(monster, MonsterDto.class);
    }
}
