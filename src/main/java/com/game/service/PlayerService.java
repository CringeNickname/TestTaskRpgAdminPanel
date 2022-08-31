package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player findById(Long id) {
        return playerRepository.getOne(id);
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }



}
