package com.game.rest.comparator;

import com.game.entity.Player;

import java.util.Comparator;

public class ExperienceComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        return o1.getExperience() - o2.getExperience();
    }
}
