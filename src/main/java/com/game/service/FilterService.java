package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.List;

public interface FilterService {
    void filter(List<Player> players, String name, String title, Long before, Long after, Race race, Profession profession, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel);
}
