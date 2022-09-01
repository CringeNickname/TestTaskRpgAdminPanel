package com.game.service;

import com.game.entity.Player;

import java.util.List;


public interface PlayerService {

    Player findById(Long id);

    List<Player> findAll();

    Player createPlayer(Player player);

    void deletePlayer(Player player);
}
