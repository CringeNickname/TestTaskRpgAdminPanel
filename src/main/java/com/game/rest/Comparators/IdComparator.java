package com.game.rest.Comparators;

import com.game.entity.Player;

import java.util.Comparator;

public class IdComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        return (int) (o1.getId() - o2.getId());
    }
}
