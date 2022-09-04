package com.game.rest.Comparators;

import com.game.entity.Player;

import java.util.Comparator;

public class BirthdayComparator implements Comparator<Player> {
    @Override
    public int compare(Player o1, Player o2) {
        return o1.getBirthday().compareTo(o2.getBirthday());
    }
}
