package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PlayerController {
    private PlayerService playerService;

    @GetMapping("rest/players")
    public List<Player> findAll() {
        return playerService.findAll();
    }
}
