package com.game.rest;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("rest/players")
public class PlayerRestController {


    private final PlayerService playerService;

    @Autowired
    public PlayerRestController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "id", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> getPlayer(@RequestParam("id") Long playerId) {
        if (playerId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Player player = playerService.findById(playerId);

        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }

    @RequestMapping(params = { "pageNumber", "pageSize", "order" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> showPlayers(@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize, @RequestParam("order") String order) {
        Player player;
        player = playerService.
        return new ResponseEntity<Player>(player, HttpStatus.OK);
    }
}
